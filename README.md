Project 3 - HTTP Server
=======================

In this project, I implemented an HTTP server, two web applications, and a full suite of tests using the following:

- Concurrency and threads
- Sockets
- JUnit and testing
- Remote deployment

## Part 1 - HTTP Server

For Part 1 of this project I implemented an HTTP server. 

### Details

1. Implemented raw sockets for the project, avoiding the use of external libraries for request parsing.
2. Mainly supports GET and POST requests, with other HTTP methods resulting in a 405 Method Not Allowed status code.
3. Supports multithreading, assigning each incoming request to a different thread for efficient handling.
4. Designed as a flexible server capable of supporting various web applications.

### Design

- Create a `Handler` interface with a `handle` method. Each web application will implement a different set of handlers, for example a search application will support a `SearchHandler` and a chat application would have a `ChatHandler`. 
- Support an `addMapping` method that will map a URI path to a specific `Handler` instance. When a new request is made to the server the server will retrieve from the mapping the `Handler` appropriate for the path in the request URI.


## Part 2 - Search Application

For Part 2 of this assignment, I developed an application enabling users to search the 'InvertedIndex', and it provides support for the following four APIs:
#### `GET /reviewsearch`

This request will return a web page containing, at minimum, a text box and button. When the user enters a query in the text box and clicks the button the `POST` method described below will be called.

#### `POST /reviewsearch`

The body of this request will look as follows: `query=term` where the value is a URL-encoded string.

Will return a web page listing all of the review search results.

#### `GET /find`

This request will return a web page containing, at minimum, a text box and button. When the user enters a message in the text box and clicks the button the `POST` method described below will be called.

#### `POST /find`

The body of this request will look as follows: `asin=123456789` where the value is a URL-encoded string.

Will return a web page listing all of the review and qa documents with the provided ASIN.

## Part 3 - Chat Application

For Part 3 of this assignment, I implemented an application that will allow a user to anonymously post a message to the Slack team and support the following two APIs:

#### `GET /slackbot`

This request will return a web page containing, at minimum, a text box and button. When the user enters a message in the text box and clicks the button the `POST` method described below will be called.

#### `POST /slackbot`

The body of this request will look as follows: `message=test+message` where the value is a URL-encoded string.

This request will trigger a post of the message specified by the body to the Slack channel `#project3`. Regardless of the user who types the message it will always appear to have come from this application.

## Part 4 - Tests

For Part 4, I demonstrated unit tests, integration tests, and system tests to test my solution. All tests executed through JUnit.

### Unit Tests

Unit tests test methods that perform complex processing or logic. 

### Integration Tests

Integration tests generally test a path of execution through a program. 
### System Tests

System tests test the end-to-end execution of your program.



