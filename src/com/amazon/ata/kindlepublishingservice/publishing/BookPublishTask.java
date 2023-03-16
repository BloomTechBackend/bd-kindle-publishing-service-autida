package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.App;
import com.amazon.ata.kindlepublishingservice.dagger.DataAccessModule;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;

public class BookPublishTask implements Runnable {
    private PublishingStatusDao publishingStatusDao;
    private CatalogDao catalogDao;

    public BookPublishTask() {
        this.publishingStatusDao = App.component.providePublishingStatusDao();
        this.catalogDao = App.component.provideCatalogDao();
    }
    @Override
    public void run() {
        CatalogItemVersion catalogItemVersion = null;
        BookPublishRequest bookPublishRequest = App.component.provideBookPublishRequestManager().getBookPublishRequestToProcess();
        if (bookPublishRequest != null) {
            publishingStatusDao.setPublishingStatus(
                    bookPublishRequest.getPublishingRecordId(),
                    PublishingRecordStatus.IN_PROGRESS,
                    bookPublishRequest.getBookId());
            try {
                catalogItemVersion = catalogDao.createOrUpdateBook(KindleFormatConverter.format(bookPublishRequest));
                publishingStatusDao.setPublishingStatus(
                        bookPublishRequest.getPublishingRecordId(),
                        PublishingRecordStatus.SUCCESSFUL,
                        catalogItemVersion.getBookId());
            } catch (Exception e) {
                publishingStatusDao.setPublishingStatus(
                        bookPublishRequest.getPublishingRecordId(),
                        PublishingRecordStatus.FAILED,
                        catalogItemVersion.getBookId(),
                        "Book is not found in the catalog.");
            }
        }
    }
}

