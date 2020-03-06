package com.iota.iri.model.persistables;

import com.iota.iri.model.Hash;

/**
 * Tag class as hashes.
 */
public class Tag extends Hashes {
    public Tag(Hash hash) {
        set.add(hash);
    }

    public Tag() {

    }
}
