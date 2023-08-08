package com.mangareader.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.mangareader.exception.BadRequestException;
import com.mangareader.exception.DataAlreadyExistsException;
import com.mangareader.exception.StorageException;
import com.mangareader.service.IStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
@SuppressWarnings("unused")
public class AWSStorageService implements IStorageService {
    @Value("${application.bucket.name}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;


    @Override
    public Resource loadAsResource(String filename, String location) {
        return null;
    }


    @Async
    public String uploadImage(MultipartFile file, String folderPath, int id) throws TimeoutException {

        if (checkFileFormat(file)) {
            return uploadFile(file, folderPath, id);
        } else {
            throw new BadRequestException("File is not image");
        }
    }

    @Async
    public List<String> uploadMultiImage(MultipartFile[] files, String folderPath) throws TimeoutException {
        if (files != null) {
            for (MultipartFile file : files) {
                if (!checkFileFormat(file)) {
                    throw new BadRequestException("Some file is not image. Check again.");
                }
            }

            return uploadMultiFile(files, folderPath);
        } else {
            log.error("Input images is null");
            throw new BadRequestException("File is null");
        }
    }

    @Async
    public List<String> uploadMultiFile(MultipartFile[] files, String folderPath) throws TimeoutException {
        if (files == null) {
            log.error("Input Multi file is null");
            throw new BadRequestException("File is null");
        }

        if (s3Client.doesObjectExist(bucketName, folderPath)) {
            deleteFolder(folderPath);
        }

        List<String> result = new ArrayList<>();

//        for (MultipartFile file : files) {
//            if (file != null && !file.isEmpty()) {
//                result.add(uploadFile(file, folderPath));
//            }
//        }

        try {
            for (int index = 1; index <= files.length; index++) {

                MultipartFile file = files[index - 1];

                if (file != null && !file.isEmpty()) {

                    result.add(uploadFile(file, folderPath, index));
                }
            }
        } catch (Exception e) {
            throw new StorageException("Failed to store file");
        }

        return result;
    }

    @Async
    public String uploadFile(MultipartFile file, String folderPath, int id) throws TimeoutException {
        if (file == null || file.isEmpty())
            throw new BadRequestException("File is null or empty.");
        String fileName = folderPath + file.getOriginalFilename();

        //Check if file exists or not
        if (s3Client.doesObjectExist(bucketName, fileName)) {
            throw new DataAlreadyExistsException("This file has already existed.");
        }

        File fileObj = convertMultipartFileToFile(file);
        //create folder if not exist
        if (!s3Client.doesObjectExist(bucketName, folderPath)) {
            s3Client.putObject(bucketName, folderPath, "");
            //wait until folder is created
            waitForS3FolderToExist(bucketName, folderPath);
        }

        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        fileName = folderPath + id + fileExtension;
//        fileName = folderPath + file.getOriginalFilename();

        //delete if object exists
//        if (s3Client.doesObjectExist(bucketName, fileName)) {
//            s3Client.deleteObject(bucketName, fileName);
//            waitForObjectDeletion(bucketName, fileName);
//        }

        s3Client.putObject(bucketName, fileName, fileObj);
        fileObj.delete();
        //wait until file is created
        waitForS3FolderToExist(bucketName, fileName);
        return s3Client.getUrl(bucketName, fileName).toString();
    }

    @Async
    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Async
    public void deleteFile(String fileName) {
        if (s3Client.doesObjectExist(bucketName, fileName)) {
            s3Client.deleteObject(bucketName, fileName);
            waitForObjectDeletion(bucketName, fileName);
        } /*else {
            throw new ResourceNotFoundException("File does not exist.");
        }*/
    }

    //need to check later
    @Async
    public void deleteFolder(String prefix) {

        if (!s3Client.doesObjectExist(bucketName, prefix)) {
            System.out.println("Folder " + prefix + " does not exist in S3 bucket " + bucketName);
            return;
        }

        ObjectListing objectListing = s3Client.listObjects(bucketName, prefix);

        while (true) {
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                s3Client.deleteObject(bucketName, objectSummary.getKey());
                waitForObjectDeletion(bucketName, objectSummary.getKey());
            }
            if (objectListing.isTruncated()) {
                objectListing = s3Client.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }

        s3Client.deleteObject(bucketName, prefix);
        waitForObjectDeletion(bucketName, prefix);
        System.out.println("Folder " + prefix + " deleted successfully from S3 bucket " + bucketName);
    }

    @Async
    private File convertMultipartFileToFile(MultipartFile file) {
        if (file == null)
            return null;

        File convertedFile;
        if (file.getOriginalFilename() == null) {
            throw new BadRequestException("File does not have name.");
        }
        convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (Exception e) {
            log.error("Error converting multipart file to file.", e);
            throw new BadRequestException("Error converting multipart file to file.");
        }
        return convertedFile;
    }

    @Async
    private void waitForS3FolderToExist(String bucketName, String folderKey) throws TimeoutException {
        long timeoutMillis = 5000; // Set the timeout to 5 seconds
        long startTimeMillis = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTimeMillis < timeoutMillis) {
            ObjectListing objectListing = s3Client.listObjects(bucketName, folderKey);
            if (objectListing.getObjectSummaries().isEmpty()) {
                // Folder not yet created, sleep for 100ms before trying again
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                // Folder exists, break out of the loop
                return;
            }
        }
        throw new TimeoutException("Timeout waiting for folder to exist in S3");
    }

    @Async
    private void waitForObjectDeletion(String bucketName, String objectKey) {
        boolean objectExists = true;
        while (objectExists) {
            try {
                s3Client.getObjectMetadata(bucketName, objectKey);
                // If no exception is thrown, the object still exists
                Thread.sleep(100); // wait for 0.5 second before checking again
            } catch (AmazonS3Exception e) {
                // If the object doesn't exist, an exception will be thrown
                if (e.getStatusCode() == 404) {
                    objectExists = false;
                } else {
                    throw e;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }

    private boolean checkFileFormat(MultipartFile file) {
        return file.getContentType() != null && file.getContentType().startsWith("image/");
    }

}
