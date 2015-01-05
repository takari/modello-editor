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

import io.takari.modello.editor.impl.model.MField;
import io.takari.modello.editor.impl.model.plugin.MetadataPluginDetailPart;
import io.takari.modello.editor.impl.model.plugin.java.MJavaFieldMetadata;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

public class JavaFieldMetadataPart extends MetadataPluginDetailPart {
    
    private Button javaGetter;
    private Button javaSetter;
    private Button javaAdder;
    private Button javaBidi;
    private Combo javaInit;
    private Combo javaClone;
    private Text javaUseInterface;

    public JavaFieldMetadataPart(IDocumentEditor editor) {
        super(editor);
    }
    
    @Override
    public Class<? extends IModel> getDetailClass() {
        return MField.class;
    }
    
    @Override
    public Class<? extends IModel> getMetadataDetailClass() {
        return MJavaFieldMetadata.class;
    }
    
    @Override
    protected void createClient(Composite parent) {
        parent.setLayout(new GridLayout(4, false));
        
        Label lblField = getManagedForm().getToolkit().createLabel(parent, "Field", SWT.RIGHT);
        GridData gd_lblField = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_lblField.widthHint = 85;
        lblField.setLayoutData(gd_lblField);
        getManagedForm().getToolkit().adapt(lblField, true, true);
        
        Composite composite = getManagedForm().getToolkit().createComposite(parent, SWT.NONE);
        GridLayout gl_composite = new GridLayout(2, false);
        gl_composite.marginWidth = 0;
        gl_composite.marginHeight = 0;
        composite.setLayout(gl_composite);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        getManagedForm().getToolkit().paintBordersFor(composite);
        
        javaGetter = new Button(composite, SWT.CHECK);
        getManagedForm().getToolkit().adapt(javaGetter, true, true);
        javaGetter.setText("Getter");
        
        javaSetter = new Button(composite, SWT.CHECK);
        getManagedForm().getToolkit().adapt(javaSetter, true, true);
        javaSetter.setText("Setter");
        
        Label sep = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        sep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
        getManagedForm().getToolkit().adapt(sep, true, true);
        
        Label lblAssociation_1 = getManagedForm().getToolkit().createLabel(parent, "Association", SWT.NONE);
        lblAssociation_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        getManagedForm().getToolkit().adapt(lblAssociation_1, true, true);
        
        Composite composite_1 = getManagedForm().getToolkit().createComposite(parent, SWT.NONE);
        composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        getManagedForm().getToolkit().paintBordersFor(composite_1);
        GridLayout gl_composite_1 = new GridLayout(2, false);
        gl_composite_1.marginHeight = 0;
        gl_composite_1.marginWidth = 0;
        composite_1.setLayout(gl_composite_1);
        
        javaAdder = new Button(composite_1, SWT.CHECK);
        getManagedForm().getToolkit().adapt(javaAdder, true, true);
        javaAdder.setText("Adder");
        
        javaBidi = new Button(composite_1, SWT.CHECK);
        getManagedForm().getToolkit().adapt(javaBidi, true, true);
        javaBidi.setText("Bi-directional");
        
        Label lblUseInterface = getManagedForm().getToolkit().createLabel(parent, "Use interface", SWT.NONE);
        lblUseInterface.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        getManagedForm().getToolkit().adapt(lblUseInterface, true, true);
        
        javaUseInterface = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        javaUseInterface.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        
        Label lblInit = getManagedForm().getToolkit().createLabel(parent, "Init", SWT.NONE);
        lblInit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        javaInit = new Combo(parent, SWT.READ_ONLY);
        javaInit.setItems(new String[] {"lazy", "constructor", "field"});
        GridData gd_javaInit = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_javaInit.widthHint = 50;
        javaInit.setLayoutData(gd_javaInit);
        getManagedForm().getToolkit().adapt(javaInit);
        getManagedForm().getToolkit().paintBordersFor(javaInit);
        
        Label lblClone = getManagedForm().getToolkit().createLabel(parent, "Clone", SWT.NONE);
        lblClone.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        javaClone = new Combo(parent, SWT.READ_ONLY);
        javaClone.setItems(new String[] {"", "shallow", "deep"});
        GridData gd_javaClone = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_javaClone.widthHint = 50;
        javaClone.setLayoutData(gd_javaClone);
        getManagedForm().getToolkit().adapt(javaClone);
        getManagedForm().getToolkit().paintBordersFor(javaClone);


    }
    
    @Override
    protected DataBindingContext createBindings() {
        return initDataBindings();
    }
    
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        IObservableValue observeSelectionJavaGetterObserveWidget = WidgetProperties.selection().observe(javaGetter);
        IObservableValue getCurrentItemJavaGetterObserveDetailValue = BeanProperties.value(MJavaFieldMetadata.class, "javaGetter", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionJavaGetterObserveWidget, getCurrentItemJavaGetterObserveDetailValue, null, null);
        //
        IObservableValue observeSelectionJavaSetterObserveWidget = WidgetProperties.selection().observe(javaSetter);
        IObservableValue getCurrentItemJavaSetterObserveDetailValue = BeanProperties.value(MJavaFieldMetadata.class, "javaSetter", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionJavaSetterObserveWidget, getCurrentItemJavaSetterObserveDetailValue, null, null);
        //
        IObservableValue observeSelectionJavaAdderObserveWidget = WidgetProperties.selection().observe(javaAdder);
        IObservableValue getCurrentItemAssociationjavaAdderObserveDetailValue = BeanProperties.value(MJavaFieldMetadata.class, "javaAdder", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionJavaAdderObserveWidget, getCurrentItemAssociationjavaAdderObserveDetailValue, null, null);
        //
        IObservableValue observeSelectionJavaBidiObserveWidget = WidgetProperties.selection().observe(javaBidi);
        IObservableValue getCurrentItemAssociationjavaBidiObserveDetailValue = BeanProperties.value(MJavaFieldMetadata.class, "javaBidi", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionJavaBidiObserveWidget, getCurrentItemAssociationjavaBidiObserveDetailValue, null, null);
        //
        IObservableValue observeTextJavaUseInterfaceObserveWidget = WidgetProperties.text(SWT.Modify).observe(javaUseInterface);
        IObservableValue getCurrentItemAssociationjavaUseInterfaceObserveDetailValue = BeanProperties.value(MJavaFieldMetadata.class, "javaUseInterface", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextJavaUseInterfaceObserveWidget, getCurrentItemAssociationjavaUseInterfaceObserveDetailValue, null, null);
        //
        IObservableValue observeTextJavaInitObserveWidget = WidgetProperties.text().observe(javaInit);
        IObservableValue getCurrentItemAssociationjavaInitObserveDetailValue = BeanProperties.value(MJavaFieldMetadata.class, "javaInit", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextJavaInitObserveWidget, getCurrentItemAssociationjavaInitObserveDetailValue, null, null);
        //
        IObservableValue observeTextJavaCloneObserveWidget = WidgetProperties.text().observe(javaClone);
        IObservableValue getCurrentItemAssociationjavaCloneObserveDetailValue = BeanProperties.value(MJavaFieldMetadata.class, "javaClone", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextJavaCloneObserveWidget, getCurrentItemAssociationjavaCloneObserveDetailValue, null, null);
        //
        return bindingContext;
    }
}
