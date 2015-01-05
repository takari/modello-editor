package io.takari.modello.editor.toolkit.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tracker;
import org.eclipse.ui.forms.widgets.SharedScrolledComposite;

public final class WidgetUtils {
    
    // see https://bugs.eclipse.org/bugs/show_bug.cgi?id=218483
    public static final int COLUMN_TRIM;
    static {
        String platform= SWT.getPlatform();
        if ("win32".equals(platform)) //$NON-NLS-1$
            COLUMN_TRIM= 4;
        else if ("carbon".equals(platform)) //$NON-NLS-1$
            COLUMN_TRIM= 24;
        else
            COLUMN_TRIM= 3;
    }
    
    public static void configureTableColumns(final Table table) {
        table.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                //Point size = table.getSize();
                int tableWidth = table.getClientArea().width; //size.x;
                
                TableColumn[] cols = table.getColumns();
                TableColumn lastColumn = cols[cols.length - 1];
                
                int fixedWidth = 0;
                for(TableColumn col: cols) {
                    if(col != lastColumn) {
                        fixedWidth += col.getWidth();
                    }
                }
                
                int remainingWidth = tableWidth - fixedWidth; // - SWTUtil.COLUMN_TRIM;
                if(remainingWidth < 32) remainingWidth = 32;
                
                lastColumn.setWidth(remainingWidth);
            }
        });
    }
    
    public static void makeResizable(Scrollable comp) {
        
        GridData gd = (GridData) comp.getLayoutData();
        if(gd != null) {
            if(gd.grabExcessVerticalSpace) {
                return;
            }
        }
        
        new ResizableHandle(comp);
    }
    
    
    private static class ResizableHandle extends Canvas {

        private static final int HSIZE = 12;
        
        private Scrollable target;
        
        public ResizableHandle(final Scrollable target) {
            super(target.getParent(), SWT.NONE);
            this.target = target;
            this.setBackground(target.getBackground());
            
            GridData gd = new GridData();
            gd.exclude = true;
            setLayoutData(gd);
            setCursor(getDisplay().getSystemCursor(SWT.CURSOR_SIZESE));

            addPaintListener(new PaintListener() {
                @Override
                public void paintControl(PaintEvent e) {
                    // draw handle
                    e.gc.setAntialias(SWT.ON);
                    e.gc.setAlpha(96);
                    e.gc.drawLine(HSIZE, 0, 0, HSIZE);
                    e.gc.drawLine(HSIZE, 4, 4, HSIZE);
                    e.gc.drawLine(HSIZE, 8, 8, HSIZE);
                }
            });
            
            addListener(SWT.MouseDown, new Listener() {
                public void handleEvent(Event e) {
                    Rectangle rect = target.getBounds();
                    
                    final Tracker tracker = new Tracker(getParent(), SWT.RESIZE | SWT.DOWN | SWT.RIGHT);
                    tracker.setStippled(true);
                    final int width = rect.width;
                    tracker.setRectangles(new Rectangle[] { rect });
                    tracker.addControlListener(new ControlAdapter() {
                        @Override
                        public void controlResized(ControlEvent e) {
                            Rectangle after = tracker.getRectangles()[0];
                            after.width = width;
                            tracker.setRectangles(new Rectangle[] { after });
                            
                            int heightWithTrim = target.computeTrim(0, 0, after.width, after.height).height;
                            int newHeight = after.height - (heightWithTrim - after.height);
                            
                            // XXX support other layouts
                            GridData gd = (GridData) target.getLayoutData();
                            gd.heightHint = newHeight;
                        }
                    });
                    if (tracker.open()) {
                        reflow(getParent());
                    }
                    tracker.dispose();
                }
            });
            
            target.addControlListener(new ControlListener() {
                @Override
                public void controlResized(ControlEvent e) {
                    moveLabel();
                }
                @Override
                public void controlMoved(ControlEvent e) {
                    moveLabel();
                }
            });
            
            moveLabel();
        }
        
        private void moveLabel() {
            Rectangle thisBounds = target.getBounds();
            Rectangle thisCA = target.getClientArea();
            
            int add = COLUMN_TRIM/2;
            
            setBounds(thisBounds.x + add + thisCA.width - HSIZE, thisBounds.y + add + thisCA.height - HSIZE, HSIZE, HSIZE);
            moveAbove(target);
        }
        
        private void reflow(Composite comp) {
            Composite c = comp;
            while (c != null) {
                c.setRedraw(false);
                c = c.getParent();
                if (c instanceof SharedScrolledComposite || c instanceof Shell) {
                    break;
                }
            }
            c = comp;
            while (c != null) {
                c.layout(true);
                c = c.getParent();
                if (c instanceof SharedScrolledComposite) {
                    ((SharedScrolledComposite) c).reflow(true);
                    break;
                }
            }
            c = comp;
            while (c != null) {
                c.setRedraw(true);
                c = c.getParent();
                if (c instanceof SharedScrolledComposite || c instanceof Shell) {
                    break;
                }
            }
        }
        
    }


    // prevent scrollable composites with inactive vbars from swallowing mouse wheel events
    public static void configureMouseWheel(final Composite composite) {
        composite.addListener(SWT.MouseWheel, new Listener() {
            @Override
            public void handleEvent(Event event) {
                
                if(composite.getVerticalBar() != null && !composite.getVerticalBar().isVisible()) {
                    // find outer scrolled composite
                    Composite c = composite.getParent();
                    while (c != null) {
                        ScrollBar vb = c.getVerticalBar();
                        if(vb != null && vb.isVisible()) {
                            vb.setSelection(vb.getSelection() - event.count * vb.getIncrement());
                            vb.notifyListeners(SWT.Selection, event);
                            c.notifyListeners(SWT.MouseWheel, event);
                            break;
                        }
                        c = c.getParent();
                    }
                    event.doit = false;
                }
            }
        });
    }
    
}
