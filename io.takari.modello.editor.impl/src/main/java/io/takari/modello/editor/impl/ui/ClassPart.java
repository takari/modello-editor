package io.takari.modello.editor.impl.ui;

import io.takari.modello.editor.impl.model.MAnnotation;
import io.takari.modello.editor.impl.model.MClass;
import io.takari.modello.editor.impl.model.MClassInterface;
import io.takari.modello.editor.impl.model.MCodeSegment;
import io.takari.modello.editor.impl.model.plugin.IMetadataUI;
import io.takari.modello.editor.impl.model.plugin.MetadataPlugins;
import io.takari.modello.editor.impl.model.plugin.MetadataPlugins.MetadataPluginRef;
import io.takari.modello.editor.impl.ui.dialogs.EditCodeSegmentsDialog;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.mapping.model.IModelExtension;
import io.takari.modello.editor.toolkit.actions.BeanListActions;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;
import io.takari.modello.editor.toolkit.ui.AbstractDetailPart;
import io.takari.modello.editor.toolkit.util.TableSingleColumnLabelProvider;

import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;

public class ClassPart extends AbstractDetailPart {
    
    private final List<IMetadataUI> uis;
    
    private Text name;
    private Text version;
    
    private Text superclass;
    
    private Table interfaces;
    private TableViewer classInterfacesViewer;
    private TableViewerColumn classInterfaceLabelViewer;
    private Button btnInterfaceAdd;
    private Button btnInterfaceRemove;
    
    private Table annotations;
    private TableViewer classAnnotationsViewer;
    private TableViewerColumn classAnnotationsLabelViewer;
    private Button btnAnnotationAdd;
    private Button btnAnnotationRemove;
    
    private Table codeSegments;
    private TableViewer codeSegmentsViewer;
    private TableColumn codeSegmentsLabel;
    private TableViewerColumn codeSegmentsLabelViewer;
    private TableColumn codeSegmentsVersion;
    private TableViewerColumn codeSegmentsVersionViewer;
    private Button btnCodeSegmentAdd;
    private Button btnCodeSegmentEdit;
    private Button btnCodeSegmentRemove;
    
    private StyledText description;
    private StyledText comment;
    
    private Text packageName;
    
    public ClassPart(IDocumentEditor editor) {
        super(editor);
        uis = new ArrayList<IMetadataUI>();
    }
    
    @Override
    public Class<? extends IModel> getDetailClass() {
        return MClass.class;
    }
    
