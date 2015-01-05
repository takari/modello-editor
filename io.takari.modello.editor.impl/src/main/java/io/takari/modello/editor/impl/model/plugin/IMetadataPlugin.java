package io.takari.modello.editor.impl.model.plugin;

import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

public interface IMetadataPlugin {
    
    IMetadataUI createModelUI(IDocumentEditor editor);
    
    IMetadataUI createInterfaceUI(IDocumentEditor editor);
    
    IMetadataUI createClassUI(IDocumentEditor editor);
    
    IMetadataUI createFieldUI(IDocumentEditor editor);
    
}
