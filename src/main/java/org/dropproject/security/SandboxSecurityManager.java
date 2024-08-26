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

import java.io.File;
import java.security.Permission;

/**
 * This is the Security Manager used by Drop Project to create a sandbox for students submissions
 */
public class SandboxSecurityManager extends SecurityManager {

    public static final String RANDOM = "/dev/random";

    private String workingFolder;
    private String javaHome;
    private String mavenRepository;

    private boolean debug = false;

    public SandboxSecurityManager() {
        this.workingFolder = new File("").getAbsolutePath();
        this.javaHome = System.getProperty("java.home");
        this.mavenRepository = System.getProperty("dropProject.maven.repository");
        if (System.getProperty("dropProject.securityManager.debug") != null) {
            debug = true;
            System.out.println("Using SandboxedSecurityManager with\n" +
                    "\tjava.home=" + javaHome + "\n" +
                    "\tmaven.repository=" + mavenRepository);
        }
    }

    @Override
    public void checkPermission(Permission perm) {

        if (isCalledFromTestClass()) {
            if (debug) {
                System.out.println("SandboxedSecurityManager.checkPermission: " + perm);
            }
        } else {
            // allow anything.
        }
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        if (isCalledFromTestClass()) {
            if (debug) {
                System.out.println("SandboxedSecurityManager.checkPermission2: " + perm);
            }
        } else {
            // allow anything.
        }
    }

    @Override
    public void checkRead(String file) {
        if (isCalledFromTestClass()) {
            if (debug) {
                System.out.println("SandboxedSecurityManager.checkRead: " + file);
            }
            if (checkIllegalPath(file)) {
                throw new SecurityException("Illegal file access (read): " + file);
            }
        } else {
            super.checkRead(file);
        }
    }

    @Override
    public void checkWrite(String file) {
        if (isCalledFromTestClass()) {
            if (debug) {
                System.out.println("SandboxedSecurityManager.checkWrite: " + file);
            }
            if (checkIllegalPath(file)) {
                throw new SecurityException("Illegal file access (write): " + file);
            }
        } else {
            super.checkRead(file);
        }
    }

    @Override
    public void checkExit(int status) {
        if (isCalledFromTestClass()) {
            throw new SecurityException("System.exit() is forbidden");
        } else {
            super.checkExit(status);
        }
    }

    private boolean isCalledFromTestClass() {
        for (Class aClass : getClassContext()) {

            // aClass.getSimpleName() enters infinite loop so have to manually extract the className

            String fullName = aClass.getName();
            String[] parts = fullName.split("\\.");
            String name = parts[parts.length - 1];

            if (name.startsWith("Test")) {
                return true;
            }
        }

        return false;
    }

    private boolean checkIllegalPath(String file) {

        // allow access to files in the maven repository
        // this is needed for example by junit5
        if (mavenRepository != null && file.startsWith(mavenRepository)) {
            return false;
        }

        return file.startsWith("/") &&
                !(file.startsWith(workingFolder) || file.startsWith(javaHome) || file.equals(RANDOM))
                || file.startsWith("..")
                || file.startsWith("\\");
    }
}
