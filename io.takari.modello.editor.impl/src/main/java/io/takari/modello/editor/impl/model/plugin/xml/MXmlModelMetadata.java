package io.takari.modello.editor.impl.model.plugin.xml;

import io.takari.modello.editor.impl.model.MModelDetails;
import io.takari.modello.editor.mapping.dom.annotations.XMLAttr;
import io.takari.modello.editor.toolkit.model.AbstractModelBean;

public class MXmlModelMetadata extends AbstractModelBean {
    
    @XMLAttr("xml.namespace")
    private String xmlNamespace;
    
    @XMLAttr("xml.schemaLocation")
    private String xmlSchemaLocation;
    
    public MModelDetails getDetails() {
        return (MModelDetails) getParent();
    }
    
    public String getXmlNamespace() {
        return xmlNamespace;
    }

    public void setXmlNamespace(String xmlNamespace) {
        this.xmlNamespace = xmlNamespace;
    }

    public String getXmlSchemaLocation() {
        return xmlSchemaLocation;
    }

    public void setXmlSchemaLocation(String xmlSchemaLocation) {
        this.xmlSchemaLocation = xmlSchemaLocation;
    }
    
    @Override
    public String getLabelValue() {
        return "";
    }
}
