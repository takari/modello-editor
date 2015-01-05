package io.takari.modello.editor.mapping.proxy;

import io.takari.modello.editor.mapping.api.IModelAccessor;
import io.takari.modello.editor.mapping.api.NoAccessorException;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.mapping.model.IModelExtension;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.core.ReflectUtils;

public class InvocationHandlerManager {
    
    private volatile Map<Class<? extends IModel>, IInvocationHandler> handlers;
    
    public InvocationHandlerManager() {
        handlers = new HashMap<>();
    }
    
    public IInvocationHandler get(Class<? extends IModel> modelClass) {
        IInvocationHandler h = handlers.get(modelClass);
        if(h == null) {
            synchronized(handlers) {
                h = handlers.get(modelClass);
                if(h == null) {
                    h = new MethodHandler(modelClass);
                    handlers.put(modelClass, h);
                }
            }
        }
        return h;
    }
    
    private Method getModelMethod(Class<? extends IModel> modelClass, String name) {
        for(Method m: modelClass.getMethods()) {
            if(m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }
    
    private class MethodHandler implements IInvocationHandler {
        
        private Map<String, String> methods = new HashMap<String, String>();
        private Map<String, ICaller> callers = new HashMap<>();
        
        public MethodHandler(final Class<? extends IModel> modelClass) {
            
            PropertyDescriptor[] pds = ReflectUtils.getBeanGetters(modelClass);
            
            for(final PropertyDescriptor pd: pds) {
                String name = pd.getName();
                
                String cappedName = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                cappedName = singular(cappedName);
                Method getter = pd.getReadMethod();
                Method setter = pd.getWriteMethod();
                Method adder = getModelMethod(modelClass, "add" + cappedName);
                Method remover = getModelMethod(modelClass, "remove" + cappedName);
                Method toucher = getModelMethod(modelClass, "touch" + cappedName);
                
                methods.put(getter.getName(), "get." + name);
                if(setter != null) methods.put(setter.getName(), "set." + name);
                if(adder != null) methods.put(adder.getName(), "add." + name);
                if(remover != null) methods.put(remover.getName(), "remove." + name);
                if(toucher != null) methods.put(toucher.getName(), "touch." + name);
                
                callers.put("get." + name, new ICaller() {
                    @Override
                    public Object call(IModelAccessor<?> ctx, IModelExtension model, Object[] args) {
                        return ctx.get(model, pd);
                    }
                });
                callers.put("set." + name, new ICaller() {
                    @Override
                    public Object call(IModelAccessor<?> ctx, IModelExtension model, Object[] args) {
                        ctx.set(model, pd, args[0]);
                        return null;
                    }
                });
                callers.put("add." + name, new ICaller() {
                    @Override
                    public Object call(IModelAccessor<?> ctx, IModelExtension model, Object[] args) {
                        return ctx.add(model, pd);
                    }
                });
                callers.put("remove." + name, new ICaller() {
                    @Override
                    public Object call(IModelAccessor<?> ctx, IModelExtension model, Object[] args) {
                        ctx.remove(model, pd, args[0]);
                        return null;
                    }
                });
                callers.put("move." + name, new ICaller() {
                    @Override
                    public Object call(IModelAccessor<?> ctx, IModelExtension model, Object[] args) {
                        ctx.move(model, pd, args[0], (int) args[1]);
                        return null;
                    }
                });
                callers.put("touch." + name, new ICaller() {
                    @Override
                    public Object call(IModelAccessor<?> ctx, IModelExtension model, Object[] args) {
                        ctx.touch(model, pd);
                        return null;
                    }
                });
                callers.put("touch", new ICaller() {
                    @Override
                    public Object call(IModelAccessor<?> ctx, IModelExtension model, Object[] args) {
                        // touch self
                        ctx.touch(model, null);
                        return null;
                    }
                });
            }
        }
        
        @Override
        public Object handle(IModelAccessor<?> ctx, IModelExtension model, Method method, Object[] args) {
            String call = methods.get(method.getName());
            if(call == null) throw new NoAccessorException();
            return invoke(ctx, model, call, args);
        }
        
        @Override
        public void touch(IModelAccessor<?> ctx, IModelExtension model, String property) {
            invoke(ctx, model, "touch" + (property != null ? ("." + property) : ""), null);
        }
        
        @Override
        public Object get(IModelAccessor<?> ctx, IModelExtension model, String property) {
            return invoke(ctx, model, "get." + property, null);
        }
        
        @Override
        public void set(IModelAccessor<?> ctx, IModelExtension model, String property, Object value) {
            invoke(ctx, model, "set." + property, new Object[]{ value });
        }
        
        @Override
        public IModelExtension add(IModelAccessor<?> ctx, IModelExtension model, String property) {
            return (IModelExtension) invoke(ctx, model, "add." + property, null);
        }
        
        @Override
        public void remove(IModelAccessor<?> ctx, IModelExtension model, String property, IModelExtension item) {
            invoke(ctx, model, "remove." + property, new Object[]{ item });
        }
        
        @Override
        public void move(IModelAccessor<?> ctx, IModelExtension model, String property, IModelExtension item, int pos) {
            invoke(ctx, model, "move." + property, new Object[]{ item, pos });
        }
        
        private Object invoke(IModelAccessor<?> ctx, IModelExtension model, String name, Object[] args) {
            ICaller caller = callers.get(name);
            if(caller == null) {
                throw new NoAccessorException();
            }
            
            try {
                return caller.call(ctx, model, args);
            } catch(NoAccessorException e) {
                callers.remove(name);
                throw e;
            }
        }
    }
    
    private static interface ICaller {
        Object call(IModelAccessor<?> ctx, IModelExtension model, Object[] args);
    }
    
    private static String singular( String name )
    {
        if ( name == null || name.trim().isEmpty() )
        {
            return name;
        }

        if ( name.endsWith( "ies" ) )
        {
            return name.substring( 0, name.length() - 3 ) + "y";
        }
        else if ( name.endsWith( "es" ) && name.endsWith( "ches" ) )
        {
            return name.substring( 0, name.length() - 2 );
        }
        else if ( name.endsWith( "xes" ) )
        {
            return name.substring( 0, name.length() - 2 );
        }
        else if ( name.endsWith( "s" ) && ( name.length() != 1 ) )
        {
            return name.substring( 0, name.length() - 1 );
        }

        return name;
    }

}
