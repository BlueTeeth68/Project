package com.mangareader.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public interface IStorageService {
    void init(String location);

    String store(MultipartFile file, String location);

    String store(MultipartFile file, String location, String fileName);

    List<String> storeMultipleFile(MultipartFile[] files, String location);

    Stream<Path> loadAll(String location);

    Path load(String filename, String location);

    Resource loadAsResource(String filename, String location);

    void deleteAll(String location);
}
