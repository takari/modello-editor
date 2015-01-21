package io.takari.modello.editor.mapping.proxy;

import io.takari.modello.editor.mapping.MappingPlugin;
import io.takari.modello.editor.mapping.annotations.EditableList;
import io.takari.modello.editor.mapping.annotations.Observe;
import io.takari.modello.editor.mapping.api.IModelAccessor;
import io.takari.modello.editor.mapping.api.NoAccessorException;
import io.takari.modello.editor.mapping.model.IListControl;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.mapping.model.IModelExtension;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.proxy.MethodProxy;

public class ModelDelegate implements IModelExtension {
    
	private final ModelProxyGenerator gen;
    private final IModelAccessor<?> ctx;
    private final Class<? extends IModel> modelClass;
    private final IInvocationHandler handler;
    
    private IModelExtension parent;
    private String parentProperty;
    private int index = 0;
    private Map<String, IListControl> listControls;
    
    private IModelExtension model;
    
    private Map<String, Object> data;
    
    private PropertyChangeSupport pchange;
    private Map<String, PropertyDescriptor> descriptorMap;
    private Map<String, Set<String>> observableProperties;
    
    public ModelDelegate(ModelProxyGenerator gen, IModelAccessor<?> ctx, Class<? extends IModel> modelClass) {
        this.gen = gen;
		this.ctx = ctx;
        this.modelClass = modelClass;
        handler = MappingPlugin.getInvocationHandlerCache().get(modelClass);
    }
    
    void _init(IModelExtension parent, String parentProperty, IModelExtension model) {
        this.parent = parent;
        this.parentProperty = parentProperty;
        this.model = model;
        pchange = new PropertyChangeSupport(model);
        
        _configure();
    }
    
    @Override
    public IModelExtension _createSubProxy(Class<? extends IModel> modelClass) {
        IModelExtension proxy = (IModelExtension) gen.createProxy(ctx, modelClass, model, ".");
        
        return proxy;
    }
    
    public IModelExtension _getDelegate() {
        return this;
    }
    
    public Class<? extends IModel> _getModelClass() {
        return modelClass;
    }
    
    public IModelExtension _getModel() {
        return this.model;
    }
    
    public IModelExtension getParent() {
        return parent;
    }
    
    @Override
    public void _setParent(IModelExtension parent) {
        this.parent = parent;
        if(data != null) data.clear();
        _resetProperties();
    }
    
    @Override
    public String _getParentProperty() {
        return parentProperty;
    }
    
    @Override
    public int _getIndex() {
        return index;
    }
    
    @Override
    public void _setIndex(int index) {
        this.index = index;
    }
    
    public Object _getData(String key) {
        return data == null ? null : data.get(key);
    }
    
    public void _setData(String key, Object value) {
        if(data == null) {
            data = new HashMap<String, Object>();
        }
        data.put(key, value);
    }
    
    public Object _invokePropertyMethod(Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        try {
            return handler.handle(ctx, model, method, args);
        } catch(NoAccessorException e) {
            return methodProxy.invokeSuper(model, args);
        }
    }
    
    @Override
    public void _touch(String property) {
        handler.touch(ctx, model, property);
    }
    
    @Override
    public Object _get(String property) {
        return handler.get(ctx, model, property);
    }
    
    @Override
    public void _set(String property, Object value) {
        handler.set(ctx, model, property, value);
    }
    
