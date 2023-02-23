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
	    <groupId>pt.ulusofona.deisi</groupId>
	    <artifactId>drop-project-security-manager</artifactId>
	    <version>0.1.3</version>
	</dependency>
	
Now, just set the security manager when calling your application like this:

    java -Djava.security.manager=org.dropProject.security.SandboxSecurityManager YourMainClass

You can pass the location of the maven repository, if you want it excluded from verification:

    java -Djava.security.manager=org.dropProject.security.SandboxSecurityManager -DdropProject.maven.repository=/Users/tester/.m2/repository YourMainClass