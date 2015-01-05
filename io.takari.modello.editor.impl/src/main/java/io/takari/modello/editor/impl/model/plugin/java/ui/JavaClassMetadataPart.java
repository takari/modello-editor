package io.takari.modello.editor.impl.model.plugin.java.ui;

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

import io.takari.modello.editor.impl.model.MClass;
import io.takari.modello.editor.impl.model.plugin.MetadataPluginDetailPart;
import io.takari.modello.editor.impl.model.plugin.java.MJavaClassMetadata;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

public class JavaClassMetadataPart extends MetadataPluginDetailPart {
    
    private Button javaEnabled;
    private Button javaAbstract;
    private Combo javaCloneMode;
    private Text javaCloneHook;
    private Button javaGenerateToString;
    private Button javaGenerateBuilder;
    private Button javaGenerateStaticCreators;

    public JavaClassMetadataPart(IDocumentEditor editor) {
        super(editor);
    }
    
    @Override
    public Class<? extends IModel> getDetailClass() {
        return MClass.class;
    }
    
    @Override
    public Class<? extends IModel> getMetadataDetailClass() {
        return MJavaClassMetadata.class;
    }
    
    @Override
    protected void createClient(Composite parent) {
        parent.setLayout(new GridLayout(2, false));

        javaEnabled = new Button(parent, SWT.CHECK);
        getManagedForm().getToolkit().adapt(javaEnabled, true, true);
        javaEnabled.setText("Enabled");
        
        javaAbstract = new Button(parent, SWT.CHECK);
        getManagedForm().getToolkit().adapt(javaAbstract, true, true);
        javaAbstract.setText("Abstract");
        
        Label lblCloneMode = getManagedForm().getToolkit().createLabel(parent, "Clone Mode", SWT.NONE);
        lblCloneMode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        javaCloneMode = new Combo(parent, SWT.READ_ONLY);
        javaCloneMode.setItems(new String[] {"", "none", "shallow", "deep"});
        javaCloneMode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        getManagedForm().getToolkit().adapt(javaCloneMode);
        getManagedForm().getToolkit().paintBordersFor(javaCloneMode);
        
        Label lblCloneHook = getManagedForm().getToolkit().createLabel(parent, "Clone Hook", SWT.NONE);
        lblCloneHook.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        javaCloneHook = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        javaCloneHook.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblGenerate = getManagedForm().getToolkit().createLabel(parent, "Generate", SWT.NONE);
        lblGenerate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        Composite javaCheckBoxes = getManagedForm().getToolkit().createComposite(parent, SWT.NONE);
        GridLayout gl_composite = new GridLayout(3, false);
        gl_composite.marginHeight = 0;
        gl_composite.marginWidth = 0;
        javaCheckBoxes.setLayout(gl_composite);
        javaCheckBoxes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        getManagedForm().getToolkit().paintBordersFor(javaCheckBoxes);
        
        javaGenerateToString = new Button(javaCheckBoxes, SWT.CHECK);
        getManagedForm().getToolkit().adapt(javaGenerateToString, true, true);
        javaGenerateToString.setText("toString()");
        
        javaGenerateBuilder = new Button(javaCheckBoxes, SWT.CHECK);
        getManagedForm().getToolkit().adapt(javaGenerateBuilder, true, true);
        javaGenerateBuilder.setText("Builder");
        
        javaGenerateStaticCreators = new Button(javaCheckBoxes, SWT.CHECK);
        getManagedForm().getToolkit().adapt(javaGenerateStaticCreators, true, true);
        javaGenerateStaticCreators.setText("Static Creators");

    }
    
    @Override
    protected DataBindingContext createBindings() {
        return initDataBindings();
    }
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        IObservableValue observeSelectionJavaEnabledObserveWidget = WidgetProperties.selection().observe(javaEnabled);
        IObservableValue currentClassJavaEnabledObserveDetailValue = BeanProperties.value(MJavaClassMetadata.class, "javaEnabled", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionJavaEnabledObserveWidget, currentClassJavaEnabledObserveDetailValue, null, null);
        //
        IObservableValue observeSelectionJavaAbstractObserveWidget = WidgetProperties.selection().observe(javaAbstract);
        IObservableValue currentClassJavaAbstractObserveDetailValue = BeanProperties.value(MJavaClassMetadata.class, "javaAbstract", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionJavaAbstractObserveWidget, currentClassJavaAbstractObserveDetailValue, null, null);
        //
        IObservableValue observeTextJavaCloneModeObserveWidget = WidgetProperties.text().observe(javaCloneMode);
        IObservableValue currentClassAnnotationsObserveDetailValue = BeanProperties.value(MJavaClassMetadata.class, "javaCloneMode", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextJavaCloneModeObserveWidget, currentClassAnnotationsObserveDetailValue, null, null);
        //
        IObservableValue observeTextJavaCloneHookObserveWidget = WidgetProperties.text(SWT.Modify).observe(javaCloneHook);
        IObservableValue currentClassJavaCloneHookObserveDetailValue = BeanProperties.value(MJavaClassMetadata.class, "javaCloneHook", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextJavaCloneHookObserveWidget, currentClassJavaCloneHookObserveDetailValue, null, null);
        //
        IObservableValue observeSelectionJavaGenerateToStringObserveWidget = WidgetProperties.selection().observe(javaGenerateToString);
        IObservableValue currentClassJavaGenerateToStringObserveDetailValue = BeanProperties.value(MJavaClassMetadata.class, "javaGenerateToString", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionJavaGenerateToStringObserveWidget, currentClassJavaGenerateToStringObserveDetailValue, null, null);
        //
        IObservableValue observeSelectionJavaGenerateBuilderObserveWidget = WidgetProperties.selection().observe(javaGenerateBuilder);
        IObservableValue currentClassJavaGenerateBuilderObserveDetailValue = BeanProperties.value(MJavaClassMetadata.class, "javaGenerateBuilder", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionJavaGenerateBuilderObserveWidget, currentClassJavaGenerateBuilderObserveDetailValue, null, null);
        //
        IObservableValue observeSelectionJavaGenerateStaticCreatorsObserveWidget = WidgetProperties.selection().observe(javaGenerateStaticCreators);
        IObservableValue currentClassJavaGenerateStaticCreatorsObserveDetailValue = BeanProperties.value(MJavaClassMetadata.class, "javaGenerateStaticCreators", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionJavaGenerateStaticCreatorsObserveWidget, currentClassJavaGenerateStaticCreatorsObserveDetailValue, null, null);

        return bindingContext;
    }
}
