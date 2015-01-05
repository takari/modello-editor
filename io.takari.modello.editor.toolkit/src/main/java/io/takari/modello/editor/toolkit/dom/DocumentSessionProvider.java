package io.takari.modello.editor.toolkit.dom;

import java.util.Deque;
import java.util.LinkedList;

import io.takari.modello.editor.mapping.api.SyncState;
import io.takari.modello.editor.mapping.dom.IDocumentSession;
import io.takari.modello.editor.mapping.dom.IDocumentSessionProvider;
import io.takari.modello.editor.toolkit.editor.IDocumentEditor;

public class DocumentSessionProvider implements IDocumentSessionProvider {
    
    private final IDocumentEditor editor;
    private final Deque<State> states;
    private boolean closed;

    public DocumentSessionProvider(IDocumentEditor editor) {
        this.editor = editor;
        states = new LinkedList<>();
    }
    
    public SyncState getSync() {
        return editor.getSync();
    }
    
    public void requestRead() {
        request(Mode.READ);
    }
    public void requestWrite() {
        request(Mode.WRITE);
    }
    
    private void request(Mode mode) {
        State curState = states.peek();
        if(curState != null) {
            if(mode.ordinal() <= curState.mode.ordinal()) {
                curState.count++;
                return;
            }
            
            // upgrade
            if(curState.session != null) {
                curState.session.close();
                curState.session = null;
            }
        }
        
        states.push(new State(mode));
        
        return;
    }
    
    public void release() {
        State curState = states.peek();
        if(curState == null) return;
        
        curState.count--;
        if(curState.count <= 0) {
            
            if(curState.session != null) {
                curState.session.close();
            }
            
            states.pop();
        }
    }
    
    @Override
    public void close() {
        closed = true;
    }
    
    public boolean isClosed() {
        return closed;
    }
    
    public IDocumentSession currentSession() {
        State curState = states.peek();
        if(curState == null) throw new IllegalStateException("Session not opened");
        
        if(curState.session == null) {
            
            switch(curState.mode) {
            case READ:
                curState.session = new DocumentSession(editor, true);
                break;
            case WRITE:
                curState.session = new DocumentSession(editor, false);
                break;
            }
            
        }
        return curState.session;
    }
    
    private class State {
        int count = 1;
        Mode mode;
        DocumentSession session;
        
        State(Mode mode) {
            this.mode = mode;
        }
        
        public String toString() {
            return mode + "[" + count + "]";
        }
    }
    
    private enum Mode {
        READ,
        WRITE;
    }
}
