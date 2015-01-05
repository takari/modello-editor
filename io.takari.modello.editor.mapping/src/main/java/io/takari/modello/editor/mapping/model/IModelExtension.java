package io.takari.modello.editor.mapping.model;

import java.util.Set;


public interface IModelExtension extends IModel {
    
    IModel getParent();
    
    void _setParent(IModelExtension model);
    
    String _getParentProperty();
    
    int _getIndex();
    
    void _setIndex(int index);
    
    Class<? extends IModel> _getModelClass();
    
    IModelExtension _getDelegate();
    
    Set<String> _getProperties();
    
    void _touch(String property);
    
    Object _get(String property);
    
    void _set(String property, Object value);
    
    Set<String> _getControlledListProperties();
    
    IListControl _getListControl(String property);
    
    void _firePropertyChange(String propertyName, Object oldValue, Object newValue);

    Object _getData(String key);

    void _setData(String key, Object data);
    
    void _apply(IModel model);
    
    void _copyFrom(IModel model);
    
    IModelExtension _createSubProxy(Class<? extends IModel> model);

}
