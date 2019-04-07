package org.dropProject.security;

import org.junit.Test;

import java.io.File;

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

}
