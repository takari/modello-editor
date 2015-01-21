package io.takari.modello.editor.mapping.dom.accessor;

import io.takari.modello.editor.mapping.dom.DomMappingPlugin;
import io.takari.modello.editor.mapping.dom.accessor.PathParser.DomPath;
import io.takari.modello.editor.mapping.dom.impl.DomSection;
import io.takari.modello.editor.mapping.model.IModel;
import io.takari.modello.editor.mapping.model.IModelExtension;

public class ModelSectionAccessor extends BaseAccessor<IModelExtension> {
    
    private Class<? extends IModel> type;

    public ModelSectionAccessor(Class<? extends IModel> type, String name, String path) {
        super(name, path);
        this.type = type;
    }

    @Override
    public Object get(DomModelAccessor ctx, IModelExtension model) {
        DomMappingPlugin.trace("Get model " + property);
        return getPropertyData(ctx, model);
    }
    
    @Override
    public void touch(DomModelAccessor ctx, IModelExtension model) {
        ctx.getContainer(getPropertyData(ctx, model)).touch(ctx.getDomHelper());
    }

    @Override
    protected IModelExtension createPropertyData(DomModelAccessor ctx, IModelExtension model, DomPath path) {
        IModelExtension newModel = (IModelExtension) ctx.getProxyGenerator().createProxy(ctx, type, model, property);
        DomSection section = path.section(ctx.getContainer(model));
        
        ctx.setContainer(newModel, section);
        
        return newModel;
        
    }
}