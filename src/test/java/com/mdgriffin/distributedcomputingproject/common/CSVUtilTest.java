package com.mdgriffin.distributedcomputingproject.common;

import org.checkerframework.checker.signature.qual.FieldDescriptor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVUtilTest {

    @Test
    public void withFileDescriptionList_csvEncoded () {
        List<FileDescription> fileList = new ArrayList<>();
        fileList.add(new FileDescription("file1.txt", 5));
        fileList.add(new FileDescription("file2.txt", 10));
        fileList.add(new FileDescription("file3.txt", 15));
        fileList.add(new FileDescription("file4.txt", 20));

        String result = CSVUtil.fileListToCSV(fileList);

        assertEquals("file1.txt;5\nfile2.txt;10\nfile3.txt;15\nfile4.txt;20\n", result);
    }

    @Test
    public void withEncodedCSVString_fileListDecoded () {
        String csv = "file1.txt;5\nfile2.txt;10\nfile3.txt;15\nfile4.txt;20\n";

        List<FileDescription> files = CSVUtil.csvToFileList(csv);

        assertEquals(4, files.size());

        FileDescription file1 = files.get(0);
        assertEquals("file1.txt", file1.getFilename());
        assertEquals(5, file1.getFilesize());

        FileDescription file2 = files.get(1);
        assertEquals("file2.txt", file2.getFilename());
        assertEquals(10, file2.getFilesize());

        FileDescription file3 = files.get(2);
        assertEquals("file3.txt", file3.getFilename());
        assertEquals(15, file3.getFilesize());

        FileDescription file4 = files.get(3);
        assertEquals("file4.txt", file4.getFilename());
        assertEquals(20, file4.getFilesize());
    }
}
