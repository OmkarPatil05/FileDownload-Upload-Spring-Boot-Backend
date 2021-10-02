package com.om.fileuploadanddownload.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Paths.get;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/file")
public class FileController {

    // define a location of upload files
    public static final String DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads" ;

    // define REST Endpoint for upload files
    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> multipartFiles ) throws IOException {

            List<String> filenames = new ArrayList<>();
            for(MultipartFile file : multipartFiles){

                System.out.println("without cleanpath: "+file.getOriginalFilename());
                String filename = StringUtils.cleanPath(file.getOriginalFilename());
                System.out.println("with cleanpath: "+filename);


                Path fileStorage = get(DIRECTORY , filename ).toAbsolutePath().normalize();

                copy( file.getInputStream() , fileStorage , REPLACE_EXISTING  );

                filenames.add(filename);
            }


            return ResponseEntity.ok().body(filenames);
    }
    // define REST Endpoint for download files
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("filename") String filename) throws IOException {
        Path filePath = get( DIRECTORY ).toAbsolutePath().normalize().resolve(filename);
        if(!Files.exists(filePath)){
            throw new FileNotFoundException(filename + " can't be found. ");
        }
        Resource resource = new UrlResource( filePath.toUri() );
        HttpHeaders headers = new HttpHeaders();
        headers.add("File-Name",filename);
        headers.add(CONTENT_DISPOSITION,"attachment;File-Name="+filename);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath))).headers(headers).body(resource);
    }

}
