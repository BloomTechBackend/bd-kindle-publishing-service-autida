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
        CatalogItemVersion catalogItemVersion = null;
        BookPublishRequest bookPublishRequest = App.component.provideBookPublishRequestManager().getBookPublishRequestToProcess();
        if (bookPublishRequest != null) {
            dataAccessModule.providePublishingStatusDao().setPublishingStatus(
                    bookPublishRequest.getPublishingRecordId(),
                    PublishingRecordStatus.IN_PROGRESS,
                    bookPublishRequest.getBookId());
            try {
                catalogItemVersion = dataAccessModule.provideCatalogDao().createOrUpdateBook(KindleFormatConverter.format(bookPublishRequest));
                dataAccessModule.providePublishingStatusDao().setPublishingStatus(
                        bookPublishRequest.getPublishingRecordId(),
                        PublishingRecordStatus.SUCCESSFUL,
                        catalogItemVersion.getBookId());
            } catch (Exception e) {
                dataAccessModule.providePublishingStatusDao().setPublishingStatus(
                        bookPublishRequest.getPublishingRecordId(),
                        PublishingRecordStatus.FAILED,
                        catalogItemVersion.getBookId(),
                        "Book is not found in the catalog.");
            }
        }
    }
}

