package com.mdgriffin.distributedcomputingproject.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileSystemImpl implements FileSystem {

    public void saveFile () {

    }

    public void readFile () {

    }

    public boolean createDirectory (String path, String dirName) {
        return new File(path + dirName).mkdirs();
    }

    public List<String> listDirectory (String path) {
        List<String> fileNames = new ArrayList<String>();
        File directory = new File(path);
        File[] fileList = directory.listFiles();

        for (File file: fileList) {
            if (file.isFile()) {
                fileNames.add(file.getName());
            }
        }

        return fileNames;
    }

    @Override
    public boolean directoryExists(String path) {
        File tempFile = new File(path);
        return tempFile.exists() && tempFile.isDirectory();
    }

    @Override
    public boolean fileExists (String path) {
        File tempFile = new File(path);
        return tempFile.exists() && tempFile.isFile();
    }

}
