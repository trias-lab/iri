package com.iota.iri.model.persistables;

import com.iota.iri.model.Hash;

/**
 * Approvee as hashes.
 */
public class Approvee extends Hashes{
    public Approvee(Hash hash) {
        set.add(hash);
    }

    public Approvee() {

    }
}
