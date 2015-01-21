package io.takari.modello.editor.mapping.dom.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

public class DomRoot extends DomSection {
    
    private String namespace;
    private String location;

    public DomRoot(String namespace, String location, String name) {
        super(null, name);
        this.namespace = namespace;
        this.location = location;
    }
    
    @Override
    protected Element getNode(DomHelper ctx, boolean create) {
        Document doc = ctx.getDocument();
        Element elem = doc.getDocumentElement();
        
        if(create) {
            
            Node first = doc.getFirstChild();
            if(first == null || !(first instanceof ProcessingInstruction)) {
                doc.insertBefore(doc.createProcessingInstruction("xml", "version=\"1.0\" encoding=\"UTF-8\""), first);
                doc.insertBefore(doc.createTextNode("\n"), first);
            }
            
            if(elem == null)
            {
                //doc.setXmlVersion("1.0");
                elem = doc.createElement(name);
                elem.setAttribute("xmlns", namespace);
                elem.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
                elem.setAttribute("xsi:schemaLocation", namespace + " " + location);
                doc.appendChild(elem);
                doc.appendChild(doc.createTextNode("\n"));
            }
        }
        
        return elem;
    }
}
