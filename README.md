# demo
I have DEMO as the root folder of my VS Code worksapce. It contains .vscode folder which has setting.json having maven and java path set as:

{
  "java.configuration.runtimes": [
    {
      "name": "JavaSE-17",
      "path": "E:\\HarKrishan\\software\\jdk-17.0.12_windows-x64_bin\\jdk-17.0.12",
      "default": true
    }
  ],
  "maven.executable.path": "E:\\HarKrishan\\software\\apache-maven-3.9.12\\bin\\mvn.cmd",
  "java.format.settings.url": "${workspaceFolder}/.vscode/eclipse-formatter.xml",
  "java.saveActions.organizeImports": true,
  "java.configuration.updateBuildConfiguration": "interactive"
}


Per-project (recommended): place the file at <workspace-root>/.vscode/settings.json.
Global: use VS Code User Settings (Preferences → Settings) if you want the JVMs available in all workspaces.


Set Temporary path for current session:
E:\HarKrishan\workspace\vscode\demo> 
$env:JAVA_HOME = 'E:\HarKrishan\software\jdk-17.0.12_windows-x64_bin\jdk-17.0.12'
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path
mvn -v
java -version

View -> Command Pallet..
View -> Open View..
View -> Terminal


E:\HarKrishan\workspace\vscode\demo\accounts-api

Build Spring Boot Project from termonal:
E:\HarKrishan\workspace\vscode\demo> & "E:\HarKrishan\software\apache-maven-3.9.12\bin\mvn.cmd" install -f "e:\HarKrishan\workspace\vscode\demo\accounts-api\pom.xml"


Git Repo
https://github.com/HarKrishanLal/demo