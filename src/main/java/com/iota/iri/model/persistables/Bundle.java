package com.iota.iri.model.persistables;

import com.iota.iri.model.Hash;

/**
 * Bundle as hashes.
 */
public class Bundle extends Hashes{
    public Bundle(Hash hash) {
        set.add(hash);
    }

    public Bundle() {

    }
}
