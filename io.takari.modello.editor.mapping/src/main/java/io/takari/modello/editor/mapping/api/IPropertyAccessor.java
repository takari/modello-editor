package io.takari.modello.editor.mapping.api;

import io.takari.modello.editor.mapping.model.IModelExtension;

public interface IPropertyAccessor<E extends IModelAccessor<E>> {
    
    Object get(E ctx, IModelExtension model);
    
    void set(E ctx, IModelExtension model, Object object);
    
    Object add(E ctx, IModelExtension model);
    
    void remove(E ctx, IModelExtension model, Object item);
    
    void move(E ctx, IModelExtension model, Object item, int pos);
    
    void touch(E ctx, IModelExtension model);
    
}