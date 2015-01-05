package io.takari.modello.editor.impl.model;

import io.takari.modello.editor.mapping.dom.annotations.XMLText;
import io.takari.modello.editor.toolkit.model.ITreeBean;

import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.graphics.Image;

public class MField extends AbstractModelloModel implements ITreeBean {
    
    @XMLText("alias")
    private String alias;
    
    @XMLText("type")
    private String type;
    
    @XMLText("defaultValue")
    private String defaultValue;
    
    @XMLText("required")
    private boolean required;
    
    @XMLText("identifier")
    private boolean identifier;
    
    @XMLText("association/type")
    private String associationType;

    @XMLText("association/multiplicity")
    private String multiplicity;
    
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isIdentifier() {
        return identifier;
    }

    public void setIdentifier(boolean identifier) {
        this.identifier = identifier;
    }
    
    public String getAssociationType() {
        return associationType;
    }
    
    public void setAssociationType(String associationType) {
        this.associationType = associationType;
    }

    public String getMultiplicity() {
        return this.multiplicity;
    }

    public void setMultiplicity(String multiplicity) {
        this.multiplicity = multiplicity;
    }

    @Override
    public Image getImage() {
        return JavaUI.getSharedImages().getImage(ISharedImages.IMG_FIELD_PUBLIC);
    }
    
    public boolean isHasChildren() {
        return false;
    }

}
