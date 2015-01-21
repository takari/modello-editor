package io.takari.modello.editor.mapping.dom.accessor;

import io.takari.modello.editor.mapping.api.AbstractModelAccessor;
import io.takari.modello.editor.mapping.dom.DomMappingPlugin;
import io.takari.modello.editor.mapping.dom.IDocumentSessionProvider;
import io.takari.modello.editor.mapping.dom.impl.DomHelper;
import io.takari.modello.editor.mapping.dom.impl.DomRoot;
import io.takari.modello.editor.mapping.proxy.ModelProxyGenerator;

public class DomModelAccessor extends AbstractModelAccessor<DomModelAccessor> {
    
    private final IDocumentSessionProvider sessionProvider;
    private final ModelProxyGenerator proxyGenerator;
    private final DomHelper domHelper;
    private final String namespace;
    private final String location;
    private final String root;

    public DomModelAccessor(IDocumentSessionProvider sessionProvider, ModelProxyGenerator proxyGenerator, String namespace, String location, String root) {
        super(DomMappingPlugin.getDomAccessorManager());
        this.sessionProvider = sessionProvider;
        this.proxyGenerator = proxyGenerator;
        this.namespace = namespace;
        this.location = location;
        this.root = root;
        domHelper = new DomHelper(sessionProvider);
    }
    
    public DomHelper getDomHelper() {
        return domHelper;
    }
    
    public ModelProxyGenerator getProxyGenerator() {
        return proxyGenerator;
    }
    
    public IDocumentSessionProvider getSessionProvider() {
        return sessionProvider;
    }
    
    public DomRoot createRoot() {
        return new DomRoot(namespace, location, root);
    }
}
