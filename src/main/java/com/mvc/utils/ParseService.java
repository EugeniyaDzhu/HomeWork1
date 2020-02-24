package com.mvc.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import com.mvc.ParseException;

public class ParseService<T> {

    public static <T> List<T> parseUsersFromFile(MultipartFile multipartFile, Function<String, T> toModel){
        File tempFile = null;
        List<T> users = new ArrayList<>();
        try {
            tempFile = File.createTempFile("temp", multipartFile.getOriginalFilename());

            tempFile.deleteOnExit();
            multipartFile.transferTo(tempFile);
            users = Files.lines(Paths.get(tempFile.getAbsoluteFile().toString()), StandardCharsets.UTF_8)
                    .map(toModel).collect(Collectors.toList());
        } catch (IOException e) {
            throw new ParseException("Cant parse file");
        }
        tempFile.delete();
        return users;
    }    
}
