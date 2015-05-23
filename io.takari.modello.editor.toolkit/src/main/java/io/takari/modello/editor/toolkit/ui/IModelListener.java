package io.takari.modello.editor.toolkit.ui;

import io.takari.modello.editor.mapping.model.IModelExtension;

public interface IModelListener {
    
    void modelAdded(IModelExtension model);
    
    void modelRemoved(IModelExtension model);
    
}
