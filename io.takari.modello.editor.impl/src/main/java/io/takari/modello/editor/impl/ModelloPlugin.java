package io.takari.modello.editor.impl;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class ModelloPlugin extends AbstractUIPlugin {
    
    public static final String ID = "io.takari.modello.editor.impl";
    
    private static ModelloPlugin instance;
    
    public ModelloPlugin() {
    }
    
    @Override
    public void start(BundleContext context) throws Exception {
        instance = this;
        super.start(context);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        instance = null;
        super.stop(context);
    }
    
    static ModelloPlugin getInstance() {
        return instance;
    }
    
    public static void logException(Exception e) {
        logException(e.getMessage(), e);
    }
    public static void logException(String message,  Exception e) {
        getInstance().getLog().log(new Status(IStatus.ERROR, ID, e.getMessage(), e));
    }

}
