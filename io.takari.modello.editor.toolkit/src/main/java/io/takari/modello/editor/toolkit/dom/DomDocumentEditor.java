package io.takari.modello.editor.toolkit.dom;

import io.takari.modello.editor.mapping.api.IModelAccessor;
import io.takari.modello.editor.mapping.dom.IDocumentSessionProvider;
import io.takari.modello.editor.mapping.dom.accessor.DomModelAccessor;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.mapping.proxy.ModelProxyGenerator;
import io.takari.modello.editor.toolkit.editor.DocumentEditor;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLEditorMessages;

@SuppressWarnings("restriction")
public abstract class DomDocumentEditor extends DocumentEditor {

    private IDocumentSessionProvider sessionProvider;

    public DomDocumentEditor() {
        super();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
        
        sessionProvider = new DocumentSessionProvider(this);
    }
    
    @Override
    protected String getTextEditorContentType() {
        return ContentTypeIdForXML.ContentTypeID_XML + ".source";
    }

    @Override
    protected TextEditor createTextEditor() {
        return new StructuredTextEditor();
    }

    @Override
    protected String getSourcePageText() {
        return XMLEditorMessages.XMLMultiPageEditorPart_0;
    }
    
    protected IDocumentSessionProvider getSessionProvider() {
        return sessionProvider;
    }
    
    protected IModelAccessor<?> createModelAccessor(ModelProxyGenerator gen) {
        return new DomModelAccessor(sessionProvider, gen);
    }
    
    protected abstract Class<? extends IModel> getModelClass();
    
}
