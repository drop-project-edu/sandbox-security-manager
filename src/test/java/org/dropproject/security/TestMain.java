/*-
 * ========================LICENSE_START=================================
 * Drop Project Security Manager
 * %%
 * Copyright (C) 2020 - 2024 Pedro Alves
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package org.dropproject.security;

import org.junit.BeforeClass;
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
