package io.takari.modello.editor.mapping.dom.accessor;

import io.takari.modello.editor.mapping.dom.accessor.PathParser.DomPath;
import io.takari.modello.editor.mapping.dom.impl.DomSection;
import io.takari.modello.editor.mapping.model.IModelExtension;

public class ModelSelfAccessor extends BaseAccessor<DomSection> {
    
    protected ModelSelfAccessor() {
        super(null, null);
    }
    
    @Override
    public Object get(DomModelAccessor ctx, IModelExtension model) {
        return model;
    }

    @Override
    public void touch(DomModelAccessor ctx, IModelExtension model) {
        ctx.getContainer(model).touch(ctx.getDomHelper());
    }

    @Override
    protected DomSection createPropertyData(DomModelAccessor ctx, IModelExtension model, DomPath path) {
        return null;
    }
}
