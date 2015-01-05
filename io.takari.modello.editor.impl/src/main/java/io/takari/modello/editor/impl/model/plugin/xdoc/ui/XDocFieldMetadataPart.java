package io.takari.modello.editor.impl.model.plugin.xdoc.ui;

import io.takari.modello.editor.impl.model.MField;
import io.takari.modello.editor.impl.model.plugin.MetadataPluginDetailPart;
import io.takari.modello.editor.impl.model.plugin.xdoc.MXDocFieldMetadata;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class XDocFieldMetadataPart extends MetadataPluginDetailPart {
    
    private Combo xdocSeparator;
    
    public XDocFieldMetadataPart(IDocumentEditor editor) {
        super(editor);
    }
    
    @Override
    public Class<? extends IModel> getDetailClass() {
        return MField.class;
    }
    
    @Override
    public Class<? extends IModel> getMetadataDetailClass() {
        return MXDocFieldMetadata.class;
    }
    
    @Override
    protected void createClient(Composite parent) {
        parent.setLayout(new GridLayout(2, false));
        
        Label lblSeparator = getManagedForm().getToolkit().createLabel(parent, "Separator", SWT.RIGHT);
        GridData gd_lblSeparator = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_lblSeparator.widthHint = 85;
        lblSeparator.setLayoutData(gd_lblSeparator);
        
        xdocSeparator = new Combo(parent, SWT.READ_ONLY);
        xdocSeparator.setItems(new String[] {"none", "blank"});
        xdocSeparator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    }
    
    @Override
    protected DataBindingContext createBindings() {
        return initDataBindings();
    }
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        IObservableValue observeTextXdocSeparatorObserveWidget = WidgetProperties.text().observe(xdocSeparator);
        IObservableValue getCurrentItemXdocSeparatorObserveDetailValue = BeanProperties.value(MXDocFieldMetadata.class, "xdocSeparator", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextXdocSeparatorObserveWidget, getCurrentItemXdocSeparatorObserveDetailValue, null, null);
        //
        return bindingContext;
    }
}
