package io.takari.modello.editor.mapping.model;

public interface IListControl {
    
    boolean isEditable();
    
    String getName();
    
    void move(IModelExtension item, int pos);
    
    IModelExtension add();
    
    void remove(IModelExtension item);
    
}
