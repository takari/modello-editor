package io.takari.modello.editor.impl.ui;

import io.takari.modello.editor.toolkit.editor.IDocumentEditor;
import io.takari.modello.editor.toolkit.ui.AbstractEditorPage;
import io.takari.modello.editor.toolkit.ui.ModelMasterDetailsBlock;

import org.eclipse.ui.forms.IManagedForm;

public class ModelloDesignPage extends AbstractEditorPage {

    private final ModelMasterDetailsBlock modelBlock;
    
    public ModelloDesignPage(IDocumentEditor editor) {
        super(editor, "designPage", "Model");
        modelBlock = new ModelMasterDetailsBlock(editor)
            .addPart(new ModelDetailsPart(editor))
            .addPart(new ClassPart(editor))
            .addPart(new InterfacePart(editor))
            .addPart(new FieldPart(editor));
    }
    
    @Override
    protected void createContents(IManagedForm managedForm) {
        modelBlock.createContent(managedForm, managedForm.getForm().getBody());
    }
    
}
