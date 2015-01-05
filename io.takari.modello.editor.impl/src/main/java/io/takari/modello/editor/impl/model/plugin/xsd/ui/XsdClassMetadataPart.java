package io.takari.modello.editor.impl.model.plugin.xsd.ui;

import io.takari.modello.editor.impl.model.MClass;
import io.takari.modello.editor.impl.model.plugin.MetadataPluginDetailPart;
import io.takari.modello.editor.impl.model.plugin.xsd.MXsdClassMetadata;
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

public class XsdClassMetadataPart extends MetadataPluginDetailPart {
    
    private Combo xsdCompositor;

    public XsdClassMetadataPart(IDocumentEditor editor) {
        super(editor);
    }
    
    @Override
    public Class<? extends IModel> getDetailClass() {
        return MClass.class;
    }
    
    @Override
    public Class<? extends IModel> getMetadataDetailClass() {
        return MXsdClassMetadata.class;
    }
    
    @Override
    protected void createClient(Composite parent) {
        parent.setLayout(new GridLayout(2, false));
        
        Label lblCompositor = getManagedForm().getToolkit().createLabel(parent, "Compositor", SWT.NONE);
        lblCompositor.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblCompositor.setBounds(0, 0, 55, 15);
        
        xsdCompositor = new Combo(parent, SWT.READ_ONLY);
        xsdCompositor.setItems(new String[] {"all", "sequence"});
        xsdCompositor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        getManagedForm().getToolkit().adapt(xsdCompositor);
        getManagedForm().getToolkit().paintBordersFor(xsdCompositor);

    }
    
    @Override
    protected DataBindingContext createBindings() {
        return initDataBindings();
    }
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        IObservableValue observeTextXsdCompositorObserveWidget = WidgetProperties.text().observe(xsdCompositor);
        IObservableValue getCurrentItemXsdCompositorObserveDetailValue = BeanProperties.value(MXsdClassMetadata.class, "xsdCompositor", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextXsdCompositorObserveWidget, getCurrentItemXsdCompositorObserveDetailValue, null, null);
        //
        return bindingContext;
    }
}
