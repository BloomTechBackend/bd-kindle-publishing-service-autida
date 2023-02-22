package com.amazon.ata.kindlepublishingservice.publishing;

import javax.inject.Inject;
import java.util.*;

public class BookPublishRequestManager {
    Queue<BookPublishRequest> bookPublishRequestQueue;
    @Inject
    public BookPublishRequestManager() {
        bookPublishRequestQueue = new LinkedList<>();
    }
    public void addBookPublishRequest(BookPublishRequest bookPublishRequest) {
        bookPublishRequestQueue.add(bookPublishRequest);
    }
    public BookPublishRequest getBookPublishRequestToProcess() {
        if(!bookPublishRequestQueue.isEmpty()) {
            return bookPublishRequestQueue.peek();
        }
        return null;
    }
}
