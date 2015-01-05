package io.takari.modello.editor.impl.model.plugin.java;

import io.takari.modello.editor.impl.model.plugin.DetailPartMetadataUI;
import io.takari.modello.editor.impl.model.plugin.IMetadataPlugin;
import io.takari.modello.editor.impl.model.plugin.IMetadataUI;
import io.takari.modello.editor.impl.model.plugin.java.ui.JavaClassMetadataPart;
import io.takari.modello.editor.impl.model.plugin.java.ui.JavaFieldMetadataPart;
import io.takari.modello.editor.impl.model.plugin.java.ui.JavaModelMetadataPart;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

public class JavaMetadataPlugin implements IMetadataPlugin {
    
    @Override
    public IMetadataUI createModelUI(IDocumentEditor editor) {
        return new DetailPartMetadataUI(new JavaModelMetadataPart(editor));
    }

    @Override
    public IMetadataUI createInterfaceUI(IDocumentEditor editor) {
        return null;
    }

    @Override
    public IMetadataUI createClassUI(IDocumentEditor editor) {
        return new DetailPartMetadataUI(new JavaClassMetadataPart(editor));
    }

    @Override
    public IMetadataUI createFieldUI(IDocumentEditor editor) {
        return new DetailPartMetadataUI(new JavaFieldMetadataPart(editor));
    }
}
