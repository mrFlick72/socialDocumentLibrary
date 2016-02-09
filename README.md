# Social Document Library 1.0

The Software is been developed in a microservice architecture style.
The main projectual philosophy was the User Centered Design (UCD).
The application is a document Library for IT books, in the documentation I explain in more details the scope and the 
developed life cycle of the project. Maninly it is composed by two app the app for final user and app for employee user.
The application was developed for HCI exam for the Master Degree in "La Sapienza" of Rome

# Main Technologies 
<ul>
    <li>User Center Design</li>
    <li>REST: Spring MVC/HEATOAS for REST WS </li> 
    <li>Messaging: WebSoket, JMS with Apache ActiveMQ as messaging middleware</li>
    <li>EIP: Spring integration</li>
    <li>Microservice architecture</li>
    <li>NoSql: MongoDB</li>
</ul>

# Spring Projects
<ul>
    <li>Spring</li> 
    <li>Spring Boot</li>
    <li>Spring MVC</li>    
    <li>Spring HATEOAS</li> 
    <li>Spring Security</li>
    <li>Spring Integration</li>
    <li>Spring Data JPA</li>
    <li>Spring Data MongoDB</li>
</ul>

# Building Projects
<ul>
    <li>Apache Maven</li> 
    <li>Gradle</li>
</ul>

# Web Tecnologies
<ul>
    <li>WebJars</li>
    <li>Thymeleaf</li> 
    <li>Angular JS</li>
</ul>

# Building instructions
First of all you have make mvn clean install of the common-validator project for have the library avaiable on th elocal maven repository.
Then for build project you have <a href="https://maven.apache.org/">Apache Maven</a> and <a href="http://gradle.org/">Gradle</a>.
This project is based on Mongo DB, MySql and Active MQ, for this reason you have this components up and running in your machine.
Being a Spring Boot centric projects all main configurations was present in the application.properties under src/main/resources directory of all projects.
In this place you can find the basic properties for configure the project. Only for ActiveMQ and Mysql you can don't have up and running during the building becouse I used a in memory sql engine and 
the in memory messaging broker for the test. However Mongo DB must has up and running even for test.
After the building you have start all the microservice and access on the browser on the url http://localhost:9090/socialDocumentLibrary/bookUserList for final user application 
and on http://localhost:7070/adminDocumentlibraryClient/bookList for the admin application.

The admin user credential are: user:admin, password: admin. Indeed for the Final user you have create a new account.
