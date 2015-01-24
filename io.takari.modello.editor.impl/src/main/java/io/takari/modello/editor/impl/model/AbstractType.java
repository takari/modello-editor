package io.takari.modello.editor.impl.model;

import io.takari.modello.editor.mapping.annotations.EditableList;
import io.takari.modello.editor.mapping.dom.annotations.XMLList;
import io.takari.modello.editor.mapping.dom.annotations.XMLText;

import java.util.List;

public abstract class AbstractType extends AbstractModelloModel {
    
    @XMLText("packageName")
    private String packageName;
    
    @XMLList("codeSegments/codeSegment")
    private List<MCodeSegment> codeSegments;
    
    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @EditableList("Code Segment")
    public List<MCodeSegment> getCodeSegments() {
        return codeSegments;
    }

    public void setCodeSegments(List<MCodeSegment> codeSegments) {
        this.codeSegments = codeSegments;
    }
    
}
