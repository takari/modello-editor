package io.takari.modello.editor.mapping.proxy;

import io.takari.modello.editor.mapping.api.IModelAccessor;
import io.takari.modello.editor.mapping.model.IModelExtension;

import java.lang.reflect.Method;

public interface IInvocationHandler {
    
    Object handle(IModelAccessor<?> ctx, IModelExtension model, Method method, Object[] args);
    
    void touch(IModelAccessor<?> ctx, IModelExtension model, String property);
    
    Object get(IModelAccessor<?> ctx, IModelExtension model, String property);
    
    void set(IModelAccessor<?> ctx, IModelExtension model, String property, Object value);
    
    IModelExtension add(IModelAccessor<?> ctx, IModelExtension model, String property);
    
    void remove(IModelAccessor<?> ctx, IModelExtension model, String property, IModelExtension item);
    
    void move(IModelAccessor<?> ctx, IModelExtension model, String property, IModelExtension item, int pos);
}