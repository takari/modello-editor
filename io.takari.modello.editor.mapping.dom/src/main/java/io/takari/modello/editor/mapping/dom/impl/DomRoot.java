package io.takari.modello.editor.mapping.dom.impl;

import org.w3c.dom.Element;

public class DomRoot extends DomSection {
    
    public DomRoot() {
        super(null, "");
    }
    
    @Override
    protected Element getNode(DomHelper ctx, boolean create) {
        return ctx.getDocument().getDocumentElement();
    }
}
