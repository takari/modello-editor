package io.takari.modello.editor.impl.model.plugin.model;

import io.takari.modello.editor.impl.model.plugin.DetailPartMetadataUI;
import io.takari.modello.editor.impl.model.plugin.IMetadataPlugin;
import io.takari.modello.editor.impl.model.plugin.IMetadataUI;
import io.takari.modello.editor.impl.model.plugin.model.ui.ModelClassMetadataPart;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

public class ModelMetadataPlugin implements IMetadataPlugin {
    
    @Override
    public IMetadataUI createModelUI(IDocumentEditor editor) {
        return null;
    }

    @Override
    public IMetadataUI createInterfaceUI(IDocumentEditor editor) {
        return null;
    }

    @Override
    public IMetadataUI createClassUI(IDocumentEditor editor) {
        return new DetailPartMetadataUI(new ModelClassMetadataPart(editor));
    }

    @Override
    public IMetadataUI createFieldUI(IDocumentEditor editor) {
        return null;
    }
}
