package io.takari.modello.editor.toolkit.ui;

import java.util.ArrayList;
import java.util.List;

import io.takari.modello.editor.mapping.model.IModelExtension;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;

public class ModelMasterDetailsBlock extends MasterDetailsBlock {
    
    private final ModelTreePart modelTreePart;
    private final List<AbstractDetailPart> details;
    
    public ModelMasterDetailsBlock(IDocumentEditor editor) {
        modelTreePart = new ModelTreePart(editor);
        details = new ArrayList<AbstractDetailPart>();
    }
    
    public ModelMasterDetailsBlock addPart(AbstractDetailPart part) {
        details.add(part);
        return this;
    }
    
    @Override
    protected final void createMasterPart(IManagedForm managedForm, Composite parent) {
        managedForm.addPart(modelTreePart);
        
        Composite body = managedForm.getToolkit().createComposite(parent, SWT.NONE);
        modelTreePart.createContents(body);
    }
    
    @Override
    protected void createToolBarActions(IManagedForm managedForm) {
        
    }

    
    @Override
    protected final void registerPages(DetailsPart detailsPart) {
        
        sashForm.setWeights(new int[]{1, 2});
        
        for(AbstractDetailPart part: details) {
            detailsPart.registerPage(part.getDetailClass(), part);
        }
        
        detailsPart.setPageProvider(new IDetailsPageProvider() {
            @Override
            public Object getPageKey(Object object) {
                return ((IModelExtension)object)._getModelClass();
            }
            
            @Override
            public IDetailsPage getPage(Object key) {
                return null;
            }
        });
    }

}
