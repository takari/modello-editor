package io.takari.modello.editor.toolkit.dom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.eclipse.wst.xsd.contentmodel.internal.CMNodeImpl;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDModelGroupDefinition;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDParticleContent;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.XSDWildcard;
import org.w3c.dom.Element;

@SuppressWarnings("restriction")
public class XSDHelper {

    private ModelQuery mq;

    public XSDHelper(IStructuredModel model) {
        mq = ModelQueryUtil.getModelQuery(model);
    }
    
    public List<String> getElementNames(Element parentElement) {
        
        if(parentElement == null) return Collections.emptyList();
        
        CMElementDeclaration ed = mq.getCMElementDeclaration(parentElement);
        if(ed == null) return Collections.emptyList();
        
        XSDElementDeclaration xsded = (XSDElementDeclaration) ((CMNodeImpl)ed).getKey();
        if(xsded == null) return Collections.emptyList();
        
        XSDElementCollector xsdc = new XSDElementCollector();
        xsdc.collect(xsded);
        return xsdc.getResults();
    }
    
    
    private class XSDElementCollector {
        
        List<String> results;

        public XSDElementCollector() {
            results = new ArrayList<String>();
        }
        
        public List<String> getResults() {
            return results;
        }
        
        public void collect(XSDElementDeclaration element) {
            
            element = element.getResolvedElementDeclaration();
            XSDTypeDefinition type = element.getType();
            
            if(type instanceof XSDComplexTypeDefinition) {
                XSDComplexTypeDefinition ctype = (XSDComplexTypeDefinition) type;
                XSDParticle p = ctype.getComplexType();
                
                if(p != null) {
                    particle(p);
                }
            }
        }
        

        private void particle(XSDParticle particle) {
            XSDParticleContent c = particle.getContent();
            
            if(c instanceof XSDModelGroupDefinition) {
                modelGroupDef((XSDModelGroupDefinition)c);
            } else if(c instanceof XSDElementDeclaration) {
                element((XSDElementDeclaration)c);
            } else if(c instanceof XSDWildcard) {
                wildcard((XSDWildcard)c);
            } else if(c instanceof XSDModelGroup) {
                modelGroup((XSDModelGroup)c);
            }
        }
        
        private void modelGroupDef(XSDModelGroupDefinition c) {
            if(c.getModelGroup() != null) {
                modelGroup(c.getModelGroup());
            }
        }
        
        private void element(XSDElementDeclaration element) {
            results.add(element.getName());
        }
        
        private void wildcard(XSDWildcard wildcard) {
            results.add("*");
        }
        
        private void modelGroup(XSDModelGroup modelGroup) {
            List<XSDParticle> c = modelGroup.getContents();
            if(c != null) {
                for(XSDParticle p: c) {
                    particle(p);
                }
            }
        }
        
    }
}
