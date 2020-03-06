package com.iota.iri.model;

/**
 * BundleHash as key in storage.
 */
public class BundleHash extends AbstractHash {

    protected BundleHash(byte[] bytes, int offset, int sizeInBytes) {
        super(bytes, offset, sizeInBytes);
    }
}
