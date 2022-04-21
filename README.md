# Spring-GraphQL
Simple realtime chat service with GraphQL with Spring Boot, Redis
<br>
Using GraphQL subscription and Redis pub/sub, implemented realtime chatting api.
<br><br>
<img src="https://user-images.githubusercontent.com/17774927/164505897-2f62dc35-6441-4bfc-a4be-964191397c1d.gif">

## Stacks
<div>
  <img src="https://img.shields.io/badge/kotlin-7f5eff?style=for-the-badge&logo=kotlin&logoColor=white">
  <img src="https://img.shields.io/badge/graphql-e10098?style=for-the-badge&logo=graphql&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white">
  <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
  <img src="https://img.shields.io/badge/mongodb-47A248?style=for-the-badge&logo=mongodb&logoColor=white">
  <img src="https://user-images.githubusercontent.com/17774927/164499168-8c99d9d1-4145-4c6d-86be-2e8c498c1459.png">
</div>

## Run
    
1. Run databases


Before running the application, be sure to fire up Redis and MongoDB with:


    docker-compose up -d


2. Build & Run java application


    ./gradlew bootjar & java -jar /build/libs/*.jar &



## Playground
You can test graphql apis through graphiql GUI.
<br>
After starting the application, access the URL below.


    http://localhost:8080/graphiql
