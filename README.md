# Getting Started

This project represents the implementation of the Customer Referral Program.

## Requirements

- Install Java JDK 11 https://www.oracle.com/it/java/technologies/javase/jdk11-archive-downloads.html
- Install Maven https://maven.apache.org/download.cgi
- Docker runtime https://docs.docker.com/get-docker/

## How to run

From the command line, launch this command for starting the docker Postgres containers (both for main and test):

```
docker-compose -f docker-compose-dev.yml up
```

For code compiling and test execution, launch this command:

```
mvn install
```

For running the application, launch this command:

```
cd target
java -jar CustomerReferralProgramAPI-0.0.1-SNAPSHOT.jar
```

After the correct startup, you should see "CRP Api" at this link: http://localhost:8080/

The Swagger documentation is available at this other link: http://localhost:8080/swagger-ui.html

## Choices and decisions

The application has been developed using the Spring Boot framework, starting from a blueprint, generated using https://start.spring.io/ with
the addition of:

- a **persistence** layer (based on the JPA ORM library, over Postgres)
- a **REST** interface (based on Spring Web).

About the folder organization, I kept this very simple, with some hints taken from DDD and clean architecture. I decided to avoid a pure DDD
because of the current simplicity of the application. As a consequence, all the infrastructure stuff is:

- in the `api` folder and sub-folders, for what regards the inbound ports
- in the `database` folder and sub-folders, for what concerns the outbound ports.

The business logic is currently in the `service` folder.

I decided to introduce a concept of **event**, with the related folder, in order to define a first draft of an asynchronous way for creating
the relation between the sign-up process and the credit assignment, in according to the customer referral program.

At the moment, a **security** layer has not been introduced. For this reason, all the API methods could be called without any authorization.

About the **documentation**, I added a simple Java DOC and the API swagger. Considering the current simple flow, I think it is enough for a
full comprehension by a front-end developer. As a further documentation, and even for checking the implemented behaviour, I add some
integration and unit tests.

For the version of the persistence layer, I adopted Flyway (see the first SQL under `src/main/resources/db/migration`).

## Doubts

Under a technical point of view, at the moment the "credit" is an integer field in the `AppUser` class. It looks enough for covering the
actual requirements. Is it better to represent the "credit" as a more specific object, with different components? Is it worth to define it
as a float? More under a business perspective, how is the logic of accumulation? Should it be rechargeable for each new referral code? Does
it exist a limit? Is the credit of the signer user valid only if the code is not consumed? I made this assumption but maybe the signer
should be always awarded. Is the referral code always valid? Does it exist an expiration rule?

At the moment, I implemented only the sign-up logic. No sign-in (login) logic is present.

## How to do use cases

*Alice, an existing user, creates a referral. She gets a link that has a unique code in it. She emails that link to 5 of her friends.*

For doing this:

- create a new user for Alice by calling `POST /api/v1/user` with this object as a body

```
{
  "password": "<a password>",
  "username": "<the Alice's username>"
  }
```

- create a new referral code by calling `POST /api/v1/referral` with this object as a body

```
{
  "appUserId": <use the id of the user just created>,
  "code": "<the code identifier Alice wants to share with our friends>",
}
```

*Bob, one of Alice’s friends, clicks on the link. He goes through the signup process to create a new account. Once he has created his
account, he sees that he has $10 in credit.*

For emulating this flow,

- create a new user for Bob by calling `POST /api/v1/user` with this object as a body

```
{
  "myReferralCode": "<the Alice's code>",
  "password": "<a password>",
  "username": "<the Bob's username>"
  }
```

- check the Bob's credit by calling `GET /api/v1/user/{id}`, using the Bob's id as the id path variable

*Four more people follow the same process as Bob, clicking on the link Alice sent them. They all get $10 in credit. Once the fifth person
has signed up, Alice sees that she has $10 in credit.*

Repeat the previous flow more times. At the end, checks the users' status by calling `GET /api/v1/user`. Do the same for seeing the
referrals' status by calling `GET /api/v1/referral`. Here, you should see the status of `consumed` of the Bob's code.

*Jeffrey signs up using a link that does not contain a unique referral code. After he creates a new account, he has $0 in credit.*

Repeat the first flow, by creating the Jeffrey's user without specifying the `myReferralCode` field:

- `POST /api/v1/user` with this object as a body

```
{
  "password": "<a password>",
  "username": "<the Jeffrey's username>"
  }
```

and by checking the Jeffrey's status by calling `GET /api/v1/user/{id}`.

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.6/maven-plugin/reference/html/)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.5.6/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.5.6/reference/htmlsingle/#boot-features-developing-web-applications)
* [Flyway Migration](https://docs.spring.io/spring-boot/docs/2.5.6/reference/htmlsingle/#howto-execute-flyway-database-migrations-on-startup)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

