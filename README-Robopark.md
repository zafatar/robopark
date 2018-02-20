# A Simple implementation of a Toy Robot on a Board. 
#### 0. Introduction.
This is a short documentation about a toy robot presentation which can move/roam on a board with the given width and height. 
The board is simply described by its `width` and `height`. Also the board has a list of `robots` placed on itself. Robots have a unique `id` and contains `location` and `face` as direction information. The robots basically can accept a list of commands and generate some actions accordingly. A robot: 
 - can be `PLACE`d on a position with `X` and `Y` coordinates, facing to `F` given direction.
 - can `MOVE`.
 - can turn to `LEFT` or `RIGHT`.
 - can `REPORT` its status with the position and direction information.
    
In this documentation, it can also be found how to install and run this RESTful API service written in Java with the help of Spring Boot framework, and as NoSQL database, Redis. 

> The code has some different structure and logic than the requested tasks. The board system can have multiple boards and in a given time, the board which is created last is the active board where you can place a robot. Also,   

In the following sections, the detailed requirements list and compiling steps will be explained.

#### 1. Requirements.
This service is realized by using: 
  - Java - v1.8
  - Apache Maven - v3.5.2 
  - Spring Boot Framework and libraries - v1.5.10.RELEASE
  - [Redis] - v4.0.8

#### 2. Compiling and packaging.
###### 2.1. Clone the repository. 
You can clone the repository from the github link below and then enter the directory where the code has been pushed:
```sh
$ git clone git@github.com:zafatar/robopark.git
$ cd robopark/
```
###### 2.2. Clean and pack the code. 
At this step, we'll need to clean the folder (although it was kept clean in git repo) and then build the JAR package: 
```sh
$ mvn clean package
```
This will create a JAR file named as `robopark-0.1.0.jar` under the `/target` folder in the current directory.

### 3. Running the service.
In order to run the service, the command below can be run in the current directory: 
```sh
$ java -jar target/robopark-0.1.0.jar
```
After the running this command, the logs for the application and spring will be output in the shell. The service is running on the port `8080` of `localhost` by default. You need to make sure that this port is not occupied by another process or service.

> Note: The other way of running the service is to use spring build-in command, `spring-boot:run` with the help of maven. But at this level, for the sake of portability, we will stick to the JAR file.  
```sh
$ mvn clean spring-boot:run
```
Default connection to Redis is to `127.0.0.1` and to the default port of a Redis server `6379`. The default database is `1`. If you need to use and reach another Redis server, you'll need to update `application.properties` file under the `src/main/resources` folder. 
Example of `application.properties` : 
```sh 
redis.host=127.0.0.1
redis.port=6379
redis.database=1
## max number of board allowed in the runtime
boards.maxNumber=1
```
### 4. API Endpoints.
##### 4.0. Resetting boards and robots.
You can reset/delete all the boards and robots defined previously on the system. This will give a clean sheet to add new board(s) and robots. 
| HTTP Method | Path |
| ------ | ------ | 
| DELETE | /api/v1/boards/reset |
| DELETE | /api/v1/robots/reset |

Possible return codes:
| HTTP Status Code | Message | 
| ---------------- | ------- |
| 204 | No Content |
| 500 | Error cases | 

Example Request: 
```sh
$ curl -XDELETE http://localhost:8080/api/v1/boards/reset
```
##### 4.1. Create a board.
At the beginning, since there are no board and no robot, as an initial requirement in runtime, a Board should be created and initialized.

| HTTP Method | Path |
| ------ | ------ | 
| POST | /api/v1/boards |

Request Body Parameters:
| Parameter name | Type | Possible Values | 
| ------ | ----- | ----- |
| width | int | * |
| height | int | * | 

Possible return codes:
| HTTP Status Code | Message | 
| ---------------- | ------- |
| 201 | Created |
| 507 | Insufficent Storage (*) | 

(*) When it was reached to the max number of boards defined in the config, a new board can not be created. 

Example Request: 
```sh
$ curl -XPOST -H "Content-Type: application/json" -d '{"width": 5,"height": 5}' http://localhost:8080/api/v1/boards
```
Example Response: 
```sh
{
    "status": "CREATED",
    "message": "board created",
    "data": {
        "id": 1,
        "width": 5,
        "height": 5,
        "robots": null
    }
}
```

##### 4.2. Retrieve a board by id.
This endpoint will retrieve the board based on the given id. 

| HTTP Method | Path |
| ------ | ------ | 
| GET | /api/v1/boards/<id> |

Possible return codes: 
| HTTP Status Code | Message | 
| ---------------- | ------- |
| 200 | OK |
| 404 | Board Not Found | 

Example Request: 
```sh
curl -XGET http://localhost:8080/api/v1/boards/1
```
Example Response: 
| Header | Value | 
| ---- | ---- |
| Content-Type |  application/json;charset=UTF-8 | 
| Transfer-Encoding |  chunked  |
| Date | <date_time> | 
```sh
{
    "status": "OK",
    "message": "board returned",
    "data": {
        "id": 1,
        "width": 5,
        "height": 5,
        "robots": null
    }
}
```
##### 4.3. Create a robot.
This endpoint will create a robot based on the given name.

| HTTP Method | Path |
| ------ | ------ | 
| POST | /api/v1/robots |

Request Body Parameters:
| Parameter name | Type | Possible Values | 
| ------ | ----- | ----- |
| name | String | * |

