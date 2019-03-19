package com.mdgriffin.distributedcomputingproject.gui;

public class FTPFile {

    private String filename;
    private int filesize;

    public FTPFile(String filename, int filesize) {
        this.filename = filename;
        this.filesize = filesize;
    }

    public FTPFile () {
        this("", 0);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }
}
