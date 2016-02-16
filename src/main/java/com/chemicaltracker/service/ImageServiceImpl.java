package com.chemicaltracker.service;

import com.chemicaltracker.persistence.dao.ImageDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Service
public class ImageServiceImpl implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    private static final ImageDao imagesDB = ImageDao.getInstance();

    @Override
    public void add(MultipartFile image, String filename, String name) throws Exception {
        if (image.isEmpty()) {
            logger.warn("The image file was missing");
            throw new Exception("Empty image file");
        }

        byte[] bytes = image.getBytes();

        // Creating the directory to store file
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "tmpImages");
        if (!dir.exists())
            dir.mkdirs();

        // Create the image on server
        File serverFile = new File(dir.getAbsolutePath()
                + File.separator + name);
        BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();

        logger.info("Server Image Location="
                + serverFile.getAbsolutePath());

        imagesDB.uploadImage(serverFile.getAbsolutePath(), filename);
    }
}
