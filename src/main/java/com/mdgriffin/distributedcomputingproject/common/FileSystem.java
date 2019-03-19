package com.mdgriffin.distributedcomputingproject.common;

import java.io.IOException;
import java.util.List;

public interface FileSystem {
    void saveFile(String path, byte[] data) throws IOException;

    byte[] readFile(String path)  throws IOException;

    boolean deleteFile (String path);

    boolean createDirectory(String path);

    boolean deleteDirectory (String path);

    List<FileDescription> listDirectory (String path);

    List<FileDescription> listDirectory (String path, boolean includeDirectories);

    boolean directoryExists (String path);

    boolean fileExists (String path);
}