    @Override
    protected void createClient(Composite parent) {
        
        parent.setLayout(new GridLayout(1, false));
        
        Section classSection = getManagedForm().getToolkit().createSection(parent, Section.TITLE_BAR);
        classSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        getManagedForm().getToolkit().paintBordersFor(classSection);
        classSection.setText("Class Details");
        
        Composite classCoreContainer = getManagedForm().getToolkit().createComposite(classSection);
        getManagedForm().getToolkit().paintBordersFor(classCoreContainer);
        classSection.setClient(classCoreContainer);
        
        classCoreContainer.setLayout(new GridLayout(3, false));
        
        Label lblPackage = getManagedForm().getToolkit().createLabel(classCoreContainer, "Package", SWT.NONE);
        lblPackage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        packageName = getManagedForm().getToolkit().createText(classCoreContainer, "", SWT.NONE);
        packageName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        Label lblName = getManagedForm().getToolkit().createLabel(classCoreContainer, "Name", SWT.NONE);
        lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        name = getManagedForm().getToolkit().createText(classCoreContainer, "", SWT.NONE);
        name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        Label lblVersion = getManagedForm().getToolkit().createLabel(classCoreContainer, "Version", SWT.NONE);
        lblVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        version = getManagedForm().getToolkit().createText(classCoreContainer, "", SWT.NONE);
        version.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        Label lblSuperclass = getManagedForm().getToolkit().createLabel(classCoreContainer, "Superclass", SWT.NONE);
        lblSuperclass.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        superclass = getManagedForm().getToolkit().createText(classCoreContainer, "", SWT.NONE);
        superclass.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        Label lblInterfaces = getManagedForm().getToolkit().createLabel(classCoreContainer, "Interfaces", SWT.NONE);
        lblInterfaces.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 2));
        
        interfaces = getManagedForm().getToolkit().createTable(classCoreContainer, SWT.FULL_SELECTION);
        getManagedForm().getToolkit().paintBordersFor(interfaces);
        GridData gd_interfaces = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
        gd_interfaces.heightHint = 30;
        gd_interfaces.widthHint = 1;
        interfaces.setLayoutData(gd_interfaces);
        
        classInterfacesViewer = new TableViewer(interfaces);
        classInterfacesViewer.setColumnProperties(new String[] {"name"});
        
        classInterfaceLabelViewer = new TableViewerColumn(classInterfacesViewer, SWT.NONE);
        TableColumn classInterfaceLabel = classInterfaceLabelViewer.getColumn();
        classInterfaceLabel.setWidth(200);
        classInterfaceLabel.setText("Label");
        
        btnInterfaceAdd = getManagedForm().getToolkit().createButton(classCoreContainer, "Add", SWT.NONE);
        btnInterfaceAdd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
        btnInterfaceRemove = getManagedForm().getToolkit().createButton(classCoreContainer, "Remove", SWT.NONE);
        btnInterfaceRemove.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
        Label lblAnnotations = getManagedForm().getToolkit().createLabel(classCoreContainer, "Annotations", SWT.NONE);
        lblAnnotations.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 2));
        
        annotations = getManagedForm().getToolkit().createTable(classCoreContainer, SWT.FULL_SELECTION);
        getManagedForm().getToolkit().paintBordersFor(annotations);
        GridData gd_annotations = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
        gd_annotations.heightHint = 30;
        gd_annotations.widthHint = 1;
        annotations.setLayoutData(gd_annotations);
        
        classAnnotationsViewer = new TableViewer(annotations);
        classAnnotationsViewer.setColumnProperties(new String[] {"name"});
        
        classAnnotationsLabelViewer = new TableViewerColumn(classAnnotationsViewer, SWT.NONE);
        TableColumn classAnnotationLabel = classAnnotationsLabelViewer.getColumn();
        classAnnotationLabel.setWidth(200);
        classAnnotationLabel.setText("Label");
        
        btnAnnotationAdd = getManagedForm().getToolkit().createButton(classCoreContainer, "Add", SWT.NONE);
        btnAnnotationAdd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
        btnAnnotationRemove = getManagedForm().getToolkit().createButton(classCoreContainer, "Remove", SWT.NONE);
        btnAnnotationRemove.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
        Label lblCodeSegments = getManagedForm().getToolkit().createLabel(classCoreContainer, "Code Segments", SWT.NONE);
        lblCodeSegments.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 3));
        
        codeSegments = getManagedForm().getToolkit().createTable(classCoreContainer, SWT.FULL_SELECTION);
        getManagedForm().getToolkit().paintBordersFor(codeSegments);
        GridData gd_codeSegments = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 3);
        gd_codeSegments.heightHint = 30;
        gd_codeSegments.widthHint = 1;
        codeSegments.setLayoutData(gd_codeSegments);
        
        codeSegmentsViewer = new TableViewer(codeSegments);
        codeSegmentsViewer.setColumnProperties(new String[] {"name"});
        
        codeSegmentsVersionViewer = new TableViewerColumn(codeSegmentsViewer, SWT.NONE);
        codeSegmentsVersion = codeSegmentsVersionViewer.getColumn();
        codeSegmentsVersion.setWidth(50);
        
        codeSegmentsLabelViewer = new TableViewerColumn(codeSegmentsViewer, SWT.NONE);
        codeSegmentsLabel = codeSegmentsLabelViewer.getColumn();
        codeSegmentsLabel.setWidth(200);
        
        btnCodeSegmentAdd = getManagedForm().getToolkit().createButton(classCoreContainer, "Add", SWT.NONE);
        btnCodeSegmentAdd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
        btnCodeSegmentEdit = getManagedForm().getToolkit().createButton(classCoreContainer, "Edit", SWT.NONE);
        btnCodeSegmentEdit.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
        btnCodeSegmentRemove = getManagedForm().getToolkit().createButton(classCoreContainer, "Remove", SWT.NONE);
        btnCodeSegmentRemove.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
        Label lblDescription = getManagedForm().getToolkit().createLabel(classCoreContainer, "Description", SWT.NONE);
        lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        
        description = new StyledText(classCoreContainer, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        getManagedForm().getToolkit().adapt(description);
        description.setAlwaysShowScrollBars(false);
        GridData gd_description = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        gd_description.widthHint = 200;
        gd_description.heightHint = 40;
        description.setLayoutData(gd_description);
        
        Label lblComment = getManagedForm().getToolkit().createLabel(classCoreContainer, "Comment", SWT.NONE);
        lblComment.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        
        comment = new StyledText(classCoreContainer, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        comment.setAlwaysShowScrollBars(false);
        getManagedForm().getToolkit().adapt(comment);
        GridData gd_comment = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        gd_comment.widthHint = 200;
        gd_comment.heightHint = 40;
        comment.setLayoutData(gd_comment);
        
        createPluginsSections(parent);
        
        hookListeners();
    }

    private void createPluginsSections(Composite parent) {
        for(MetadataPluginRef pref: MetadataPlugins.readMetadataPlugins()) {
            IMetadataUI ui = pref.getPlugin().createClassUI(getEditor());
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
        BeanListActions.configure(getCurrentItem(), "interfaces", classInterfacesViewer, btnInterfaceAdd, btnInterfaceRemove).bind();
        BeanListActions.configure(getCurrentItem(), "annotations", classAnnotationsViewer, btnAnnotationAdd, btnAnnotationRemove).bind();
        BeanListActions.configure(getCurrentItem(), "codeSegments", codeSegmentsViewer, btnCodeSegmentAdd, btnCodeSegmentRemove)
            .withDialog(btnCodeSegmentEdit, new BeanListActions.IModelProvider() {
                
                @Override
                public IModelExtension createTempModel() {
                    return (IModelExtension) getEditor().getProxyGenerator().createDirectProxy(MCodeSegment.class);
                }
                
                @Override
                public boolean showDialog(IModelExtension tempModel) {
                    return new EditCodeSegmentsDialog(getEditor(), (MCodeSegment) tempModel).open() == Dialog.OK;
                }
                
            }).bind();
    }
    
    @Override
    protected void refreshPart() {
        classInterfacesViewer.refresh();
        classAnnotationsViewer.refresh();
        codeSegmentsViewer.refresh();
    }
    
    @Override
    protected DataBindingContext createBindings() {
        return initDataBindings();
    }
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        IObservableValue observeTextPackageNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(packageName);
        IObservableValue getCurrentItemPackageNameObserveDetailValue = BeanProperties.value(MClass.class, "packageName", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextPackageNameObserveWidget, getCurrentItemPackageNameObserveDetailValue, null, null);
        //
        IObservableValue observeTextTxtClassNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(name);
        IObservableValue currentClassNameObserveDetailValue = BeanProperties.value(MClass.class, "name", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextTxtClassNameObserveWidget, currentClassNameObserveDetailValue, null, null);
        //
        IObservableValue observeTextVersionObserveWidget = WidgetProperties.text(SWT.Modify).observe(version);
        IObservableValue currentClassVersionObserveDetailValue = BeanProperties.value(MClass.class, "version", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextVersionObserveWidget, currentClassVersionObserveDetailValue, null, null);
        //
        IObservableValue observeTextClassSuperObserveWidget = WidgetProperties.text(SWT.Modify).observe(superclass);
        IObservableValue currentClassSuperClassObserveDetailValue = BeanProperties.value(MClass.class, "superClass", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextClassSuperObserveWidget, currentClassSuperClassObserveDetailValue, null, null);
        //
        ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
        IObservableMap[] observeMaps = BeansObservables.observeMaps(listContentProvider.getKnownElements(), MClassInterface.class, new String[]{"image", "label"});
        classInterfacesViewer.setLabelProvider(new TableSingleColumnLabelProvider(observeMaps));
        classInterfacesViewer.setContentProvider(listContentProvider);
        //
        IObservableList currentClassChildrenObserveDetailList = BeanProperties.list(MClass.class, "interfaces", MClassInterface.class).observeDetail(getCurrentItem());
        classInterfacesViewer.setInput(currentClassChildrenObserveDetailList);
        //
        CellEditor cellEditor = new TextCellEditor(classInterfacesViewer.getTable());
        IValueProperty cellEditorProperty = CellEditorProperties.control().value(WidgetProperties.text(SWT.Modify));
        IBeanValueProperty valueProperty = BeanProperties.value("name");
        classInterfaceLabelViewer.setEditingSupport(ObservableValueEditingSupport.create(classInterfacesViewer, bindingContext, cellEditor, cellEditorProperty, valueProperty));
        //
        ObservableListContentProvider listContentProvider_1 = new ObservableListContentProvider();
        IObservableMap[] observeMaps_1 = BeansObservables.observeMaps(listContentProvider_1.getKnownElements(), MAnnotation.class, new String[]{"image", "label"});
        classAnnotationsViewer.setLabelProvider(new TableSingleColumnLabelProvider(observeMaps_1));
        classAnnotationsViewer.setContentProvider(listContentProvider_1);
        //
        IObservableList currentClassAnnotationsObserveDetailList = BeanProperties.list(MClass.class, "annotations", MAnnotation.class).observeDetail(getCurrentItem());
        classAnnotationsViewer.setInput(currentClassAnnotationsObserveDetailList);
        //
        CellEditor cellEditor_1 = new TextCellEditor(classAnnotationsViewer.getTable());
        IValueProperty cellEditorProperty_1 = CellEditorProperties.control().value(WidgetProperties.text(SWT.Modify));
        IBeanValueProperty valueProperty_1 = BeanProperties.value("name");
        classAnnotationsLabelViewer.setEditingSupport(ObservableValueEditingSupport.create(classAnnotationsViewer, bindingContext, cellEditor_1, cellEditorProperty_1, valueProperty_1));
        //
        ObservableListContentProvider listContentProvider_2 = new ObservableListContentProvider();
        IObservableMap[] observeMaps_2 = BeansObservables.observeMaps(listContentProvider_2.getKnownElements(), MCodeSegment.class, new String[]{"version", "labelValue"});
        codeSegmentsViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps_2));
        codeSegmentsViewer.setContentProvider(listContentProvider_2);
        //
        IObservableList currentClassCodeSegmentsObserveDetailList = BeanProperties.list(MClass.class, "codeSegments", MCodeSegment.class).observeDetail(getCurrentItem());
        codeSegmentsViewer.setInput(currentClassCodeSegmentsObserveDetailList);
        //
        IObservableValue observeTextDescriptionObserveWidget = WidgetProperties.text(SWT.Modify).observe(description);
        IObservableValue currentClassDescriptionObserveDetailValue = BeanProperties.value(MClass.class, "description", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextDescriptionObserveWidget, currentClassDescriptionObserveDetailValue, null, null);
        //
        IObservableValue observeTextCommentObserveWidget = WidgetProperties.text(SWT.Modify).observe(comment);
        IObservableValue currentClassCommentObserveDetailValue = BeanProperties.value(MClass.class, "comment", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextCommentObserveWidget, currentClassCommentObserveDetailValue, null, null);
        //
        return bindingContext;
    }
}
