package com.mdgriffin.distributedcomputingproject.common;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileSystemImpl implements FileSystem {

    private String basePath;

    FileSystemImpl (String basePath) {
        this.basePath = basePath;
    }

    @Override
    public void saveFile (String path, byte[] data) throws IOException {
        Path file = Paths.get(this.basePath + path);
        Files.write(file, data);
    }

    @Override
    public byte[] readFile () {
        return new byte[]{};
    }

    @Override
    public void deleteFile(String path) {

    }

    @Override
    public boolean createDirectory (String path) {
        if (!directoryExists(path)) {
            return new File(this.basePath + path).mkdirs();
        }
        return true;
    }

    @Override
    public boolean deleteDirectory(String path) {
        return false;
    }

    @Override
    public List<String> listDirectory (String path) {
        List<String> fileNames = new ArrayList<String>();
        File directory = new File(this.basePath + path);
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
        File tempFile = new File(this.basePath + path);
        return tempFile.exists() && tempFile.isDirectory();
    }

    @Override
    public boolean fileExists (String path) {
        File tempFile = new File(this.basePath + path);
        return tempFile.exists() && tempFile.isFile();
    }
}
