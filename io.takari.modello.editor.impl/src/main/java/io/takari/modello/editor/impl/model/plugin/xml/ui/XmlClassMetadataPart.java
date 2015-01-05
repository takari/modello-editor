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

import io.takari.modello.editor.impl.model.MClass;
import io.takari.modello.editor.impl.model.plugin.MetadataPluginDetailPart;
import io.takari.modello.editor.impl.model.plugin.xml.MXmlClassMetadata;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

public class XmlClassMetadataPart extends MetadataPluginDetailPart {
    
    private Text xmlTagName;
    private Button xmlStandaloneRead;

    public XmlClassMetadataPart(IDocumentEditor editor) {
        super(editor);
    }
    
    @Override
    public Class<? extends IModel> getDetailClass() {
        return MClass.class;
    }
    
    @Override
    public Class<? extends IModel> getMetadataDetailClass() {
        return MXmlClassMetadata.class;
    }
    
    @Override
    protected void createClient(Composite parent) {
        parent.setLayout(new GridLayout(2, false));
        
        Label lblXmlTagName = getManagedForm().getToolkit().createLabel(parent, "Tag Name", SWT.NONE);
        lblXmlTagName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        xmlTagName = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        xmlTagName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        xmlStandaloneRead = new Button(parent, SWT.CHECK);
        xmlStandaloneRead.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        getManagedForm().getToolkit().adapt(xmlStandaloneRead, true, true);
        xmlStandaloneRead.setText("Standalone Read");
    }
    
    @Override
    protected DataBindingContext createBindings() {
        return initDataBindings();
    }
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        IObservableValue observeTextXmlTagNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(xmlTagName);
        IObservableValue currentClassXmlTagNameObserveDetailValue = BeanProperties.value(MXmlClassMetadata.class, "xmlTagName", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextXmlTagNameObserveWidget, currentClassXmlTagNameObserveDetailValue, null, null);
        //
        IObservableValue observeSelectionXmlStandaloneReadObserveWidget = WidgetProperties.selection().observe(xmlStandaloneRead);
        IObservableValue currentClassXmlStandaloneReadObserveDetailValue = BeanProperties.value(MXmlClassMetadata.class, "xmlStandaloneRead", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionXmlStandaloneReadObserveWidget, currentClassXmlStandaloneReadObserveDetailValue, null, null);
        //
        return bindingContext;
    }
}
