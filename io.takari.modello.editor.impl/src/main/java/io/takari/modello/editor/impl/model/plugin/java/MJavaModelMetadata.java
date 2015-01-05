package io.takari.modello.editor.impl.model.plugin.java;

import io.takari.modello.editor.impl.model.MModelDetails;
import io.takari.modello.editor.mapping.dom.annotations.DefValue;
import io.takari.modello.editor.mapping.dom.annotations.XMLAttr;
import io.takari.modello.editor.toolkit.model.AbstractModelBean;

public class MJavaModelMetadata extends AbstractModelBean {

    @XMLAttr("java.suppressAllWarnings")
    @DefValue("true")
    private boolean javaSuppressAllWarnings;
    
    public MModelDetails getDetails() {
        return (MModelDetails) getParent();
    }
    
    public boolean isJavaSuppressAllWarnings() {
        return javaSuppressAllWarnings;
    }

    public void setJavaSuppressAllWarnings(boolean javaSuppressAllWarnings) {
        this.javaSuppressAllWarnings = javaSuppressAllWarnings;
    }
    
    @Override
    public String getLabelValue() {
        return "";
    }
}
