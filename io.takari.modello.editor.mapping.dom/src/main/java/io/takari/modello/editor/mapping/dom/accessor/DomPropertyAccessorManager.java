package io.takari.modello.editor.mapping.dom.accessor;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.takari.modello.editor.mapping.api.IPropertyAccessor;
import io.takari.modello.editor.mapping.api.IPropertyAccessorManager;
import io.takari.modello.editor.mapping.dom.accessor.ValueAccessor.VType;
import io.takari.modello.editor.mapping.dom.annotations.DefValue;
import io.takari.modello.editor.mapping.dom.annotations.XMLAttr;
import io.takari.modello.editor.mapping.dom.annotations.XMLCData;
import io.takari.modello.editor.mapping.dom.annotations.XMLList;
import io.takari.modello.editor.mapping.dom.annotations.XMLSection;
import io.takari.modello.editor.mapping.dom.annotations.XMLText;
import io.takari.modello.editor.mapping.model.IModel;

public class DomPropertyAccessorManager implements IPropertyAccessorManager<DomModelAccessor> {

    private final Map<String, IPropertyAccessor<DomModelAccessor>> accessorCache;
    
    public DomPropertyAccessorManager() {
        accessorCache = new HashMap<>();
    }
    
    @Override
    public IPropertyAccessor<DomModelAccessor> getAccessor(Class<? extends IModel> modelClass, PropertyDescriptor pd) {
        
        String key = modelClass.getName() + "." + (pd != null ? pd.getName() : "$self");
        if(!accessorCache.containsKey(key)) {
            synchronized(accessorCache) {
                if(!accessorCache.containsKey(key)) {
                    IPropertyAccessor<DomModelAccessor> accessor = createAccessor(modelClass, pd);
                    accessorCache.put(key, accessor);
                    return accessor;
                }
            }
        }
        return accessorCache.get(key);
    }
        
    private IPropertyAccessor<DomModelAccessor> createAccessor(Class<? extends IModel> modelClass, PropertyDescriptor pd) {
        
        if(pd == null) {
            return new ModelSelfAccessor();
        }
        
        Field fld = findField(modelClass, pd.getName());
        Method readMethod = pd.getReadMethod();
        
        Map<Class<? extends Annotation>, Annotation> annotations = getAnnotations(fld, readMethod);
        
        Class<?> type = pd.getPropertyType();
        
        if(IModel.class.isAssignableFrom(type)) {
            XMLSection section = (XMLSection) annotations.get(XMLSection.class);
            if(section == null) {
                return null;
            }
            
            return new ModelSectionAccessor(type.asSubclass(IModel.class), pd.getName(), section.value());
        }
        
        if(type.isAssignableFrom(List.class)) {
            XMLList list = (XMLList) annotations.get(XMLList.class);
            if(list == null) {
                return null;
            }
            
            if(fld == null) {
                throw new IllegalStateException("Cannot find field for " + modelClass.getName() + "." + pd.getName());
            }
            
            Class<?> itemType = null;
            
            Type gtype = fld.getGenericType();
            if(gtype instanceof ParameterizedType) {
                ParameterizedType ptype = (ParameterizedType) gtype;
                Type targ = ptype.getActualTypeArguments()[0];
                if(targ instanceof Class) {
                    itemType = (Class<?>) targ;
                }
            }
            
            if(itemType == null) {
                throw new IllegalStateException("Cannot infer component type for " + modelClass.getName() + "." + pd.getName());
            }
            
            if(!IModel.class.isAssignableFrom(itemType)) {
                throw new IllegalStateException("Unsupported component type for " + modelClass.getName() + "." + pd.getName());
            }
            
            return new ModelListAccessor(itemType.asSubclass(IModel.class), pd.getName(), list.value());
        }
        
        XMLText text = (XMLText) annotations.get(XMLText.class);
        XMLCData cdata= (XMLCData) annotations.get(XMLCData.class);
        XMLAttr attr= (XMLAttr) annotations.get(XMLAttr.class);
        DefValue defValue = (DefValue) annotations.get(DefValue.class);
        String def = defValue == null ? null : defValue.value();
        if(text != null) {
            return new ValueAccessor(type, pd.getName(), text.value(), VType.TEXT, def);
        }
        if(cdata != null) {
            return new ValueAccessor(type, pd.getName(), cdata.value(), VType.CDATA, def);
        }
        if(attr != null) {
            return new ValueAccessor(type, pd.getName(), attr.value(), VType.ATTR, def);
        }
        
        return null;
    }
    
    private Map<Class<? extends Annotation>, Annotation> getAnnotations(AccessibleObject ... objs) {
        Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        for(AccessibleObject obj: objs) {
            if(obj != null) {
                for(Annotation a: obj.getAnnotations()) {
                    annotations.put(a.annotationType(), a);
                }
            }
        }
        return annotations;
    }
    
    private static Field findField(Class<?> clazz, String name) {
        
        while(clazz != null) {
            for(Field f: clazz.getDeclaredFields()) {
                if(f.getName().equals(name)) return f;
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }
    
}
