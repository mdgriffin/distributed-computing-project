package com.mdgriffin.distributedcomputingproject.common;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class FileSystemTest {

    private static final String TEST_PATH = "/DC_Temp/DC_Test/";

    private static FileSystem fs = new FileSystemImpl(TEST_PATH);

    @BeforeAll
    public static void setup () {
        try {
            fs.saveFile("test1.txt", "Hello World".getBytes());
            fs.saveFile("test2.txt", "Hello World".getBytes());
            fs.saveFile("test3.txt", "Hello World".getBytes());
        } catch (IOException exc) {
            System.out.println(exc);
            fail();
        }
    }

    @AfterAll
    public static void cleanUp () {
        fs.deleteFile("test1.txt");
        fs.deleteFile("test2.txt");
        fs.deleteFile("test3.txt");
        fs.deleteDirectory("/temp_testing");
    }

    @Test
    public void canSaveAndDeleteFile () {
        try {
            int numInDirectory = fs.listDirectory("").size();
            fs.saveFile("test4.txt", "Hello World".getBytes());
            int numInDirectoryAfterSave = fs.listDirectory("").size();

            assertEquals(numInDirectory + 1, numInDirectoryAfterSave);

            assertTrue(fs.deleteFile("test4.txt"));

            int numInDirectoryAfterDelete = fs.listDirectory("").size();
            assertEquals(numInDirectory, numInDirectoryAfterDelete);
        } catch (IOException exc) {
            fail();
        }
    }

    @Test
    public void canReadFile () {
        try {
            byte[] fileBytes = fs.readFile("test3.txt");
            String fileStr = new String(fileBytes, StandardCharsets.UTF_8);

            assertTrue(fileBytes.length > 0);
            assertEquals("Hello World", fileStr);
        } catch (IOException exc) {
            System.out.println(exc);
            fail();
        }
    }

    @Test
    public void canRetrieveDirectoryContents () {
        List<String> directoryListing = fs.listDirectory("");

        assertEquals(3, directoryListing.size());
        assertEquals(true, directoryListing.contains("test1.txt"));
    }

    @Test
    public void canRetrieveDirectoryContents_withSubdirectories () {
        String testDir1 = "temp_testing1";
        String testDir2 = "temp_testing2";

        List<String> directoryListingBefore = fs.listDirectory("", true);

        fs.createDirectory(testDir1);
        fs.createDirectory(testDir2);

        List<String> directoryListingAfter = fs.listDirectory("", true);

        assertEquals(directoryListingBefore.size() + 2, directoryListingAfter.size());
        assertTrue(directoryListingAfter.contains(testDir1));
        assertTrue(directoryListingAfter.contains(testDir2));

        fs.deleteDirectory(testDir1);
        fs.deleteDirectory(testDir2);

        List<String> directoryListingAfterDelete = fs.listDirectory("", true);

        assertEquals(directoryListingBefore.size(), directoryListingAfterDelete.size());
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


    @Test
    public void whenListingFiles_AndDirectoryDoesNotExist_emptyList () {
        List<String> directoryListing = fs.listDirectory("does_not_exist");
        assertEquals(0, directoryListing.size());
    }
}
