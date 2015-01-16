package io.takari.modello.editor.impl.ui;

import java.util.ArrayList;
import java.util.List;

import io.takari.modello.editor.impl.model.MCodeSegment;
import io.takari.modello.editor.impl.model.MInterface;
import io.takari.modello.editor.impl.ui.dialogs.EditCodeSegmentsDialog;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.mapping.model.IModelExtension;
import io.takari.modello.editor.toolkit.actions.BeanListActions;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;
import io.takari.modello.editor.toolkit.ui.AbstractDetailPart;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
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
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.beans.BeansObservables;

import io.takari.modello.editor.impl.model.MAnnotation;
import io.takari.modello.editor.impl.model.plugin.IMetadataUI;
import io.takari.modello.editor.impl.model.plugin.MetadataPlugins;
import io.takari.modello.editor.impl.model.plugin.MetadataPlugins.MetadataPluginRef;
import io.takari.modello.editor.toolkit.util.TableSingleColumnLabelProvider;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.jface.databinding.viewers.CellEditorProperties;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;

public class InterfacePart extends AbstractDetailPart {

    private final List<IMetadataUI> uis;
    
    private Text txtIfaceName;
    private Text packageName;
    private Text version;
    private Text superinterface;
    
    private Table annotations;
    private TableViewer classAnnotationsViewer;
    private TableViewerColumn classAnnotationsLabelViewer;
    private Button btnAnnotationAdd;
    private Button btnAnnotationRemove;
    private StyledText description;
    private StyledText comment;
    
    private Table codeSegments;
    private TableViewer codeSegmentsViewer;
    private TableViewerColumn codeSegmentsVersionViewer;
    private TableColumn codeSegmentsVersion;
    private TableViewerColumn codeSegmentsLabelViewer;
    private TableColumn codeSegmentsLabel;
    private Button btnCodeSegmentAdd;
    private Button btnCodeSegmentEdit;
    private Button btnCodeSegmentRemove;

    public InterfacePart(IDocumentEditor editor) {
        super(editor);
        uis = new ArrayList<IMetadataUI>();
    }
    
    @Override
    public Class<? extends IModel> getDetailClass() {
        return MInterface.class;
    }

