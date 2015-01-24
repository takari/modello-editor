package io.takari.modello.editor.toolkit.ui;

import java.util.List;

import io.takari.modello.editor.mapping.model.IListControl;
import io.takari.modello.editor.mapping.model.IModelExtension;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;
import io.takari.modello.editor.toolkit.model.ITreeBean;
import io.takari.modello.editor.toolkit.ui.AbstractEditorFormPart;
import io.takari.modello.editor.toolkit.util.ModelListDragSupport;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wb.rcp.databinding.BeansListObservableFactory;
import org.eclipse.wb.rcp.databinding.TreeBeanAdvisor;
import org.eclipse.wb.rcp.databinding.TreeObservableStyledCellLabelProvider;

public class ModelTreePart extends AbstractEditorFormPart {
    private TreeViewer modelTreeViewer;
    private Tree modelTree;
    
    public ModelTreePart(IDocumentEditor editor) {
        super(editor);
    }
    
    @Override
    protected void createClient(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        
        Section modelTreeSection = getManagedForm().getToolkit().createSection(parent, Section.TITLE_BAR);
        GridData gd_modelTreeSection = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_modelTreeSection.heightHint = 300;
        modelTreeSection.setLayoutData(gd_modelTreeSection);
        getManagedForm().getToolkit().paintBordersFor(modelTreeSection);
        modelTreeSection.setText("Contents");
        
        Composite modelTreeContainer = getManagedForm().getToolkit().createComposite(modelTreeSection, SWT.NONE);
        getManagedForm().getToolkit().paintBordersFor(modelTreeContainer);
        modelTreeContainer.setLayout(new GridLayout(1, false));
        
        modelTreeSection.setClient(modelTreeContainer);
        
        modelTreeViewer = new TreeViewer(modelTreeContainer, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        modelTree = modelTreeViewer.getTree();
        GridData gd_modelTree = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_modelTree.minimumHeight = 200;
        gd_modelTree.heightHint = 200;
        modelTree.setLayoutData(gd_modelTree);
        getManagedForm().getToolkit().adapt(modelTree);
        getManagedForm().getToolkit().paintBordersFor(modelTree);
        
        hookListeners();
        
        hookMenu();
    }
    
    private void hookListeners() {
        modelTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                getManagedForm().fireSelectionChanged(ModelTreePart.this, event.getSelection());
            }
        });
        
        ModelListDragSupport.configure(modelTreeViewer);
    }
    
    
    
    private void hookMenu() {
        
        MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                ModelTreePart.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(modelTreeViewer.getControl());
        menuMgr.setRemoveAllWhenShown(true);
        modelTreeViewer.getControl().setMenu(menu);
        getEditor().getEditorPart().getEditorSite().registerContextMenu(getEditor().getEditorPart().getEditorSite().getId() + ".menu.modelTree", menuMgr, modelTreeViewer, false);
        
    }
    
    protected void fillContextMenu(IMenuManager manager) {
        
        final IModelExtension model = (IModelExtension) ((IStructuredSelection)modelTreeViewer.getSelection()).getFirstElement();
        if(model == null) return;
        
        final IModelExtension delegate = model._getDelegate();
        
        for(String lprop: delegate._getControlledListProperties()) {
            final IListControl control = model._getListControl(lprop);
            if(!control.hasHint("modelTree")) continue;
            
            if(control.isEditable()) {
                manager.add(new Action("Add " + control.getName()){
                    @Override
                    public void run() {
                        IModelExtension newItem = control.add();
                        modelTreeViewer.expandToLevel(model, 1);
                        modelTreeViewer.setSelection(new StructuredSelection(newItem));
                    }
                });
            }
        }
        
        IModelExtension parent = (IModelExtension) delegate.getParent();
        if(parent != null) {
            IModelExtension parentDelegate = parent._getDelegate();
            String pprop = delegate._getParentProperty();
            final IListControl parentControl = parentDelegate._getListControl(pprop);
            if(parentControl != null && parentControl.isEditable()) {
                final int idx = delegate._getIndex();
                if(idx > 0) {
                    manager.add(new Action("Move Up"){
                        @Override
                        public void run() {
                            parentControl.move(model, idx - 1);
                        }
                    });
                }
                if(idx + 1 < ((List<?>)parent._get(pprop)).size()) {
                    manager.add(new Action("Move Down"){
                        @Override
                        public void run() {
                            parentControl.move(model, idx + 1);
                        }
                    });
                }
                manager.add(new Action("Delete " + parentControl.getName()){
                    @Override
                    public void run() {
                        parentControl.remove(model);
                    }
                });
            }
        }
        
    }
    
    @Override
    protected void refreshPart() {
        modelTreeViewer.refresh();
    }
    
    @Override
    public void setFocus() {
        modelTree.setFocus();
    }
    
    @Override
    protected DataBindingContext createBindings() {
        /*
         * prevent confusing windowbuilder with TreeObservableStyledCellLabelProvider
         */
        return initDataBindings0();
    }
    
    protected DataBindingContext initDataBindings0() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        BeansListObservableFactory treeObservableFactory = new BeansListObservableFactory(ITreeBean.class, "children");
        TreeBeanAdvisor treeAdvisor = new TreeBeanAdvisor(ITreeBean.class, null, "children", "hasChildren");
        ObservableListTreeContentProvider treeContentProvider = new ObservableListTreeContentProvider(treeObservableFactory, treeAdvisor);
        modelTreeViewer.setLabelProvider(new TreeObservableStyledCellLabelProvider(treeContentProvider.getKnownElements(), ITreeBean.class, "label", "image"));
        modelTreeViewer.setContentProvider(treeContentProvider);
        //
        IObservableList childrenEditorgetModelObserveList = BeanProperties.list("children").observe(getModel());
        modelTreeViewer.setInput(childrenEditorgetModelObserveList);
        //
        return bindingContext;
    }
}
