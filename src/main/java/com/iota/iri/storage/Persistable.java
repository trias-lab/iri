package com.iota.iri.storage;

import java.io.Serializable;

/**
 * Persistable interface as Value in DB.
 */
public interface Persistable extends Serializable {
    byte[] bytes();
    void read(byte[] bytes);
    byte[] metadata();
    void readMetadata(byte[] bytes);
    boolean merge();
}
