package io.takari.modello.editor.impl.model.plugin.java;

import io.takari.modello.editor.mapping.dom.annotations.DefValue;
import io.takari.modello.editor.mapping.dom.annotations.XMLAttr;
import io.takari.modello.editor.toolkit.model.AbstractModelBean;

public class MJavaFieldMetadata extends AbstractModelBean {
    
    // FIELD METADATA
    
    @XMLAttr("java.getter")
    @DefValue("true")
    private boolean javaGetter;
    
    @XMLAttr("java.setter")
    @DefValue("true")
    private boolean javaSetter;
    
    // ASSOCIATION METADATA
    
    @XMLAttr("association/java.adder")
    @DefValue("true")
    private boolean javaAdder;

    @XMLAttr("association/java.bidi")
    @DefValue("true")
    private boolean javaBidi;

    @XMLAttr("association/java.useInterface")
    private String javaUseInterface;

    @XMLAttr("association/java.init")
    @DefValue("lazy")
    private String javaInit;

    @XMLAttr("association/java.clone")
    private String javaClone;

    public boolean isJavaGetter() {
        return javaGetter;
    }

    public void setJavaGetter(boolean javaGetter) {
        this.javaGetter = javaGetter;
    }

    public boolean isJavaSetter() {
        return javaSetter;
    }

    public void setJavaSetter(boolean javaSetter) {
        this.javaSetter = javaSetter;
    }


    public boolean isJavaAdder() {
        return this.javaAdder;
    }

    public void setJavaAdder(boolean javaAdder) {
        this.javaAdder = javaAdder;
    }

    public boolean isJavaBidi() {
        return this.javaBidi;
    }

    public void setJavaBidi(boolean javaBidi) {
        this.javaBidi = javaBidi;
    }

    public String getJavaUseInterface() {
        return this.javaUseInterface;
    }

    public void setJavaUseInterface(String javaUseInterface) {
        this.javaUseInterface = javaUseInterface;
    }

    public String getJavaInit() {
        return this.javaInit;
    }

    public void setJavaInit(String javaInit) {
        this.javaInit = javaInit;
    }

    public String getJavaClone() {
        return this.javaClone;
    }

    public void setJavaClone(String javaClone) {
        this.javaClone = javaClone;
    }

    public String getLabelValue() {
        return "";
    }
}
