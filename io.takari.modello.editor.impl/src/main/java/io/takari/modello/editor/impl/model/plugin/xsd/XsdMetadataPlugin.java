package io.takari.modello.editor.impl.model.plugin.xsd;

import io.takari.modello.editor.impl.model.plugin.DetailPartMetadataUI;
import io.takari.modello.editor.impl.model.plugin.IMetadataPlugin;
import io.takari.modello.editor.impl.model.plugin.IMetadataUI;
import io.takari.modello.editor.impl.model.plugin.xsd.ui.XsdClassMetadataPart;
import io.takari.modello.editor.impl.model.plugin.xsd.ui.XsdModelMetadataPart;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

public class XsdMetadataPlugin implements IMetadataPlugin {
    
    @Override
    public IMetadataUI createModelUI(IDocumentEditor editor) {
        return new DetailPartMetadataUI(new XsdModelMetadataPart(editor));
    }

    @Override
    public IMetadataUI createInterfaceUI(IDocumentEditor editor) {
        return null;
    }

    @Override
    public IMetadataUI createClassUI(IDocumentEditor editor) {
        return new DetailPartMetadataUI(new XsdClassMetadataPart(editor));
    }

    @Override
    public IMetadataUI createFieldUI(IDocumentEditor editor) {
        return null;
    }
}
