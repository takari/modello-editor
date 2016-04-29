package io.takari.modello.editor.impl.ui;

import org.eclipse.ui.PartInitException;

import io.takari.modello.editor.impl.model.MModel;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.toolkit.dom.DomDocumentEditor;

public class ModelloEditor extends DomDocumentEditor {
    
    @Override
    protected Class<? extends IModel> getModelClass() {
        return MModel.class;
    }

    @Override
    protected void createFormPages() throws PartInitException {
        addPage(new ModelloDesignPage(this));
    }
    
    @Override
    protected String getModelNamespace() {
        return "http://codehaus-plexus.github.io/MODELLO/1.8.0";
    }
    
    @Override
    protected String getModelSchemaLocation() {
        return "http://codehaus-plexus.github.io/modello/xsd/modello-1.8.0.xsd";
    }
    
    @Override
    protected String getModelRoot() {
        return "model";
    }
}
