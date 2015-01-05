package io.takari.modello.editor.impl.model.plugin.xsd;

import io.takari.modello.editor.mapping.dom.annotations.DefValue;
import io.takari.modello.editor.mapping.dom.annotations.XMLAttr;
import io.takari.modello.editor.toolkit.model.AbstractModelBean;

public class MXsdClassMetadata extends AbstractModelBean {
    
    @XMLAttr("xsd.compositor")
    @DefValue("all")
    private String xsdCompositor;

    public String getXsdCompositor() {
        return xsdCompositor;
    }
    
    public void setXsdCompositor(String xsdCompositor) {
        this.xsdCompositor = xsdCompositor;
    }
    
    public String getLabelValue() {
        return "";
    }
}
