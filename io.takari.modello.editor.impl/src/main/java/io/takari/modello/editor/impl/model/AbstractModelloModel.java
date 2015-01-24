package io.takari.modello.editor.impl.model;

import io.takari.modello.editor.mapping.annotations.EditableList;
import io.takari.modello.editor.mapping.annotations.Observe;
import io.takari.modello.editor.mapping.dom.annotations.XMLCData;
import io.takari.modello.editor.mapping.dom.annotations.XMLList;
import io.takari.modello.editor.mapping.dom.annotations.XMLText;
import io.takari.modello.editor.toolkit.model.AbstractModelBean;

import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.TextStyle;

public abstract class AbstractModelloModel extends AbstractModelBean {
    
    @XMLText("name")
    private String name;
    
    @XMLText("version")
    private String version;
    
    @XMLCData("description")
    private String description;
    
    @XMLCData("comment")
    private String comment;
    
    @XMLList("annotations/annotation")
    private List<MAnnotation> annotations;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @EditableList("Annotation")
    public List<MAnnotation> getAnnotations() {
        return this.annotations;
    }
    
    public void setAnnotations(List<MAnnotation> annotations) {
        this.annotations = annotations;
    }
    
    @Observe("name")
    @Override
    public String getLabelValue() {
        String name = getName();
        return name == null ? "" : name;
    }
    
    @Observe({"labelValue", "version"})
    @Override
    public StyledString getLabel() {
        StyledString ss = new StyledString();
        String val = getLabelValue();
        String version = getVersion();
        
        if(version != null && !version.isEmpty()) {
            if(version.endsWith("+")) {
                ss.append(val).append("  ").append(version, STYLER_VERSION);
            } else {
                ss.append(val, STYLER_DEPRECATED).append("  ", STYLER_DEPRECATED).append(version, new MultiStyler(STYLER_VERSION, STYLER_DEPRECATED));
            }
        } else {
            ss.append(val);
        }
        
        return ss;
    }
    
    private static final Styler STYLER_VERSION = new Styler(){
        public void applyStyles(TextStyle textStyle) {
            textStyle.font = JFaceResources.getFontRegistry().getItalic("");
            textStyle.foreground = JFaceResources.getColorRegistry().get(COLOR_GRAY);
        }
    };
    
    private static final Styler STYLER_DEPRECATED = new Styler(){
        public void applyStyles(TextStyle textStyle) {
            textStyle.strikeout = true;
            textStyle.strikeoutColor = JFaceResources.getColorRegistry().get(COLOR_GRAY);
        }
    };
    
    private static final String COLOR_GRAY = AbstractModelloModel.class.getName() + "/gray";
    private static final String COLOR_LGRAY = AbstractModelloModel.class.getName() + "/lgray";
    
    static {
        JFaceResources.getColorRegistry().put(COLOR_GRAY, new RGB(0x5f, 0x5f, 0x5f));
        JFaceResources.getColorRegistry().put(COLOR_LGRAY, new RGB(0x8f, 0x8f, 0x8f));
    }
    
    public static class MultiStyler extends Styler {
        private Styler[] stylers;
        
        public MultiStyler(Styler ... stylers) {
            this.stylers = stylers;
        }
        
        @Override
        public void applyStyles(TextStyle textStyle) {
            for(Styler styler: stylers) {
                styler.applyStyles(textStyle);
            }
        }
    }
}
