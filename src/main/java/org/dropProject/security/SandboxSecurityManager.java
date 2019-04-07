package org.dropProject.security;

import java.io.File;
import java.security.Permission;

/**
 * This is the Security Manager used by Drop Project to create a sandbox for students submissions
 */
public class SandboxSecurityManager extends SecurityManager {

    private String workingFolder;

    private boolean debug = false;

    public SandboxSecurityManager() {
        this.workingFolder = new File("").getAbsolutePath();
        if (System.getProperty("dropProject.securityManager.debug") != null) {
            debug = true;
            System.out.println("Using SandboxedSecurityManager");
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
            if (file.startsWith("/") && !file.startsWith(workingFolder)
                    || file.startsWith("..")
                    || file.startsWith("\\")) {
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
            if (file.startsWith("/") && !file.startsWith(workingFolder)
                    || file.startsWith("..")
                    || file.startsWith("\\")) {
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
}
