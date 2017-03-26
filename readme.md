### File storage

#### Overview
The "File Storage" component is a part of large system, which consists of many various microservices. Therefore component should also be designed as microservice. This component provides persistent storage for arbitrary files - it is used by other components / frontend clients to store and retrieve files.

#### Front-End requirements: 
dashboard for user with all saved files.
search by name, extension, date of uploaded
sorting
form for file upload
add, edit comments for file (1 file can have several comments)
file downloading

#### Back-end requirements:
store file with following attributes :
binary data
file name
extension
mime type
date of upload
store comments
retrieving file descriptions
retrieving file for download
garbage-collection – scheduled cleanup of files older than 2 days


### Architecture and technical considerations
Files are stored using Mongo GridFS in manner of binary and even metadata storage.
Interface should follow orthodox REST API + Hateoas
Component should be based on Spring Cloud, Spring Boot, Spring Data
TDD, BDD tests should be included
For front end use one frameworks (Angular, React or Backbone) 
Use CSS Processor (Sass or Less)