package io.takari.modello.editor.mapping.dom.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class DomList extends DomSection {

    protected final String itemName;

    DomList(DomSection parent, String itemName) {
        super(parent, null);
        this.itemName = itemName;
    }
    
    @Override
    protected Element getNode(DomHelper ctx, boolean create) {
        return getParent().getNode(ctx, create);
    }
    
    public List<DomSection> getSections(DomHelper ctx) {
        if(ctx.isClosed()) return Collections.emptyList();
        
        List<DomSection> sections = new ArrayList<>();
        
        Element wrapper = getNode(ctx, false);
        
        if(wrapper != null) {
            int l = elementCount(ctx, wrapper, itemName);
            
            for(int i = 0; i < l; i++) {
                sections.add(new ListSection(i));
            }
        }
        
        return sections;
    }
    
    public DomSection addSection(DomHelper ctx) {
        if(ctx.isClosed()) return null;
        
        Element parentNode = getNode(ctx, true);
        
        int size = elementCount(ctx, parentNode, itemName);
        newElement(ctx, parentNode, itemName, false);
        
        return new ListSection(size);
    }
    
    public void removeSection(DomHelper ctx, DomSection section) {
        if(ctx.isClosed()) return;
        
        Element parentNode = getNode(ctx, false);
        if(parentNode == null) return;
        
        Element node = section.getNode(ctx, false);
        if(node == null) return;
        
        remove(ctx, node);
        getParent().removeIfNoChildren(ctx);
        section.index = -1;
    }
    
    public boolean moveSection(DomHelper ctx, DomSection f, int pos) {
        if(ctx.isClosed()) return false;
        
        Element node = f.getNode(ctx, false);
        Element wrapper = getNode(ctx, false);
        
        if(wrapper != null) {
            int l = elementCount(ctx, wrapper, itemName);
            if(pos >= 0 && pos < l) {
                if(pos > f.index) pos++;
                Node beforeNode = findElement(ctx, wrapper, itemName, pos);
                
                // section is considered to also contain all leading text up to previous element
                
                // squeeze section between targetSection-1 and its trailing text
                // handle last section's trailing text
                if(beforeNode == null) {
                    beforeNode = wrapper.getLastChild();
                    if(beforeNode instanceof Element) beforeNode = null;
                }
                if(beforeNode != null) {
                    while(beforeNode.getPreviousSibling() != null && !(beforeNode.getPreviousSibling() instanceof Element)) {
                        beforeNode = beforeNode.getPreviousSibling();
                    }
                }
                
                // move all leading text along with the section
                List<Node> toMove = new ArrayList<Node>();
                Node moving = node;
                do {
                    toMove.add(moving);
                    moving = moving.getPreviousSibling();
                } while(moving != null && !(moving instanceof Element));
                Collections.reverse(toMove);
                
                // perform move
                for(Node n: toMove) {
                    wrapper.removeChild(n);
                    if(beforeNode != null) wrapper.insertBefore(n, beforeNode);
                    else wrapper.appendChild(n);
                }
                return true;
            }
            
        }
        return false;
    }
    
    public List<DomValue> getValues(DomHelper ctx, boolean cdata) {
        if(ctx.isClosed()) return Collections.emptyList();
        
        List<DomValue> values = new ArrayList<>();
        
        Element wrapper = getNode(ctx, false);
        
        if(wrapper != null) {
            int l = elementCount(ctx, wrapper, itemName);
            
            for(int i = 0; i < l; i++) {
                values.add(new ListValue(i, cdata));
            }
        }
        
        return values;
    }
    
    public DomValue addValue(DomHelper ctx, boolean cdata) {
        if(ctx.isClosed()) return null;
        
        Element parentNode = getNode(ctx, true);
        
        int size = elementCount(ctx, parentNode, itemName);
        newElement(ctx, parentNode, itemName, false);
        
        return new ListValue(size, cdata);
    }
    
    public void removeValue(DomHelper ctx, DomValue value) {
        if(ctx.isClosed()) return;
        
        Element parentNode = getNode(ctx, false);
        if(parentNode == null) return;
        
        Element node = value.getNode(ctx, false);
        if(node == null) return;
        
        remove(ctx, node);
        getParent().removeIfNoChildren(ctx);
    }
    
    public <E> IBeanList<E> mapSections(IBeanMapper<DomSection, E> mapper) {
        return new SectionList<E>(mapper);
    }
    
    public <E> IBeanList<E> mapValues(IBeanMapper<DomValue, E> mapper, boolean cdata) {
        return new ValueList<E>(mapper, cdata);
    }
    
    protected boolean testNoChildren(DomHelper ctx, Node parent) {
        boolean noChildren = false;
        
        Node ch = parent.getFirstChild();
        while(ch != null) {
            
            if(ch instanceof Text) {
                
                // only allow whitespace text
                Text text = (Text) ch;
                String value = text.getNodeValue();
                if(!value.matches("\\s*")) {
                    return false;
                }
                
            } else {
                
                return false;
                
            }
            
            ch = ch.getNextSibling();
        }
        
        return noChildren;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass().equals(DomList.class)) {
            DomList other = (DomList) obj;
            return getParent().equals(other.getParent()) && name.equals(other.name) && index == other.index && itemName.equals(other.itemName);
        }
        return false;
    }
    
    private class ListSection extends DomSection {

        ListSection(int idx) {
            super(DomList.this.getParent(), itemName, idx);
        }
        
        @Override
        protected Element getNode(DomHelper ctx, boolean create) {
            Element parentNode = DomList.this.getNode(ctx, create);
            return findElement(ctx, parentNode, itemName, index);
        }
        
        @Override
        public DomValue text(String name) {
            return new ListSectionText(this, name, false);
        }
        
        @Override
        public DomValue cdata(String name) {
            return new ListSectionText(this, name, true);
        }
        
        @Override
        public boolean equals(Object obj) {
            if(obj.getClass().equals(ListSection.class)) {
                ListSection other = (ListSection) obj;
                return getParent().equals(other.getParent()) && name.equals(other.name) && index == other.index;
            }
            return false;
        }
    }
    
    private class ListValue extends DomText {

        private int idx;

        ListValue(int idx, boolean cdata) {
            super(DomList.this.getParent(), itemName, cdata);
            this.idx = idx;
        }
        
        @Override
        protected Element getNode(DomHelper ctx, boolean create) {
            Element parentNode = DomList.this.getNode(ctx, create);
            return findElement(ctx, parentNode, itemName, idx);
        }
        
    }
    
    private class SectionList<T> extends BeanList<DomSection, T> {
        public SectionList(IBeanMapper<DomSection, T> mapper) {
            super(mapper);
        }

        @Override
        protected List<DomSection> getSource(DomHelper ctx) {
            return getSections(ctx);
        }

        @Override
        protected DomSection add0(DomHelper ctx) {
            return addSection(ctx);
        }

        @Override
        protected void remove0(DomHelper ctx, DomSection f) {
            removeSection(ctx, f);
        }
        
        @Override
        protected boolean move0(DomHelper ctx, DomSection f, int dir) {
            return moveSection(ctx, f, dir);
        }
    }
    
    private class ValueList<T> extends BeanList<DomValue, T> {
        
        private boolean cdata;

        public ValueList(IBeanMapper<DomValue, T> mapper, boolean cdata) {
            super(mapper);
            this.cdata = cdata;
        }

        @Override
        protected List<DomValue> getSource(DomHelper ctx) {
            return getValues(ctx, cdata);
        }

        @Override
        protected DomValue add0(DomHelper ctx) {
            return addValue(ctx, cdata);
        }

        @Override
        protected void remove0(DomHelper ctx, DomValue f) {
            removeValue(ctx, f);
        }
        
        @Override
        protected boolean move0(DomHelper ctx, DomValue f, int dir) {
            throw new UnsupportedOperationException();
        }
    }
    
    private class ListSectionText extends DomText {
        ListSectionText(DomSection parent, String name, boolean cdata) {
            super(parent, name, cdata);
        }
        
        @Override
        protected void setDefault(DomHelper ctx, Node node) {
            Node parent = node.getParentNode();
            super.setDefault(ctx, node);
            
            if(testNoChildren(ctx, parent)) {
                removeSection(ctx, getParent());
            }
        }
    }

}
