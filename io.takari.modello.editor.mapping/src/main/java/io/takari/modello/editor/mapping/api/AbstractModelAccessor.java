package io.takari.modello.editor.mapping.api;

import io.takari.modello.editor.mapping.model.IModelExtension;

import java.beans.PropertyDescriptor;

@SuppressWarnings("unchecked")
public class AbstractModelAccessor<E extends AbstractModelAccessor<E>> implements IModelAccessor<E> {
    
    private final IPropertyAccessorManager<E> mgr;

    public AbstractModelAccessor(IPropertyAccessorManager<E> mgr) {
        this.mgr = mgr;
    }
    
    @Override
    public IPropertyAccessorManager<E> getManager() {
        return mgr;
    }

    @Override
    public Object get(IModelExtension model, PropertyDescriptor pd) {
        return getAccessor(model, pd).get((E)this, model);
    }

    @Override
    public void set(IModelExtension model, PropertyDescriptor pd, Object value) {
        getAccessor(model, pd).set((E)this, model, value);
    }

    @Override
    public Object add(IModelExtension model, PropertyDescriptor pd) {
        return getAccessor(model, pd).add((E)this, model);
    }

    @Override
    public void remove(IModelExtension model, PropertyDescriptor pd, Object item) {
        getAccessor(model, pd).remove((E)this, model, item);
    }

    @Override
    public void move(IModelExtension model, PropertyDescriptor pd, Object item, int pos) {
        getAccessor(model, pd).move((E)this, model, item, pos);
    }

    @Override
    public void touch(IModelExtension model, PropertyDescriptor pd) {
        getAccessor(model, pd).touch((E)this, model);
    }
    
    
    private IPropertyAccessor<E> getAccessor(IModelExtension model, PropertyDescriptor pd) {
        IPropertyAccessor<E> a = mgr.getAccessor(model._getModelClass(), pd);
        if(a == null) throw new NoAccessorException();
        return a;
    }
}
