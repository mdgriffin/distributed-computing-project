package com.mdgriffin.distributedcomputingproject.common;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FileSystemTest {

    private static final String TEST_PATH = "/DC_Test/";

    private static FileSystem fs = new FileSystemImpl(TEST_PATH);

    @BeforeAll
    public static void setup () {
        System.out.println("Setup before tests");
        try {
            fs.saveFile("test1.txt", "Hello World".getBytes());
            fs.saveFile("test2.txt", "Hello World".getBytes());
            fs.saveFile("test3.txt", "Hello World".getBytes());
        } catch (IOException exc) {
            System.out.println(exc);
            fail();
        }
    }

    @Test
    public void canSaveFile () {
        // TODO:
    }

    @Test
    public void canReadFile () {
        // TODO:
    }

    @Test
    public void canDeleteFile () {
        // TODO:
    }

    @Test
    public void canRetrieveDirectoryContents () {
        List<String> directoryListing = fs.listDirectory("");

        assertEquals(3, directoryListing.size());
        assertEquals(true, directoryListing.contains("test1.txt"));
    }

    @Test
    public void canRetrieveDirectoryContents_withSubdirectories () {
        // TODO:
    }

    @Test
    public void canCreateDirectory () {
       boolean result = fs.createDirectory("/temp_testing");
       assertEquals(true, result);
     }

    @Test
    public void whenDirectoryExists_existsCheck_returnsTrue () {
        fs.createDirectory("/temp_testing");
        boolean result = fs.directoryExists("/temp_testing");
        assertEquals(true, result);
    }

    @Test
    public void whenDirectoryDoesNotExist_existsCheck_returnsFalse () {
        boolean result = fs.directoryExists("/does_not_exist");
        assertEquals(false, result);
    }

    @Test
    public void whenFileExists_existsCheck_returnsTrue () {
        boolean result = fs.fileExists("/test1.txt");
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
        fs.deleteFile("test1.txt");
        fs.deleteFile("test2.txt");
        fs.deleteFile("test3.txt");
        fs.deleteDirectory("/temp_testing");
    }
}
