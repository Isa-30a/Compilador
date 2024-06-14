### Running and compiling from Visual Studio Code
First you need to have the Java extension for VS and JDK 21 installed.
#### Steps
1. Create an environment variable called: **JAVA_HOME** with the path of the JDK 21 installation folder.
2.  **Create a Maven project** from Visual Studio to automatically install Maven and then configure it.

	press `CTRL + Shift + P` to open the command line, type and select   `Java: Create Java Project` --> `Maven` --> `maven-archetype-quickstart` --> `choose a version e: 1.4` -> and finish the build.
	
3.  **Set up Visual Studio** to understand Maven.
	
	  You must go to the Visual Studio configuration for the Maven wrapper: `maven.executable.path` in this field you copy the following path: 
			C:\User\User\.m2\wrapper\dists\apache-maven-3.6.3-bin\1iopthnavndlasol9gbrbg6bf2\apache-maven-3.6.3\bin\mvn
	
	 After that, close VS and delete the folder called repository in:
			C:\User\User\.m2
	 
	Open this project and press again `CTRL + Shift + P`, type and select `Maven: Run Commands` -> `compile`.
	 
3. **Enjoy**
	You can use the command `mvn exec:java` for execution or press the button in the user interface.
