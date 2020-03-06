package com.iota.iri.model.persistables;

import com.iota.iri.hash.Sponge;
import com.iota.iri.hash.SpongeFactory;
import com.iota.iri.model.Hash;
import com.iota.iri.model.HashFactory;
import com.iota.iri.utils.Converter;

import static com.iota.iri.model.Hash.SIZE_IN_TRITS;

/**
 * RawTransaction as hashes.
 */
public class RawTransaction extends Hashes {
    public RawTransaction(){}
    public RawTransaction(Hash hash) {
        set.add(hash);
    }
}
