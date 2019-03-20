package com.mdgriffin.distributedcomputingproject.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {

    public static String fileListToCSV (List<FileDescription> filelist) {
        return filelist.toString().replace(", ", "").replace("[", "").replace("]", "");
    }

    public static List<FileDescription> csvToFileList (String encodedCSV) {
        List<FileDescription> result = new ArrayList<FileDescription>();
        String[] lines = encodedCSV.split("\n");

        for(String line :lines) {
            String[] parts = line.split(";");

            if (parts.length == 2) {
                long filesize = Long.parseLong(parts[1]);
                result.add(new FileDescription(parts[0],  filesize));
            }
        }

        return result;
    }

}
