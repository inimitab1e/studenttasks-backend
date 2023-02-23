# studenttasks-backend
### Project Description
This is a local server for authentiacation in my Android apps.

# Start instruction
1. You must create PostgreSQL database on your computer. You can use [pgAdmin](https://www.pgadmin.org/) to create database. [Here](https://www.pgadmin.org/docs/pgadmin4/latest/index.html) you can see pgAdmin docs. First, create database (you can name it as you like) and create 2 tables: `tokens` and `users`. Use .sql files in folder "studenttasks-backend/database/".
2. Create file `database.properties` in root folder. You must specify next params:
```properties
#this is database.properties file
dataSourceClassName=org.postgresql.ds.PGSimpleDataSource
dataSource.user='user_name'
dataSource.password='your_password'
dataSource.portNumber='your_port'
dataSource.databaseName='database_name'
dataSource.serverName=localhost
```
Now you good to go.

## Endpoints

After successful starting server will be available at `http://localhost:8080`. But you can easly change it in file "studenttasks-backend/src/main/kotlin/ru/studenttask/Application.kt" by changing "host" params.

### Authorization

* `POST /register`: registration
* `POST /login`: login
* `POST /refresh`: refreshing ACCESS and REFRESH tokens

### User features

* `GET /validity`: tokens validity
* `GET /users`: list of users
