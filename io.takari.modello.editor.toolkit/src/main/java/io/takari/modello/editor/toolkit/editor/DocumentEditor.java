package io.takari.modello.editor.toolkit.editor;

import io.takari.modello.editor.mapping.api.IModelAccessor;
import io.takari.modello.editor.mapping.api.SyncState;
import io.takari.modello.editor.mapping.dom.IDocumentSessionProvider;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.mapping.proxy.ModelProxyGenerator;
import io.takari.modello.editor.toolkit.ToolkitPlugin;
import io.takari.modello.editor.toolkit.ui.EditorFormToolkit;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorSite;

public abstract class DocumentEditor extends FormEditor implements IResourceChangeListener, IGotoMarker, IDocumentEditor {

    private TextEditor textEditor;
    private int sourcePageIndex;
    
    private IDocument document;
    private IModel model;
    
    private long lastModificationStamp = -1;
    private volatile SyncState sync = new SyncState();
    private ModelProxyGenerator proxyGenerator;

    protected DocumentEditor() {
        super();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }
    
    protected abstract Class<? extends IModel> getModelClass();
    
    protected abstract void createFormPages() throws PartInitException;
    
    protected abstract String getTextEditorContentType();
    
    protected abstract TextEditor createTextEditor();
    
    protected abstract String getSourcePageText();
    
    protected abstract IDocumentSessionProvider getSessionProvider();
    
    protected abstract IModelAccessor<?> createModelAccessor(ModelProxyGenerator gen); 
    
    
    private void initDocument() {
        document = textEditor.getDocumentProvider().getDocument(getEditorInput());
        
        proxyGenerator = new ModelProxyGenerator();
        model = proxyGenerator.createProxy(createModelAccessor(proxyGenerator), getModelClass());
    }
    
    @Override
    public IFile getFile() {
        IEditorInput editorInput = getEditorInput();
        
        if(editorInput instanceof IFileEditorInput) {
            return ((IFileEditorInput) editorInput).getFile();
        }
        
        return null;
    }
    
    protected void addPages() {
        try {
            createFormPages();
            createSourcePage();
        } catch(PartInitException e) {
            ErrorDialog.openError(
                    getSite().getShell(),
                    "Error creating nested text editor",
                    null,
                    e.getStatus());
        }
        
        initDocument();
        
        int activePageIndex = getPreferenceStore().getInt(getEditorSite().getId() + ".lastActivePage"); //$NON-NLS-1$;
        if ((activePageIndex >= 0) && (activePageIndex < getPageCount())) {
            setActivePage(activePageIndex);
        } else {
            setActivePage(0);
        }
    }
    
    private void createSourcePage() throws PartInitException {
        textEditor = createTextEditor();
        sourcePageIndex = addPage(textEditor, getEditorInput());
        setPageText(sourcePageIndex, getSourcePageText());
        firePropertyChange(PROP_TITLE);
    }
    
    protected IEditorSite createSite(IEditorPart page) {
        IEditorSite site = null;
        if (page == textEditor) {
            site = new MultiPageEditorSite(this, page) {
                
                public IEditorActionBarContributor getActionBarContributor() {
                    IEditorActionBarContributor contributor = super.getActionBarContributor();
                    IEditorActionBarContributor multiContributor = DocumentEditor.this.getEditorSite().getActionBarContributor();
                    if(multiContributor instanceof DocumentEditorContributor) {
                      contributor = ((DocumentEditorContributor) multiContributor).sourceViewerActionContributor;
                    }
                    return contributor;
                }
                
                public String getId() {
                    return getTextEditorContentType(); //$NON-NLS-1$;
                }
            };
        }
        else {
            site = super.createSite(page);
        }
        return site;
    }
    
