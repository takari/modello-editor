package io.takari.modello.editor.impl.model;

import io.takari.modello.editor.mapping.annotations.Observe;
import io.takari.modello.editor.mapping.dom.annotations.XMLText;

public class MCodeSegment extends AbstractModelloModel {
    
    @XMLText("code")
    private String code;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    @Observe({"code", "comment"})
    @Override
    public String getLabelValue() {
        String code = getCode();
        if(code != null) {
            String[] lines = code.split("\n");
            for(String line: lines) {
                line = line.trim();
                if(line.isEmpty()) continue;
                if(line.startsWith("/*") || line.startsWith("//") || line.startsWith("*") || line.startsWith("@")) continue;
                return line;
            }
        }
        
        String comment = getComment();
        if(comment != null) {
            comment = comment.trim();
            String[] lines = code.split("\n");
            String line = lines[0];
            if(line.length() > 20) {
                line = line.substring(0, 17) + "...";
            }
            return line;
        }
        return "";
    }
}
