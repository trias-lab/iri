package com.iota.iri.controllers;

import com.iota.iri.model.Hash;
import com.iota.iri.model.persistables.RawTransaction;
import com.iota.iri.storage.Indexable;
import com.iota.iri.storage.Persistable;
import com.iota.iri.storage.Tangle;
import com.iota.iri.utils.Pair;

import java.util.Set;

public class RawTransactionHashViewModel implements HashesViewModel {
    private RawTransaction self;
    private Indexable hash;

    public RawTransactionHashViewModel(Hash hash) {
        this.hash = hash;
    }

    private RawTransactionHashViewModel(RawTransaction hashes, Indexable hash) {
        self = hashes == null || hashes.set == null ? new RawTransaction(): hashes;
        this.hash = hash;
    }

    public static RawTransactionHashViewModel load(Tangle tangle, Indexable hash) throws Exception {
        return new RawTransactionHashViewModel((RawTransaction) tangle.load(RawTransaction.class, hash), hash);
    }

    public boolean store(Tangle tangle) throws Exception {
        return tangle.save(self, hash);
    }

    public int size() {
        return self.set.size();
    }

    public boolean addHash(Hash theHash) {
        return getHashes().add(theHash);
    }

    public Indexable getIndex() {
        return hash;
    }

    public Set<Hash> getHashes() {
        return self.set;
    }
    @Override
    public void delete(Tangle tangle) throws Exception {
        tangle.delete(RawTransaction.class,hash);
    }

    public static RawTransactionHashViewModel first(Tangle tangle) throws Exception {
        Pair<Indexable, Persistable> bundlePair = tangle.getFirst(RawTransaction.class, Hash.class);
        if(bundlePair != null && bundlePair.hi != null) {
            return new RawTransactionHashViewModel((RawTransaction) bundlePair.hi, (Hash) bundlePair.low);
        }
        return null;
    }

    public RawTransactionHashViewModel next(Tangle tangle) throws Exception {
        Pair<Indexable, Persistable> bundlePair = tangle.next(RawTransaction.class, hash);
        if(bundlePair != null && bundlePair.hi != null) {
            return new RawTransactionHashViewModel((RawTransaction) bundlePair.hi, (Hash) bundlePair.low);
        }
        return null;
    }
}
