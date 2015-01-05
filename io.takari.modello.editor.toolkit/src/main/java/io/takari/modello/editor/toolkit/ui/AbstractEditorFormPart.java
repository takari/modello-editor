package io.takari.modello.editor.toolkit.ui;

import io.takari.modello.editor.mapping.api.SyncState;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;

public abstract class AbstractEditorFormPart extends AbstractFormPart implements IDetailsPage {
    
    private static final String PART_KEY = AbstractEditorFormPart.class.getName() + ".part";
    
    private IDocumentEditor editor;
    private SyncState sync;

    private DataBindingContext bindingContext;

    public AbstractEditorFormPart(IDocumentEditor editor) {
        this.editor = editor;
    }
    
    public void createContents(Composite parent) {
        parent.setData(PART_KEY, this);
        createClient(parent);
        
        bindingContext = createBindings();
    }
    
    /**
     * @wbp.parser.entryPoint
     */
    protected abstract void createClient(Composite parent);
    
    protected abstract DataBindingContext createBindings();
    
    public void selectionChanged(IFormPart part, ISelection selection) {
    }
    
    public IDocumentEditor getEditor() {
        return editor;
    }
    
    public IModel getModel() {
        return editor.getModel();
    }
    
    @Override
    public boolean isStale() {
        return sync == null || !sync.isValid() || super.isStale();
    }
    
    @Override
    public void refresh() {
        if(bindingContext != null) bindingContext.updateTargets();
        sync = editor.getSync();
        refreshPart();
        super.refresh();
    }
    
    protected void refreshPart() {
    }
    
    @Override
    public void dispose() {
        if(bindingContext != null) bindingContext.dispose();
        super.dispose();
    }
    
    
    void registerControl(final Control control) {
        if(control instanceof Text || control instanceof StyledText || control instanceof Combo) {
            control.addFocusListener(DOM_LISTENER);
        }
    }
    
    private final FocusListener DOM_LISTENER = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            getEditor().requestWrite();
        }
        
        @Override
        public void focusLost(FocusEvent e) {
            getEditor().releaseWrite();
        }
    };
    
    static void registerWritableControls(Control control) {
        Composite p = control.getParent();
        while(p != null) {
            
            AbstractEditorFormPart part = (AbstractEditorFormPart) p.getData(PART_KEY);
            if(part != null) {
                part.registerControl(control);
                break;
            }
            
            p = p.getParent();
        }
    }
}
