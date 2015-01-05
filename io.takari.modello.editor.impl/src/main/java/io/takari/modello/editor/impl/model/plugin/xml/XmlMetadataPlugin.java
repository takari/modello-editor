package io.takari.modello.editor.impl.model.plugin.xml;

import io.takari.modello.editor.impl.model.plugin.DetailPartMetadataUI;
import io.takari.modello.editor.impl.model.plugin.IMetadataPlugin;
import io.takari.modello.editor.impl.model.plugin.IMetadataUI;
import io.takari.modello.editor.impl.model.plugin.xml.ui.XmlClassMetadataPart;
import io.takari.modello.editor.impl.model.plugin.xml.ui.XmlFieldMetadataPart;
import io.takari.modello.editor.impl.model.plugin.xml.ui.XmlModelMetadataPart;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

public class XmlMetadataPlugin implements IMetadataPlugin {
    
    @Override
    public IMetadataUI createModelUI(IDocumentEditor editor) {
        return new DetailPartMetadataUI(new XmlModelMetadataPart(editor));
    }

    @Override
    public IMetadataUI createInterfaceUI(IDocumentEditor editor) {
        return null;
    }

    @Override
    public IMetadataUI createClassUI(IDocumentEditor editor) {
        return new DetailPartMetadataUI(new XmlClassMetadataPart(editor));
    }

    @Override
    public IMetadataUI createFieldUI(IDocumentEditor editor) {
        return new DetailPartMetadataUI(new XmlFieldMetadataPart(editor));
    }
}
