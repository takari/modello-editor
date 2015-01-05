package io.takari.modello.editor.toolkit.util;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.swt.graphics.Image;

public class TableSingleColumnLabelProvider extends ObservableMapLabelProvider {

    public TableSingleColumnLabelProvider(IObservableMap[] attributeMaps) {
        super(attributeMaps);
    }
    
    public TableSingleColumnLabelProvider(IObservableMap attributeMap) {
        super(attributeMap);
    }
    
    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        Object result = attributeMaps[0].get(element);
        return result == null ? null : (Image) result;
    }
    
    public String getColumnText(Object element, int columnIndex) {
        Object result = attributeMaps[1].get(element);
        return result == null ? "" : result.toString(); //$NON-NLS-1$
    }

}
