package io.takari.modello.editor.toolkit.model;

import io.takari.modello.editor.mapping.annotations.Observe;
import io.takari.modello.editor.mapping.model.IModel;

import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

public abstract class AbstractModelBean implements IModel {
    
    public IModel getParent() {
        return null;
    }
    
    public boolean isHasChildren() {
        return true;
    }
    
    public List<? extends ITreeBean> getChildren() {
        return null;
    }
    
    public abstract String getLabelValue();
    
    @Observe("labelValue")
    public StyledString getLabel() {
        return new StyledString(getLabelValue());
    }
    
    public Image getImage() {
        return null;
    }
    
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        // NOOP
    }
    
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        // NOOP
    }
}
