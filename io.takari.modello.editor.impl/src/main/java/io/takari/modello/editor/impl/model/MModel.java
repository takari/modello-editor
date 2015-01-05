package io.takari.modello.editor.impl.model;

import java.util.Arrays;
import java.util.List;

import io.takari.modello.editor.mapping.dom.annotations.XMLSection;
import io.takari.modello.editor.toolkit.model.AbstractModelBean;
import io.takari.modello.editor.toolkit.model.ITreeBean;

public class MModel extends AbstractModelBean implements ITreeBean {
    
    @XMLSection
    private MModelDetails details;
    
    @XMLSection("classes")
    private MClasses classes;
    
    @XMLSection("interfaces")
    private MInterfaces interfaces;

    public MModelDetails getDetails() {
        return this.details;
    }

    public MClasses getClasses() {
        return this.classes;
    }

    public MInterfaces getInterfaces() {
        return this.interfaces;
    }

    @Override
    public List<? extends ITreeBean> getChildren() {
        return Arrays.asList(getDetails(), getClasses(), getInterfaces());
    }

    @Override
    public String getLabelValue() {
        return "";
    }
}
