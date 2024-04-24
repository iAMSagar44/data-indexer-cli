package com.example.dataloader.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.azure.storage.blob.BlobClient;

@Service
public class BlobUpload {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlobUpload.class);
    private final BlobClient blobClient;

    public BlobUpload(BlobClient blobClient) {
        this.blobClient = blobClient;
    }

    public void uploadFile(String localFilePath) {
        LOGGER.info("Uploading file {} to Blob Storage {}", localFilePath, blobClient.getContainerName());
        blobClient.uploadFromFile(localFilePath, true);
    }
}
