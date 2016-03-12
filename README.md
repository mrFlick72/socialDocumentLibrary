# Social Document Library 2.0

The Software is been developed in a microservice architecture style.
The main projectual philosophy was the User Centered Design (UCD).
The application is a document Library for IT books, in the documentation I explain in more details the scope and the 
developed life cycle of the project. Maninly it is composed by two app the app for final user and app for employee user.
The application was developed for HCI exam for the Master Degree in "La Sapienza" of Rome.

# In the new version I added the uso of Spring Cloud technology, for now the system could be instable becouse is under development

# Main Technologies 
<ul>
    <li>User Center Design</li>
    <li>REST: Spring MVC/HEATOAS for REST WS </li> 
    <li>Messaging: WebSoket, JMS with Apache ActiveMQ as messaging middleware</li>
    <li>EIP: Spring integration</li>
    <li>Microservice architecture</li>
    <li>NoSql: MongoDB</li>
    <li>Docker</li>
</ul>

# Spring Projects
<ul>
    <li>Spring</li> 
    <li>Spring Boot</li>
    <li>Spring MVC</li>    
    <li>Spring Cloud</li> 
    <li>Spring Cloud Netflix : Eureka, Zuul, Ribbon</li> 
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
First of all you have make mvn clean install of the common-validator project for have the library avaiable on the local maven repository.
Then for build project you have <a href="https://maven.apache.org/">Apache Maven</a> and <a href="http://gradle.org/">Gradle</a>.
In this version I start to integrate docker. Now the main configuration suppose that you use docker for redis server, mongo, activeMQ and MySql.
The use the final project you have installed on your pc docker. For this propouse you can follow this link <a href="https://docs.docker.com/mac/">hear</a>.
After that you have installed Docker on your pc you can start the Docker Toolbox and use this subsequnt command:
<ul>
    <li>docker run -p 6379:6379 redis</li> 
    <li>docker run -p 27017:27017 mongo</li> 
    <li>docker run -d -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 mysql</li> 
    <li>docker run --name='activemq' -p 8161:8161 -p 61616:61616 -p 61613:61613 webcenter/activemq:5.13.1</li> 
</ul>

For Mysql after that the docker conteiner is up you must create the mysql schema for the app. For this propuse you can typing 

mysql -h 192.168.99.100 -u root -proot

create database user_document_library_client;
create database admin_document_library_client;

the default ip of docker in my machine is 192.168.99.100 for this reason please keep caution becouse if your machine will pubblish a different ip you have update the reference in the projects.


The admin user credential are: user:admin, password: admin.
Indeed for the Final user you have create a new account.
