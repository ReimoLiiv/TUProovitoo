
# Proovitoo Application with ZK Framework

## Introduction
This is a Spring Boot web application developed using the ZK Framework. 

## Prerequisites
To run this application from command line, you'll need:
- Java 17 or higher set as path environment variable. (instructions: https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/windows-7-install.html)

## Running the Application
1. Download the zipped project. 
2. Extract the zip file to your desired location.
3. Open a terminal and navigate to the extracted folder.
4. Run the application using the following command:
   java -jar target/proovitoo.jar

PS. If zip file is not provided... 
1. Download or clone it from there: [GitHub repository](https://github.com/ReimoLiiv/TUProovitoo).
2. Open a terminal and navigate to the folder.
3. Run the following command: mvn clean package (need to have maven installed)
4. Run the application using the following command on the project root folder:
      java -jar target/\proovitoo-0.0.1-SNAPSHOT.jar.jar

## Accessing the Application
Once the application is running, open your web browser and navigate to `http://localhost:8080/proovitoo/login.zul`. 
Log in using these credentials:

- **User 1:**
  - Username: `joonas123`
  - Password: `fiction`
- **User 2:**
  - Username: `maria123`
  - Password: `doping`
- **User 3:**
  - Username: `toomas111`
  - Password: `soccer`

The application demonstrates various functionalities provided by the ZK Framework in a Spring Boot environment. Enjoy exploring the application!

## Future improvements
This is demo application and not commercial ready.
To improve application, password encryption must be done. Also, This application don't have any tests, 
in the future it is recommended to write tests as well.

## Support
If you encounter any issues or have questions, please contact Reimo Liiv (liivreimo@gmail.com)
