package com.amazon.ata.kindlepublishingservice.exceptions;

public class KindlePublishingClientException extends RuntimeException{
    private static final long serialVersionUID = -3468495131709324955L;
    public KindlePublishingClientException(String message) {
        super(message);
    }
    public KindlePublishingClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
