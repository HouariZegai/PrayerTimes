package com.houarizegai.prayertimes.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Tools {

    public static List<String> getFilesNameFromFolder(String path) {
        File folder = new File(path);

        File[] listOfFiles = folder.listFiles();

        return Arrays.stream(listOfFiles)
                .filter(File::isFile)
                .map(file -> file.getName())
                .collect(Collectors.toList());
    }
}
