# Error Management

Restdude automatically intercepts, persists and responds with a proper JSON representation for all own exceptions when handling a REST request. It also allows client applications to persist their own internal exceptions. The corresponding entities are SystemError and ClientError respectively.

## Datamodel

The tables bellow describe the datamodel for all errors:

### BaseError Properties

BaseError properties are common to all error types.

Property     | Type   | Description
------------ | ------ | -------------------
createdDate  | LocalDateTime | Date created
lastModifiedDate  | LocalDateTime | Date last modified
createdBy | User | Created by
title   | String | Message for users
remoteAddress  | String | The address the request originated from
user | User | User in context
userAgent    | UserAgent | A record corresponding to the UA string provided with the request
errorLog     | ErrorLog  | The error llog or stacktrace

### SystemError Properties

SystemErrors are created exclusively by the system, i.e. without manual intervention, to handle and inform the user about runtime exceptions. hey may be persisted automatically according to restdude.validationErrors.system.persist* configuration properties. System validationErrors have a many-to-one relationship with ErrorLog records, as those are shared based on their hash to save space.

Property     | Type   | Description
------------ | ------ | -------------------
requestMethod  | String | The HTTP request method
requestUrl  | String | The HTTP request URL, relative to system base URL
httpStatusCode | Integer | The HTTP response status code
validationErrors   | Set<ConstraintViolationEntry> | Transient, only contained in relevant JSON HTTP responses

#### HTTP Status Codes

The table bellow shows the HTTP status codes used for each exceptiontype

Class                                | Code  | Description
------------------------------------ | ----- | ------------
NoSuchRequestHandlingMethodException | 404 | Not found
AuthenticationException | 401 | Unauthorized
UsernameNotFoundException | 401 | Unauthorized
AccessDeniedException | 401 | Unauthorized
ObjectNotFoundException | 404 | Not found
EntityNotFoundException | 404 | Not found
EntityExistsException | 409 | Conflict
HttpRequestMethodNotSupportedException | 405 | Method not allowed
HttpMediaTypeNotSupportedException | 415 | Unsupported media type
HttpMediaTypeNotAcceptableException | 406 | Not acceptable
MissingPathVariableException | 500 | Internal server error
DataIntegrityViolationException | 400 | Bad request
MissingServletRequestParameterException | 400 | Bad request
ServletRequestBindingException | 400 | Bad request
ValidationException | 400 | Bad request
ConversionNotSupportedException | 500 | Internal server error
TypeMismatchException | 400 | Bad request
HttpMessageNotReadableException | 400 | Bad request
HttpMessageNotWritableException | 500 | Internal server error
MethodArgumentNotValidException | 400 | Bad request
MissingServletRequestPartException | 400 | Bad request
BindException | 400 | Bad request
NoHandlerFoundException | 404 | Not found
AsyncRequestTimeoutException | 503 | Service unavailable
RuntimeException | 500 | Internal server error
Exception | 500 | Internal server error


### ClientError Properties

Client errors are created upon a client application's request and refer to exceptions occurred specifically within client application code.

Property     | Type   | Description
------------ | ------ | -------------------
screenshotUrl  | String | A client application screenshot (uploaded via seperate multipart request) demonstrating the issue
description  | String | The error description provided by the user, if any

### UserAgent Properties

UserAgent instances are used to normalize browser signatures as a seperate entity.

Property     | Type   | Description
------------ | ------ | -------------------
id  | String | The signature hash, automatically created
value  | String | UA string signature value

### ErrorLog Properties

Used to persist error stacktraces using the corresponding hash as the ID. The generated hash ignores line numbers (in case of SystemError) and is thus tolerant of small code changes, like adding a comment line.

Property     | Type   | Description
------------ | ------ | -------------------
id           | String | The stacktrace hash, automatically generated
rootCauseMessage  | String | The root cause message
firstOccurred  | LocalDateTime | First occurrence date
lastOccurred   | LocalDateTime | Last occurrence date
errorCount     | Integer  | Readonly, the number of errors corresponding to this stacktrace
stacktrace     | String   | The actual stacktrace

## API

The API provides SCRUD methods as typically provided by resthub controllers, see the MDD module. The base request mapping paths are

    /api/rest/allErrors
    /api/rest/systemErrors
    /api/rest/clientErrors

## Web UI

The merionette module provides a UI for managing records of all error types.


