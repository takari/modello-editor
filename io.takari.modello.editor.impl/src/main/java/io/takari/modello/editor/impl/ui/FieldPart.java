package io.takari.modello.editor.impl.ui;

import java.util.ArrayList;
import java.util.List;

import io.takari.modello.editor.impl.model.MAnnotation;
import io.takari.modello.editor.impl.model.MClass;
import io.takari.modello.editor.impl.model.MClasses;
import io.takari.modello.editor.impl.model.MField;
import io.takari.modello.editor.impl.model.MModel;
import io.takari.modello.editor.impl.model.plugin.IMetadataUI;
import io.takari.modello.editor.impl.model.plugin.MetadataPlugins;
import io.takari.modello.editor.impl.model.plugin.MetadataPlugins.MetadataPluginRef;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.mapping.model.IModelExtension;
import io.takari.modello.editor.toolkit.actions.BeanListActions;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;
import io.takari.modello.editor.toolkit.ui.AbstractDetailPart;
import io.takari.modello.editor.toolkit.util.ModelListDragSupport;
import io.takari.modello.editor.toolkit.util.TableSingleColumnLabelProvider;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.CellEditorProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;

public class FieldPart extends AbstractDetailPart {
    
    private final List<IMetadataUI> uis;
    
    private Text name;
    private Text defaultValue;
    private Text alias;
    private Text version;
    private Button btnAnnotationAdd;
    private Button btnAnnotationRemove;
    private Table annotations;
    private TableViewer annotationsViewer;
    private TableViewerColumn annotationsLabelViewer;
    private StyledText description;
    private StyledText comment;
    private Button required;
    private Button identifier;
    private Combo type;
    private Combo associationType;
    private ComboViewer associationTypeViewer;
    private Combo associationMultiplicity;

    public FieldPart(IDocumentEditor editor) {
        super(editor);
        uis = new ArrayList<IMetadataUI>();
    }
    
    @Override
    public Class<? extends IModel> getDetailClass() {
        return MField.class;
    }
    
    public MClasses getClassesBean() {
        return ((MModel) getModel()).getClasses();
    }
    
