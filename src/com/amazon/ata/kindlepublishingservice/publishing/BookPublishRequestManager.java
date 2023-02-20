package com.amazon.ata.kindlepublishingservice.publishing;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BookPublishRequestManager {
    Queue<BookPublishRequest> bookPublishRequestStack;
    @Inject
    public BookPublishRequestManager() {
        bookPublishRequestStack = new LinkedList<>();
    }
    public void addBookPublishRequest(BookPublishRequest bookPublishRequest) {
        bookPublishRequestStack.add(bookPublishRequest);
    }
    public BookPublishRequest getBookPublishRequestToProcess() {
        if(!bookPublishRequestStack.isEmpty()) {
            bookPublishRequestStack.peek();
        }
        return null;
    }
}
