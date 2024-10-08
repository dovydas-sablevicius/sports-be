## Backend application setup

### Required tools

### HomeBrew (MacOS only)

- bash command: '/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"'

#### Java

- Java 17 (Windows)
	- Java 17 JDK download: [https://adoptium.net/](https://adoptium.net/)
	- Environment variables needed (Windows)
		- “JAVA_HOME” - https://confluence.atlassian.com/doc/setting-the-java_home-variable-in-windows-8895.html
		- "Path" – add path to your JDK bin file (e.g. C:\Program Files\Eclipse Adoptium\jdk-17.0.4.101-hotspot\bin) 
- Java 17 (MacOS)
	- JAVA 17 JDK download: console command: "brew install --cask temurin"
	- Environment variables applying
		- e.g. export JAVA_HOME=`/usr/libexec/java_home -v 17`
- PC restart might be needed for environment variables to be applied

#### Gradle

- Gradle
	- [https://gradle.org/install/](https://gradle.org/install/)
	- Environment variables needed (Windows)
		- "Path" (e.g. C:\Gradle\gradle-7.3.3\bin) 
- PC restart might be needed for environment variables to be applied

#### Liquibase

- Liquibase
	- [https://www.liquibase.com/download](https://www.liquibase.com/download)
	- Environment variables needed (Windows)
		- "Path" (e.g. C:\Program Files\liquibase) 
- PC restart might be needed for environment variables to be applied

#### Docker

- Docker
	- [https://www.docker.com/](https://www.docker.com/)
	
#### IDE
- Recommended IDE – Intellij Community version (If there is posibility Ultimate version would be even better)
	- https://www.jetbrains.com/idea/download/#section=windows
	- Nothing wrong if Eclipse or Visual Studio code will be used

### Git

- https://www.simplilearn.com/tutorials/git-tutorial/git-installation-on-windows

## Project

### Bitbucket

- Set up ssh
  - https://www.theserverside.com/blog/Coffee-Talk-Java-News-Stories-and-Opinions/BitBucket-SSH-Key-Example
- Cloning repository
  - Click button "clone"
    ![Gradle sidebar](/assets/images/bitbucket_clone.PNG)
  - Copy bitbucket url and execute it in console
    ![Gradle sidebar](/assets/images/copy_bitbucket_url.PNG)
    ![Gradle sidebar](/assets/images/cmd_paste_bitbucket_url.PNG)

### Setting up database and docker to use it

#### Starting required docker containers
- go to "...vln-2023-spring-steampunkers-be" folder
- run `docker compose up` command (should startup database container if docker sut up correctly) 
- if you are using intellij to setup database tables (You will need to make this 
step each time you will add new migration to liquibase):
	- In right sidebar you should see tab "Gradle"
	![Gradle sidebar](/assets/images/gradle.PNG)
    - Go to sport -> tasks -> liquibase
    ![Liquibase_Folder](/assets/images/liquibase_folder.PNG)
    - Double click update
    ![Liquibase_Update](/assets/images/liquibase_update.PNG)

#### Connecting to database
- With intellij (if you have ultimate version)
  - Go to "Database" tab in left sidebar
  - Click "+"
  - Select "Data source"
  - Select "Postgres"
  - In window, which was opened enter following fields
    - host - "localhost"
    - port - "5432"
    - User - "postgres"
    - Password - "postgres"
    - Database - "sports"
  - Click "Test connection"
  - If it succeeds click "apply" and exit this window
  - Now inside "Database" tab you should see Database you connected to
  - To access sport database select sports schema from database

fix
