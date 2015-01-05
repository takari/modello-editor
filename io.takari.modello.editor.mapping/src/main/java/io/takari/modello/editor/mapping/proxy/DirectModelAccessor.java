package io.takari.modello.editor.mapping.proxy;

import java.beans.PropertyDescriptor;

import io.takari.modello.editor.mapping.api.AbstractModelAccessor;
import io.takari.modello.editor.mapping.api.IPropertyAccessor;
import io.takari.modello.editor.mapping.api.IPropertyAccessorManager;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.mapping.model.IModelExtension;

public class DirectModelAccessor extends AbstractModelAccessor<DirectModelAccessor>{
    
    public static final DirectModelAccessor INSTANCE = new DirectModelAccessor();
    
    private DirectModelAccessor() {
        super(new DirectPropertyAccessorFactory());
    }
    
    private static class DirectPropertyAccessorFactory implements IPropertyAccessorManager<DirectModelAccessor> {
        @Override
        public IPropertyAccessor<DirectModelAccessor> getAccessor(Class<? extends IModel> modelClass, PropertyDescriptor property) {
            return new DirectPropertyAccessor(property.getName());
        }
    }
    
    private static class DirectPropertyAccessor implements IPropertyAccessor<DirectModelAccessor> {
        
        private String name;
        
        public DirectPropertyAccessor(String name) {
            this.name = name;
        }
        
        @Override
        public Object get(DirectModelAccessor ctx, IModelExtension model) {
            return model._getData(DirectModelAccessor.class.getName() + ".values." + name);
        }

        @Override
        public void set(DirectModelAccessor ctx, IModelExtension model, Object value) {
            IModelExtension delegate = model._getDelegate();
            
            Object oldValue = model._getData(DirectModelAccessor.class.getName() + ".props." + name);
            model._setData(DirectModelAccessor.class.getName() + ".values." + name, value);
            
            delegate._firePropertyChange(name, oldValue, value);
        }

        @Override
        public Object add(DirectModelAccessor ctx, IModelExtension model) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(DirectModelAccessor ctx, IModelExtension model, Object item) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void move(DirectModelAccessor ctx, IModelExtension model, Object item, int pos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void touch(DirectModelAccessor ctx, IModelExtension model) {
            throw new UnsupportedOperationException();
        }
        
    }
}
