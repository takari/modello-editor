package io.takari.modello.editor.impl.model;

import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.graphics.Image;

import io.takari.modello.editor.mapping.dom.annotations.XMLText;
import io.takari.modello.editor.toolkit.model.ITreeBean;

public class MInterface extends AbstractType implements ITreeBean {
    
    @XMLText("superInterface")
    private String superInterface;

    public String getSuperInterface() {
        return this.superInterface;
    }

    public void setSuperInterface(String superInterface) {
        this.superInterface = superInterface;
    }
    
    @Override
    public Image getImage() {
        return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_INTERFACE);
    }
    
    public boolean isHasChildren() {
        return false;
    }

}
