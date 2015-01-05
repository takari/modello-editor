package io.takari.modello.editor.mapping;

import io.takari.modello.editor.mapping.proxy.InvocationHandlerManager;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class MappingPlugin implements BundleActivator {
    
    private static MappingPlugin instance;
    
    private final InvocationHandlerManager ihMgr;
    
    public MappingPlugin() {
        ihMgr = new InvocationHandlerManager();
    }
    
    @Override
    public void start(BundleContext context) throws Exception {
        instance = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        instance = null;
    }
    
    static MappingPlugin getInstance() {
        return instance;
    }
    
    public static InvocationHandlerManager getInvocationHandlerCache() {
        return getInstance().ihMgr;
    }
    
}
