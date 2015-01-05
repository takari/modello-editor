package io.takari.modello.editor.impl.model.plugin.model.ui;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import io.takari.modello.editor.impl.model.MClass;
import io.takari.modello.editor.impl.model.plugin.MetadataPluginDetailPart;
import io.takari.modello.editor.impl.model.plugin.model.MModelClassMetadata;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

public class ModelClassMetadataPart extends MetadataPluginDetailPart {
    
    private Button modelRootElement;
    private Text modelSourceTracker;
    private Text modelLocationTracker;
    
    public ModelClassMetadataPart(IDocumentEditor editor) {
        super(editor);
    }
    
    @Override
    public Class<? extends IModel> getDetailClass() {
        return MClass.class;
    }
    
    @Override
    public Class<? extends IModel> getMetadataDetailClass() {
        return MModelClassMetadata.class;
    }
    
    @Override
    protected void createClient(Composite parent) {
        parent.setLayout(new GridLayout(2, false));
        
        modelRootElement = new Button(parent, SWT.CHECK);
        modelRootElement.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        getManagedForm().getToolkit().adapt(modelRootElement, true, true);
        modelRootElement.setText("Root Element");
        
        Label lblSourceTracker = getManagedForm().getToolkit().createLabel(parent, "Source Tracker", SWT.NONE);
        lblSourceTracker.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        modelSourceTracker = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        modelSourceTracker.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblLocationTracker = getManagedForm().getToolkit().createLabel(parent, "Location Tracker", SWT.NONE);
        lblLocationTracker.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        modelLocationTracker = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        modelLocationTracker.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

    }
    
    @Override
    protected DataBindingContext createBindings() {
        return initDataBindings();
    }
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        IObservableValue observeSelectionModelRootElementObserveWidget = WidgetProperties.selection().observe(modelRootElement);
        IObservableValue currentClassModelRootElementObserveDetailValue = BeanProperties.value(MModelClassMetadata.class, "modelRootElement", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionModelRootElementObserveWidget, currentClassModelRootElementObserveDetailValue, null, null);
        //
        IObservableValue observeTextModelSourceTrackerObserveWidget = WidgetProperties.text(SWT.Modify).observe(modelSourceTracker);
        IObservableValue currentClassModelSourceTrackerObserveDetailValue = BeanProperties.value(MModelClassMetadata.class, "modelSourceTracker", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelSourceTrackerObserveWidget, currentClassModelSourceTrackerObserveDetailValue, null, null);
        //
        IObservableValue observeTextModelLocationTrackerObserveWidget = WidgetProperties.text(SWT.Modify).observe(modelLocationTracker);
        IObservableValue currentClassModelLocationTrackerObserveDetailValue = BeanProperties.value(MModelClassMetadata.class, "modelLocationTracker", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelLocationTrackerObserveWidget, currentClassModelLocationTrackerObserveDetailValue, null, null);
        //
        return bindingContext;
    }
}
