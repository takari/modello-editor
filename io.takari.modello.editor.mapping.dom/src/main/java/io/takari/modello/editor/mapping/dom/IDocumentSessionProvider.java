package io.takari.modello.editor.mapping.dom;

import io.takari.modello.editor.mapping.api.SyncState;

public interface IDocumentSessionProvider {

    void requestRead();

    void requestWrite();

    IDocumentSession currentSession();
    
    SyncState getSync();

    void release();

    void close();

    boolean isClosed();

}
