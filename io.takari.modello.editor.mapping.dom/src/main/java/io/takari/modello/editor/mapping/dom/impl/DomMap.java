package io.takari.modello.editor.mapping.dom.impl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DomMap extends DomList {

    private final String keyName;
    //private final String valueName;

    DomMap(DomSection parent, String itemName, String keyName/*, String valueName*/) {
        super(parent, itemName);
        this.keyName = keyName;
        //this.valueName = valueName;
    }
    
    public DomSection section(String key) {
        return new MapSection(key);
    }
    
    /*
    public DomValue getValue(String key, boolean cdata) {
        return new MapValue(key, cdata);
    }
    */
    
    protected Element getMapValueNode(DomHelper ctx, String key, boolean create) {
        Element parentNode = getNode(ctx, create);
        Element sectionNode = findElementBySubNode(ctx, parentNode, itemName, keyName, key, create);
        return sectionNode;
        //return findElement(ctx, sectionNode, valueName, create);
    }
    
    private class MapSection extends DomSection {

        private String key;

        MapSection(String key) {
            super(DomMap.this.getParent(), itemName);
            this.key = key;
        }
        
        @Override
        protected Element getNode(DomHelper ctx, boolean create) {
            return getMapValueNode(ctx, key, create);
        }
        
        @Override
        public DomValue text(String name) {
            return new MapSectionText(this, name, false);
        }
        
        @Override
        public DomValue cdata(String name) {
            return new MapSectionText(this, name, true);
        }
    }
    
    /*
    private class MapValue extends DomText {

        private String key;

        MapValue(String key, boolean cdata) {
            super(DomMap.this.getParent(), itemName, cdata);
            this.key = key;
        }
        
        @Override
        protected Element getNode(DomContext ctx, boolean create) {
            return getMapValueNode(ctx, key, create);
        }
        
        @Override
        protected void setDefault(DomContext ctx, Node node) {
            Node parent = node.getParentNode();
            super.setDefault(ctx, node);
            
            removeIfOnlyNodeLeft(ctx, parent, keyName);
        }
    }
    */
    
    private class MapSectionText extends DomText {
        MapSectionText(DomSection parent, String name, boolean cdata) {
            super(parent, name, cdata);
        }
        
        @Override
        protected void setDefault(DomHelper ctx, Node node) {
            Node parent = node.getParentNode();
            super.setDefault(ctx, node);
            
            removeIfOnlyNodeLeft(ctx, parent, keyName);
        }
    }
    
}
