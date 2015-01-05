package io.takari.modello.editor.toolkit.ui;

import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public abstract class AbstractEditorPage extends FormPage {

    public AbstractEditorPage(IDocumentEditor editor, String subIdentifier, String title) {
        super(editor.getEditorPart(), editor.getEditorPart().getEditorSite().getId() + "." + subIdentifier, title);
    }
    
    @Override
    protected void createFormContent(IManagedForm managedForm) {
        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        form.setText(getTitle());
        toolkit.paintBordersFor(form.getBody());
        
        createContents(managedForm);
        
        form.updateToolBar();
        toolkit.decorateFormHeading(form.getForm());
    }
    
    protected abstract void createContents(IManagedForm managedForm);
    
}
