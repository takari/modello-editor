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

public class MClasses extends AbstractModelBean implements ITreeBean {
    
    @XMLList("class")
    private List<MClass> classes;
    
    @EditableList("Class")
    public List<MClass> getClasses() {
        return this.classes;
    }
    
    @Observe("classes")
    @Override
    public List<? extends ITreeBean> getChildren() {
        return getClasses();
    }

    @Override
    public String getLabelValue() {
        return "Classes";
    }
    
    @Override
    public Image getImage() {
        return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_LIBRARY);
    }

}
