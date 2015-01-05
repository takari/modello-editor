package io.takari.modello.editor.mapping.api;

import io.takari.modello.editor.mapping.model.IModelExtension;

import java.beans.PropertyDescriptor;

public interface IModelAccessor<E extends IModelAccessor<E>> {
    
    IPropertyAccessorManager<E> getManager();
    
    Object get(IModelExtension model, PropertyDescriptor pd);
    
    void set(IModelExtension model, PropertyDescriptor pd, Object value);
    
    Object add(IModelExtension model, PropertyDescriptor pd);
    
    void remove(IModelExtension model, PropertyDescriptor pd, Object item);
    
    void move(IModelExtension model, PropertyDescriptor pd, Object item, int pos);
    
    void touch(IModelExtension model, PropertyDescriptor pd);
    
}
