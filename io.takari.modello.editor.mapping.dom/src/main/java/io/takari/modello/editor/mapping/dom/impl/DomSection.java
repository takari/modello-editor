package io.takari.modello.editor.mapping.dom.impl;

import org.w3c.dom.Element;

public class DomSection extends AbstractDom {
    
    protected final String name;
    protected int index;
    
    DomSection(DomSection parent, String name) {
        this(parent, name, 0);
    }
    DomSection(DomSection parent, String name, int index) {
        super(parent);
        this.name = name;
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
    
    /*
    public DomSection removeEmpty() {
        removeEmpty = true;
        return this;
    }
    */
    
    @Override
    protected Element getNode(DomHelper ctx, boolean create) {
        return findElement(ctx, getParent().getNode(ctx, create), name, create);
    }
    
    public DomSection section(String name) {
        if(name.trim().isEmpty()) return this;
        return new DomSection(this, name);
    }
    
    public DomList list(String itemName) {
        return new DomList(this, itemName);
    }
    
    public DomMap map(String itemName, String keyName/*, String valueName*/) {
        return new DomMap(this, itemName, keyName/*, valueName*/);
    }
    
    public DomValue text(String name) {
        return new DomText(this, name, false);
    }
    
    public DomValue text() {
        return new DomText(this, null, false);
    }
    
    public DomValue cdata(String name) {
        return new DomText(this, name, true);
    }
    
    public DomValue cdata() {
        return new DomText(this, null, true);
    }
    
    public DomValue attr(String name) {
        return new DomAttr(this, name);
    }

    void removeIfEmpty(DomHelper ctx) {
        removeIfNoChildren(ctx, getNode(ctx, false));
    }
    
}
