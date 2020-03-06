package com.iota.iri.model.persistables;

import com.iota.iri.model.Hash;

/**
 * ObsoleteTag as hashes.
 */
public class ObsoleteTag extends Tag {

    public ObsoleteTag(Hash hash) {
        super(hash);
    }

    // used by the persistence layer to instantiate the object
    public ObsoleteTag() {

    }
}
