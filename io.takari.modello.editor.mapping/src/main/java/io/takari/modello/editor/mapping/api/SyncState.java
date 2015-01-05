package io.takari.modello.editor.mapping.api;

public class SyncState {
    private volatile boolean valid = true;
    
    public void invalidate() {
        valid = false;
    }
    
    public boolean isValid() {
        return valid;
    }
}
