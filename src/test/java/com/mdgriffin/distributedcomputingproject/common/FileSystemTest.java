package com.mdgriffin.distributedcomputingproject.common;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileSystemTest {

    private FileSystem fs = new FileSystemImpl();

    private static final String TEST_PATH = "/DC_Test";

    @BeforeAll
    public static void setup () {
        new File(TEST_PATH).mkdirs();
    }

    @BeforeEach
    public void beforeEach () {
        // To-Do: Delete the test directory
    }

    @Test
    public void canRetrieveDirectoryContents () {
        List<String> directoryListing = fs.listDirectory("./");

        assertEquals(3, directoryListing.size());
        assertEquals(true, directoryListing.contains(".gitignore"));
    }

    @Test
    public void canCreateDirectory () {
       boolean result = fs.createDirectory("/", "temp_testing");
       assertEquals(true, result);
     }

    @Test
    public void whenDirectoryExists_existsCheck_returnsTrue () {
        boolean result = fs.directoryExists("/Users");
        assertEquals(true, result);
    }

    @Test
    public void whenDirectoryDoesNotExist_existsCheck_returnsFalse () {
        boolean result = fs.directoryExists("/does_not_exist");
        assertEquals(false, result);
    }

    @Test
    public void whenFileExists_existsCheck_returnsTrue () {
        boolean result = fs.fileExists("/windows-version.txt");
        assertEquals(true, result);
    }

    @Test
    public void whenFileDoesNotExist_existsCheck_returnsFalse () {
        boolean result = fs.fileExists("/does_not_exist.txt");
        assertEquals(false, result);
    }

    @AfterAll
    public static void cleanUp () {
        System.out.println("Clean up after all tests");
    }
}
