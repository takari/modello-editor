package io.takari.modello.editor.impl.model;

import io.takari.modello.editor.mapping.annotations.EditableList;
import io.takari.modello.editor.mapping.annotations.Observe;
import io.takari.modello.editor.mapping.dom.annotations.XMLList;
import io.takari.modello.editor.mapping.dom.annotations.XMLText;
import io.takari.modello.editor.toolkit.model.ITreeBean;

import java.util.List;

import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.graphics.Image;

public class MClass extends AbstractType implements ITreeBean {
    
    @XMLText("superClass")
    private String superClass;
    
    @XMLList("interfaces/interface")
    private List<MClassInterface> interfaces;
    
    @XMLList("fields/field")
    private List<MField> fields;
    
    public String getSuperClass() {
        return this.superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    @EditableList("Interface")
    public List<MClassInterface> getInterfaces() {
        return this.interfaces;
    }
    
    public void setInterfaces(List<MClassInterface> interfaces) {
        this.interfaces = interfaces;
    }
    
    @EditableList(value = "Field", hints = "modelTree")
    public List<MField> getFields() {
        return this.fields;
    }

    public void setFields(List<MField> fields) {
        this.fields = fields;
    }
    
    @Observe("fields")
    @Override
    public List<? extends ITreeBean> getChildren() {
        return getFields();
    }
    
    @Override
    public Image getImage() {
        return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_CLASS);
    }

}
