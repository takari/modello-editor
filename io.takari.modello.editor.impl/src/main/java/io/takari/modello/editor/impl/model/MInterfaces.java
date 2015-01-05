package io.takari.modello.editor.impl.model;

import io.takari.modello.editor.mapping.annotations.EditableList;
import io.takari.modello.editor.mapping.annotations.Observe;
import io.takari.modello.editor.mapping.dom.annotations.XMLList;
import io.takari.modello.editor.toolkit.model.AbstractModelBean;
import io.takari.modello.editor.toolkit.model.ITreeBean;

import java.util.List;

import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.graphics.Image;

public class MInterfaces extends AbstractModelBean implements ITreeBean {
    
    @XMLList("interface")
    private List<MInterface> interfaces;

    @EditableList("Interface")
    public List<MInterface> getInterfaces() {
        return this.interfaces;
    }
    
    @Observe("interfaces")
    @Override
    public List<? extends ITreeBean> getChildren() {
        return getInterfaces();
    }

    @Override
    public String getLabelValue() {
        return "Interfaces";
    }
    
    @Override
    public Image getImage() {
        return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_IMPCONT);
    }

}
