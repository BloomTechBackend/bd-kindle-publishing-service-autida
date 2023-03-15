package com.amazon.ata.kindlepublishingservice;

import com.amazon.ata.kindlepublishingservice.dagger.ATAKindlePublishingServiceManager;
import com.amazon.ata.kindlepublishingservice.dagger.ApplicationComponent;
import com.amazon.ata.kindlepublishingservice.dagger.DaggerApplicationComponent;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Queue;

@SpringBootApplication
public class App {
    public static final ApplicationComponent component = DaggerApplicationComponent.create();
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

        ATAKindlePublishingServiceManager publishingManager = component.provideATAKindlePublishingServiceManager();
        try {
            publishingManager.start();
            System.out.println("Publishing manager running!");
        } catch (Exception e) {
            System.out.println("Something went wrong!");
            System.out.println(e);
        }
    }
}
