package com.iota.iri.pluggables.tee;

import java.util.*;

public class TEE {
    String attester;
    String attestee;
    int    score;
    String ts;
    int nonce;
    String address;
    String sign;

    public String getDigetst() {
        return "[" + attester + "," + attestee + "]";
    }
}

