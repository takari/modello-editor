package io.takari.modello.editor.impl.model;

import io.takari.modello.editor.mapping.dom.annotations.DefValue;
import io.takari.modello.editor.mapping.dom.annotations.XMLText;
import io.takari.modello.editor.toolkit.model.AbstractModelBean;

public class MDefaults extends AbstractModelBean {

    @XMLText("package/value")
    @DefValue("model")
    private String defaultPackage;
    
    @XMLText("java.util.List/value")
    @DefValue("new java.util.ArrayList<?>()")
    private String listImpl;
    
    @XMLText("java.util.Set/value")
    @DefValue("new java.util.HashSet<?>()")
    private String setImpl;
    
    @XMLText("java.util.Map/value")
    @DefValue("new java.util.HashMap()")
    private String mapImpl;
    
    @XMLText("java.util.Properties/value")
    @DefValue("new java.util.Properties()")
    private String propertiesImpl;
    
    @XMLText("strictXmlAttributes/value")
    @DefValue("true")
    private boolean strictXmlAttrs;
    
    public String getDefaultPackage() {
        return defaultPackage;
    }

    public void setDefaultPackage(String defaultPackage) {
        this.defaultPackage = defaultPackage;
    }

    public String getListImpl() {
        return listImpl;
    }

    public void setListImpl(String listImpl) {
        this.listImpl = listImpl;
    }

    public String getSetImpl() {
        return setImpl;
    }

    public void setSetImpl(String setImpl) {
        this.setImpl = setImpl;
    }

    public String getMapImpl() {
        return mapImpl;
    }

    public void setMapImpl(String mapImpl) {
        this.mapImpl = mapImpl;
    }

    public String getPropertiesImpl() {
        return propertiesImpl;
    }

    public void setPropertiesImpl(String propertiesImpl) {
        this.propertiesImpl = propertiesImpl;
    }

    public boolean isStrictXmlAttrs() {
        return strictXmlAttrs;
    }

    public void setStrictXmlAttrs(boolean strictXmlAttrs) {
        this.strictXmlAttrs = strictXmlAttrs;
    }

    @Override
    public String getLabelValue() {
        return "Defaults";
    }
    
}
