package com.mangareader.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("unused")
public interface IStorageService {
    Resource loadAsResource(String filename, String location);

    String uploadImage(MultipartFile file, String folderPath, int id) throws TimeoutException;

    List<String> uploadMultiImage(MultipartFile[] files, String folderPath) throws TimeoutException;

    byte[] downloadFile(String fileName);

    void deleteFile(String fileName);

}