    @Override
    protected void createClient(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        
        Section sctnClassDetails = getManagedForm().getToolkit().createSection(parent, Section.TITLE_BAR);
        sctnClassDetails.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        getManagedForm().getToolkit().paintBordersFor(sctnClassDetails);
        sctnClassDetails.setText("Field Details");
        sctnClassDetails.setExpanded(true);
        
        Composite fieldContainer = getManagedForm().getToolkit().createComposite(sctnClassDetails, SWT.NONE);
        getManagedForm().getToolkit().paintBordersFor(fieldContainer);
        sctnClassDetails.setClient(fieldContainer);
        fieldContainer.setLayout(new GridLayout(3, false));
        
        new Label(fieldContainer, SWT.NONE);
        
        Composite buttonComposite = getManagedForm().getToolkit().createComposite(fieldContainer, SWT.NONE);
        GridLayout gl_buttonComposite = new GridLayout(2, false);
        gl_buttonComposite.marginWidth = 0;
        gl_buttonComposite.marginHeight = 0;
        buttonComposite.setLayout(gl_buttonComposite);
        buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
        getManagedForm().getToolkit().paintBordersFor(buttonComposite);
        
        required = new Button(buttonComposite, SWT.CHECK);
        required.setBounds(0, 0, 93, 16);
        getManagedForm().getToolkit().adapt(required, true, true);
        required.setText("Required");
        
        identifier = new Button(buttonComposite, SWT.CHECK);
        getManagedForm().getToolkit().adapt(identifier, true, true);
        identifier.setText("Identifier");
        
        Label lblName = getManagedForm().getToolkit().createLabel(fieldContainer, "Name", SWT.RIGHT);
        GridData gd_lblName = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_lblName.widthHint = 85;
        lblName.setLayoutData(gd_lblName);
        
        name = getManagedForm().getToolkit().createText(fieldContainer, "", SWT.NONE);
        name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        Label lblAlias = getManagedForm().getToolkit().createLabel(fieldContainer, "Alias", SWT.NONE);
        lblAlias.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        alias = getManagedForm().getToolkit().createText(fieldContainer, "", SWT.NONE);
        alias.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        Label lblVersion = getManagedForm().getToolkit().createLabel(fieldContainer, "Version", SWT.NONE);
        lblVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        version = getManagedForm().getToolkit().createText(fieldContainer, "", SWT.NONE);
        version.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        Label lblType = getManagedForm().getToolkit().createLabel(fieldContainer, "Type", SWT.NONE);
        lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        type = new Combo(fieldContainer, SWT.NONE);
        type.setItems(new String[] {"", "String", "boolean", "int"});
        type.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        getManagedForm().getToolkit().adapt(type);
        getManagedForm().getToolkit().paintBordersFor(type);
        
        Label lblDefaultValue = getManagedForm().getToolkit().createLabel(fieldContainer, "Default Value", SWT.NONE);
        lblDefaultValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        defaultValue = getManagedForm().getToolkit().createText(fieldContainer, "", SWT.NONE);
        defaultValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        Label lblAssociation = getManagedForm().getToolkit().createLabel(fieldContainer, "Association", SWT.NONE);
        lblAssociation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        associationTypeViewer = new ComboViewer(fieldContainer, SWT.NONE);
        associationType = associationTypeViewer.getCombo();
        associationType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        getManagedForm().getToolkit().paintBordersFor(associationType);
        
        associationMultiplicity = new Combo(fieldContainer, SWT.READ_ONLY);
        associationMultiplicity.setItems(new String[] {"", "1", "*"});
        associationMultiplicity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        getManagedForm().getToolkit().adapt(associationMultiplicity);
        getManagedForm().getToolkit().paintBordersFor(associationMultiplicity);
        
        Label lblAnnotations = getManagedForm().getToolkit().createLabel(fieldContainer, "Annotations", SWT.NONE);
        lblAnnotations.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 2));
        
        annotations = getManagedForm().getToolkit().createTable(fieldContainer, SWT.FULL_SELECTION);
        getManagedForm().getToolkit().paintBordersFor(annotations);
        GridData gd_annotations = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2);
        gd_annotations.heightHint = 30;
        gd_annotations.widthHint = 1;
        annotations.setLayoutData(gd_annotations);
        
        annotationsViewer = new TableViewer(annotations);
        annotationsViewer.setColumnProperties(new String[] {"name"});
        
        annotationsLabelViewer = new TableViewerColumn(annotationsViewer, SWT.NONE);
        TableColumn classAnnotationLabel = annotationsLabelViewer.getColumn();
        classAnnotationLabel.setWidth(200);
        classAnnotationLabel.setText("Label");
        
        btnAnnotationAdd = getManagedForm().getToolkit().createButton(fieldContainer, "Add", SWT.NONE);
        btnAnnotationAdd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
        btnAnnotationRemove = getManagedForm().getToolkit().createButton(fieldContainer, "Remove", SWT.NONE);
        btnAnnotationRemove.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
        Label lblDescription = getManagedForm().getToolkit().createLabel(fieldContainer, "Description", SWT.NONE);
        lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        
        description = new StyledText(fieldContainer, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        getManagedForm().getToolkit().adapt(description);
        description.setAlwaysShowScrollBars(false);
        GridData gd_description = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        gd_description.widthHint = 200;
        gd_description.heightHint = 40;
        description.setLayoutData(gd_description);
        
        Label lblComment = getManagedForm().getToolkit().createLabel(fieldContainer, "Comment", SWT.NONE);
        lblComment.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        
        comment = new StyledText(fieldContainer, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        getManagedForm().getToolkit().adapt(comment);
        comment.setAlwaysShowScrollBars(false);
        GridData gd_comment = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        gd_comment.widthHint = 200;
        gd_comment.heightHint = 40;
        comment.setLayoutData(gd_comment);
        
        createPluginSections(parent);
        
        hookListeners();
    }

    private void createPluginSections(Composite parent) {
        for(MetadataPluginRef pref: MetadataPlugins.readMetadataPlugins()) {
            IMetadataUI ui = pref.getPlugin().createFieldUI(getEditor());
            if(ui != null) {
                uis.add(ui);
                Section modelDetailsJavaSection = getManagedForm().getToolkit().createSection(parent, Section.TWISTIE | Section.TITLE_BAR);
                modelDetailsJavaSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
                getManagedForm().getToolkit().paintBordersFor(modelDetailsJavaSection);
                modelDetailsJavaSection.setText(pref.getName());
                
                Composite modelDetailsJavaTabContainer = getManagedForm().getToolkit().createComposite(modelDetailsJavaSection, SWT.NONE);
                getManagedForm().getToolkit().paintBordersFor(modelDetailsJavaTabContainer);
                modelDetailsJavaSection.setClient(modelDetailsJavaTabContainer);
                
                ui.createContents(getManagedForm(), modelDetailsJavaTabContainer);
            }
        }
    }

    private void hookListeners() {
        new BeanListActions(getEditor(), getCurrentItem(), "annotations", annotationsViewer, btnAnnotationAdd, btnAnnotationRemove).bind();
        ModelListDragSupport.configure(annotationsViewer);
    }
    
    @Override
    public void modelAdded(IModelExtension model) {
        MField field = (MField) model;
        field.setVersion(field.getParent().getVersion());
    }
    
    @Override
    protected DataBindingContext createBindings() {
        return initDataBindings();
    }
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        IObservableValue observeSelectionRequiredObserveWidget = WidgetProperties.selection().observe(required);
        IObservableValue getCurrentItemRequiredObserveDetailValue = BeanProperties.value(MField.class, "required", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionRequiredObserveWidget, getCurrentItemRequiredObserveDetailValue, null, null);
        //
        IObservableValue observeSelectionIdentifierObserveWidget = WidgetProperties.selection().observe(identifier);
        IObservableValue getCurrentItemIdentifierObserveDetailValue = BeanProperties.value(MField.class, "identifier", boolean.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeSelectionIdentifierObserveWidget, getCurrentItemIdentifierObserveDetailValue, null, null);
        //
        IObservableValue observeTextNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(name);
        IObservableValue getCurrentItemNameObserveDetailValue = BeanProperties.value(MField.class, "name", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextNameObserveWidget, getCurrentItemNameObserveDetailValue, null, null);
        //
        IObservableValue observeTextAliasObserveWidget = WidgetProperties.text(SWT.Modify).observe(alias);
        IObservableValue getCurrentItemAliasObserveDetailValue = BeanProperties.value(MField.class, "alias", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextAliasObserveWidget, getCurrentItemAliasObserveDetailValue, null, null);
        //
        IObservableValue observeTextVersionObserveWidget = WidgetProperties.text(SWT.Modify).observe(version);
        IObservableValue getCurrentItemVersionObserveDetailValue = BeanProperties.value(MField.class, "version", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextVersionObserveWidget, getCurrentItemVersionObserveDetailValue, null, null);
        //
        IObservableValue observeTextTypeObserveWidget = WidgetProperties.text().observe(type);
        IObservableValue getCurrentItemTypeObserveDetailValue = BeanProperties.value(MField.class, "type", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextTypeObserveWidget, getCurrentItemTypeObserveDetailValue, null, null);
        //
        IObservableValue observeTextDefaultValueObserveWidget = WidgetProperties.text(SWT.Modify).observe(defaultValue);
        IObservableValue getCurrentItemDefaultValueObserveDetailValue = BeanProperties.value(MField.class, "defaultValue", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextDefaultValueObserveWidget, getCurrentItemDefaultValueObserveDetailValue, null, null);
        //
        IObservableValue observeTextAssociationTypeObserveWidget = WidgetProperties.text().observe(associationType);
        IObservableValue getCurrentItemAssociationtypeObserveDetailValue = BeanProperties.value(MField.class, "associationType", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextAssociationTypeObserveWidget, getCurrentItemAssociationtypeObserveDetailValue, null, null);
        //
        ObservableListContentProvider listContentProvider_1 = new ObservableListContentProvider();
        IObservableMap observeMap = BeansObservables.observeMap(listContentProvider_1.getKnownElements(), MClass.class, "name");
        associationTypeViewer.setLabelProvider(new ObservableMapLabelProvider(observeMap));
        associationTypeViewer.setContentProvider(listContentProvider_1);
        //
        IObservableList childrenGetClassesBeanObserveList = BeanProperties.list("children").observe(getClassesBean());
        associationTypeViewer.setInput(childrenGetClassesBeanObserveList);
        //
        IObservableValue observeTextAssociationMultiplicityObserveWidget = WidgetProperties.text().observe(associationMultiplicity);
        IObservableValue getCurrentItemAssociationmultiplicityObserveDetailValue = BeanProperties.value(MField.class, "multiplicity", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextAssociationMultiplicityObserveWidget, getCurrentItemAssociationmultiplicityObserveDetailValue, null, null);
        //
        ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
        IObservableMap[] observeMaps = BeansObservables.observeMaps(listContentProvider.getKnownElements(), MAnnotation.class, new String[]{"image", "name"});
        annotationsViewer.setLabelProvider(new TableSingleColumnLabelProvider(observeMaps));
        annotationsViewer.setContentProvider(listContentProvider);
        //
        IObservableList getCurrentItemAnnotationsObserveDetailList = BeanProperties.list(MField.class, "annotations", MAnnotation.class).observeDetail(getCurrentItem());
        annotationsViewer.setInput(getCurrentItemAnnotationsObserveDetailList);
        //
        CellEditor cellEditor = new TextCellEditor(annotationsViewer.getTable());
        IValueProperty cellEditorProperty = CellEditorProperties.control().value(WidgetProperties.text(SWT.Modify));
        IBeanValueProperty valueProperty = BeanProperties.value("name");
        annotationsLabelViewer.setEditingSupport(ObservableValueEditingSupport.create(annotationsViewer, bindingContext, cellEditor, cellEditorProperty, valueProperty));
        //
        IObservableValue observeTextDescriptionObserveWidget = WidgetProperties.text(SWT.Modify).observe(description);
        IObservableValue getCurrentItemDescriptionObserveDetailValue = BeanProperties.value(MField.class, "description", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextDescriptionObserveWidget, getCurrentItemDescriptionObserveDetailValue, null, null);
        //
        IObservableValue observeTextCommentObserveWidget = WidgetProperties.text(SWT.Modify).observe(comment);
        IObservableValue getCurrentItemCommentObserveDetailValue = BeanProperties.value(MField.class, "comment", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextCommentObserveWidget, getCurrentItemCommentObserveDetailValue, null, null);
        //
        return bindingContext;
    }
}
