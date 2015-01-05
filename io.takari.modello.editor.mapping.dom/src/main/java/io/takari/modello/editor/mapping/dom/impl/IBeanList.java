package io.takari.modello.editor.mapping.dom.impl;

import java.util.List;

public interface IBeanList<E> {
    
    List<E> getList(DomHelper ctx);
    
    E add(DomHelper ctx);
    
    void remove(DomHelper ctx, E bean);
    
    void move(DomHelper ctx, E bean, int pos);
    
    boolean isLoaded();
    
}
