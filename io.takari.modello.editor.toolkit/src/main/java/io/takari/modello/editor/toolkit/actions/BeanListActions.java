package io.takari.modello.editor.toolkit.actions;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import io.takari.modello.editor.mapping.model.IListControl;
import io.takari.modello.editor.mapping.model.IModelExtension;

public class BeanListActions {
    
    public static BeanListActions configure(IObservableValue obj, String property, TableViewer viewer, Button addButton, Button removeButton) {
        BeanListActions actions = new BeanListActions(obj, property, viewer, addButton, removeButton);
        return actions;
    }
    
    private IObservableValue obj;
    private String property;
    private TableViewer viewer;
    private Button addButton;
    private Button removeButton;
    private Button editButton;
    private IModelProvider provider;
    
    BeanListActions(IObservableValue obj, String property, TableViewer viewer, Button addButton, Button removeButton) {
        this.obj = obj;
        this.property = property;
        this.viewer = viewer;
        this.addButton = addButton;
        this.removeButton = removeButton;
    }
    
    public BeanListActions withDialog(Button editButton, IModelProvider provider) {
        this.editButton = editButton;
        this.provider = provider;
        return this;
    }
    
    private IModelExtension getSelectedItem() {
        return (IModelExtension) ((IStructuredSelection)viewer.getSelection()).getFirstElement();
    }

    public void bind() {
        
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                
                updateButtons();
                
            }
        });
        
        if(addButton != null) {
            addButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    
                    IModelExtension tempModel = null;
                    
                    if(provider != null) {
                        tempModel = provider.createTempModel();
                        if(!provider.showDialog(tempModel)) {
                            return;
                        }
                    }
                    
                    IModelExtension newItem = add();
                    if(tempModel != null) {
                        tempModel._apply(newItem);
                    }
                    viewer.setSelection(new StructuredSelection(newItem));
                }
            });
        }
        
        if(removeButton != null) {
            removeButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    IModelExtension item = getSelectedItem();
                    if(item != null)
                    {
                        int curIdx = viewer.getTable().getSelectionIndex();
                        
                        remove(item);
                        viewer.refresh();
                        
                        int count = viewer.getTable().getItemCount();
                        Object newElem;
                        if(count == 0) newElem = null;
                        else if(curIdx < count) newElem = viewer.getElementAt(curIdx);
                        else newElem = viewer.getElementAt(count - 1);
                        
                        if(newElem != null) {
                            viewer.setSelection(new StructuredSelection(newElem));
                        }
                    }
                }
            });
        }
        
        if(provider != null) {
            if(editButton != null) {
                editButton.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        edit();
                    }
                });
            }
            
            viewer.addDoubleClickListener(new IDoubleClickListener() {
                @Override
                public void doubleClick(DoubleClickEvent event) {
                    edit();
                }
            });
        }
        updateButtons();
    }
    
    private void edit() {
        IModelExtension item = getSelectedItem();
        if(item != null) {
            IModelExtension tempModel = provider.createTempModel();
            item._apply(tempModel);
            
            if(!provider.showDialog(tempModel)) {
                return;
            }
            
            tempModel._apply(item);
        }
    }
    
    private void updateButtons() {
        Object item = getSelectedItem();
        if(removeButton != null) removeButton.setEnabled(item != null);
        if(editButton != null) editButton.setEnabled(item != null);
    }
    
    private IModelExtension add() {
        return getListControl().add();
    }

    private void remove(IModelExtension item) {
        getListControl().remove(item);
    }

    private IListControl getListControl() {
        IListControl listControl = ((IModelExtension) obj.getValue())._getDelegate()._getListControl(property);
        if(listControl == null) throw new IllegalArgumentException("No list control for " + property);
        return listControl;
    }
    
    public static interface IModelProvider {

        IModelExtension createTempModel();

        boolean showDialog(IModelExtension tempModel);
        
    }
    
}
