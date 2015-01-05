package io.takari.modello.editor.impl.model.plugin.xsd.ui;

import io.takari.modello.editor.impl.model.MModelDetails;
import io.takari.modello.editor.impl.model.plugin.MetadataPluginDetailPart;
import io.takari.modello.editor.impl.model.plugin.xsd.MXsdModelMetadata;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class XsdModelMetadataPart extends MetadataPluginDetailPart {
    
    private Text modelDetailsXsdNamespace;
    private Text modelDetailsXsdTargetNamespace;
    
    public XsdModelMetadataPart(IDocumentEditor editor) {
        super(editor);
    }
    
    @Override
    public Class<? extends IModel> getDetailClass() {
        return MModelDetails.class;
    }
    
    @Override
    public Class<? extends IModel> getMetadataDetailClass() {
        return MXsdModelMetadata.class;
    }
    
    @Override
    protected void createClient(Composite parent) {
        parent.setLayout(new GridLayout(2, false));
        
        Label lblModelDetailsXsdNamespace = getManagedForm().getToolkit().createLabel(parent, "Namespace", SWT.NONE);
        lblModelDetailsXsdNamespace.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        modelDetailsXsdNamespace = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        modelDetailsXsdNamespace.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblModelDetailsXsdTargetNamespace = getManagedForm().getToolkit().createLabel(parent, "Target namespace", SWT.NONE);
        lblModelDetailsXsdTargetNamespace.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        modelDetailsXsdTargetNamespace = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        modelDetailsXsdTargetNamespace.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    }
    
    @Override
    protected DataBindingContext createBindings() {
        return initDataBindings();
    }
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        IObservableValue observeTextModelDetailsXsdNamespaceObserveWidget = WidgetProperties.text(SWT.Modify).observe(modelDetailsXsdNamespace);
        IObservableValue modelDetailsxsdNamespaceObserveDetailValue = BeanProperties.value(MXsdModelMetadata.class, "xsdNamespace", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelDetailsXsdNamespaceObserveWidget, modelDetailsxsdNamespaceObserveDetailValue, null, null);
        //
        IObservableValue observeTextModelDetailsXsdTargetNamespaceObserveWidget = WidgetProperties.text(SWT.Modify).observe(modelDetailsXsdTargetNamespace);
        IObservableValue modelDetailsxsdTargetNamespaceObserveDetailValue = BeanProperties.value(MXsdModelMetadata.class, "xsdTargetNamespace", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelDetailsXsdTargetNamespaceObserveWidget, modelDetailsxsdTargetNamespaceObserveDetailValue, null, null);
        //
        return bindingContext;
    }
}
