This utility is used to index excel file. It extensible to index any excel file.
It takes java model and creates a elastic search index out of it. Model should represent the excel that needs to be processed.

This application is sprint boot application. It uses java watcher service to process the files.
Excel file is dropped in a folder. Based on the file type a parser picks it up process it converts it into JSON file.

JSON file is picked up by another fie watcher service. It process the JSON file and pushes it into elastic search using bulk api.
Once the excel file in available in elastci serach server. You can use the controller you can run get few results.

Currently it supports
exact search
Phrase search

Project Setup:
1. Need to setup 3 folders.Make sure you have permission to read and file to these folders
    Drop excel file in this folder.
    file.processed.excel.path=D:\\prateek\\excelReader

    Intermediate JSON files are moved here. Excel is processed and converted to JSON and stored here.
    file.processed.json.path=D:\\prateek\\jsonReader

    Processed files are kept here. These are deleted after some time by FileDeletionService
    file.processed.path=D:\\prateek\\processedFiles

2. You need elastic search to for this application to run.

    #Cluster Name
    spring.data.elasticsearch.cluster-name=my-application

    #ElasticServer host name and port
    spring.data.elasticsearch.cluster-nodes=localhost,9300


 Steps to run the application

     Start elastic search server

     mvn clean install

     java -jar -Dserver.port=8080 excelFileIndexDemo-0.0.1-SNAPSHOT.jar


Rest Endpoint to get info about indexed files

Exact Search (Ger Request)
    http://localhost:8080/excelFileIndexDemo/employee/id/{id}
    http://localhost:8080/excelFileIndexDemo/employee/name/{name}

Phrase Search (Post Request) Use post master

http://localhost:8080/excelFileIndexDemo/employee/phraseSearch

Body
name="text to be searched"