package io.takari.modello.editor.mapping.dom;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface IDocumentSession {
    
    Document getDocument();
    
    List<String> getAvailableContent(Element node);
    
    void format(Node newNode);
    
    void contentChanged(Node node);
    
    void rollback();

}
