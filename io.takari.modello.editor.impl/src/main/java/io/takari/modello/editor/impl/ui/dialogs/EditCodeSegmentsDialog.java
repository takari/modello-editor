package io.takari.modello.editor.impl.ui.dialogs;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.forms.IManagedForm;

import io.takari.modello.editor.impl.model.MCodeSegment;
import io.takari.modello.editor.impl.ui.CodeSegmentPart;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;
import io.takari.modello.editor.toolkit.ui.AbstractEditorFormDialog;

public class EditCodeSegmentsDialog extends AbstractEditorFormDialog {

    private CodeSegmentPart codeSegmentsPart;

    public EditCodeSegmentsDialog(IDocumentEditor editor, MCodeSegment codeSegment) {
        super(editor);
        addPart(codeSegmentsPart = new CodeSegmentPart(editor, codeSegment));
    }
    
    @Override
    protected Point getInitialSize() {
        return new Point(600, 400);
    }
    
    @Override
    protected void createFormContent(IManagedForm mform) {
        codeSegmentsPart.createContents(mform.getForm().getBody());
    }
    
}
