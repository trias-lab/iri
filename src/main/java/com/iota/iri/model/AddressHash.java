package com.iota.iri.model;

/**
 * AddressHash class for key in storage.
 */
public class AddressHash extends AbstractHash {

    protected AddressHash(byte[] bytes, int offset, int sizeInBytes) {
        super(bytes, offset, sizeInBytes);
    }
}
