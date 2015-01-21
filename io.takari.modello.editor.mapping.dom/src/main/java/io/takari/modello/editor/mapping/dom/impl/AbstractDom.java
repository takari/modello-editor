package io.takari.modello.editor.mapping.dom.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public abstract class AbstractDom {

    private final DomSection parent;

    AbstractDom(DomSection parent) {
        this.parent = parent;
    }
    
    public DomSection getParent() {
        return parent;
    }
    
    public void touch(DomHelper ctx) {
        Node node = getNode(ctx, false);
        if(node != null) {
            ctx.updateSelection(node);
        }
    }
    
    protected abstract Node getNode(DomHelper ctx, boolean create);
    
    protected void clear(DomHelper ctx, Node node) {
        Node child;
        while((child = node.getFirstChild()) != null) {
            node.removeChild(child);
        }
        ctx.clearTextCache();
    }
    
    protected String getText(DomHelper ctx, Node node) {
        
        boolean searchingCDATA = true;
        CDATASection singleCDATA = null;
        
        Node ch = node.getFirstChild();
        if(ch == null) return "";
        
        StringBuilder sb = new StringBuilder();
        while(ch != null) {
            if(searchingCDATA && ch instanceof CDATASection) {
                if(singleCDATA == null) singleCDATA = (CDATASection) ch;
                else {
                    singleCDATA = null;
                    searchingCDATA = false;
                }
            }
            
            if(ch instanceof CharacterData) {
                sb.append(((CharacterData) ch).getData());
            }
            ch = ch.getNextSibling();
        }
        
        if(singleCDATA != null) return singleCDATA.getData();
        
        return sb.toString();
    }
    
    private Text getSingleText(Node node) {
        
        NodeList children = node.getChildNodes();
        if(children.getLength() == 1) {
            Node ch = children.item(0);
            if(ch instanceof Text) {
                return (Text) children.item(0);
            }
        }
        return null;
    }
    
    private Text createText(DomHelper ctx, Node node) {
        clear(ctx, node);
        Text text = ctx.getDocument().createTextNode("");
        node.appendChild(text);
        return text;
    }
    
    private CDATASection getSingleCDATA(Node node) {
        
        Node cdata = null;
        NodeList children = node.getChildNodes();
        int l = children.getLength();
        for(int i = 0; i < l; i++) {
            Node ch = children.item(i);
            if(ch instanceof CDATASection) {
                
                if(cdata != null) {
                    // another cdata?
                    return null;
                }
                cdata = ch;
            }
        }
        return (CDATASection) cdata;
    }
    
    private CDATASection createCDATA(DomHelper ctx, Node node) {
        clear(ctx, node);
        CDATASection cdataSection = ctx.getDocument().createCDATASection("");
        node.appendChild(cdataSection);
        return cdataSection;
    }
    
    protected void setText(DomHelper ctx, Node node, String value, boolean cdata) {
        CharacterData cd = ctx.getCachedText(this);
        
        if(cd == null) cd = getSingleCDATA(node);
        if(cd == null) cd = getSingleText(node);
        
        if(cd == null) {
            cd = cdata ? createCDATA(ctx, node) : createText(ctx, node);
            ctx.setCachedText(this, cd);
        }
        cd.setData(value);
        
        ctx.updateSelection(cd);
    }
    
    protected Element findElement(DomHelper ctx, Node parentNode, String name, int idx) {
        return findElement(ctx, parentNode, name, idx, false);
    }
    
    protected Element findElement(DomHelper ctx, Node parentNode, String name, boolean create) {
        return findElement(ctx, parentNode, name, -1, create);
    }

    private Element findElement(DomHelper ctx, Node parentNode, String name, int idx, boolean create) {
        
        if(parentNode == null) return null;
        
        List<Element> nodes = findElements(ctx, parentNode, name);
        
        Element node;
        if(idx == -1) {
            node = nodes.isEmpty() ? null : nodes.get(0);
        } else {
            node = idx < nodes.size() ? nodes.get(idx) : null;
        }
        if(node == null && create) {
            node = newElement(ctx, parentNode, name, idx == -1);
        }
        
        return node;
    }

    protected Element findElementBySubNode(DomHelper ctx, Element parentNode, String name, String keyName, String key, boolean create) {
        if(parentNode == null) return null;
        
        List<Element> nodes = findElements(ctx, parentNode, name);
        
        for(Element node: nodes) {
            Element keyNode = findElement(ctx, node, keyName, false);
            String value = keyNode.getTextContent();
            if(value != null && value.equals(key)) {
                return node;
            }
        }
        
        if(create) {
            Element node = newElement(ctx, parentNode, name, false);
            Element keyNode = newElement(ctx, node, keyName, false);
            keyNode.appendChild(ctx.getDocument().createTextNode(key));
            
            return node;
        }
        
        return null;
    }

    protected List<Element> findElements(DomHelper ctx, Node parentNode, String name) {
        
        if(parentNode == null) return Collections.emptyList();
        NodeList children;
        if(parentNode instanceof Document) {
            children = ((Document)parentNode).getChildNodes();
        } else if(parentNode instanceof Element) {
             children = ((Element)parentNode).getChildNodes();
        } else {
            throw new IllegalStateException("Unsupported parentNode " + parentNode.getClass());
        }
        
        int l = children.getLength();
        if(l == 0) return Collections.emptyList();
        
        List<Element> nodes = new ArrayList<>();
        for(int i = 0; i < l; i++) {
            
            Node node = children.item(i);
            if(node instanceof Element) {
                Element e = (Element) node;
                if(e.getTagName().equals(name)) {
                    nodes.add(e);
                }
            }
        }
        return nodes;
    }
    
    
    protected int elementCount(DomHelper ctx, Node parentNode, String name) {
        return findElements(ctx, parentNode, name).size();
    }
    
    protected Attr findAttribute(DomHelper ctx, Element element, String name, boolean create) {
        Attr attr = element.getAttributeNode(name);
        if(attr == null && create) {
            element.setAttribute(name, "");
            attr = element.getAttributeNode(name);
        }
        return attr;
    }
    
    protected Element newElement(DomHelper ctx, Node parent, String name, boolean calcPosition) {
        Element newElement = ctx.getDocument().createElement(name);
        
        Element before = null;
        
        if(calcPosition) {
            int idx = -1;
            
            List<Element> existingElements = new ArrayList<Element>();
            Node ch = parent.getFirstChild();
            while(ch != null) {
                if(ch instanceof Element) {
                    Element element = (Element) ch;
                    existingElements.add(element);
                }
                ch = ch.getNextSibling();
            }
            
            if(existingElements.size() > 0 && parent instanceof Element) {
                List<String> allowedNames = ctx.getAllowedContent((Element)parent);
                
                int wpos = allowedNames.indexOf("*");
                int pos = allowedNames.indexOf(name);
                if(pos == -1) pos = wpos;
                
                if(pos >= 0) {
                    idx = 0;
                    for(Element e: existingElements) {
                        String ename = e.getTagName();
                        
                        int epos = allowedNames.indexOf(ename);
                        if(epos == -1) epos = wpos;
                        
                        if(epos >= 0) {
                           if(epos > pos) {
                               break;
                           }
                        }
                        
                        idx++;
                    }
                    
                    before = idx < existingElements.size() ? existingElements.get(idx) : null;
                }
            }
        }
        
        parent.insertBefore(newElement, before);
        format(ctx, newElement);
        ctx.updateSelection(newElement);
        return newElement;
    }
    
    protected void remove(DomHelper ctx, Node child) {
        if (child != null) {
            Node parent;
            if(child instanceof Attr) {
                parent = ((Attr) child).getOwnerElement();
            } else {
                parent = child.getParentNode();
            }
            
            if(child instanceof Attr) {
                
                ((Element)parent).removeAttribute(((Attr) child).getName());
                ctx.updateSelection(parent);
                
            } else {
            
                Node prev = child.getPreviousSibling();
                Node next = child.getNextSibling();
                if (prev instanceof Text) {
                    Text txt = (Text) prev;
                    int lastnewline = getLastEolIndex(txt.getData());
                    if (lastnewline >= 0) {
                        txt.setData(txt.getData().substring(0, lastnewline));
                    }
                }
                
                parent.removeChild(child);
                ctx.updateSelection(next == null ? parent : next);
                ctx.clearTextCache();
            }
        }
    }
    
    private static int getLastEolIndex(String s) {
        if (s == null || s.length() == 0) {
            return -1;
        }
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if (c == '\r') {
                return i;
            }
            if (c == '\n') {
                if (i > 0 && s.charAt(i - 1) == '\r') {
                    return i - 1;
                }
                return i;
            }
        }
        return -1;
    }
    
    protected boolean removeIfEmpty() {
        return true;
    }

    protected void removeIfNoChildren(DomHelper ctx) {
        removeIfOnlyNodeLeft(ctx, null);
    }
    
    protected void removeIfOnlyNodeLeft(DomHelper ctx, String allowedName) {
        Node el = getNode(ctx, false);
        
        if(el == null) return;
        
        if(el.getAttributes().getLength() > 0) return;
        
        List<Node> textNodes = new ArrayList<Node>();
        
        NodeList nl = el.getChildNodes();
        boolean hasChildren = false;
        for (int i = 0; i < nl.getLength(); i++) {
            Node child = nl.item(i);
            if (child instanceof Element) {
                if(allowedName != null) {
                    hasChildren |= !((Element) child).getTagName().equals(allowedName);
                } else {
                    hasChildren |= true;
                }
                if(hasChildren) return;
            } else if(child instanceof Text) {
                textNodes.add(child);
            }
        }
        if (!hasChildren) {
            Node parent = el.getParentNode();
            if (parent != null && parent instanceof Element) {
                if(removeIfEmpty()) {
                    remove(ctx, el);
                    getParent().removeIfNoChildren(ctx);
                } else {
                    // clear all text
                    for(Node t: textNodes) {
                        el.removeChild(t);
                    }
                }
                ctx.clearTextCache();
            }
        }
    }
    
    protected void format(DomHelper ctx, Node newNode) {
        
        if(newNode.getParentNode() != null && newNode.equals(newNode.getParentNode().getLastChild())) {
            // add a new line to get the newly generated content correctly formatted.
            newNode.getParentNode().appendChild(newNode.getParentNode().getOwnerDocument().createTextNode("\n")); //$NON-NLS-1$
        }
        
        ctx.format(newNode);
    }
}
