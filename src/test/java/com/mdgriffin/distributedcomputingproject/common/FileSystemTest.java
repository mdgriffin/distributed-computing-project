package com.mdgriffin.distributedcomputingproject.common;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileSystemTest {

    private static final String TEST_PATH = "/DC_Test";

    @BeforeAll
    public static void setup () {
        new File(TEST_PATH).mkdirs();
    }

    @Test
    public void canRetrieveDirectoryContents () {
        FileSystem fs = new FileSystemImpl();
        assertEquals(0, fs.listDirectory("/DC_Test").size());
    }


}
