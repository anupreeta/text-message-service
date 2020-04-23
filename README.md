### Running the project
``./gradlew bootRun``

### API
- Create message. <br>
POST http://localhost:8080/messages
<br>
Request body:

```json
{
	"recipient": "anupreeta",
	"content" : "Hello Anupreeta"
}
```

Response:

```json
{
    "recipient": "anupreeta",
    "content": "Hello Anupreeta",
    "status": "NEW",
    "dateSent": "2020-04-22T22:58:17.542+0000"
}
```

- Get messages by recipient and status. <br>
GET http://localhost:8080/messages?recipient=anupreeta&status=NEW
<br>
Response:

```json
[
    {
        "recipient": "anupreeta",
        "content": "hello Anupreeta",
        "status": "FETCHED",
        "dateSent": "01:03:37"
    }
]
```
The status changes to FETCHED. The second time the request is sent with NEW status, it will be empty

- Get messages by start and stop time index. <br>
 GET http://localhost:8080/messages?startTime=00:01&stopTime=02:00
 <br>
 Response: 
 
 ```json
[
    {
        "recipient": "anupreeta",
        "content": "hello Anupreeta",
        "status": "FETCHED",
        "dateSent": "01:03:37"
    }
]
```

- Delete one or more messages <br>
DELETE http://localhost:8080/messages?messageId=1 <br>
DELETE http://localhost:8080/messages <br>

### Creating Boot Jar
``./gradlew clean build bootJar``

### Run jar
``java -jar build/libs/demo-0.0.1-SNAPSHOT.jar``

