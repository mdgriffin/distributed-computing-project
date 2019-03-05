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

    public void createDirectory () {

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

}
