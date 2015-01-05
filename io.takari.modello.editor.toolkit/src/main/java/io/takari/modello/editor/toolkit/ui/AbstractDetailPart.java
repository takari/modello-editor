package io.takari.modello.editor.toolkit.ui;

import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.mapping.model.IModelExtension;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.forms.IFormPart;

public abstract class AbstractDetailPart extends AbstractEditorFormPart {
    
    private final WritableValue currentItem;

    public AbstractDetailPart(IDocumentEditor editor) {
        super(editor);
        currentItem = new WritableValue();
    }
    
    public abstract Class<? extends IModel> getDetailClass();
    
    
    @Override
    public void selectionChanged(IFormPart part, ISelection selection) {
        if(selection instanceof StructuredSelection) {
            Object selectedBean = ((StructuredSelection) selection).getFirstElement();
            setSelection(selectedBean);
        }
    }
    
    protected void setSelection(Object selectedBean) {
        currentItem.setValue(selectedBean);
        if(selectedBean instanceof IModelExtension) {
            ((IModelExtension) selectedBean)._touch(null);
        }
    }
    
    public IObservableValue getCurrentItem() {
        return currentItem;
    }
    
}
