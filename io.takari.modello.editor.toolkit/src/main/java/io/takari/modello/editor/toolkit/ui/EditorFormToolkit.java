package io.takari.modello.editor.toolkit.ui;

import java.util.ArrayList;
import java.util.List;

import io.takari.modello.editor.toolkit.util.WidgetUtils;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class EditorFormToolkit extends FormToolkit {

    private List<Control> selectables  = new ArrayList<Control>();
    
    public EditorFormToolkit(Display display) {
        super(display);
    }
    
    @Override
    public void adapt(Composite composite) {
        super.adapt(composite);
        customize(composite);
    }
    
    @Override
    public void adapt(Control control, boolean trackFocus, boolean trackKeyboard) {
        super.adapt(control, trackFocus, trackKeyboard);
        customize(control);
    }
    
    
    @Override
    public Text createText(Composite parent, String value, int style) {
        Text text = super.createText(parent, value, style);
        customize(text);
        return text;
    }

    private void customize(Control control) {
        if(control instanceof Table) {
            WidgetUtils.configureTableColumns((Table) control);
        }
        
        if(control instanceof StyledText || 
                control instanceof Table || 
                control instanceof org.eclipse.swt.widgets.List || 
                control instanceof Tree) {
            
            WidgetUtils.makeResizable((Scrollable) control);
        }
        
        if(control instanceof StyledText || 
                control instanceof Table || 
                control instanceof Tree) {
            
            WidgetUtils.configureMouseWheel((Composite) control);
        }
        
        // StyledTexts maintain independents selections, make sure only one of text controls has anything selected at a time
        if(control instanceof Text || control instanceof StyledText || control instanceof Combo) {
            selectables.add(control);
            control.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    clearSelections(e.getSource());
                }
            });
            control.addDisposeListener(new DisposeListener() {
                @Override
                public void widgetDisposed(DisposeEvent e) {
                    selectables.remove(e.widget);
                }
            });
        }
        
        AbstractEditorFormPart.registerWritableControls(control);
    }
    
    private void clearSelections(Object source) {
        for(Control ctrl: selectables) {
            if(ctrl instanceof Text) {
                ((Text) ctrl).clearSelection();
            } else if(ctrl instanceof Combo) {
                ((Combo) ctrl).clearSelection();
            } else if(ctrl instanceof StyledText) {
                StyledText st = (StyledText) ctrl;
                st.setSelection(st.getSelection().x);
            }
        }
    }
    
    
}