    @Override
    public void _apply(IModel model) {
        if(!modelClass.isInstance(model)) {
            throw new IllegalArgumentException("Cannot apply model " + modelClass.getName() + " to " + model.getClass().getName());
        }
        
        for(Map.Entry<String, PropertyDescriptor> entry: descriptorMap.entrySet()) {
            PropertyDescriptor pd = entry.getValue();
            Method setter = pd.getWriteMethod();
            if(setter == null) continue;
            
            // XXX can models and lists of models be safely adapter to the new object?
            if(IModelExtension.class.isAssignableFrom(pd.getPropertyType())) continue;
            if(List.class.isAssignableFrom(pd.getPropertyType())) continue;
            
            Object value = _get(entry.getKey());
            try {
                setter.invoke(model, new Object[]{ value });
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        }
        
    }
    
    @Override
    public void _copyFrom(IModel model) {
        if(!modelClass.isInstance(model)) {
            throw new IllegalArgumentException("Cannot copy from model " + modelClass.getName() + " to " + model.getClass().getName());
        }
        
        for(Map.Entry<String, PropertyDescriptor> entry: descriptorMap.entrySet()) {
            PropertyDescriptor pd = entry.getValue();
            Method getter = pd.getReadMethod();
            if(getter == null) continue;
            
            // XXX can models and lists of models be safely adapter to the new object?
            if(IModelExtension.class.isAssignableFrom(pd.getPropertyType())) continue;
            if(List.class.isAssignableFrom(pd.getPropertyType())) continue;
            
            Object value;
            try {
                value = getter.invoke(model);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
            
            _set(entry.getKey(), value);
        }
    }
    
    @Override
    public Set<String> _getProperties() {
        return Collections.unmodifiableSet(descriptorMap.keySet());
    }
    
    @Override
    public Set<String> _getControlledListProperties() {
        if(listControls == null) return Collections.emptySet();
        return Collections.unmodifiableSet(listControls.keySet());
    }
    
    @Override
    public IListControl _getListControl(String property) {
        if(listControls == null) return null;
        return listControls.get(property);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pchange.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pchange.removePropertyChangeListener(listener);
    }

    public void _firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        pchange.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    private void _resetProperties() {
        for(Map.Entry<String, PropertyDescriptor> e: descriptorMap.entrySet()) {
            if(e.getValue().getWriteMethod() != null) {
                String property = e.getKey();
                // TODO track old values?
                _firePropertyChange(property, null, _get(property));
            }
        }
    }

    private void _configure() {
        PropertyDescriptor[] pds = ReflectUtils.getBeanGetters(modelClass);
        
        descriptorMap = new HashMap<>();
        observableProperties = new HashMap<>();
        
        for(PropertyDescriptor pd: pds) {
            String name = pd.getName();
            descriptorMap.put(name, pd);
            
            Method getter = pd.getReadMethod();
            
            // list
            EditableList elist = getter.getAnnotation(EditableList.class);
            if(List.class.isAssignableFrom(pd.getPropertyType())) {
                if(listControls == null) listControls = new HashMap<String, IListControl>();
                listControls.put(name, new ListControl(name, elist));
            }
            
            // observe
            Observe observe = getter.getAnnotation(Observe.class);
            if(observe != null) {
                String[] observedProperties = observe.value();
                
                for(String op: observedProperties) {
                    Set<String> observers = observableProperties.get(op);
                    if(observers == null) {
                        observers = new HashSet<>();
                        observableProperties.put(op, observers);
                    }
                    observers.add(name);
                }
                
            }
        }
        
        if(!observableProperties.isEmpty()) {
            addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    Set<String> observers = observableProperties.get(evt.getPropertyName());
                    if(observers != null) {
                        for(String prop: observers) {
                            Method read = descriptorMap.get(prop).getReadMethod();
                            Object value;
                            try {
                                value = read.invoke(model);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new IllegalStateException(e);
                            }
                            
                            Object oldValue = null;
                            if(evt.getNewValue() == value) {
                                oldValue = evt.getOldValue();
                            }
                            
                            _firePropertyChange(prop, oldValue, value);
                        }
                    }
                }
            });
        }
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof IModelExtension) {
            IModelExtension other = ((IModelExtension) obj)._getDelegate();
            
            return ctx.compare(this, other) && index == other._getIndex();
            
        }
        return false;
    }
    
    private class ListControl implements IListControl {

        private String name;
        private EditableList elist;

        public ListControl(String name, EditableList elist) {
            this.name = name;
            this.elist = elist;
        }
        
        @Override
        public boolean isEditable() {
            return elist != null;
        }

        @Override
        public String getName() {
            return elist == null ? name : elist.value();
        }

        @Override
        public void move(IModelExtension item, int pos) {
            handler.move(ctx, model, name, item, pos);
        }

        @Override
        public IModelExtension add() {
            return handler.add(ctx, model, name);
        }

        @Override
        public void remove(IModelExtension item) {
            handler.remove(ctx, model, name, item);
        }
        
    }

}
