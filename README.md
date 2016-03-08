#Restful TicTacToe Game Server 

##What is it?
A java Rest Controller for playing TicTacToe with multiple users support

##Technologies
- Java 8
- Spring 4
- JUnit (Testing rest controllers)
- Mockito
- Maven

##Set up

1. Import the project as Maven Project into your favourite IDE (Eclise, IntelliJ...)
2. Set the build path to JavaSE-1.8 and the compiler compliance to Java 1.8
3. Run maven to create your repository or just create your goals:

>`mvn clean install`

4. Run tomcat:

>`mvn tomcat7:run-war`

##Testing

- Run maven tests through the console:

>`mvn test`

- Run as JUnit Tests to see the metrics. (E.g: in eclipse, right click over project folder -> Run As -> JUnit Test)

##Services

| url     | method | consumes      | produces | description               |
|---------|--------|---------------|----------|---------------------------|
| /new    | POST   | JSON: name    | JSON     | Creates a new player      |
| /join   | POST   | JSON: uuid    | JSON     | Joins a room              |
| /play   | POST   | JSON: move    | JSON     | Makes a move              |
| /whose  | GET    | param: uuid   | JSON     | Whose turn is it?         |
| /status | GET    | param: roomId | JSON     | Status of the game's room |


##How to Play

You can either use a Rest Client such [Postman](https://www.getpostman.com) or just the command [cURL](https://curl.haxx.se)

* Step 1: Create a **new user**:

>Provide a JSON with the following format:

>```json
{"name":"ivan"}
```

>Example:

>`curl --data '{"name":"ivan"}' -v -X POST -H "Content-Type: application/json" "http://localhost:8080/tictactoe/new"`

>Take into account escape quotes if you are running curl in a Windows machine. E.g:

>`curl --data "{\"name\":\"ivan\"}" -v -X POST -H "Content-Type: application/json" "http://localhost:8080/tictactoe/new"`

>You will receive a response like this:

>```json
{
  "success": true,
  "message": "User created",
  "uuid": "add13b40-c856-45be-8270-a11239d2053e"
}
```

* Step 2: **Join  a room**:

>Provide a JSON with the previously received uuid:

>```json
{"uuid":"add13b40-c856-45be-8270-a11239d2053e"}
```

>Response:

>```json
{
  "success": true,
  "message": "Joined to room 1",
  "uuid": "add13b40-c856-45be-8270-a11239d2053e"
}
```

* Step 3: You will need to **wait for another player** to join your room. So just create another user and join your room

* Step 4: **Start playing!** just make your move:

>```json
{
    "uuid":"79b8c7f4-0066-4383-9c71-4b6de76bf5e7", 
    "position" : {
        "row": 0, 
        "column":2
    }
}
```

>Response:

>```json
{
  "success": true,
  "message": "Move OK"
}
```

* You will need to ask the server **whose turn is it** before your next move:

>`curl --data '{"uuid":"79b8c7f4-0066-4383-9c71-4b6de76bf5e7"}' -v -X GET -H "Content-Type: application/json" http://localhost:8080/tictactoe/whose`


>```json
{
  "success": true,
  "message": "It is your turn. Moves accepted (0-2)(0-2)",
  "uuid": null
}
```

* **Wait for your rival's move**...and so on. Eventually somebody will win....or not! Good Luck

