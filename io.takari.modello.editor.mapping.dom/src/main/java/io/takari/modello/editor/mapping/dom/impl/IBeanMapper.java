package io.takari.modello.editor.mapping.dom.impl;

public interface IBeanMapper<F, T> {
    
    T map(F from);
    
    F unmap(T from);
    
    void setIndex(T to, int index);
}
