package io.takari.modello.editor.toolkit.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.forms.editor.FormEditor;

import io.takari.modello.editor.mapping.api.SyncState;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.mapping.proxy.ModelProxyGenerator;

public interface IDocumentEditor {
    
    FormEditor getEditorPart();
    
    IModel getModel();
    
    IDocument getDocument();
    
    void requestWrite();
    
    void releaseWrite();
    
    /**
     * Selects a specified range of text in the editor
     */
    void select(int offset, int length);
    
    /**
     * Notifies editor that the document has changed but the changes are already in the model
     */
    void synchronize();

    SyncState getSync();

    ModelProxyGenerator getProxyGenerator();

    IFile getFile();
    
}
