package io.takari.modello.editor.impl.model.plugin;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;

public class DetailPartMetadataUI implements IMetadataUI {
    
    private final MetadataPluginDetailPart part;
    
    public DetailPartMetadataUI(MetadataPluginDetailPart part) {
        this.part = part;
    }
    
    @Override
    public void createContents(IManagedForm form, Composite parent) {
        form.addPart(part);
        part.createContents(parent);
    }

}
