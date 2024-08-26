# Drop Project Security Manager

Custom Security Manager used by [Drop Project](https://github.com/drop-project-edu/drop-project) to sandbox students' submissions.

Current checks include:
- Calling System.exit()
- Trying to read a file outside the working dir (or one of its subdirs)
- Trying to write a file outside the working dir (or one of its subdirs)

Any of these will be detected by the Security Manager and will lead to a SecurityException being thrown.

## How to use

Include the following dependency on your pom file:

    <dependency>
	    <groupId>org.dropproject</groupId>
	    <artifactId>drop-project-security-manager</artifactId>
	    <version>0.2.2</version>
	</dependency>
	
Now, just set the security manager when calling your application like this:

    java -Djava.security.manager=org.dropproject.security.SandboxSecurityManager YourMainClass

## Remarks

Since this usually runs within Drop Project, it is expecting a system property `dropProject.maven.repository`