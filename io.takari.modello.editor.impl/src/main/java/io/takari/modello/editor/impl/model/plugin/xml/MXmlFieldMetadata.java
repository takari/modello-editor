package io.takari.modello.editor.impl.model.plugin.xml;

import io.takari.modello.editor.mapping.dom.annotations.DefValue;
import io.takari.modello.editor.mapping.dom.annotations.XMLAttr;
import io.takari.modello.editor.toolkit.model.AbstractModelBean;

public class MXmlFieldMetadata extends AbstractModelBean {
    

    @XMLAttr("xml.attribute")
    private boolean xmlAttribute;
    
    @XMLAttr("xml.content")
    private boolean xmlContent;
    
    @XMLAttr("xml.trim")
    @DefValue("true")
    private boolean xmlTrim;
    
    @XMLAttr("xml.tagName")
    private String xmlTagName;
    
    @XMLAttr("xml.format")
    private String xmlFormat;
    
    @XMLAttr("xml.transient")
    private boolean xmlTransient;
    
    @XMLAttr("xml.insertParentFieldsUpTo")
    private String xmlInsertParentFieldsUpTo;
    
    @XMLAttr("association/xml.tagName")
    private String xmlAssociationTagName;

    @XMLAttr("association/xml.itemsStyle")
    @DefValue("wrapped")
    private String xmlItemsStyle;

    @XMLAttr("association/xml.mapStyle")
    @DefValue("inline")
    private String xmlMapStyle;

    @XMLAttr("association/xml.reference")
    private String xmlReference;
    
    public boolean isXmlAttribute() {
        return xmlAttribute;
    }

    public void setXmlAttribute(boolean xmlAttribute) {
        this.xmlAttribute = xmlAttribute;
    }

    public boolean isXmlContent() {
        return xmlContent;
    }

    public void setXmlContent(boolean xmlContent) {
        this.xmlContent = xmlContent;
    }

    public boolean isXmlTrim() {
        return xmlTrim;
    }

    public void setXmlTrim(boolean xmlTrim) {
        this.xmlTrim = xmlTrim;
    }

    public String getXmlTagName() {
        return xmlTagName;
    }

    public void setXmlTagName(String xmlTagName) {
        this.xmlTagName = xmlTagName;
    }

    public String getXmlFormat() {
        return xmlFormat;
    }

    public void setXmlFormat(String xmlFormat) {
        this.xmlFormat = xmlFormat;
    }

    public boolean isXmlTransient() {
        return xmlTransient;
    }

    public void setXmlTransient(boolean xmlTransient) {
        this.xmlTransient = xmlTransient;
    }

    public String getXmlInsertParentFieldsUpTo() {
        return xmlInsertParentFieldsUpTo;
    }

    public void setXmlInsertParentFieldsUpTo(String xmlInsertParentFieldsUpTo) {
        this.xmlInsertParentFieldsUpTo = xmlInsertParentFieldsUpTo;
    }

    public String getXmlAssociationTagName() {
        return this.xmlAssociationTagName;
    }

    public void setXmlAssociationTagName(String xmlAssociationTagName) {
        this.xmlAssociationTagName = xmlAssociationTagName;
    }

    public String getXmlItemsStyle() {
        return this.xmlItemsStyle;
    }

    public void setXmlItemsStyle(String xmlItemsStyle) {
        this.xmlItemsStyle = xmlItemsStyle;
    }

    public String getXmlMapStyle() {
        return this.xmlMapStyle;
    }

    public void setXmlMapStyle(String xmlMapStyle) {
        this.xmlMapStyle = xmlMapStyle;
    }

    public String getXmlReference() {
        return this.xmlReference;
    }

    public void setXmlReference(String xmlReference) {
        this.xmlReference = xmlReference;
    }

    public String getLabelValue() {
        return "";
    }
}
