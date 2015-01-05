package io.takari.modello.editor.toolkit.dom;

import io.takari.modello.editor.mapping.dom.IDocumentSession;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

import java.util.List;

import org.eclipse.jface.text.DocumentRewriteSession;
import org.eclipse.jface.text.DocumentRewriteSessionType;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.undo.IStructuredTextUndoManager;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@SuppressWarnings("restriction")
public class DocumentSession implements IDocumentSession {
    
    private final IDocumentEditor editor;
    private final XSDHelper xsdHelper;
    
    private IDOMModel model;
    private IStructuredTextUndoManager undo;
    private DocumentRewriteSession session;
    private boolean readOnly;
    
    DocumentSession(IDocumentEditor editor, boolean readOnly) {
        this.editor = editor;
        this.readOnly = readOnly;
        
        open();
        
        xsdHelper = new XSDHelper(model);
    }
    
    @Override
    public Document getDocument() {
        if(model == null) throw new IllegalStateException("Model not open");
        return model.getDocument();
    }
    

    @Override
    public List<String> getAvailableContent(Element node) {
        return xsdHelper.getElementNames(node);
    }
    
    @Override
    public void format(Node newNode) {
        FormatProcessorXML formatProcessor = new FormatProcessorXML();
        //ignore any line width settings, causes wrong formatting of <foo>bar</foo>
        formatProcessor.getFormatPreferences().setLineWidth(2000);
        formatProcessor.formatNode(newNode);
    }


    @Override
    public void contentChanged(Node node) {
        if(node instanceof IndexedRegion) {
            IndexedRegion region = (IndexedRegion) node;
            editor.select(region.getStartOffset(), 0);
        }
    }
    
    @Override
    public void rollback() {
        if(undo != null) {
            undo.undo();
        }
    }

    void open() {
        
        IModelManager modelManager = StructuredModelManager.getModelManager();
        
        if(readOnly) {
            model = (IDOMModel) modelManager.getExistingModelForRead(editor.getDocument());
            if(model == null) {
                model = (IDOMModel) modelManager.getModelForRead((IStructuredDocument) editor.getDocument());
            }
            
        } else {
            model = (IDOMModel) modelManager.getExistingModelForEdit(editor.getDocument());
            if(model == null) {
                model = (IDOMModel) modelManager.getModelForEdit((IStructuredDocument) editor.getDocument());
            }
            
            IStructuredDocument structDoc = model.getStructuredDocument();
            model.aboutToChangeModel();
            
            //let the document know we make changes
            if(structDoc instanceof IDocumentExtension4) {
              IDocumentExtension4 ext4 = (IDocumentExtension4) structDoc;
              session = ext4.startRewriteSession(DocumentRewriteSessionType.UNRESTRICTED_SMALL);
            }
            undo = structDoc.getUndoManager();
            undo.beginRecording(model);
            
        }
    }
    
    void close() {
        if(readOnly) {
            // read
            model.releaseFromRead();
            
        } else {
            if(undo != null) {
                undo.endRecording(model);
                
                if(session != null) {
                  IDocumentExtension4 ext4 = (IDocumentExtension4) model.getStructuredDocument();
                  ext4.stopRewriteSession(session);
                }
                
                model.changedModel();
                model.releaseFromEdit();
                editor.synchronize();
            }
            
        }
        
        undo = null;
        session = null;
        model = null;
    }
}
