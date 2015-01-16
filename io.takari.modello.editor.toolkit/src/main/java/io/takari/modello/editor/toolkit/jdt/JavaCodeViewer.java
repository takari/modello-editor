package io.takari.modello.editor.toolkit.jdt;

import io.takari.modello.editor.toolkit.ToolkitPlugin;

import java.util.Arrays;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jdt.internal.corext.util.CodeFormatterUtil;
import org.eclipse.jdt.internal.debug.ui.JDIContentAssistPreference;
import org.eclipse.jdt.internal.debug.ui.contentassist.IJavaDebugContentAssistContext;
import org.eclipse.jdt.internal.debug.ui.contentassist.JavaDebugContentAssistProcessor;
import org.eclipse.jdt.internal.debug.ui.contentassist.TypeContext;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.JavaSourceViewer;
import org.eclipse.jdt.internal.ui.text.java.JavaAutoIndentStrategy;
import org.eclipse.jdt.internal.ui.text.java.JavaStringAutoIndentStrategy;
import org.eclipse.jdt.internal.ui.text.java.SmartSemicolonAutoEditStrategy;
import org.eclipse.jdt.internal.ui.text.javadoc.JavaDocAutoIndentStrategy;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.DefaultLineTracker;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.TabsToSpacesConverter;
import org.eclipse.jface.text.WhitespaceCharacterPainter;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

@SuppressWarnings("restriction")
public class JavaCodeViewer extends JavaSourceViewer {
    
    private IHandlerService handlerService;
    
    private IHandler contentAssistHandler;
    private IHandler undoHandler;
    private IHandler redoHandler;
    
    protected IHandlerActivation contentAssistActivation;
    private IHandlerActivation undoActivation;
    private IHandlerActivation redoActivation;
    
    public JavaCodeViewer(Composite parent, int style) {
        super(parent, null, null, false, style, JavaPlugin.getDefault().getCombinedPreferenceStore());
        handlerService = (IHandlerService) PlatformUI.getWorkbench().getAdapter(IHandlerService.class);
    }
    
    public void init(IProject project, String contextTypeName) {
        
        IPreferenceStore preferenceStore = JavaPlugin.getDefault().getCombinedPreferenceStore();
        JavaTextTools tools= JavaPlugin.getDefault().getJavaTextTools();
        
        final IDocument document = new Document();
        tools.setupJavaDocumentPartitioner( document, IJavaPartitions.JAVA_PARTITIONING);
        setInput(document);
        
        final IJavaProject javaProject = JavaCore.create(project);
        
        IType javaType = null;
        
        if(javaProject != null && javaProject.exists()) {
            try {
                javaType = javaProject.findType(contextTypeName);
            } catch (JavaModelException e) {
                ToolkitPlugin.logException(e);
            }
            
            if(javaType == null) {
                try {
                    javaType = javaProject.findType("java.lang.Object");
                } catch (JavaModelException e) {
                    ToolkitPlugin.logException(e);
                }
            }
        }
        
        IJavaDebugContentAssistContext context = new TypeContext(javaType, -1);
        
        
        final IContentAssistProcessor completionProcessor = new JavaDebugContentAssistProcessor(context);
        SourceViewerConfiguration viewerConfiguration = new JavaSourceViewerConfiguration(tools.getColorManager(), preferenceStore, null, IJavaPartitions.JAVA_PARTITIONING) {
            public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
                ContentAssistant assistant = new ContentAssistant();
                assistant.setContentAssistProcessor(completionProcessor, IDocument.DEFAULT_CONTENT_TYPE);
                
                JDIContentAssistPreference.configure(assistant, getColorManager());
                
                assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
                assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
                
                return assistant;
            }
            
            @Override
            public int getTabWidth(ISourceViewer sourceViewer) {
                return CodeFormatterUtil.getTabWidth(javaProject);
            }
            
            public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
                String partitioning= getConfiguredDocumentPartitioning(sourceViewer);
                if (IJavaPartitions.JAVA_DOC.equals(contentType) || IJavaPartitions.JAVA_MULTI_LINE_COMMENT.equals(contentType))
                    return new IAutoEditStrategy[] { new JavaDocAutoIndentStrategy(partitioning) };
                else if (IJavaPartitions.JAVA_STRING.equals(contentType))
                    return new IAutoEditStrategy[] { new SmartSemicolonAutoEditStrategy(partitioning), new JavaStringAutoIndentStrategy(partitioning, javaProject) };
                else if (IJavaPartitions.JAVA_CHARACTER.equals(contentType) || IDocument.DEFAULT_CONTENT_TYPE.equals(contentType))
                    return new IAutoEditStrategy[] { new SmartSemicolonAutoEditStrategy(partitioning), new JavaAutoIndentStrategy(partitioning, javaProject, sourceViewer) };
                else
                    return new IAutoEditStrategy[] { new JavaAutoIndentStrategy(partitioning, javaProject, sourceViewer) };
            }
            
