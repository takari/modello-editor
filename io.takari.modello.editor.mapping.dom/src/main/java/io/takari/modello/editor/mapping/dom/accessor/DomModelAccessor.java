package io.takari.modello.editor.mapping.dom.accessor;

import io.takari.modello.editor.mapping.api.AbstractModelAccessor;
import io.takari.modello.editor.mapping.dom.DomMappingPlugin;
import io.takari.modello.editor.mapping.dom.IDocumentSessionProvider;
import io.takari.modello.editor.mapping.dom.impl.DomHelper;
import io.takari.modello.editor.mapping.proxy.ModelProxyGenerator;

public class DomModelAccessor extends AbstractModelAccessor<DomModelAccessor> {
    
    private final IDocumentSessionProvider sessionProvider;
    private final ModelProxyGenerator proxyGenerator;
    private final DomHelper domHelper;

    public DomModelAccessor(IDocumentSessionProvider sessionProvider, ModelProxyGenerator proxyGenerator) {
        super(DomMappingPlugin.getDomAccessorManager());
        this.sessionProvider = sessionProvider;
        this.proxyGenerator = proxyGenerator;
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
}
