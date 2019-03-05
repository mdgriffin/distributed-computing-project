package com.mdgriffin.distributedcomputingproject.common;

import java.util.List;

public interface FileSystem {
    void saveFile();

    void readFile();

    void createDirectory();

    List<String> listDirectory (String path);
}
