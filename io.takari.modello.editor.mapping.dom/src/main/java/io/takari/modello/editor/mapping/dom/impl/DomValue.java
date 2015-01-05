package io.takari.modello.editor.mapping.dom.impl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class DomValue extends AbstractDom {
    
    protected String defaultValue;
    
    DomValue(DomSection parent) {
        super(parent);
    }
    
    public DomValue def(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
    
    public DomValue def(boolean value) {
        def(String.valueOf(value));
        return this;
    }
    
    @Override
    protected abstract Element getNode(DomHelper ctx, boolean create);
    
    public void setValue(DomHelper ctx, String value) {
        if(ctx.isClosed()) return;
        
        if((defaultValue == null ? "" : defaultValue).equals(value)) {
            value = null;
        }
        
        if(value == null) {
            Node node = getNode(ctx, false);
            if(node != null) setDefault(ctx, node);
        } else {
            setValue(ctx, getNode(ctx, true), value);
        }
    }
    
    protected abstract void setDefault(DomHelper ctx, Node node);
    
    protected abstract void setValue(DomHelper ctx, Node node, String value);
    
    protected abstract String getValue(DomHelper ctx, Node node);
    
    private String getDefaultValue() {
        return defaultValue == null ? "" : defaultValue;
    }
    
    public String getValue(DomHelper ctx) {
        if(ctx.isClosed()) return "";
        
        Node node = getNode(ctx, false);
        if(node == null) return getDefaultValue();
        
        String value = getValue(ctx, node);
        if(value == null) return getDefaultValue();
        return value;
    }
    
    public boolean getBoolean(DomHelper ctx) {
        String value = getValue(ctx);
        if(value == null) return false;
        return Boolean.valueOf(value);
    }
    
    public void setValue(DomHelper ctx, boolean value) {
        if(defaultValue == null && !value) {
            setValue(ctx, null);
        } else {
            setValue(ctx, String.valueOf(value));
        }
    }
}
