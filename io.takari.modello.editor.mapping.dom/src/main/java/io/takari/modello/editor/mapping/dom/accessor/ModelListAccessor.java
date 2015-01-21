package io.takari.modello.editor.mapping.dom.accessor;

import java.util.List;

import io.takari.modello.editor.mapping.dom.DomMappingPlugin;
import io.takari.modello.editor.mapping.dom.accessor.PathParser.DomPath;
import io.takari.modello.editor.mapping.dom.impl.DomSection;
import io.takari.modello.editor.mapping.dom.impl.IBeanList;
import io.takari.modello.editor.mapping.dom.impl.IBeanMapper;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.mapping.model.IModelExtension;

public class ModelListAccessor extends BaseAccessor<IBeanList<Object>> {
    
    private Class<? extends IModel> itemType;

    public ModelListAccessor(Class<? extends IModel> itemType, String name, String path) {
        super(name, path);
        this.itemType = itemType;
    }

    @Override
    public Object get(DomModelAccessor ctx, IModelExtension model) {
        DomMappingPlugin.trace("Get list " + property);
        return getPropertyData(ctx, model).getList(ctx.getDomHelper());
    }
    
    @Override
    public Object add(DomModelAccessor ctx, IModelExtension model) {
        IBeanList<Object> list = getPropertyData(ctx, model);
        Object item;
        
        List<Object> oldData = null;
        if(list.isLoaded()) {
            oldData = list.getList(ctx.getDomHelper());
        }
        
        ctx.getSessionProvider().requestWrite();
        try {
            DomMappingPlugin.trace("Add to list " + property);
            item = list.add(ctx.getDomHelper());
        } finally {
            ctx.getSessionProvider().release();
        }
        model._getDelegate()._firePropertyChange(property, oldData, list.getList(ctx.getDomHelper()));
        return item;
    }

    @Override
    public void remove(DomModelAccessor ctx, IModelExtension model, Object item) {
        IBeanList<Object> list = getPropertyData(ctx, model);
        List<Object> oldData = null;
        if(list.isLoaded()) {
            oldData = list.getList(ctx.getDomHelper());
        }
        
        ctx.getSessionProvider().requestWrite();
        try {
            DomMappingPlugin.trace("Remove from list " + property);
            list.remove(ctx.getDomHelper(), (IModelExtension) item);
        } finally {
            ctx.getSessionProvider().release();
        }
        model._getDelegate()._firePropertyChange(property, oldData, list.getList(ctx.getDomHelper()));
    }
    
    @Override
    public void move(DomModelAccessor ctx, IModelExtension model, Object item, int pos) {
        IBeanList<Object> list = getPropertyData(ctx, model);
        
        List<Object> oldData = null;
        if(list.isLoaded()) {
            oldData = list.getList(ctx.getDomHelper());
        }
        
        ctx.getSessionProvider().requestWrite();
        try {
            DomMappingPlugin.trace("Move Down list " + property);
            list.move(ctx.getDomHelper(), (IModelExtension) item, pos);
        } finally {
            ctx.getSessionProvider().release();
        }
        model._getDelegate()._firePropertyChange(property, oldData, list.getList(ctx.getDomHelper()));
    }
    
    @Override
    public void touch(DomModelAccessor ctx, IModelExtension model) {
        ctx.getContainer(model).touch(ctx.getDomHelper());
    }
    
    @Override
    protected IBeanList<Object> createPropertyData(final DomModelAccessor ctx, final IModelExtension model, DomPath path) {
        return path.list(ctx.getContainer(model)).mapSections(new IBeanMapper<DomSection, Object>() {
            @Override
            public IModelExtension map(DomSection from) {
                IModelExtension child = (IModelExtension) ctx.getProxyGenerator().createProxy(ctx, itemType, model, property);
                child._setIndex(from.getIndex());
                ctx.setContainer(child, from);
                from.persistent(); // do not delete such sections when empty
                return child;
            }
            @Override
            public DomSection unmap(Object from) {
                return ctx.getContainer((IModelExtension) from);
            }
            
            @Override
            public void setIndex(Object to, int index) {
                ((IModelExtension)to)._setIndex(index);
            }
        });
    }

    
}