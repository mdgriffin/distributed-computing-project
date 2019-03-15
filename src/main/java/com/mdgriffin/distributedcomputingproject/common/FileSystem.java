package com.mdgriffin.distributedcomputingproject.common;

import java.io.IOException;
import java.util.List;

public interface FileSystem {
    void saveFile(String path, byte[] data) throws IOException;

    byte[] readFile();

    void deleteFile (String path);

    boolean createDirectory(String path);

    boolean deleteDirectory (String path);

    List<String> listDirectory (String path);

    boolean directoryExists (String path);

    boolean fileExists (String path);
}
