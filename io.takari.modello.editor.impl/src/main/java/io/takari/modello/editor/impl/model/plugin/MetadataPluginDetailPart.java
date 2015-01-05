package io.takari.modello.editor.impl.model.plugin;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.forms.IFormPart;

import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.mapping.model.IModelExtension;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;
import io.takari.modello.editor.toolkit.ui.AbstractDetailPart;

public abstract class MetadataPluginDetailPart extends AbstractDetailPart {
    
    //private IModelExtension metadataModel;
    
    protected MetadataPluginDetailPart(IDocumentEditor editor) {
        super(editor);
    }
    
    protected abstract Class<? extends IModel> getMetadataDetailClass();
    
    @Override
    public void selectionChanged(IFormPart part, ISelection selection) {
        
        IModelExtension model = (IModelExtension) ((IStructuredSelection)selection).getFirstElement();
        IModelExtension delegate = model._getDelegate();
        
        if(getDetailClass().isAssignableFrom(delegate._getModelClass())) {
            
            String key = MetadataPluginDetailPart.class.getName() + "#metadata#" + getMetadataDetailClass().getName();
            IModelExtension metadataModel = (IModelExtension) delegate._getData(key);
            
            if(metadataModel == null) {
                metadataModel = (IModelExtension) delegate._createSubProxy(getMetadataDetailClass());
                delegate._setData(key, metadataModel);
            }
            
            selection = new StructuredSelection(metadataModel);
            
        } else {
            
            selection = new StructuredSelection();
            
        }
        super.selectionChanged(part, selection);
    }
    
}
