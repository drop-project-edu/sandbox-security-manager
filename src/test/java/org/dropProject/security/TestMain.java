package org.dropProject.security;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static junit.framework.TestCase.assertTrue;

public class TestMain {

    @Test
    public void testFileAccessOK() {
        assertTrue(Main.fileAccess(new File("src")));
    }

    @Test(expected = SecurityException.class)
    public void testFileAccessNOK() {
        Main.fileAccess(new File("/etc"));
    }

    @Test
    public void testReadAllLines() {
        try {
            Files.readAllLines(new File("pom.xml").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
