package com.iota.iri.model.persistables;

import com.iota.iri.model.Hash;

/**
 * Address as hashes
 */
public class Address extends Hashes{
    public Address(){}
    public Address(Hash hash) {
        set.add(hash);
    }
}
