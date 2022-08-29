# MyTVBatchApp

## Steps to run the database(MySQL)

    Goto the root directory ./MyTVBatchApp
    run command, docker-compose up -d --build (Make sure you have docker installed)
    This is to bring up Mysql cluster at port 3307

## Steps to run the application

    Make sure you have Java 17 and maven installed.
    Goto the root directory ./MyTVBatchApp
    Run, mvn clean package (It will build and package the application is necessary jars required to run the application)
    Goto target folder.
    Run the application using command, java -jar MyTVBatchApp-0.0.1-SNAPSHOT.jar