    @Override
    protected void createClient(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        
        Section interfaceSection = getManagedForm().getToolkit().createSection(parent, Section.TITLE_BAR);
        interfaceSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        getManagedForm().getToolkit().paintBordersFor(interfaceSection);
        interfaceSection.setText("Interface Details");
        
        Composite interfaceDetails = getManagedForm().getToolkit().createComposite(interfaceSection, SWT.NONE);
        getManagedForm().getToolkit().paintBordersFor(interfaceDetails);
        interfaceSection.setClient(interfaceDetails);
        
        interfaceDetails.setLayout(new GridLayout(3, false));
        
        Label lblPackage = getManagedForm().getToolkit().createLabel(interfaceDetails, "Package", SWT.NONE);
        lblPackage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        packageName = getManagedForm().getToolkit().createText(interfaceDetails, "", SWT.NONE);
        packageName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        Label lblIfaceName = getManagedForm().getToolkit().createLabel(interfaceDetails, "Name", SWT.NONE);
        lblIfaceName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        txtIfaceName = getManagedForm().getToolkit().createText(interfaceDetails, "", SWT.NONE);
        txtIfaceName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        Label lblVersion = getManagedForm().getToolkit().createLabel(interfaceDetails, "Version", SWT.NONE);
        lblVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        version = getManagedForm().getToolkit().createText(interfaceDetails, "", SWT.NONE);
        version.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        Label lblSuperinterface = getManagedForm().getToolkit().createLabel(interfaceDetails, "Superinterface", SWT.NONE);
        lblSuperinterface.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        superinterface = getManagedForm().getToolkit().createText(interfaceDetails, "", SWT.NONE);
        superinterface.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        Label lblAnnotations = getManagedForm().getToolkit().createLabel(interfaceDetails, "Annotations", SWT.NONE);
        lblAnnotations.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 2));
        
        annotations = getManagedForm().getToolkit().createTable(interfaceDetails, SWT.FULL_SELECTION);
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
        
        btnAnnotationAdd = getManagedForm().getToolkit().createButton(interfaceDetails, "Add", SWT.NONE);
        btnAnnotationAdd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
        btnAnnotationRemove = getManagedForm().getToolkit().createButton(interfaceDetails, "Remove", SWT.NONE);
        btnAnnotationRemove.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
        Label lblCodeSegments = getManagedForm().getToolkit().createLabel(interfaceDetails, "Code Segments", SWT.NONE);
        lblCodeSegments.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 3));
        
        codeSegments = getManagedForm().getToolkit().createTable(interfaceDetails, SWT.FULL_SELECTION);
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
        
        btnCodeSegmentAdd = getManagedForm().getToolkit().createButton(interfaceDetails, "Add", SWT.NONE);
        btnCodeSegmentAdd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
        btnCodeSegmentEdit = getManagedForm().getToolkit().createButton(interfaceDetails, "Edit", SWT.NONE);
        btnCodeSegmentEdit.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
        btnCodeSegmentRemove = getManagedForm().getToolkit().createButton(interfaceDetails, "Remove", SWT.NONE);
        btnCodeSegmentRemove.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        
        Label lblDescription = getManagedForm().getToolkit().createLabel(interfaceDetails, "Description", SWT.NONE);
        lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        
        description = new StyledText(interfaceDetails, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        getManagedForm().getToolkit().adapt(description);
        description.setAlwaysShowScrollBars(false);
        GridData gd_description = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        gd_description.widthHint = 200;
        gd_description.heightHint = 40;
        description.setLayoutData(gd_description);
        
        Label lblComment = getManagedForm().getToolkit().createLabel(interfaceDetails, "Comment", SWT.NONE);
        lblComment.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        
        comment = new StyledText(interfaceDetails, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        comment.setAlwaysShowScrollBars(false);
        getManagedForm().getToolkit().adapt(comment);
        GridData gd_comment = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        gd_comment.widthHint = 200;
        gd_comment.heightHint = 40;
        comment.setLayoutData(gd_comment);
        
        createPluginSections(parent);
        
        hookListeners();
    }

    private void createPluginSections(Composite parent) {
        for(MetadataPluginRef pref: MetadataPlugins.readMetadataPlugins()) {
            IMetadataUI ui = pref.getPlugin().createInterfaceUI(getEditor());
            if(ui != null) {
                uis.add(ui);
                Section pluginSection = getManagedForm().getToolkit().createSection(parent, Section.TWISTIE | Section.TITLE_BAR);
                pluginSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
                getManagedForm().getToolkit().paintBordersFor(pluginSection);
                pluginSection.setText(pref.getName());
                
                Composite pluginContainer = getManagedForm().getToolkit().createComposite(pluginSection, SWT.NONE);
                getManagedForm().getToolkit().paintBordersFor(pluginContainer);
                pluginSection.setClient(pluginContainer);
                
                ui.createContents(getManagedForm(), pluginContainer);
            }
        }
    }
    
    private void hookListeners() {
        BeanListActions.configure(getEditor(), getCurrentItem(), "annotations", classAnnotationsViewer, btnAnnotationAdd, btnAnnotationRemove).bind();
        BeanListActions.configure(getEditor(), getCurrentItem(), "codeSegments", codeSegmentsViewer, btnCodeSegmentAdd, btnCodeSegmentRemove)
            .withDialog(btnCodeSegmentEdit, new BeanListActions.IModelProvider() {
                
                @Override
                public IModelExtension createTempModel() {
                    return (IModelExtension) getEditor().getProxyGenerator().createDirectProxy(MCodeSegment.class);
                }
                
                @Override
                public boolean showDialog(IModelExtension tempModel) {
                    return new EditCodeSegmentsDialog(getEditor(), (MInterface) getCurrentItem().getValue(), (MCodeSegment) tempModel).open() == Dialog.OK;
                }
                
            }).bind();
    }

    @Override
    protected DataBindingContext createBindings() {
        return initDataBindings();
    }
    
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        IObservableValue observeTextPackageNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(packageName);
        IObservableValue getCurrentItemPackageNameObserveDetailValue = BeanProperties.value(MInterface.class, "packageName", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextPackageNameObserveWidget, getCurrentItemPackageNameObserveDetailValue, null, null);
        //
        IObservableValue observeTextTxtIfaceNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtIfaceName);
        IObservableValue currentInterfaceNameObserveDetailValue = BeanProperties.value(MInterface.class, "name", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextTxtIfaceNameObserveWidget, currentInterfaceNameObserveDetailValue, null, null);
        //
        IObservableValue observeTextVersionObserveWidget = WidgetProperties.text(SWT.Modify).observe(version);
        IObservableValue getCurrentItemVersionObserveDetailValue = BeanProperties.value(MInterface.class, "version", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextVersionObserveWidget, getCurrentItemVersionObserveDetailValue, null, null);
        //
        IObservableValue observeTextSuperinterfaceObserveWidget = WidgetProperties.text(SWT.Modify).observe(superinterface);
        IObservableValue getCurrentItemSuperInterfaceObserveDetailValue = BeanProperties.value(MInterface.class, "superInterface", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextSuperinterfaceObserveWidget, getCurrentItemSuperInterfaceObserveDetailValue, null, null);
        //
        ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
        IObservableMap[] observeMaps = BeansObservables.observeMaps(listContentProvider.getKnownElements(), MAnnotation.class, new String[]{"image", "label"});
        classAnnotationsViewer.setLabelProvider(new TableSingleColumnLabelProvider(observeMaps));
        classAnnotationsViewer.setContentProvider(listContentProvider);
        //
        IObservableList getCurrentItemAnnotationsObserveDetailList = BeanProperties.list(MInterface.class, "annotations", MAnnotation.class).observeDetail(getCurrentItem());
        classAnnotationsViewer.setInput(getCurrentItemAnnotationsObserveDetailList);
        //
        CellEditor cellEditor = new TextCellEditor(classAnnotationsViewer.getTable());
        IValueProperty cellEditorProperty = CellEditorProperties.control().value(WidgetProperties.text(SWT.Modify));
        IBeanValueProperty valueProperty = BeanProperties.value("name");
        classAnnotationsLabelViewer.setEditingSupport(ObservableValueEditingSupport.create(classAnnotationsViewer, bindingContext, cellEditor, cellEditorProperty, valueProperty));
        //
        ObservableListContentProvider listContentProvider_1 = new ObservableListContentProvider();
        IObservableMap[] observeMaps_1 = BeansObservables.observeMaps(listContentProvider_1.getKnownElements(), MCodeSegment.class, new String[]{"version", "labelValue"});
        codeSegmentsViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps_1));
        codeSegmentsViewer.setContentProvider(listContentProvider_1);
        //
        IObservableList getCurrentItemCodeSegmentsObserveDetailList = BeanProperties.list(MInterface.class, "codeSegments", MCodeSegment.class).observeDetail(getCurrentItem());
        codeSegmentsViewer.setInput(getCurrentItemCodeSegmentsObserveDetailList);
        //
        IObservableValue observeTextDescriptionObserveWidget = WidgetProperties.text(SWT.Modify).observe(description);
        IObservableValue getCurrentItemDescriptionObserveDetailValue = BeanProperties.value(MInterface.class, "description", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextDescriptionObserveWidget, getCurrentItemDescriptionObserveDetailValue, null, null);
        //
        IObservableValue observeTextCommentObserveWidget = WidgetProperties.text(SWT.Modify).observe(comment);
        IObservableValue getCurrentItemCommentObserveDetailValue = BeanProperties.value(MInterface.class, "comment", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextCommentObserveWidget, getCurrentItemCommentObserveDetailValue, null, null);
        //
        return bindingContext;
    }
}
