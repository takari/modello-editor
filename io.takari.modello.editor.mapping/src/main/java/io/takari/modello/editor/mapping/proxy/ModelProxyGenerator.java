package io.takari.modello.editor.mapping.proxy;

import io.takari.modello.editor.mapping.api.IModelAccessor;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.mapping.model.IModelExtension;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ModelProxyGenerator {
    
    private final Map<Method, Method> delegateMethods;
    
    public ModelProxyGenerator() {
        this.delegateMethods = new HashMap<Method, Method>();
    }
    
    public <T extends IModel> T createDirectProxy(Class<T> modelClass) {
        return createProxy(DirectModelAccessor.INSTANCE, modelClass);
    }
    
    public <T extends IModel> T createProxy(IModelAccessor<?> acc, Class<T> modelClass) {
        return createProxy(acc, modelClass, null, null);
    }
    
    public <T extends IModel> T createProxy(IModelAccessor<?> acc, Class<T> modelClass, IModelExtension parent, String parentProperty) {
        
        ModelDelegate delegate = new ModelDelegate(this, acc, modelClass);
        
        IModelExtension proxy = createProxy(modelClass, delegate);
        delegate._init(parent, parentProperty, proxy);
        
        @SuppressWarnings("unchecked")
        T proxiedModel = (T) proxy;
        
        return proxiedModel;
    }

    private IModelExtension createProxy(Class<? extends IModel> modelClass, ModelDelegate delegate) {
        
        Enhancer enh = new Enhancer();
        enh.setClassLoader(Thread.currentThread().getContextClassLoader());
        
        enh.setSuperclass(modelClass);
        enh.setInterfaces(new Class[]{ IModelExtension.class });
        enh.setCallback(new ModelExtensionInterceptor(delegate));
        
        return (IModelExtension) enh.create();
    }
    
    private Method findExtensionMethod(Method method) {
        
        Method dmethod = delegateMethods.get(method);
        
        if(dmethod == null && !delegateMethods.containsKey(method)) {
            for(Method m: IModelExtension.class.getMethods()) {
                if(m.getName().equals(method.getName()) && 
                        Arrays.equals(m.getParameterTypes(), method.getParameterTypes()) && 
                        m.getReturnType().isAssignableFrom(method.getReturnType())) {
                    dmethod = m;
                    break;
                }
            }
            delegateMethods.put(method, dmethod);
        }
        return dmethod;
    }
    
    private class ModelExtensionInterceptor implements MethodInterceptor {
        
        private ModelDelegate delegate;
        
        public ModelExtensionInterceptor(ModelDelegate delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            
            if(method.getName().equals("equals")) {
               if(obj == args[0]) return true;
               return delegate.equals(args[0]);
            }
            
            if(method.getName().equals("hashCode")) {
                return delegate.hashCode();
             }
             
            Method m = findExtensionMethod(method);
            if(m != null) {
                return m.invoke(delegate, args);
            }
            if(method.getDeclaringClass().equals(IModelExtension.class)) {
                return proxy.invoke(delegate, args);
            }
            return delegate._invokePropertyMethod(method, args, proxy);
        }
    }
}
