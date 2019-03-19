package com.mdgriffin.distributedcomputingproject.common;

public class FileDescription {

    private String filename;
    private long filesize;

    public FileDescription(String filename, long filesize) {
        this.filename = filename;
        this.filesize = filesize;
    }

    private FileDescription () {}

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    @Override
    public String toString () {
        return filename + ";" + filesize + "\n";
    }

    @Override
    public boolean equals (Object otherObject) {

        if (this == otherObject) {
            return true;
        }

        if (otherObject instanceof  FileDescription) {
            FileDescription otherFileDescription = (FileDescription) otherObject;

            if (this.getFilename().equals(otherFileDescription.getFilename())) {
                return true;
            }
        }

        return false;
    }
}
