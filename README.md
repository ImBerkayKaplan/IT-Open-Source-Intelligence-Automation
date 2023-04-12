# IT-Open-Source-Intelligence-Automation
A project to automate the open source intelligence gathering step in risk assessment of third party vendors. The GUI of the project requests the company name and website to assess the security level of their digital infrastructure and any incidents related to IT. It generates an incomplete report from various websites using webscraping techniques. 

## Input

The GUI will only require you to enter the company name and its web domain

## Output

There are three output elements after you input the company name and its web domain.

**SSL Labs**: Your default browser will take you to ```https://www.ssllabs.com/ssltest/analyze.html?d=<company-name>``` that shows the company's server grades

**Screen Output**: A summary of the search results will be printed out to the GUI's screen. If a step fails, an error message will be printed out.
  
**```Open Source Intelligence Gathering on <company-name>.rtf``` File**: The complete report including every step will be printed to a file in the same directory as the class files that you ran.

## Running The Project Linux

This tutorial assumes that you have the Java Development Kit (JDK) installed on your system. If you don't, you can download it from the official Oracle website. I'm running this project from my Linux VirtualBox.

Organize your Java files: Ensure that your Java files (e.g., Main.java and OSUIntelligenceTools.java) are located in the same directory.

Download the Jsoup library JAR file from the official Jsoup website, https://jsoup.org/download. Select the "jsoup-x.x.x.jar" file (where x.x.x is the version number).

Save the downloaded JAR file in the same directory as your Java files (e.g., Main.java and OSUIntelligenceTools.java).

Open a terminal (Command Prompt on Windows, Terminal on macOS or Linux) and navigate to the directory containing the Java files using the cd command. For example:

```cd /IT-Open-Source-Intelligence-Automation/src```

Compile the Java files using the javac command and replace the x with version number:

```javac -cp .:jsoup-x.x.x.jar Main.java OSUIntelligenceTools.java```

This command will compile your Java files and generate corresponding .class files.

Run the compiled Main.class file using the java command and replace the x with version number:

```java -cp .:jsoup-x.x.x.jar Main```

This command will execute the Main class, and you should see the GUI on your screen.