            @Override
            public String[] getIndentPrefixes(ISourceViewer sourceViewer, String contentType) {
                final int tabWidth= CodeFormatterUtil.getTabWidth(javaProject);
                final int indentWidth= CodeFormatterUtil.getIndentWidth(javaProject);
                boolean allowTabs= tabWidth <= indentWidth;

                String indentMode;
                if (javaProject == null)
                    indentMode= JavaCore.getOption(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR);
                else
                    indentMode= javaProject.getOption(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, true);

                boolean useSpaces= JavaCore.SPACE.equals(indentMode) || DefaultCodeFormatterConstants.MIXED.equals(indentMode);

                Assert.isLegal(allowTabs || useSpaces);

                if (!allowTabs) {
                    char[] spaces= new char[indentWidth];
                    Arrays.fill(spaces, ' ');
                    return new String[] { new String(spaces), "" }; //$NON-NLS-1$
                } else if  (!useSpaces)
                    return getIndentPrefixesForTab(tabWidth);
                else
                    return getIndentPrefixesForSpaces(tabWidth);
            }
            
            private String[] getIndentPrefixesForSpaces(int tabWidth) {
                String[] indentPrefixes= new String[tabWidth + 2];
                indentPrefixes[0]= getStringWithSpaces(tabWidth);

                for (int i= 0; i < tabWidth; i++) {
                    String spaces= getStringWithSpaces(i);
                    if (i < tabWidth)
                        indentPrefixes[i+1]= spaces + '\t';
                    else
                        indentPrefixes[i+1]= new String(spaces);
                }

                indentPrefixes[tabWidth + 1]= ""; //$NON-NLS-1$

                return indentPrefixes;
            }
            
            private String getStringWithSpaces(int count) {
                char[] spaceChars= new char[count];
                Arrays.fill(spaceChars, ' ');
                return new String(spaceChars);
            }
        };
        configure(viewerConfiguration);
        
        getTextWidget().setFont(JFaceResources.getFont(PreferenceConstants.EDITOR_TEXT_FONT));
        
        if(preferenceStore.getBoolean(AbstractTextEditor.PREFERENCE_SHOW_WHITESPACE_CHARACTERS)) {
            addPainter(new WhitespaceCharacterPainter(this));
        }
        
        int tabWidth = viewerConfiguration.getTabWidth(this);
        String formatterTabChar = null;
        if(javaProject != null) {
            formatterTabChar = javaProject.getOption(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, true);
        }
        if(formatterTabChar == null) {
            formatterTabChar = JavaCore.getOption(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR);
        }
        boolean spacesForTabs = JavaCore.SPACE.equals(formatterTabChar);
        
        if (spacesForTabs) {
            TabsToSpacesConverter tabToSpacesConverter= new TabsToSpacesConverter();
            tabToSpacesConverter.setNumberOfSpacesPerTab(tabWidth);
            tabToSpacesConverter.setLineTracker(new DefaultLineTracker());
            setTabsToSpacesConverter(tabToSpacesConverter);
            
            // update
            String[] types= viewerConfiguration.getConfiguredContentTypes(this);
            for (int i= 0; i < types.length; i++) {
                String[] prefixes= viewerConfiguration.getIndentPrefixes(this, types[i]);
                if (prefixes != null && prefixes.length > 0)
                    setIndentPrefixes(prefixes, types[i]);
            }
        }
        
        contentAssistHandler = new AbstractHandler() {
            public Object execute(ExecutionEvent event) throws org.eclipse.core.commands.ExecutionException {
                doOperation(ISourceViewer.CONTENTASSIST_PROPOSALS);
                return null;
            }
        };
        undoHandler = new AbstractHandler() {
            public Object execute(ExecutionEvent event) throws org.eclipse.core.commands.ExecutionException {
                doOperation(ITextOperationTarget.UNDO);
                return null;
            }
        };
        redoHandler = new AbstractHandler() {
            public Object execute(ExecutionEvent event) throws org.eclipse.core.commands.ExecutionException {
                doOperation(ITextOperationTarget.REDO);
                return null;
            }
        };
        getTextWidget().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                activateHandlers(); 
            }
            @Override
            public void focusLost(FocusEvent e) {
                deactivateHandlers(); 
            }
        });
        
        getControl().addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                deactivateHandlers(); 
            }
        });
        
        prependVerifyKeyListener(new VerifyKeyListener() {
            @Override
            public void verifyKey(VerifyEvent event) {
                if(event.character == '\t') {
                    
                    if(event.stateMask == SWT.SHIFT) {
                        // shift left
                        shift(false, false, true);
                        event.doit = false;
                    } else if(event.stateMask == 0) {
                        // shift right
                        if(canDoOperation(ITextOperationTarget.SHIFT_RIGHT)) {
                            shift(false, true, true);
                            event.doit = false;
                        }
                    }
                }
            }
        });
    }
    
    private void activateHandlers() {
        contentAssistActivation = handlerService.activateHandler(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS, contentAssistHandler);
        undoActivation = handlerService.activateHandler(IWorkbenchCommandConstants.EDIT_UNDO, undoHandler);
        redoActivation = handlerService.activateHandler(IWorkbenchCommandConstants.EDIT_REDO, redoHandler);
        
    }
    
    private void deactivateHandlers() {
        if (contentAssistActivation != null) {
            handlerService.deactivateHandler(contentAssistActivation);
            contentAssistActivation= null;
        }
        if (undoActivation != null) {
            handlerService.deactivateHandler(undoActivation);
            undoActivation= null;
        }
        if (redoActivation != null) {
            handlerService.deactivateHandler(redoActivation);
            redoActivation= null;
        }
    }
    
}
