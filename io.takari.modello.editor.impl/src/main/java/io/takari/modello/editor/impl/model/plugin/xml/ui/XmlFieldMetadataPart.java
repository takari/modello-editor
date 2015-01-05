package io.takari.modello.editor.impl.model.plugin.xml.ui;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import io.takari.modello.editor.impl.model.MField;
import io.takari.modello.editor.impl.model.plugin.MetadataPluginDetailPart;
import io.takari.modello.editor.impl.model.plugin.xml.MXmlFieldMetadata;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

public class XmlFieldMetadataPart extends MetadataPluginDetailPart {
    
    private Text xmlTagName;
    private Text xmlAssociationTagName;
    private Text xmlReference;
    private Button xmlAttribute;
    private Button xmlContent;
    private Button xmlTrim;
    private Button xmlTransient;
    private Combo xmlFormat;
    private Text xmlInsertParents;
    private Combo xmlItemsStyle;
    private Combo xmlMapStyle;
    
    public XmlFieldMetadataPart(IDocumentEditor editor) {
        super(editor);
    }
    
    @Override
    public Class<? extends IModel> getDetailClass() {
        return MField.class;
    }
    
    @Override
    public Class<? extends IModel> getMetadataDetailClass() {
        return MXmlFieldMetadata.class;
    }
    
    @Override
    protected void createClient(Composite parent) {
        parent.setLayout(new GridLayout(4, false));
        
        Label lblXmlField = getManagedForm().getToolkit().createLabel(parent, "Field", SWT.RIGHT);
        GridData gd_lblXmlField = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_lblXmlField.widthHint = 85;
        lblXmlField.setLayoutData(gd_lblXmlField);
        
        Composite composite_2 = getManagedForm().getToolkit().createComposite(parent, SWT.NONE);
        GridLayout gl_composite_2 = new GridLayout(4, false);
        gl_composite_2.marginWidth = 0;
        gl_composite_2.marginHeight = 0;
        composite_2.setLayout(gl_composite_2);
        composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        getManagedForm().getToolkit().paintBordersFor(composite_2);
        
        xmlAttribute = new Button(composite_2, SWT.CHECK);
        getManagedForm().getToolkit().adapt(xmlAttribute, true, true);
        xmlAttribute.setText("Attribute");
        
        xmlContent = new Button(composite_2, SWT.CHECK);
        getManagedForm().getToolkit().adapt(xmlContent, true, true);
        xmlContent.setText("Content");
        
        xmlTrim = new Button(composite_2, SWT.CHECK);
        getManagedForm().getToolkit().adapt(xmlTrim, true, true);
        xmlTrim.setText("Trim");
        
        xmlTransient = new Button(composite_2, SWT.CHECK);
        getManagedForm().getToolkit().adapt(xmlTransient, true, true);
        xmlTransient.setText("Transient");
        
        Label lblTag = getManagedForm().getToolkit().createLabel(parent, "Tag", SWT.NONE);
        lblTag.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        xmlTagName = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        GridData gd_xmlTagName = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_xmlTagName.widthHint = 50;
        xmlTagName.setLayoutData(gd_xmlTagName);
        
        Label lblFormat = getManagedForm().getToolkit().createLabel(parent, "Format", SWT.NONE);
        lblFormat.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        xmlFormat = new Combo(parent, SWT.NONE);
        xmlFormat.setItems(new String[] {"", "yyyy-MM-dd'T'HH:mm:ss.SSS", "long"});
        GridData gd_xmlFormat = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_xmlFormat.widthHint = 50;
        xmlFormat.setLayoutData(gd_xmlFormat);
        getManagedForm().getToolkit().adapt(xmlFormat);
        getManagedForm().getToolkit().paintBordersFor(xmlFormat);
        
        Label lblXmlParents = getManagedForm().getToolkit().createLabel(parent, "Insert parents", SWT.NONE);
        lblXmlParents.setToolTipText("Insert parent fields up to");
        lblXmlParents.setAlignment(SWT.CENTER);
        lblXmlParents.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        xmlInsertParents = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        xmlInsertParents.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        
        Label sep2 = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        sep2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
        getManagedForm().getToolkit().adapt(sep2, true, true);
        
        Label lblAssociationTag = getManagedForm().getToolkit().createLabel(parent, "Association tag", SWT.NONE);
        lblAssociationTag.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        xmlAssociationTagName = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        GridData gd_xmlAssociationTagName = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_xmlAssociationTagName.widthHint = 50;
        xmlAssociationTagName.setLayoutData(gd_xmlAssociationTagName);
        
        Label lblReference = getManagedForm().getToolkit().createLabel(parent, "Reference", SWT.NONE);
        lblReference.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        xmlReference = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        GridData gd_xmlReference = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_xmlReference.widthHint = 50;
        xmlReference.setLayoutData(gd_xmlReference);
        
        Label lblItems = getManagedForm().getToolkit().createLabel(parent, "Items", SWT.NONE);
        lblItems.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        xmlItemsStyle = new Combo(parent, SWT.READ_ONLY);
        xmlItemsStyle.setItems(new String[] {"wrapped", "flat"});
        GridData gd_xmlItemsStyle = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_xmlItemsStyle.widthHint = 50;
        xmlItemsStyle.setLayoutData(gd_xmlItemsStyle);
        getManagedForm().getToolkit().adapt(xmlItemsStyle);
        getManagedForm().getToolkit().paintBordersFor(xmlItemsStyle);
        
        Label lblMap = getManagedForm().getToolkit().createLabel(parent, "Map", SWT.NONE);
        lblMap.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        xmlMapStyle = new Combo(parent, SWT.READ_ONLY);
        xmlMapStyle.setItems(new String[] {"inline", "explode"});
        GridData gd_xmlMapStyle = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_xmlMapStyle.widthHint = 50;
        xmlMapStyle.setLayoutData(gd_xmlMapStyle);
        getManagedForm().getToolkit().adapt(xmlMapStyle);
        getManagedForm().getToolkit().paintBordersFor(xmlMapStyle);
    }
    
