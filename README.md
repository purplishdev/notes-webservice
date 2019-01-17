# Notes Webservice
Java Spring implementation of notes assignment.\
Spring DATA RestRepositories was not used intentionally.

## What is required for running the project
- Java 8 JDK

## How to run scripts that will setup database for the project
Database is already embedded within application (H2 in-memory database)

## How to build and run the project
To build runnable "fat jar" run commands at project root:
- `mvn clean install` - run tests and build jar
- `java -jar target/notes-webservice-0.0.1-SNAPSHOT.jar --spring.profiles.active=mock` - run jar file

To just start the application run command: `mvn clean spring-boot run -Dspring.profiles.active=mock`

Server will be automatically started and ready for requests at `http://localhost:8080/api/v1`

Or simply use demo available at Heroku `https://notes-webservice.herokuapp.com/api/v1`\
`NOTE: first request will take longer time because of server startup`

## Example usages (ie. like example curl commands)

- Create new note\
`curl -H "Content-Type: application/json" -d "{\"title\":\"Sample note\",\"content\":\"This is the content of note\"}" http://localhost:8080/api/v1/notes`

- Update note with id 1\
`curl -X PUT -H "Content-Type: application/json" -d "{\"title\":\"Updated note\",\"content\":\"Updated content of note\"}" http://localhost:8080/api/v1/notes`

- Get note with id 1 `curl http://localhost:8080/api/v1/notes/1`

- Get note history with id 1 `curl http://localhost:8080/api/v1/notes/1/history`

- Get note history with id 1 `curl http://localhost:8080/api/v1/notes/1/history`

- Delete with id 1 `curl -X DELETE http://localhost:8080/api/v1/notes/1/history`

- Get all notes `curl http://localhost:8080/api/v1/notes`

## Example API response

- Get all notes `curl http://localhost:8080/api/v1/notes`

```
{
    "_embedded": {
        "notes": [
            {
                "title": "Test comment",
                "content": "Hello world!",
                "created": "2019-01-17T12:48:56.615",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/v1/notes/1"
                    },
                    "notes": {
                        "href": "http://localhost:8080/api/v1/notes"
                    },
                    "noteHistory": {
                        "href": "http://localhost:8080/api/v1/notes/1/history"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/v1/notes"
        }
    }
}
```
