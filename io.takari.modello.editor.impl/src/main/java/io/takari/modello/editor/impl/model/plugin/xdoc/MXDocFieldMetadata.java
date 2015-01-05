package io.takari.modello.editor.impl.model.plugin.xdoc;

import io.takari.modello.editor.mapping.dom.annotations.DefValue;
import io.takari.modello.editor.mapping.dom.annotations.XMLAttr;
import io.takari.modello.editor.toolkit.model.AbstractModelBean;

public class MXDocFieldMetadata extends AbstractModelBean {
    
    @XMLAttr("xdoc.separator")
    @DefValue("none")
    private String xdocSeparator;
    
    public String getXdocSeparator() {
        return xdocSeparator;
    }

    public void setXdocSeparator(String xdocSeparator) {
        this.xdocSeparator = xdocSeparator;
    }

    public String getLabelValue() {
        return "";
    }
}
