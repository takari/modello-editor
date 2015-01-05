package io.takari.modello.editor.toolkit;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class ToolkitPlugin extends AbstractUIPlugin {
    
    public static final String ID = "io.takari.modello.editor.toolkit";
    
    private static ToolkitPlugin INSTANCE;

    public static ToolkitPlugin getInstance() {
        return INSTANCE;
    }
    
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        INSTANCE = this;
    }
    
    public static void logException(Exception e) {
        logException(e.getMessage(), e);
    }
    public static void logException(String message,  Exception e) {
        getInstance().getLog().log(new Status(IStatus.ERROR, ID, e.getMessage(), e));
    }

}
