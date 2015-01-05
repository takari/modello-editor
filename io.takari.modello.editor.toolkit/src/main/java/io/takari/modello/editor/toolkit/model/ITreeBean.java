package io.takari.modello.editor.toolkit.model;

import java.util.List;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

public interface ITreeBean {
    
    List<? extends ITreeBean> getChildren();
    
    boolean isHasChildren();
    
    Image getImage();
    
    StyledString getLabel();
    
}
