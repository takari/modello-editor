package io.takari.modello.editor.impl.ui;

import java.util.ArrayList;
import java.util.List;

import io.takari.modello.editor.impl.model.MModelDetails;
import io.takari.modello.editor.impl.model.plugin.IMetadataUI;
import io.takari.modello.editor.impl.model.plugin.MetadataPlugins;
import io.takari.modello.editor.impl.model.plugin.MetadataPlugins.MetadataPluginRef;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;
import io.takari.modello.editor.toolkit.ui.AbstractDetailPart;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;

public class ModelDetailsPart extends AbstractDetailPart {

    private final List<IMetadataUI> uis;
    
    private Text modelDetailsId;
    private Text modelDetailsName;
    private Text modelDetailsVersionField;
    private Combo modelDetailsVersionDef;
    private StyledText modelDetailsDescription;
    private StyledText modelDetailsComment;
    
    public ModelDetailsPart(IDocumentEditor editor) {
        super(editor);
        uis = new ArrayList<IMetadataUI>();
    }
    
    @Override
    public Class<? extends IModel> getDetailClass() {
        return MModelDetails.class;
    }
    
    @Override
    protected void createClient(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        
        Section modelSection = getManagedForm().getToolkit().createSection(parent, Section.TITLE_BAR);
        modelSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        getManagedForm().getToolkit().paintBordersFor(modelSection);
        modelSection.setText("Model Details");
        
        Composite modelDetailsCoreContainer = getManagedForm().getToolkit().createComposite(modelSection, SWT.NONE);
        getManagedForm().getToolkit().paintBordersFor(modelDetailsCoreContainer);
        modelSection.setClient(modelDetailsCoreContainer);
        modelDetailsCoreContainer.setLayout(new GridLayout(2, false));
        
        Label lblModelDetailsId = getManagedForm().getToolkit().createLabel(modelDetailsCoreContainer, "ID", SWT.NONE);
        lblModelDetailsId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        modelDetailsId = getManagedForm().getToolkit().createText(modelDetailsCoreContainer, "", SWT.NONE);
        modelDetailsId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblModelDetailsName = getManagedForm().getToolkit().createLabel(modelDetailsCoreContainer, "Name", SWT.NONE);
        lblModelDetailsName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        modelDetailsName = getManagedForm().getToolkit().createText(modelDetailsCoreContainer, "", SWT.NONE);
        modelDetailsName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblModelDetailsVersionDef = getManagedForm().getToolkit().createLabel(modelDetailsCoreContainer, "Version Definition", SWT.NONE);
        lblModelDetailsVersionDef.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        modelDetailsVersionDef = new Combo(modelDetailsCoreContainer, SWT.READ_ONLY);
        modelDetailsVersionDef.setItems(new String[] {"", "field", "namespace", "field+namespace"});
        modelDetailsVersionDef.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        getManagedForm().getToolkit().adapt(modelDetailsVersionDef);
        getManagedForm().getToolkit().paintBordersFor(modelDetailsVersionDef);
        
        Label lblModelDetailsVersionField = getManagedForm().getToolkit().createLabel(modelDetailsCoreContainer, "Version Field", SWT.NONE);
        lblModelDetailsVersionField.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        modelDetailsVersionField = getManagedForm().getToolkit().createText(modelDetailsCoreContainer, "", SWT.NONE);
        modelDetailsVersionField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblModelDetailsDescription = getManagedForm().getToolkit().createLabel(modelDetailsCoreContainer, "Description", SWT.NONE);
        lblModelDetailsDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        
        modelDetailsDescription = new StyledText(modelDetailsCoreContainer, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        getManagedForm().getToolkit().adapt(modelDetailsDescription);
        modelDetailsDescription.setAlwaysShowScrollBars(false);
        GridData gd_modelDetailsDescription = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gd_modelDetailsDescription.widthHint = 200;
        gd_modelDetailsDescription.heightHint = 40;
        modelDetailsDescription.setLayoutData(gd_modelDetailsDescription);
        
        Label lblModelDetailsComment = getManagedForm().getToolkit().createLabel(modelDetailsCoreContainer, "Comment", SWT.NONE);
        lblModelDetailsComment.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        
        modelDetailsComment = new StyledText(modelDetailsCoreContainer, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        getManagedForm().getToolkit().adapt(modelDetailsComment);
        modelDetailsComment.setAlwaysShowScrollBars(false);
        GridData gd_modelDetailsComment = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gd_modelDetailsComment.widthHint = 200;
        gd_modelDetailsComment.heightHint = 40;
        modelDetailsComment.setLayoutData(gd_modelDetailsComment);
        
        createPluginSections(parent);
    }

    private void createPluginSections(Composite parent) {
        for(MetadataPluginRef pref: MetadataPlugins.readMetadataPlugins()) {
            IMetadataUI ui = pref.getPlugin().createModelUI(getEditor());
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
    
    @Override
    protected DataBindingContext createBindings() {
        return initDataBindings();
    }
    
    // windowbuilder is not particularly happy about initDataBindings being an overridden (implemented) method
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        IObservableValue observeTextModelDetailsIdObserveWidget = WidgetProperties.text(SWT.Modify).observe(modelDetailsId);
        IObservableValue detailsidGetModelObserveValue = BeanProperties.value(MModelDetails.class, "id", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelDetailsIdObserveWidget, detailsidGetModelObserveValue, null, null);
        //
        IObservableValue observeTextModelDetailsNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(modelDetailsName);
        IObservableValue modelDetailsnameObserveDetailValue = BeanProperties.value(MModelDetails.class, "name", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelDetailsNameObserveWidget, modelDetailsnameObserveDetailValue, null, null);
        //
        IObservableValue observeTextModelDetailsVersionDefObserveWidget = WidgetProperties.text().observe(modelDetailsVersionDef);
        IObservableValue modelDetailsversionDefinitionObserveDetailValue = BeanProperties.value(MModelDetails.class, "versionDefinition", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelDetailsVersionDefObserveWidget, modelDetailsversionDefinitionObserveDetailValue, null, null);
        //
        IObservableValue observeTextModelDetailsVersionFieldObserveWidget = WidgetProperties.text(SWT.Modify).observe(modelDetailsVersionField);
        IObservableValue modelDetailsversionFieldObserveDetailValue = BeanProperties.value(MModelDetails.class, "versionField", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelDetailsVersionFieldObserveWidget, modelDetailsversionFieldObserveDetailValue, null, null);
        //
        IObservableValue observeTextModelDetailsDescriptionObserveWidget = WidgetProperties.text(SWT.Modify).observe(modelDetailsDescription);
        IObservableValue modelDetailsdescriptionObserveDetailValue = BeanProperties.value(MModelDetails.class, "description", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelDetailsDescriptionObserveWidget, modelDetailsdescriptionObserveDetailValue, null, null);
        //
        IObservableValue observeTextModelDetailsCommentObserveWidget = WidgetProperties.text(SWT.Modify).observe(modelDetailsComment);
        IObservableValue modelDetailscommentObserveDetailValue = BeanProperties.value(MModelDetails.class, "comment", String.class).observeDetail(getCurrentItem());
        bindingContext.bindValue(observeTextModelDetailsCommentObserveWidget, modelDetailscommentObserveDetailValue, null, null);
        //
        return bindingContext;
    }
}
