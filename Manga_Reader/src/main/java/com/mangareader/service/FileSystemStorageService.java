package com.mangareader.service;

import com.mangareader.exception.ResourceNotFoundException;
import com.mangareader.exception.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileSystemStorageService implements IStorageService {

    private Path rootLocation;

//    @Autowired
//    public FileSystemStorageService(StorageProperties properties) {
//        this.rootLocation = Paths.get(properties.getLocation());
//    }

    @Override
    public String store(MultipartFile file, String location) {
        try {
            if (file.isEmpty()) {
                log.error("Failed to store empty file {}", file.getOriginalFilename());
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Path pathLocation = Paths.get(location);
            log.info("Creating file {}", file.getOriginalFilename());
            Path target = pathLocation.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
//            String url = target.toFile().toURI().toURL().toString();
            return file.getOriginalFilename();
        } catch (IOException e) {
            log.error("Failed to store file {}", file.getOriginalFilename());
            throw new StorageException("Failed to store file " + file.getOriginalFilename());
        }
    }

    @Override
    public String store(MultipartFile file, String location, String fileName) {
        try {
            if (file.isEmpty()) {
                log.error("Failed to store empty file {}", file.getOriginalFilename());
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Path pathLocation = Paths.get(location);
            log.info("Creating file {}", file.getOriginalFilename());
            Path target = pathLocation.resolve(fileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
//            String url = target.relativize(pathLocation).toFile().toURI().toURL().toString();
//            String url = target.toFile().toURI().toURL().toString();
            return fileName;
        } catch (IOException e) {
            log.error("Failed to store file {}", file.getOriginalFilename());
            throw new StorageException("Failed to store file " + file.getOriginalFilename());
        }
    }

    @Override
    public Stream<Path> loadAll(String location) {
        try {
            log.info("Loading stored file ........");
            this.rootLocation = Paths.get(location);
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files");
        }

    }

    @Override
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

    @Override
    public void deleteAll(String location) {
        this.rootLocation = Paths.get(location);
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init(String location) {
        try {
            this.rootLocation = Paths.get(location);
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage");
        }
    }

}
