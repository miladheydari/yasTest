package com.yas.interactors.remote.error;

import retrofit2.Response;


public class GeneralApiException extends Exception {
    public int code;
    public String message;
    private final transient Response<?> response;


    public GeneralApiException(Response<?> response) {
        this.response = response;
    }

    /** HTTP status code. */
    public int code() {
        return code;
    }

    /** HTTP status message. */
    public String message() {
        return message;
    }

    /**
     * The full HTTP response. This may be null if the exception was serialized.
     */
    public Response<?> response() {
        return response;
    }
}
