package io.takari.modello.editor.impl.model.plugin;

import io.takari.modello.editor.impl.ModelloPlugin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class MetadataPlugins {
    
    public static List<MetadataPluginRef> readMetadataPlugins() {
        List<MetadataPluginRef> plugins = new ArrayList<MetadataPluginRef>();

        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint mappingsExtensionPoint = registry.getExtensionPoint("io.takari.modello.editor.modelloPlugin");
        if(mappingsExtensionPoint != null) {
          IExtension[] mappingsExtensions = mappingsExtensionPoint.getExtensions();
          for(IExtension extension : mappingsExtensions) {
            IConfigurationElement[] elements = extension.getConfigurationElements();
            for(IConfigurationElement element : elements) {
              if(element.getName().equals("plugin")) { //$NON-NLS-1$
                try {
                  plugins.add(new MetadataPluginRef(element.getAttribute("name"), (IMetadataPlugin) element.createExecutableExtension("class")));
                } catch(CoreException ex) {
                  ModelloPlugin.logException(ex.getMessage(), ex);
                }
              }
            }
          }
        }

        return plugins;
      }
    
    
    public static class MetadataPluginRef {
        private final String name;
        private final IMetadataPlugin plugin;
        
        private MetadataPluginRef(String name, IMetadataPlugin plugin) {
            this.name = name;
            this.plugin = plugin;
        }
        
        public String getName() {
            return this.name;
        }
        
        public IMetadataPlugin getPlugin() {
            return this.plugin;
        }
    }
}
