package io.takari.modello.editor.mapping.dom;

import io.takari.modello.editor.mapping.api.IPropertyAccessorManager;
import io.takari.modello.editor.mapping.dom.accessor.DomModelAccessor;
import io.takari.modello.editor.mapping.dom.accessor.DomPropertyAccessorManager;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class DomMappingPlugin implements BundleActivator {

    private static DomMappingPlugin instance;
    
    private final IPropertyAccessorManager<DomModelAccessor> domMgr;
    
    public DomMappingPlugin() {
        domMgr = new DomPropertyAccessorManager();
    }
    
    @Override
    public void start(BundleContext context) throws Exception {
        instance = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        instance = null;
    }
    
    static DomMappingPlugin getInstance() {
        return instance;
    }
    
    public static IPropertyAccessorManager<DomModelAccessor> getDomAccessorManager() {
        return getInstance().domMgr;
    }
    
    public static void trace(String msg) {
        //System.out.println(msg);
    }

}
