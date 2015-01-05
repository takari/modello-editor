package io.takari.modello.editor.impl.model.plugin.xml;

import io.takari.modello.editor.mapping.dom.annotations.XMLAttr;
import io.takari.modello.editor.toolkit.model.AbstractModelBean;

public class MXmlClassMetadata extends AbstractModelBean {
    
    @XMLAttr("xml.tagName")
    private String xmlTagName;
    
    @XMLAttr("xml.standaloneRead")
    private boolean xmlStandaloneRead;
    
    public String getXmlTagName() {
        return xmlTagName;
    }

    public void setXmlTagName(String xmlTagName) {
        this.xmlTagName = xmlTagName;
    }

    public boolean isXmlStandaloneRead() {
        return xmlStandaloneRead;
    }

    public void setXmlStandaloneRead(boolean xmlStandaloneRead) {
        this.xmlStandaloneRead = xmlStandaloneRead;
    }
    
    public String getLabelValue() {
        return "";
    }
}
