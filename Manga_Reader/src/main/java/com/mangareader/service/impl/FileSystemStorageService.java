package com.mangareader.service.impl;

import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.exception.StorageException;
import com.mangareader.service.IStorageService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@SuppressWarnings("unused")
public class FileSystemStorageService implements IStorageService {

    private Path rootLocation;

    private final String SERVER_NAME = "http://localhost:8080/";

    @Override
    public String uploadImage(MultipartFile file, String location, int id) {
        try {
            if (file == null || file.isEmpty()) {
                log.error("Failed to store empty file {}", file.getOriginalFilename());
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Path pathLocation = Paths.get(location);
            File temp = pathLocation.toFile();
            if (!temp.exists()) {
                temp.mkdirs();
            }

            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String newFileName = id + fileExtension;

            log.info("Creating file {}", newFileName);

//            Path target = pathLocation.resolve(file.getOriginalFilename());
            Path target = pathLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return SERVER_NAME + location + newFileName;
        } catch (IOException e) {
            log.error("Failed to store file {}", file.getOriginalFilename());
            throw new StorageException("Failed to store file " + file.getOriginalFilename());
        }
    }

//    public String store(MultipartFile file, String location, String fileName) {
//        try {
//            if (file.isEmpty()) {
//                log.error("Failed to store empty file {}", file.getOriginalFilename());
//                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
//            }
//            Path pathLocation = Paths.get(location);
//            File temp = pathLocation.toFile();
//            if (!temp.exists()) {
//                temp.mkdirs();
//            }
//            log.info("Creating file {}", file.getOriginalFilename());
//            Path target = pathLocation.resolve(fileName);
//            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
//            return fileName;
//        } catch (IOException e) {
//            log.error("Failed to store file {}", file.getOriginalFilename());
//            throw new StorageException("Failed to store file " + file.getOriginalFilename());
//        }
//    }

    @Override
    @Transactional
    public List<String> uploadMultiImage(MultipartFile[] files, String location) {
        try {
            if (files == null) {
                log.error("Failed to store empty file: file is empty");
                throw new StorageException("Failed to store empty file");
            }
            log.info("Create path from location: {}", location);
            Path pathLocation = Paths.get(location);

            //clear the location
            log.info("Clear location.");
            if (Files.exists(pathLocation)) {
                Files.walk(pathLocation)
                        .sorted(java.util.Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
            File temp = pathLocation.toFile();
            temp.mkdirs();

            List<String> fileNames = new ArrayList<>();

//            for (MultipartFile file : files) {
//                log.info("Creating file {}", file.getOriginalFilename());
//                Path target = pathLocation.resolve(String.valueOf(index));
//                Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
//                fileNames.add(SERVER_NAME + location + index);
//
//            }

            try {
                for (int index = 1; index <= files.length; index++) {
                    MultipartFile file = files[index - 1];
                    if (!file.isEmpty()) {
                        log.info("Creating file {}", file.getOriginalFilename());

                        String originalFileName = file.getOriginalFilename();
                        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                        String newFileName = index + fileExtension;

                        Path target = pathLocation.resolve(newFileName);
                        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

                        fileNames.add(SERVER_NAME + location + newFileName);
                    }
                }
            } catch (Exception e) {
                throw new StorageException("Failed to store file");
            }


            return fileNames;
        } catch (IOException e) {
            log.error("Failed to store file: store error");
            throw new StorageException("Failed to store file");
        }
    }

    public Stream<Path> loadAll(String location) {
        try {
            log.info("Loading stored file ........");

            this.rootLocation = Paths.get(location);
            if (Files.exists(this.rootLocation)) {
                return Files.walk(this.rootLocation, 1)
                        .filter(path -> !path.equals(this.rootLocation))
                        .map(path -> this.rootLocation.relativize(path));
            }
            throw new StorageException("Failed to read stored files");
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files");
        }

    }

    public Path load(String filename, String location) {
        this.rootLocation = Paths.get(location);
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename, String location) {
        try {
            Path file = load(filename, location);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("Could not read file: " + location + "/" + filename);
            }
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("Could not read file: " + location + "/" + filename);
        }
    }

    public void deleteAll(String location) {
        this.rootLocation = Paths.get(location);
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public byte[] downloadFile(String fileName) {
        return new byte[0];
    }

    @Override
    public void deleteFile(String fileName) {

    }

    public void init(String location) {
        try {
            this.rootLocation = Paths.get(location);
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage");
        }
    }


}
