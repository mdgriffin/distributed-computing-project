package com.mdgriffin.distributedcomputingproject.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileSystemImpl implements FileSystem {

    private String basePath;

    public FileSystemImpl (String basePath) {
        this.basePath = basePath;
    }

    @Override
    public void saveFile (String path, byte[] data) throws IOException {
        Path file = Paths.get(basePath + path);
        Files.write(file, data);
    }

    @Override
    public byte[] readFile (String path)  throws IOException {
        return Files.readAllBytes(Paths.get(basePath, path));
    }

    @Override
    public boolean deleteFile(String path) {
        if (fileExists(path)) {
            File file  = new File(basePath + path);
            return file.delete();
        }
        return false;
    }

    @Override
    public boolean createDirectory (String path) {
        if (!directoryExists(path)) {
            return new File(basePath + path).mkdirs();
        }
        return true;
    }

    @Override
    public boolean deleteDirectory(String path) {
        if (directoryExists(path) && listDirectory(path).size() == 0) {
            File dir = new File(basePath + path);
            return dir.delete();
        }
        return false;
    }

    @Override
    public List<String> listDirectory (String path) {
        return listDirectory(path, false);
    }

    @Override
    public List<String> listDirectory (String path, boolean includeDirectories) {
        List<String> fileNames = new ArrayList<String>();
        File directory = new File(basePath + path);
        File[] fileList = directory.listFiles();

        for (File file: fileList) {
            if (includeDirectories || file.isFile()) {
                fileNames.add(file.getName());
            }
        }

        return fileNames;
    }

    @Override
    public boolean directoryExists(String path) {
        File tempFile = new File(basePath + path);
        return tempFile.exists() && tempFile.isDirectory();
    }

    @Override
    public boolean fileExists (String path) {
        File tempFile = new File(basePath + path);
        return tempFile.exists() && tempFile.isFile();
    }
}
