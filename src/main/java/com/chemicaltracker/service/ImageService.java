package com.chemicaltracker.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void add(MultipartFile image, String filename, String name) throws Exception;
}
