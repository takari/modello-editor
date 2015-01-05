package io.takari.modello.editor.toolkit.ui;

import java.util.ArrayList;
import java.util.List;

import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public abstract class AbstractEditorFormDialog extends TrayDialog {

    private final IDocumentEditor editor;
    private final List<IFormPart> parts;
    private FormToolkit toolkit;
    private IManagedForm mform;
    
    public AbstractEditorFormDialog(IDocumentEditor editor) {
        super(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
        this.editor = editor;
        parts = new ArrayList<IFormPart>();
    }
    
    @Override
    protected boolean isResizable() {
        return true;
    }
    
    public IDocumentEditor getEditor() {
        return editor;
    }
    
    protected IManagedForm getManagedForm() {
        return mform;
    }
    
    protected void addPart(IFormPart part) {
        parts.add(part);
    }
    
    protected abstract void createFormContent(IManagedForm mform);
    
    protected Control createDialogArea(Composite parent) {
        toolkit = new EditorFormToolkit(parent.getDisplay());
        
        ScrolledForm sform = toolkit.createScrolledForm(parent);
        sform.setLayoutData(new GridData(GridData.FILL_BOTH));
        mform = new ManagedForm(toolkit, sform);
        toolkit.paintBordersFor(sform.getBody());
        
        for(IFormPart part: parts) {
            part.initialize(mform);
        }
        
        createFormContent(mform);
        
        toolkit.decorateFormHeading(sform.getForm());
        applyDialogFont(sform.getBody());
        return sform;
    }
    
    protected Control createButtonBar(Composite parent) {
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        //Composite sep = new Composite(parent, SWT.NULL);
        //sep.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
        //gd.heightHint = 1;
        Label sep = new Label(parent, SWT.HORIZONTAL|SWT.SEPARATOR);
        sep.setLayoutData(gd);
        Control bar = super.createButtonBar(parent);
        return bar;
    }
    
    public boolean close() {
        boolean rcode = super.close();
        toolkit.dispose();
        for(IFormPart part: parts) {
            part.dispose();
        }
        return rcode;
    }
}
