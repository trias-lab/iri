package com.iota.iri.service.dto;

/**
 * IXIResponse class.
 */
public class IXIResponse extends AbstractResponse {
    private Object ixi;

    public static IXIResponse create(Object myixi) {
        IXIResponse ixiResponse = new IXIResponse();
        ixiResponse.ixi = myixi;
        return ixiResponse;
    }

    public Object getResponse() {
        return ixi;
    }
}
