package io.takari.modello.editor.mapping.dom.accessor;

import io.takari.modello.editor.mapping.dom.impl.DomList;
import io.takari.modello.editor.mapping.dom.impl.DomSection;
import io.takari.modello.editor.mapping.dom.impl.DomValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathParser {
    
    /*
     * root/elem/something
     * root/listvalues/listvalue
     * root/mapvalues/mapvalue[keytag=>valuetag]
     */
    private static final Pattern P_PATH = Pattern.compile("([^\\[\\]]+)(?:\\[(\\w+)])?");
    
    public static DomPath parse(String text) {
        
        if(text.isEmpty()) {
            return new DomPath(null, "", null);
        }
        
        DomPath domPath = null;
        
        String[] parts = text.split("/");
        for(String path: parts) {
            path = path.trim();
            
            Matcher m = P_PATH.matcher(path);
            if(!m.matches()) {
                throw new IllegalArgumentException("Wrong `" + path + "` in `" + text + "`");
            }
            String name = m.group(1);
            String key = m.group(2);
            
            domPath = new DomPath(domPath, name, key);
        }
        
        return domPath;
    }
    
    public static class DomPath {

        private DomPath parent;
        private String path;
        private String key;

        public DomPath(DomPath parent, String path, String key) {
            this.parent = parent;
            this.path = path;
            this.key = key;
        }
        
        protected DomSection parentSection(DomSection context) {
            if(parent != null) {
                return parent.section(context);
            }
            return context;
        }
        
        public DomSection section(DomSection context) {
            if(key != null) {
                return parentSection(context).map(path, key);
            }
            
            return parentSection(context).section(path);
        }
        
        public DomValue text(DomSection context) {
            if(path.isEmpty()) {
                return parentSection(context).text();
            }
            return parentSection(context).text(path);
        }
        
        public DomValue cdata(DomSection context) {
            if(path.isEmpty()) {
                return parentSection(context).cdata();
            }
            return parentSection(context).cdata(path);
        }
        
        public DomValue attr(DomSection context) {
            notEmpty();
            return parentSection(context).attr(path);
        }
        
        public DomList list(DomSection context) {
            notEmpty();
            return parentSection(context).list(path);
        }

        private void notEmpty() {
            if(path.isEmpty()) throw new IllegalArgumentException("Only section mappings are allowed specify empty path");
        }
        
    }
}
