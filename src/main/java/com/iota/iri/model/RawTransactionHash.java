package com.iota.iri.model;

import com.iota.iri.hash.Sponge;
import com.iota.iri.hash.SpongeFactory;
import com.iota.iri.model.persistables.RawTransaction;
import com.iota.iri.utils.Converter;

public class RawTransactionHash extends AbstractHash {
    public RawTransactionHash(byte[] bytes, int offset, int sizeInBytes) {
        super(bytes, offset, sizeInBytes);
    }

    public static RawTransactionHash calculate(SpongeFactory.Mode mode, byte[] trits) {
        return calculate(trits, 0, trits.length, SpongeFactory.create(mode));
    }

    public static RawTransactionHash calculate(byte[] bytes, int tritsLength, Sponge sponge) {
        byte[] trits = new byte[tritsLength];
        Converter.getTrits(bytes, trits);
        return calculate(trits, 0, tritsLength, sponge);
    }

    public static RawTransactionHash calculate(byte[] tritsToCalculate, int offset, int length, Sponge sponge) {
        byte[] hashTrits = new byte[SIZE_IN_TRITS];
        sponge.reset();
        sponge.absorb(tritsToCalculate, offset, length);
        sponge.squeeze(hashTrits, 0, SIZE_IN_TRITS);
        return (RawTransactionHash) HashFactory.RAWTRANSACTION.create(hashTrits, 0, SIZE_IN_TRITS);
    }
}
