package io.takari.modello.editor.mapping.dom.impl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

class DomText extends DomValue {

    private final String name;
    private final boolean cdata;

    DomText(DomSection parent, String name, boolean cdata) {
        super(parent);
        this.name = name;
        this.cdata = cdata;
    }
    
    @Override
    protected Element getNode(DomHelper ctx, boolean create) {
        if(name == null) {
            return getParent().getNode(ctx, create);
        }
        return findElement(ctx, getParent().getNode(ctx, create), name, create);
    }
    
    protected void setDefault(DomHelper ctx, Node node) {
        remove(ctx, node);
        getParent().removeIfEmpty(ctx);
    }
    
    @Override
    protected void setValue(DomHelper ctx, Node node, String value) {
        setText(ctx, node, value, cdata);
    }
    
    @Override
    protected String getValue(DomHelper ctx, Node node) {
        return getText(ctx, node);
    }
}
