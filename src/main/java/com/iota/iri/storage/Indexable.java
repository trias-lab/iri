package com.iota.iri.storage;

import java.io.Serializable;

/**
 * Indexable interface as Key in DB.
 */
public interface Indexable extends Comparable<Indexable>, Serializable {
    byte[] bytes();
    void read(byte[] bytes);
    Indexable incremented();
    Indexable decremented();
}