    @Override
    protected Composite createPageContainer(Composite parent) {
        Composite container = super.createPageContainer(parent);
        
        container.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                getSessionProvider().close();
            }
        });
        
        return container;
    }
    
    public String getTitle() {
        String title = null;
        if (textEditor == null) {
            if (getEditorInput() != null) {
                title = getEditorInput().getName();
            }
        }
        else {
            title = textEditor.getTitle();
        }
        if (title == null) {
            title = getPartName();
        }
        return title;
    }
    
    @Override
    protected FormToolkit createToolkit(Display display) {
        return new EditorFormToolkit(display);
    }

    public void dispose() {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        super.dispose();
    }
    
    public void doSave(IProgressMonitor monitor) {
        textEditor.doSave(monitor);
    }
    
    public void doSaveAs() {
        textEditor.doSaveAs();
        setPageText(0, textEditor.getTitle());
        setInput(textEditor.getEditorInput());
    }
    
    public void gotoMarker(IMarker marker) {
        setActivePage(1);
        IDE.gotoMarker(textEditor, marker);
    }
    
    public void init(IEditorSite site, IEditorInput editorInput)
        throws PartInitException {
        if (!(editorInput instanceof IFileEditorInput))
            throw new PartInitException("Invalid Input: Must be IFileEditorInput");
        super.init(site, editorInput);
    }
    
    public boolean isSaveAsAllowed() {
        return true;
    }
    
    @Override
    public boolean isDirty() {
        return textEditor.isDirty();
    }
    
    public void resourceChanged(final IResourceChangeEvent event){
        if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
            Display.getDefault().asyncExec(new Runnable(){
                public void run(){
                    IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
                    for (int i = 0; i<pages.length; i++){
                        if(((FileEditorInput)textEditor.getEditorInput()).getFile().getProject().equals(event.getResource())){
                            IEditorPart editorPart = pages[i].findEditor(textEditor.getEditorInput());
                            pages[i].closeEditor(editorPart,true);
                        }
                    }
                }
            });
        }
    }
    
    protected void pageChange(int newPageIndex) {
        
        if(newPageIndex == sourcePageIndex) getSessionProvider().release();
        else getSessionProvider().requestRead();
        
        super.pageChange(newPageIndex);
        
        saveLastActivePageIndex(newPageIndex);
        
        if(newPageIndex != sourcePageIndex) {
            refreshPage();
        }
    }

    private void saveLastActivePageIndex(int newPageIndex) {
        // save the last active page index to preference manager
        getPreferenceStore().setValue(getEditorSite().getId() + ".lastActivePage", newPageIndex); //$NON-NLS-1$
    }

    private IPreferenceStore getPreferenceStore() {
        return ToolkitPlugin.getInstance().getPreferenceStore();
    }
    
    private long getDocTimestamp(IDocument document) {
        if(document instanceof IDocumentExtension4) {
            IDocumentExtension4 doc4 = (IDocumentExtension4) document;
            return doc4.getModificationStamp();
        }
        return -1;
    }
    
    private void refreshPage() {
        final IDocument document = getDocument();
        
        long newTimestamp = getDocTimestamp(document);
        
        if(newTimestamp == -1L || newTimestamp > lastModificationStamp) {
            
            lastModificationStamp = newTimestamp;
            
            SyncState olds = sync;
            sync = new SyncState();
            olds.invalidate();
        }
        
        getActivePageInstance().getManagedForm().refresh();
    }
    
    @Override
    public FormEditor getEditorPart() {
        return this;
    }

    @Override
    public IDocument getDocument() {
        return document;
    }
    
    @Override
    public IModel getModel() {
        return model;
    }
    
    public ModelProxyGenerator getProxyGenerator() {
        return proxyGenerator;
    }
    
    @Override
    public void synchronize() {
        lastModificationStamp = getDocTimestamp(getDocument());
    }

    @Override
    public void requestWrite() {
        getSessionProvider().requestWrite();
    }
    
    @Override
    public void releaseWrite() {
        getSessionProvider().release();
    }
    
    @Override
    public SyncState getSync() {
        return sync;
    }
    
    public void select(int offset, int length) {
        textEditor.getSelectionProvider().setSelection(new TextSelection(offset, length));
        /*
        textEditor.getTextViewer().setSelectedRange(offset, length);
        textEditor.getTextViewer().revealRange(offset, length);
        */
    }

}
