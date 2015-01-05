package io.takari.modello.editor.impl.model.plugin.xdoc;

import io.takari.modello.editor.impl.model.plugin.DetailPartMetadataUI;
import io.takari.modello.editor.impl.model.plugin.IMetadataPlugin;
import io.takari.modello.editor.impl.model.plugin.IMetadataUI;
import io.takari.modello.editor.impl.model.plugin.xdoc.ui.XDocFieldMetadataPart;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

public class XDocMetadataPlugin implements IMetadataPlugin {
    
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
        return null;
    }

    @Override
    public IMetadataUI createFieldUI(IDocumentEditor editor) {
        return new DetailPartMetadataUI(new XDocFieldMetadataPart(editor));
    }
}
