package io.takari.modello.editor.mapping.dom.impl;

import org.w3c.dom.Element;

public class DomSection extends AbstractDom {
    
    protected final String name;
    protected int index;
    protected boolean removeIfEmpty = true;
    
    DomSection(DomSection parent, String name) {
        this(parent, name, 0);
    }
    DomSection(DomSection parent, String name, int index) {
        super(parent);
        this.name = name;
        this.index = index;
    }
    
    public DomSection persistent() {
        removeIfEmpty = false;
        return this;
    }
    
    public int getIndex() {
        return index;
    }
    
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

    protected boolean removeIfEmpty() {
        return removeIfEmpty;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass().equals(DomSection.class)) {
            DomSection other = (DomSection) obj;
            return getParent().equals(other.getParent()) && name.equals(other.name) && index == other.index;
        }
        return false;
    }
    
}
