package io.takari.modello.editor.mapping.dom.impl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

class DomAttr extends DomValue {

    private final String name;

    DomAttr(DomSection parent, String name) {
        super(parent);
        this.name = name;
    }
    
    @Override
    protected Element getNode(DomHelper ctx, boolean create) {
        return (Element) getParent().getNode(ctx, create);
    }
    
    @Override
    protected void setDefault(DomHelper ctx, Node node) {
        ((Element)node).removeAttribute(name);
        ctx.updateSelection(node);
        getParent().removeIfEmpty(ctx);
    }
    
    @Override
    protected void setValue(DomHelper ctx, Node node, String value) {
        ((Element)node).setAttribute(name, value);
        ctx.updateSelection(node);
    }
    
    @Override
    protected String getValue(DomHelper ctx, Node node) {
        Element element = (Element)node;
        if(element.getAttributeNode(name) == null) return null;
        return element.getAttribute(name);
    }
}