    @Override
    protected DataBindingContext createBindings() {
        return initDataBindings();
    }
    
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        IObservableValue observeSelectionXmlAttributeObserveWidget = WidgetProperties.selection().observe(xmlAttribute);
        IObservableValue getCurrentItemXmlAttributeObserveDetailValue = BeanProperties.value(MXmlFieldMetadata.class, "xmlAttribute", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionXmlAttributeObserveWidget, getCurrentItemXmlAttributeObserveDetailValue, null, null);
        //
        IObservableValue observeSelectionXmlContentObserveWidget = WidgetProperties.selection().observe(xmlContent);
        IObservableValue getCurrentItemXmlContentObserveDetailValue = BeanProperties.value(MXmlFieldMetadata.class, "xmlContent", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionXmlContentObserveWidget, getCurrentItemXmlContentObserveDetailValue, null, null);
        //
        IObservableValue observeSelectionXmlTrimObserveWidget = WidgetProperties.selection().observe(xmlTrim);
        IObservableValue getCurrentItemXmlTrimObserveDetailValue = BeanProperties.value(MXmlFieldMetadata.class, "xmlTrim", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionXmlTrimObserveWidget, getCurrentItemXmlTrimObserveDetailValue, null, null);
        //
        IObservableValue observeSelectionXmlTransientObserveWidget = WidgetProperties.selection().observe(xmlTransient);
        IObservableValue getCurrentItemXmlTransientObserveDetailValue = BeanProperties.value(MXmlFieldMetadata.class, "xmlTransient", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionXmlTransientObserveWidget, getCurrentItemXmlTransientObserveDetailValue, null, null);
        //
        IObservableValue observeTextXmlTagNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(xmlTagName);
        IObservableValue getCurrentItemXmlTagNameObserveDetailValue = BeanProperties.value(MXmlFieldMetadata.class, "xmlTagName", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextXmlTagNameObserveWidget, getCurrentItemXmlTagNameObserveDetailValue, null, null);
        //
        IObservableValue observeTextXmlFormatObserveWidget = WidgetProperties.text().observe(xmlFormat);
        IObservableValue getCurrentItemXmlFormatObserveDetailValue = BeanProperties.value(MXmlFieldMetadata.class, "xmlFormat", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextXmlFormatObserveWidget, getCurrentItemXmlFormatObserveDetailValue, null, null);
        //
        IObservableValue observeTextXmlInsertParentsObserveWidget = WidgetProperties.text(SWT.Modify).observe(xmlInsertParents);
        IObservableValue getCurrentItemXmlInsertParentFieldsUpToObserveDetailValue = BeanProperties.value(MXmlFieldMetadata.class, "xmlInsertParentFieldsUpTo", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextXmlInsertParentsObserveWidget, getCurrentItemXmlInsertParentFieldsUpToObserveDetailValue, null, null);
        //
        IObservableValue observeTextXmlAssociationTagNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(xmlAssociationTagName);
        IObservableValue getCurrentItemAssociationxmlTagNameObserveDetailValue = BeanProperties.value(MXmlFieldMetadata.class, "xmlAssociationTagName", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextXmlAssociationTagNameObserveWidget, getCurrentItemAssociationxmlTagNameObserveDetailValue, null, null);
        //
        IObservableValue observeTextXmlReferenceObserveWidget = WidgetProperties.text(SWT.Modify).observe(xmlReference);
        IObservableValue getCurrentItemAssociationxmlReferenceObserveDetailValue = BeanProperties.value(MXmlFieldMetadata.class, "xmlReference", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextXmlReferenceObserveWidget, getCurrentItemAssociationxmlReferenceObserveDetailValue, null, null);
        //
        IObservableValue observeTextXmlItemsStyleObserveWidget = WidgetProperties.text().observe(xmlItemsStyle);
        IObservableValue getCurrentItemAssociationxmlItemsStyleObserveDetailValue = BeanProperties.value(MXmlFieldMetadata.class, "xmlItemsStyle", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextXmlItemsStyleObserveWidget, getCurrentItemAssociationxmlItemsStyleObserveDetailValue, null, null);
        //
        IObservableValue observeTextXmlMapStyleObserveWidget = WidgetProperties.text().observe(xmlMapStyle);
        IObservableValue getCurrentItemAssociationxmlMapStyleObserveDetailValue = BeanProperties.value(MXmlFieldMetadata.class, "xmlMapStyle", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextXmlMapStyleObserveWidget, getCurrentItemAssociationxmlMapStyleObserveDetailValue, null, null);
        //
        return bindingContext;
    }
}
