package io.takari.modello.editor.mapping.dom.impl;

import io.takari.modello.editor.mapping.api.SyncState;
import io.takari.modello.editor.mapping.dom.DomMappingPlugin;

import java.util.ArrayList;
import java.util.List;

abstract class BeanList<F, T> implements IBeanList<T> {

    private IBeanMapper<F, T> mapper;
    private boolean loaded;
    private List<T> data;
    private SyncState sync;

    protected BeanList(IBeanMapper<F, T> mapper) {
        this.mapper = mapper;
    }
    
    private void clear() {
        synchronized(this) {
            loaded = false;
            data = null;
            sync = null;
        }
    }
    
    @Override
    public boolean isLoaded() {
        return loaded;
    }
    
    protected abstract List<F> getSource(DomHelper ctx);
    
    protected abstract F add0(DomHelper ctx);
    
    protected abstract void remove0(DomHelper ctx, F f);
    
    protected abstract boolean move0(DomHelper ctx, F f, int dir);
    
    private void load(DomHelper ctx) {
        synchronized(this) {
            if(sync != null && !sync.isValid()) {
                DomMappingPlugin.trace("Stale list");
                clear();
            }
            
            if(!loaded) {
                DomMappingPlugin.trace("Loading list");
                List<F> source = getSource(ctx);
                List<T> data = new ArrayList<>();
                for(F f: source) {
                    data.add(mapper.map(f));
                }
                this.data = data;
                sync = ctx.getSync();
                loaded = true;
            }
        }
    }
    
    public List<T> getList(DomHelper ctx) {
        load(ctx);
        return data;
    }

    public T add(DomHelper ctx) {
        synchronized(this) {
            T newItem = mapper.map(add0(ctx));
            if(loaded) {
                List<T> newData = new ArrayList<>(data);
                newData.add(newItem);
                data = newData;
            }
            return newItem;
        }
    }

    public void remove(DomHelper ctx, T bean) {
        
        synchronized(this) {
            remove0(ctx, mapper.unmap(bean));
            if(loaded) {
                List<T> newData = new ArrayList<>(data);
                
                int idx = newData.indexOf(bean);
                
                mapper.setIndex(bean, -1);
                newData.remove(idx);
                
                for(; idx < newData.size(); idx++) {
                    T t = newData.get(idx);
                    ((DomSection)mapper.unmap(t)).index = idx;
                    mapper.setIndex(t, idx);
                }
                
                data = newData;
            }
        }
    }
    
    @Override
    public void move(DomHelper ctx, T bean, int pos) {
        if(move0(ctx, mapper.unmap(bean), pos)) {
            if(loaded) {
                List<T> newData = new ArrayList<>(data);
                
                int oldidx = newData.indexOf(bean);
                
                newData.remove(bean);
                newData.add(pos, bean);
                
                int from = Math.min(oldidx, pos);
                int to = Math.max(oldidx, pos);
                
                for(; from <= to; from++) {
                    T t = newData.get(from);
                    //TODO DomValue support
                    ((DomSection)mapper.unmap(t)).index = from;
                    mapper.setIndex(t, from);
                }
                data = newData;
            }
        }
    }
    
}