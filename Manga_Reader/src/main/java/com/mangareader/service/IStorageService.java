package com.mangareader.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IStorageService {
    void init(String location);

    void store(MultipartFile file, String location);

    Stream<Path> loadAll(String location);

    Path load(String filename, String location);

    Resource loadAsResource(String filename, String location);

    void deleteAll(String location);
}
