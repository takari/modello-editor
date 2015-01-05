package io.takari.modello.editor.impl.model.plugin.model;

import io.takari.modello.editor.mapping.dom.annotations.XMLAttr;
import io.takari.modello.editor.toolkit.model.AbstractModelBean;

public class MModelClassMetadata extends AbstractModelBean {
    
    @XMLAttr("rootElement")
    private boolean modelRootElement;
    
    @XMLAttr("sourceTracker")
    private String modelSourceTracker;
    
    @XMLAttr("locationTracker")
    private String modelLocationTracker;
    
    public boolean isModelRootElement() {
        return modelRootElement;
    }

    public void setModelRootElement(boolean modelRootElement) {
        this.modelRootElement = modelRootElement;
    }

    public String getModelSourceTracker() {
        return modelSourceTracker;
    }

    public void setModelSourceTracker(String modelSourceTracker) {
        this.modelSourceTracker = modelSourceTracker;
    }

    public String getModelLocationTracker() {
        return modelLocationTracker;
    }

    public void setModelLocationTracker(String modelLocationTracker) {
        this.modelLocationTracker = modelLocationTracker;
    }

    public String getLabelValue() {
        return "";
    }
}
