package io.takari.modello.editor.mapping.dom.accessor;

import io.takari.modello.editor.mapping.api.IPropertyAccessor;
import io.takari.modello.editor.mapping.dom.accessor.PathParser.DomPath;
import io.takari.modello.editor.mapping.dom.impl.DomSection;
import io.takari.modello.editor.mapping.model.IModelExtension;

public abstract class BaseAccessor<T> implements IPropertyAccessor<DomModelAccessor> {
    
    protected final String property;
    protected final String path;

    protected BaseAccessor(String property, String path) {
        this.property = property;
        this.path = path;
    }
    
    @Override
    public void set(DomModelAccessor ctx, IModelExtension model, Object object) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object add(DomModelAccessor ctx, IModelExtension model) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void remove(DomModelAccessor ctx, IModelExtension model, Object item) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void move(DomModelAccessor ctx, IModelExtension model, Object item, int pos) {
        throw new UnsupportedOperationException();
    }
    
    protected T getPropertyData(DomModelAccessor ctx, IModelExtension model) {
        IModelExtension delegate = model._getDelegate();
        
        if(ctx.getContainer(model) == null) {
            
            DomSection section = null;
            
            if(delegate.getParent() != null) {
                section = ctx.getContainer(((IModelExtension) delegate.getParent())._getDelegate());
            } else {
                section = ctx.createRoot();
            }
            
            ctx.setContainer(model, section);
        }
        
        //BeanData beanData = getBeanData(delegate);
        String key = BaseAccessor.class.getName() + ".prop." + property;
        
        @SuppressWarnings("unchecked")
        T propertyData = (T) delegate._getData(key);
        if(propertyData == null) {
            DomPath domPath = PathParser.parse(path);
            propertyData = createPropertyData(ctx, model, domPath);
            delegate._setData(key, propertyData);
        }
        
        return propertyData;
    }
    
    protected abstract T createPropertyData(DomModelAccessor ctx, IModelExtension model, DomPath path);
}
