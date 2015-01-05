package io.takari.modello.editor.impl.model;

import io.takari.modello.editor.mapping.dom.annotations.XMLSection;
import io.takari.modello.editor.mapping.dom.annotations.XMLText;
import io.takari.modello.editor.toolkit.model.ITreeBean;

import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.graphics.Image;

public class MModelDetails extends AbstractModelloModel implements ITreeBean {

    @XMLText("id")
    private String id;
    
    @XMLText("versionDefinition/type")
    private String versionDefinition;
    
    @XMLText("versionDefinition/name")
    private String versionField;
    
    @XMLSection("defaults/default[key]")
    private MDefaults defaults;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersionDefinition() {
        return versionDefinition;
    }

    public void setVersionDefinition(String versionDefinition) {
        this.versionDefinition = versionDefinition;
    }

    public String getVersionField() {
        return versionField;
    }

    public void setVersionField(String versionField) {
        this.versionField = versionField;
    }

    public MDefaults getDefaults() {
        return defaults;
    }

    @Override
    public String getLabelValue() {
        return "Model";
    }
    
    @Override
    public Image getImage() {
        return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_PACKAGE);
    }
    
    public boolean isHasChildren() {
        return false;
    }

}
