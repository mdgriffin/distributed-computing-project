package com.mdgriffin.distributedcomputingproject.common;

import java.util.List;

public interface FileSystem {
    void saveFile();

    void readFile();

    boolean createDirectory(String path, String dirName);

    List<String> listDirectory (String path);

    boolean directoryExists (String path);

    boolean fileExists (String path);
}
