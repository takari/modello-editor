package io.takari.modello.editor.toolkit.util;

import io.takari.modello.editor.mapping.model.IListControl;
import io.takari.modello.editor.mapping.model.IModelExtension;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

public class ModelListDragSupport {
    
    public static void configure(final StructuredViewer viewer) {
        
        viewer.addDragSupport(DND.DROP_MOVE, new Transfer[]{ LocalSelectionTransfer.getTransfer() }, new DragSourceListener() {
            @Override
            public void dragStart(DragSourceEvent event) {
                LocalSelectionTransfer.getTransfer().setSelection(viewer.getSelection());
            }
            @Override
            public void dragSetData(DragSourceEvent event) {
            }
            @Override
            public void dragFinished(DragSourceEvent event) {
                LocalSelectionTransfer.getTransfer().setSelection(null);
            }
        });
        
        viewer.addDropSupport(DND.DROP_MOVE, new Transfer[]{ LocalSelectionTransfer.getTransfer() }, new ViewerDropAdapter(viewer) {
            
            IListControl list;
            IModelExtension sourceElem;
            int newIdx;
            
            @Override
            public boolean validateDrop(Object target, int operation, TransferData transferType) {
                if(target == null) return false;
                
                sourceElem = ((IModelExtension) getSelectedObject());
                IModelExtension sourceDelegate = sourceElem._getDelegate();
                IModelExtension targetElem = ((IModelExtension) target)._getDelegate();
                
                String parentProp = sourceDelegate._getParentProperty();
                IModelExtension parent = (IModelExtension) sourceDelegate.getParent();
                
                list = parent._getListControl(parentProp);
                if(list == null || !list.isEditable()) return false;
                
                IModelExtension targetParent = (IModelExtension) targetElem.getParent();
                if(parent != targetParent) return false;
                
                int oldIdx = sourceDelegate._getIndex();
                int newIdx = targetElem._getIndex();
                
                if(newIdx > oldIdx) {
                    newIdx--;
                }
                
                int loc = getCurrentLocation();
                switch(loc) {
                case LOCATION_AFTER:
                    newIdx++;
                    break;
                }
                this.newIdx = newIdx;
                return true;
            }
            
            @Override
            public boolean performDrop(Object data) {
                
                if(sourceElem._getIndex() != newIdx) {
                    list.move(sourceElem, newIdx);
                }
                list = null;
                sourceElem = null;
                newIdx = 0;
                return true;
            }
            
        });
    }
    
}
