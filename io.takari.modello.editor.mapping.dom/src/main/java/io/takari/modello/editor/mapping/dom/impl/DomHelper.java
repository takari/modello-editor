package io.takari.modello.editor.mapping.dom.impl;

import io.takari.modello.editor.mapping.api.SyncState;
import io.takari.modello.editor.mapping.dom.IDocumentSessionProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DomHelper {
    
    private final IDocumentSessionProvider provider;
    private final Map<AbstractDom, CharacterData> valueCache;
    
    public DomHelper(IDocumentSessionProvider provider) {
        this.provider = provider;
        valueCache = new HashMap<>();
    }
    
    public SyncState getSync() {
        return provider.getSync();
    }

    Document getDocument() {
        return provider.currentSession().getDocument();
    }
    
    boolean isClosed() {
        return provider.isClosed();
    }
    
    CharacterData getCachedText(AbstractDom val) {
        CharacterData node = valueCache.get(val);
        return node;
        /*
        Node p = node;
        while(p != null) {
            if(node instanceof Document) {
                return node;
            }
            p = p.getParentNode();
        }
        // stale node
        valueCache.remove(val);
        return null;
        */
    }
    
    void setCachedText(AbstractDom val, CharacterData node) {
        valueCache.put(val, node);
    }
    
    void clearTextCache() {
        valueCache.clear();
    }
    
    void updateSelection(Node node) {
        provider.currentSession().contentChanged(node);
    }
    
    List<String> getAllowedContent(Element element) {
        return provider.currentSession().getAvailableContent(element);
    }
    
    void format(Node newNode) {
        provider.currentSession().format(newNode);
    }
}