Possible return codes:
| HTTP Status Code | Message | 
| ---------------- | ------- |
| 201 | Created |
| 500 | Default error cases | 

Example: 
```sh
curl -XPOST -H "Content-Type: application/json" -d '{"name": "R2D2-v2"}' http://localhost:8080/api/v1/robots
```
Example Response: 
```sh
{
    "status": "CREATED",
    "message": "robot created",
    "data": {
        "id": 2,
        "name": "R2D2-v2-v2",
        "location": null,
        "face": null
    }
}
```
##### 4.4. Retrieve a robot by its id.
This endpoint will retrieve the list of children category based on a given category UUID.

| HTTP Method | Path |
| ------ | ------ |
| GET | /api/v1/robots/<id> |

Possible return codes: 
| HTTP Status Code | Message | 
| ---------------- | ------- |
| 200 | OK |
| 404 | Robot Not Found | 

Example Request:
```sh
curl -XGET http://localhost:8080/api/v1/robots/1
```
Example Response: 
```sh
{
    "status": "OK",
    "message": "robot returned",
    "data": {
        "id": 1,
        "name": "R2D2-v2",
        "location": null,
        "face": null
    }
}
```
##### 4.5. Place a robot on a given coordinate with a direction.
This endpoint will place the robot to the given position with the given direction.
| HTTP Method | Path |
| ------ | ------ |
| POST | /api/v1/robot/<id>/place |

Request Body Parameters:
| Parameter name | Type | Possible Values | 
| ------ | ----- | ----- |
| location.x | int | * |
| location.y | int | * |
| direction | String | <NORTH\|SOUTH\|EAST\|WEST>| 

Possible return codes:
| HTTP Status Code | Message | 
| ---------------- | ------- |
| 200 | OK |
| 404 | Robot not found |
| 404 | Board not found |
| 409 | Out of border |

Example:
```sh
curl -XGET -H "Content-Type: application/json" -d '{"location":{"x": 3, "y": 2}, "face": "NORTH"}' http://localhost:8080/api/v1/robot/<id>/place
```
##### 4.6. Robot actions #1: MOVE.
This endpoint allows the robot to move one cell in the current direction of the robot. If the robot is already at one of the edges of the board, move not possible exception returns.

| HTTP Method | Path |
| ------ | ------ |
| POST | /api/v1/robot/<id>/move |

Possible return codes:
| HTTP Status Code | Message | 
| ---------------- | ------- |
| 200 | OK |
| 409 | Robot Move Not Possible |

Example Request:
```sh
curl -XPOST http://localhost:8080/api/v1/robot/1/move
```
Example Response:
```sh
{
    "status": "OK",
    "message": "robot moved",
    "data": {
        "id": 1,
        "name": "R2D2-v2",
        "location": {
            "x": 3,
            "y": 3
        },
        "face": "NORTH"
    }
}
```
##### 4.7. Robot actions #2: LEFT.
This endpoint allows the robot to turn left, i.e. rotate in current cell towards to the left.
| HTTP Method | Path |
| ------ | ------ |
| POST | /api/v1/robot/<id>/left |

Possible return codes:
| HTTP Status Code | Message | 
| ---------------- | ------- |
| 200 | OK |

Example Request:
```sh
curl -XPOST http://localhost:8080/api/v1/robot/1/left
```
Example Response:
```sh
{
    "status": "OK",
    "message": "robot turned to left",
    "data": {
        "id": 1,
        "name": "R2D2-v2-v2",
        "location": {
            "x": 3,
            "y": 3
        },
        "face": "WEST"
    }
}
```
##### 4.8. Robot actions #3: RIGHT.
This endpoint allows the robot to turn right, i.e. rotate in current cell towards to the right.
| HTTP Method | Path |
| ------ | ------ |
| POST | /api/v1/robot/<id>/right |

Possible return codes:
| HTTP Status Code | Message | 
| ---------------- | ------- |
| 200 | OK |

Example Request:
```sh
curl -XPOST http://localhost:8080/api/v1/robot/1/right
```
Example Response: 
```sh
{
    "status": "OK",
    "message": "robot turned to right",
    "data": {
        "id": 1,
        "name": "R2D2-v2-v2",
        "location": {
            "x": 3,
            "y": 3
        },
        "face": "NORTH"
    }
}
```
##### 4.9. Robot actions #4: REPORT.
This method allows to return a report string. It's quite similar to the GET robot call but it also includes a report message.
| HTTP Method | Path |
| ------ | ------ |
| GET | /api/v1/robot/<id>/report |

Possible return codes:
| HTTP Status Code | Message | 
| ---------------- | ------- |
| 200 | OK |

Example Request:
```sh
curl -XGET http://localhost:8080/api/v1/robot/1/report
```
Example Response: 
```sh
{
    "status": "OK",
    "message": "Robot ID#1 Output: 3.0, 3.0, NORTH",
    "data": {
        "id": 1,
        "name": "R2D2-v2-v2",
        "location": {
            "x": 3,
            "y": 3
        },
        "face": "NORTH"
    }
}
```
### 5. Future works.
 - Better serialization of the Board and Robot POJO.
 - Better flow for creating/resetting boards and robots.
 - Better error handling (ex. the case of missing parameters in PLACE action). 
 - Bugfixes if any found.
 - Writing some integration tests.

[//]: # (This is the end... and some useful links)
   [Redis]: <https://redis.io>