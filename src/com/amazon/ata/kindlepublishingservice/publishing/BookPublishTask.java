package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.App;
import com.amazon.ata.kindlepublishingservice.dagger.DataAccessModule;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;

public class BookPublishTask implements Runnable {
    private final DataAccessModule dataAccessModule;

    public BookPublishTask() {
        this.dataAccessModule = new DataAccessModule();
    }
    @Override
    public void run() {
        System.out.println("BookPublishTask run() called!");
        CatalogItemVersion catalogItemVersion = null;
        BookPublishRequest bookPublishRequest = App.component.provideBookPublishRequestManager().getBookPublishRequestToProcess();
        if (bookPublishRequest == null) {
            return;
        } else {
            dataAccessModule.providePublishingStatusDao().setPublishingStatus(
                    bookPublishRequest.getPublishingRecordId(),
                    PublishingRecordStatus.IN_PROGRESS,
                    bookPublishRequest.getBookId());
            //Format book
            KindleFormattedBook kindleFormattedBook = KindleFormatConverter.format(bookPublishRequest);
            try {
                catalogItemVersion = dataAccessModule.provideCatalogDao().createOrUpdateBook(kindleFormattedBook);
            } catch (Exception e) {
                dataAccessModule.providePublishingStatusDao().setPublishingStatus(
                        bookPublishRequest.getPublishingRecordId(),
                        PublishingRecordStatus.FAILED,
                        bookPublishRequest.getBookId(),
                        "Book is not found in the catalog.");
            }
//            System.out.println(String.format("Above success %s,", bookPublishRequest.getBookId()));
            dataAccessModule.providePublishingStatusDao().setPublishingStatus(
                    bookPublishRequest.getPublishingRecordId(),
                    PublishingRecordStatus.SUCCESSFUL,
                    catalogItemVersion.getBookId());
//        }
        }
    }
}

