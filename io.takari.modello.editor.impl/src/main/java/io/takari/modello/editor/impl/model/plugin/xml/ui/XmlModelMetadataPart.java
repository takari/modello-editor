package io.takari.modello.editor.impl.model.plugin.xml.ui;

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

import io.takari.modello.editor.impl.model.MModelDetails;
import io.takari.modello.editor.impl.model.plugin.MetadataPluginDetailPart;
import io.takari.modello.editor.impl.model.plugin.xml.MXmlModelMetadata;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

public class XmlModelMetadataPart extends MetadataPluginDetailPart {
    
    private Text modelDetailsXmlNamespace;
    private Text modelDetailsXmlSchemaLocation;
    private Button modelDetailsXmlStrictAttributes;
    
    public XmlModelMetadataPart(IDocumentEditor editor) {
        super(editor);
    }
    
    @Override
    public Class<? extends IModel> getDetailClass() {
        return MModelDetails.class;
    }
    
    @Override
    public Class<? extends IModel> getMetadataDetailClass() {
        return MXmlModelMetadata.class;
    }
    
    @Override
    protected void createClient(Composite parent) {
        parent.setLayout(new GridLayout(2, false));
        
        Label lblModelDetailsXmlNamespace = getManagedForm().getToolkit().createLabel(parent, "Namespace", SWT.NONE);
        lblModelDetailsXmlNamespace.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        modelDetailsXmlNamespace = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        modelDetailsXmlNamespace.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblModelDetailsSchemaLocation = getManagedForm().getToolkit().createLabel(parent, "Schema Location", SWT.NONE);
        lblModelDetailsSchemaLocation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        modelDetailsXmlSchemaLocation = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        modelDetailsXmlSchemaLocation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        modelDetailsXmlStrictAttributes = new Button(parent, SWT.CHECK);
        modelDetailsXmlStrictAttributes.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        getManagedForm().getToolkit().adapt(modelDetailsXmlStrictAttributes, true, true);
        modelDetailsXmlStrictAttributes.setText("Strict Attributes");

    }
    
    @Override
    protected DataBindingContext createBindings() {
        return initDataBindings();
    }
    
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        IObservableValue observeTextModelDetailsXmlNamespaceObserveWidget = WidgetProperties.text(SWT.Modify).observe(modelDetailsXmlNamespace);
        IObservableValue modelDetailsxmlNamespaceObserveDetailValue = BeanProperties.value(MXmlModelMetadata.class, "xmlNamespace", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelDetailsXmlNamespaceObserveWidget, modelDetailsxmlNamespaceObserveDetailValue, null, null);
        //
        IObservableValue observeTextModelDetailsXmlSchemaLocationObserveWidget = WidgetProperties.text(SWT.Modify).observe(modelDetailsXmlSchemaLocation);
        IObservableValue modelDetailsxmlSchemaLocationObserveDetailValue = BeanProperties.value(MXmlModelMetadata.class, "xmlSchemaLocation", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelDetailsXmlSchemaLocationObserveWidget, modelDetailsxmlSchemaLocationObserveDetailValue, null, null);
        //
        IObservableValue observeSelectionModelDetailsXmlStrictAttributesObserveWidget = WidgetProperties.selection().observe(modelDetailsXmlStrictAttributes);
        IObservableValue modelDetailsdefaultsstrictXmlAttrsObserveDetailValue = BeanProperties.value(MXmlModelMetadata.class, "details.defaults.strictXmlAttrs", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionModelDetailsXmlStrictAttributesObserveWidget, modelDetailsdefaultsstrictXmlAttrsObserveDetailValue, null, null);
        //
        return bindingContext;
    }
}
