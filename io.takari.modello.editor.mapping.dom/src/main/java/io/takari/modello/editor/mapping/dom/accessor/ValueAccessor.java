package io.takari.modello.editor.mapping.dom.accessor;

import io.takari.modello.editor.mapping.dom.DomMappingPlugin;
import io.takari.modello.editor.mapping.dom.accessor.PathParser.DomPath;
import io.takari.modello.editor.mapping.dom.impl.DomValue;
import io.takari.modello.editor.mapping.model.IModelExtension;

public class ValueAccessor extends BaseAccessor<DomValue> {

    private Class<?> ptype;
    private VType type;
    private String defValue;

    protected ValueAccessor(Class<?> ptype, String property, String path, VType type, String defValue) {
        super(property, path);
        this.ptype = ptype;
        this.type = type;
        this.defValue = defValue;
    }

    @Override
    public Object get(DomModelAccessor ctx,  IModelExtension model) {
        Object result;
        if(String.class.isAssignableFrom(ptype)) {
            result = getPropertyData(ctx, model).getValue(ctx.getDomHelper());
        } else if(Boolean.class.isAssignableFrom(ptype) || boolean.class.isAssignableFrom(ptype)) {
            result = getPropertyData(ctx, model).getBoolean(ctx.getDomHelper());
        } else {
            throw new IllegalStateException("Unsupported value type " + ptype);
        }
        
        DomMappingPlugin.trace("Get value " + property + "=" + result);
        return result;
    }
    
    @Override
    public void set(DomModelAccessor ctx, IModelExtension model, Object object) {
        ctx.getSessionProvider().requestWrite();
        try {
            DomMappingPlugin.trace("Set value " + property + "=" + object);
            if(object instanceof Boolean) {
                getPropertyData(ctx, model).setValue(ctx.getDomHelper(), ((Boolean)object).booleanValue());
            } else {
                getPropertyData(ctx, model).setValue(ctx.getDomHelper(), object == null ? "" : object.toString());
            }
        } finally {
            ctx.getSessionProvider().release();
        }
        
        model._firePropertyChange(property, null, object);
    }
    
    @Override
    public void touch(DomModelAccessor ctx, IModelExtension model) {
        getPropertyData(ctx, model).touch(ctx.getDomHelper());
    }

    @Override
    protected DomValue createPropertyData(DomModelAccessor ctx, IModelExtension model, DomPath path) {
        DomValue val = null;
        switch(type) {
        case TEXT:
            val = path.text(getContainer(model));
            break;
        case CDATA:
            val = path.cdata(getContainer(model));
            break;
        case ATTR:
            val = path.attr(getContainer(model));
            break;
        }
        return val.def(defValue);
    }
    
    public enum VType {
        TEXT,
        CDATA,
        ATTR;
    }
}
