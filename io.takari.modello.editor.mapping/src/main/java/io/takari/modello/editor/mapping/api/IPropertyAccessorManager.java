package io.takari.modello.editor.mapping.api;

import java.beans.PropertyDescriptor;

import io.takari.modello.editor.mapping.model.IModel;

public interface IPropertyAccessorManager<E extends IModelAccessor<E>> {
    
    IPropertyAccessor<E> getAccessor(Class<? extends IModel> modelClass, PropertyDescriptor property);
}   
