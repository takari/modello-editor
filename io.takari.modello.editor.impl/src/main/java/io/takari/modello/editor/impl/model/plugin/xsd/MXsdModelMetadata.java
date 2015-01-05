package io.takari.modello.editor.impl.model.plugin.xsd;

import io.takari.modello.editor.mapping.dom.annotations.XMLAttr;
import io.takari.modello.editor.toolkit.model.AbstractModelBean;

public class MXsdModelMetadata extends AbstractModelBean {
    
    @XMLAttr("xsd.namespace")
    private String xsdNamespace;
    
    @XMLAttr("xsd.targetNamespace")
    private String xsdTargetNamespace;
    
    public String getXsdNamespace() {
        return xsdNamespace;
    }

    public void setXsdNamespace(String xsdNamespace) {
        this.xsdNamespace = xsdNamespace;
    }

    public String getXsdTargetNamespace() {
        return xsdTargetNamespace;
    }

    public void setXsdTargetNamespace(String xsdTargetNamespace) {
        this.xsdTargetNamespace = xsdTargetNamespace;
    }

    @Override
    public String getLabelValue() {
        return "";
    }
}
