# thumb-creator

This project server side component for thumb-creator-web project. This module resizes the image and uploads to s3.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You will need followings to run the project

```
1. Java (java >= 8)
2. Kafka (kafka >=2.1.0)
3. Mysql (mysql ~ 8.0.11)
3. Maven (mvn ~ 3.5.4)
```

### Installing

A step by step series of examples that tell you how to get a development env running

These are the following steps to install the project.

```
1. mvn clean install
```
This will create snapshot jar in target folder. To run this use
```
1. java -jar processor-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```
This is will start a Java application which reads data from kafka, resizes image and write back the data to s3

## Overview
![alt text](https://github.com/fynryder/thumb-creator/blob/master/controlflow.jpg)
![alt text](https://github.com/fynryder/thumb-creator/blob/master/Untitled%20Diagram.jpg)

## Built With

* [Maven](https://https://maven.apache.org/) - Dependency Management
* [Java](https://www.java.com/en/download/) - Progamming Langauge and Runtime
* [MySQL](https://materializecss.com/) - Data Persistence
* [Kafka](http://kafka.apache.org) - Messaging Queue
* [Hibernate](http://hibernate.org/) - ORM

## Authors

* **Mukesh Verma** - *Initial work* - [thumb-creator](https://github.com/fynryder/thumb-creator-web)

See also the list of [contributors](https://github.com/fynryder/thumb-creator-web/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Stackoverflow
* Google
