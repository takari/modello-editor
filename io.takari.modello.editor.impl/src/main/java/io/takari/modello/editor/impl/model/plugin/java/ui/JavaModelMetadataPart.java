package io.takari.modello.editor.impl.model.plugin.java.ui;

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
import io.takari.modello.editor.impl.model.plugin.java.MJavaModelMetadata;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

public class JavaModelMetadataPart extends MetadataPluginDetailPart {
    
    private Text modelDetailsJavaPackage;
    private Text modelDetailsJavaListImpl;
    private Text modelDetailsJavaSetImpl;
    private Text modelDetailsJavaMapImpl;
    private Text modelDetailsJavaPropertiesImpl;
    private Button modelDetailsJavaSuppressAllWarnings;
    
    public JavaModelMetadataPart(IDocumentEditor editor) {
        super(editor);
    }
    
    @Override
    public Class<? extends IModel> getDetailClass() {
        return MModelDetails.class;
    }
    
    @Override
    public Class<? extends IModel> getMetadataDetailClass() {
        return MJavaModelMetadata.class;
    }
    
    @Override
    protected void createClient(Composite parent) {
        parent.setLayout(new GridLayout(2, false));

        Label lblModelDetailsJavaPackage = getManagedForm().getToolkit().createLabel(parent, "Default package", SWT.NONE);
        lblModelDetailsJavaPackage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        modelDetailsJavaPackage = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        modelDetailsJavaPackage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        
        Label lblModelDetailsJavaListImpl = getManagedForm().getToolkit().createLabel(parent, "List impl", SWT.NONE);
        lblModelDetailsJavaListImpl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        modelDetailsJavaListImpl = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        modelDetailsJavaListImpl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblModelDetailsJavaSetImpl = getManagedForm().getToolkit().createLabel(parent, "Set impl", SWT.NONE);
        lblModelDetailsJavaSetImpl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        modelDetailsJavaSetImpl = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        modelDetailsJavaSetImpl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblModelDetailsJavaMapImpl = getManagedForm().getToolkit().createLabel(parent, "Map impl", SWT.NONE);
        lblModelDetailsJavaMapImpl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        modelDetailsJavaMapImpl = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        modelDetailsJavaMapImpl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblModelDetailsJavaPropertiesImpl = getManagedForm().getToolkit().createLabel(parent, "Properties impl", SWT.NONE);
        lblModelDetailsJavaPropertiesImpl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        modelDetailsJavaPropertiesImpl = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        modelDetailsJavaPropertiesImpl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        modelDetailsJavaSuppressAllWarnings = new Button(parent, SWT.CHECK);
        modelDetailsJavaSuppressAllWarnings.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        getManagedForm().getToolkit().adapt(modelDetailsJavaSuppressAllWarnings, true, true);
        modelDetailsJavaSuppressAllWarnings.setText("Suppress All Warnings");

    }
    
    @Override
    protected DataBindingContext createBindings() {
        return initDataBindings();
    }
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        IObservableValue observeTextModelDetailsJavaPackageObserveWidget = WidgetProperties.text(SWT.Modify).observe(modelDetailsJavaPackage);
        IObservableValue getCurrentItemDetailsdefaultsdefaultPackageObserveDetailValue = BeanProperties.value(MJavaModelMetadata.class, "details.defaults.defaultPackage", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelDetailsJavaPackageObserveWidget, getCurrentItemDetailsdefaultsdefaultPackageObserveDetailValue, null, null);
        //
        IObservableValue observeTextModelDetailsJavaListImplObserveWidget = WidgetProperties.text(SWT.Modify).observe(modelDetailsJavaListImpl);
        IObservableValue modelDetailsdefaultslistImplObserveDetailValue = BeanProperties.value(MJavaModelMetadata.class, "details.defaults.listImpl", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelDetailsJavaListImplObserveWidget, modelDetailsdefaultslistImplObserveDetailValue, null, null);
        //
        IObservableValue observeTextModelDetailsJavaSetImplObserveWidget = WidgetProperties.text(SWT.Modify).observe(modelDetailsJavaSetImpl);
        IObservableValue modelDetailsdefaultssetImplObserveDetailValue = BeanProperties.value(MJavaModelMetadata.class, "details.defaults.setImpl", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelDetailsJavaSetImplObserveWidget, modelDetailsdefaultssetImplObserveDetailValue, null, null);
        //
        IObservableValue observeTextModelDetailsJavaMapImplObserveWidget = WidgetProperties.text(SWT.Modify).observe(modelDetailsJavaMapImpl);
        IObservableValue modelDetailsdefaultsmapImplObserveDetailValue = BeanProperties.value(MJavaModelMetadata.class, "details.defaults.mapImpl", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelDetailsJavaMapImplObserveWidget, modelDetailsdefaultsmapImplObserveDetailValue, null, null);
        //
        IObservableValue observeTextModelDetailsJavaPropertiesImplObserveWidget = WidgetProperties.text(SWT.Modify).observe(modelDetailsJavaPropertiesImpl);
        IObservableValue modelDetailsdefaultspropertiesImplObserveDetailValue = BeanProperties.value(MJavaModelMetadata.class, "details.defaults.propertiesImpl", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelDetailsJavaPropertiesImplObserveWidget, modelDetailsdefaultspropertiesImplObserveDetailValue, null, null);
        //
        IObservableValue observeSelectionModelDetailsJavaSuppressAllWarningsObserveWidget = WidgetProperties.selection().observe(modelDetailsJavaSuppressAllWarnings);
        IObservableValue modelDetailsjavaSuppressAllWarningsObserveDetailValue = BeanProperties.value(MJavaModelMetadata.class, "javaSuppressAllWarnings", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionModelDetailsJavaSuppressAllWarningsObserveWidget, modelDetailsjavaSuppressAllWarningsObserveDetailValue, null, null);
        //
        return bindingContext;
    }
}
