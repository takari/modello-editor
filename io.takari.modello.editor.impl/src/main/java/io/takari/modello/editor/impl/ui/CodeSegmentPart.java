package io.takari.modello.editor.impl.ui;

import io.takari.modello.editor.impl.model.AbstractType;
import io.takari.modello.editor.impl.model.MCodeSegment;
import io.takari.modello.editor.impl.model.MModel;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;
import io.takari.modello.editor.toolkit.jdt.JavaCodeViewer;
import io.takari.modello.editor.toolkit.model.AbstractModelBean;
import io.takari.modello.editor.toolkit.ui.AbstractEditorFormPart;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CodeSegmentPart extends AbstractEditorFormPart {

    private AbstractType type;
    private MCodeSegment codeSegment;
    private Text version;
    private StyledText comment;
    private JavaCodeViewer codeViewer;
    private StyledText code;
    
    public CodeSegmentPart(IDocumentEditor editor, AbstractType type, MCodeSegment codeSegment) {
        super(editor);
        this.type = type;
        this.codeSegment = codeSegment;
    }
    
    protected MModel getModel() {
        return (MModel) ((AbstractModelBean)type.getParent()).getParent();
    }
    
    @Override
    protected void createClient(Composite parent) {
        parent.setLayout(new GridLayout(2, false));
        
        Label lblVersion = getManagedForm().getToolkit().createLabel(parent, "Version", SWT.NONE);
        lblVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        
        version = getManagedForm().getToolkit().createText(parent, "", SWT.NONE);
        version.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblComment = getManagedForm().getToolkit().createLabel(parent, "Comment", SWT.NONE);
        lblComment.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        
        comment = new StyledText(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        comment.setAlwaysShowScrollBars(false);
        GridData gd_comment = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gd_comment.heightHint = 40;
        comment.setLayoutData(gd_comment);
        getManagedForm().getToolkit().adapt(comment);
        getManagedForm().getToolkit().paintBordersFor(comment);
        
        Label lblCode = getManagedForm().getToolkit().createLabel(parent, "Code", SWT.NONE);
        lblCode.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        
        codeViewer = new JavaCodeViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
        code = codeViewer.getTextWidget();
        
        GridData gd_code = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        gd_code.widthHint = 200;
        gd_code.heightHint = 40;
        code.setLayoutData(gd_code);
        getManagedForm().getToolkit().adapt(code);
        getManagedForm().getToolkit().paintBordersFor(code);
        
        configureSourceViewer();
    }
    
    @Override
    public void commit(boolean onSave) {
        codeSegment.setCode(codeViewer.getDocument().get());
        super.commit(onSave);
    }

    private void configureSourceViewer() {
        IProject project;
        
        IFile currentFile = getEditor().getFile();
        
        if(currentFile != null) {
            project = currentFile.getProject();
        } else {
            project = null;
        }
        
        String packageName = type.getPackageName();
        if(packageName.isEmpty()) packageName = getModel().getDetails().getDefaults().getDefaultPackage();
        String typeName = (packageName.isEmpty() ? "" : (packageName + ".")) + type.getName();
        
        codeViewer.init(project, typeName);
        
        codeViewer.getDocument().set(codeSegment.getCode());
    }
    
    @Override
    protected DataBindingContext createBindings() {
        return initDataBindings();
    }
    protected DataBindingContext initDataBindings() {
        DataBindingContext bindingContext = new DataBindingContext();
        //
        IObservableValue observeTextVersionObserveWidget = WidgetProperties.text(SWT.Modify).observe(version);
        IObservableValue versionCodeSegmentObserveValue = BeanProperties.value("version").observe(codeSegment);
        bindingContext.bindValue(observeTextVersionObserveWidget, versionCodeSegmentObserveValue, null, null);
        //
        IObservableValue observeTextCommentObserveWidget = WidgetProperties.text(SWT.Modify).observe(comment);
        IObservableValue commentCodeSegmentObserveValue = BeanProperties.value("comment").observe(codeSegment);
        bindingContext.bindValue(observeTextCommentObserveWidget, commentCodeSegmentObserveValue, null, null);
        //
        return bindingContext;
    }
}
