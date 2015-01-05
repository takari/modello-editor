package io.takari.modello.editor.impl.model.plugin.java;

import io.takari.modello.editor.mapping.dom.annotations.DefValue;
import io.takari.modello.editor.mapping.dom.annotations.XMLAttr;
import io.takari.modello.editor.toolkit.model.AbstractModelBean;

public class MJavaClassMetadata extends AbstractModelBean {
    
    @XMLAttr("java.enabled")
    @DefValue("true")
    private boolean javaEnabled;
    
    @XMLAttr("java.abstract")
    private boolean javaAbstract;
    
    @XMLAttr("java.clone")
    private String javaCloneMode;
    
    @XMLAttr("java.clone.hook")
    private String javaCloneHook;
    
    @XMLAttr("java.toString")
    private boolean javaGenerateToString;
    
    @XMLAttr("java.builder")
    private boolean javaGenerateBuilder;
    
    @XMLAttr("java.staticCreator")
    private boolean javaGenerateStaticCreators;
    
    public boolean isJavaEnabled() {
        return this.javaEnabled;
    }

    public void setJavaEnabled(boolean javaEnabled) {
        this.javaEnabled = javaEnabled;
    }

    public boolean isJavaAbstract() {
        return this.javaAbstract;
    }

    public void setJavaAbstract(boolean javaAbstract) {
        this.javaAbstract = javaAbstract;
    }

    public String getJavaCloneMode() {
        return javaCloneMode;
    }

    public void setJavaCloneMode(String javaCloneMode) {
        this.javaCloneMode = javaCloneMode;
    }

    public String getJavaCloneHook() {
        return javaCloneHook;
    }

    public void setJavaCloneHook(String javaCloneHook) {
        this.javaCloneHook = javaCloneHook;
    }

    public boolean isJavaGenerateToString() {
        return javaGenerateToString;
    }

    public void setJavaGenerateToString(boolean javaGenerateToString) {
        this.javaGenerateToString = javaGenerateToString;
    }

    public boolean isJavaGenerateBuilder() {
        return javaGenerateBuilder;
    }

    public void setJavaGenerateBuilder(boolean javaGenerateBuilder) {
        this.javaGenerateBuilder = javaGenerateBuilder;
    }

    public boolean isJavaGenerateStaticCreators() {
        return javaGenerateStaticCreators;
    }

    public void setJavaGenerateStaticCreators(boolean javaGenerateStaticCreators) {
        this.javaGenerateStaticCreators = javaGenerateStaticCreators;
    }

    public String getLabelValue() {
        return "";
    }
}
