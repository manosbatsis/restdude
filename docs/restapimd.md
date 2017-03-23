---
layout: default
title: REST API MD
description: REST API Documentation
isHome: false
---

# Restdude API Reference 2.1.2-SNAPSHOT


<a name="overview"></a>
## Overview
Automatically-generated documentation based on [Swagger](http://swagger.io/) and created by [Springfox](http://springfox.github.io/springfox/).


### Version information
*Version* : 2.1.2-SNAPSHOT


### Contact information
*Contact* : Manos Batsis


### License information
*License* : LGPLv3  
*License URL* : https://www.gnu.org/licenses/lgpl-3.0.en.html  
*Terms of service* : urn:tos


### URI scheme
*Host* : localhost:8080  
*BasePath* : /restdude


### Tags

* Auth : Authentication operations
* AuthAccount : User account operations
* Client Errors : Client Error Operations
* ContactDetails : Contact details
* Continents : Operations about continent
* Countries : Operations about countries
* ErrorLogs : Stacktrace or other error log details.
* Errors : Generic error information (readonly)
* Friends : Friend searches
* Friendships : Operations about friendships
* Hosts : Operations about hosts
* Invitations : Invite new users
* Localities : Locality operations
* OAuth : Service provider connection operations
* PersonalDetails : Personal information operations
* PostalCodes : Postal code operations
* RegistrationCodeBatches : Codes management (admin, operator)
* Roles : Operations about roles
* STOMP Sessions : STOMP Session Operations
* System Errors : System Error Operations (readonly)
* User Agents : Collection of UA signatures
* User Credentials : Operations about user credentials
* UserRegistrationCode : User registration codes (read-only)
* Users : User management operations
* endpoint-mvc-adapter : Endpoint Mvc Adapter
* environment-mvc-endpoint : Environment Mvc Endpoint
* health-mvc-endpoint : Health Mvc Endpoint
* heapdump-mvc-endpoint : Heapdump Mvc Endpoint
* jwt-controller : Jwt Controller
* metrics-mvc-endpoint : Metrics Mvc Endpoint




<a name="paths"></a>
## Resources

<a name="auth_resource"></a>
### Auth
Authentication operations


<a name="createusingpost_2"></a>
#### Login
```
POST /api/auth/userDetails
```


##### Description
Login using a JSON object with email/password properties.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**resource**  <br>*required*|resource|[Login Submission](#login-submission)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UserDetails](#userdetails)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="rememberusingget"></a>
#### Remember
```
GET /api/auth/userDetails
```


##### Description
Login user if remembered


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UserDetails](#userdetails)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="deleteusingdelete"></a>
#### Logout
```
DELETE /api/auth/userDetails
```


##### Description
Logout and forget user


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="verifyusingpost"></a>
#### Verify
```
POST /api/auth/userDetails/verification
```


##### Description
Validation utility operation, used to verify the user based on current password.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**resource**  <br>*required*|resource|[Login Submission](#login-submission)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UserDetails](#userdetails)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="createusingpost_3"></a>
#### Login
```
POST /apiauth/userDetails
```


##### Description
Login using a JSON object with email/password properties.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**resource**  <br>*required*|resource|[Login Submission](#login-submission)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UserDetails](#userdetails)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="rememberusingget_1"></a>
#### Remember
```
GET /apiauth/userDetails
```


##### Description
Login user if remembered


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UserDetails](#userdetails)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="deleteusingdelete_1"></a>
#### Logout
```
DELETE /apiauth/userDetails
```


##### Description
Logout and forget user


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="verifyusingpost_1"></a>
#### Verify
```
POST /apiauth/userDetails/verification
```


##### Description
Validation utility operation, used to verify the user based on current password.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**resource**  <br>*required*|resource|[Login Submission](#login-submission)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UserDetails](#userdetails)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="authaccount_resource"></a>
### AuthAccount
User account operations


<a name="createusingpost"></a>
#### Register new account
```
POST /api/auth/account
```


##### Description
Register a new user


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**resource**  <br>*required*|resource|[UserAccountRegistration](#useraccountregistration)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[User](#user)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/xml`
* `application/json`


<a name="updateusingput"></a>
#### Confirm registration email and/or update account password
```
PUT /api/auth/account
```


##### Description
Confirm registration email and/or update, reset, or request to reset an account password. The operation handles three cases. 1) When logged-in, provide currentPassword, password and passwordConfirmation to immediately change password. 2) when anonymous, provide resetPasswordToken, password and passwordConfirmation to immediatelychange password. 3) when anonymous, provide email or username to have a password reset token and link sent to your inbox.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**resource**  <br>*required*|resource|[EmailConfirmationOrPasswordResetRequest](#emailconfirmationorpasswordresetrequest)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UserDetails](#userdetails)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/xml`
* `application/json`


<a name="updateusernameusingput"></a>
#### Update username
```
PUT /api/auth/account/username
```


##### Description
Updates the username of the curent user and updates the auth token cookie.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**resource**  <br>*required*|resource|[UsernameChangeRequest](#usernamechangerequest)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UserDetails](#userdetails)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/xml`
* `application/json`


<a name="createusingpost_1"></a>
#### Register new account
```
POST /api/auth/accounts
```


##### Description
Register a new user


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**resource**  <br>*required*|resource|[UserAccountRegistration](#useraccountregistration)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[User](#user)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/xml`
* `application/json`


<a name="updateusingput_1"></a>
#### Confirm registration email and/or update account password
```
PUT /api/auth/accounts
```


##### Description
Confirm registration email and/or update, reset, or request to reset an account password. The operation handles three cases. 1) When logged-in, provide currentPassword, password and passwordConfirmation to immediately change password. 2) when anonymous, provide resetPasswordToken, password and passwordConfirmation to immediatelychange password. 3) when anonymous, provide email or username to have a password reset token and link sent to your inbox.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**resource**  <br>*required*|resource|[EmailConfirmationOrPasswordResetRequest](#emailconfirmationorpasswordresetrequest)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UserDetails](#userdetails)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/xml`
* `application/json`


<a name="updateusernameusingput_1"></a>
#### Update username
```
PUT /api/auth/accounts/username
```


##### Description
Updates the username of the curent user and updates the auth token cookie.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**resource**  <br>*required*|resource|[UsernameChangeRequest](#usernamechangerequest)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UserDetails](#userdetails)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/xml`
* `application/json`


<a name="client-errors_resource"></a>
### Client Errors
Client Error Operations


<a name="plainjsonpostusingpost_1"></a>
#### Create a new resource
```
POST /api/rest/clientErrors
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**model**  <br>*required*|model|[ClientError](#clienterror)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[Resource«ClientError»](#73b8cd46e02faa249f33a8c68c3216b8)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsongetpageusingget_1"></a>
#### Search for resources (paginated).
```
GET /api/rest/clientErrors
```


##### Description
Find all resources matching the given criteria and return a paginated collection.Predefined paging properties are _pn (page number), _ps (page size) and sort. All serialized member names of the resource are supported as search criteria in the form of HTTP URL parameters.


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[PagedModelResources«ClientError»](#7433da03f116cb2139ff79212f01129d)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="optionsusingoptions_1"></a>
#### Get CORS headers
```
OPTIONS /api/rest/clientErrors
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_1"></a>
#### Get JSON Schema
```
GET /api/rest/clientErrors/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_1"></a>
#### Get UI schema
```
GET /api/rest/clientErrors/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="updatefilesusingpost"></a>
#### Update files
```
POST /api/rest/clientErrors/{id}/files
```


##### Description
The files are saved using the parameter names of the multipart files contained in this request. These are the field names of the form (like with normal parameters), not the original file names.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**id**  <br>*required*|id|string|
|**Body**|**request**  <br>*optional*|request|[MultipartHttpServletRequest](#multiparthttpservletrequest)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ClientError](#clienterror)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `multipart/*`


##### Produces

* `application/xml`
* `application/json`


<a name="jsonapigetbyidusingget_1"></a>
#### Find by pk
```
GET /api/rest/clientErrors/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«ClientError,string»](#c9eabaff21c531951357eb93831ff776)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsonputusingput_1"></a>
#### Update a resource
```
PUT /api/rest/clientErrors/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[ClientError](#clienterror)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«ClientError»](#73b8cd46e02faa249f33a8c68c3216b8)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapideleteusingdelete_1"></a>
#### Delete a resource
```
DELETE /api/rest/clientErrors/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="jsonapipatchusingpatch_1"></a>
#### Patch (partially plainJsonPut) a resource given as a JSON API Document
```
PATCH /api/rest/clientErrors/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«ClientError,string»](#c9eabaff21c531951357eb93831ff776)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«ClientError,string»](#c9eabaff21c531951357eb93831ff776)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetrelatedusingget_2"></a>
#### Find related by root pk
```
GET /api/rest/clientErrors/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«ClientError»](#118c438bfd8447ff0a28faaf0ab0a63e)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsongetrelatedusingget_3"></a>
#### Find related by root pk
```
GET /api/rest/clientErrors/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«ClientError»](#118c438bfd8447ff0a28faaf0ab0a63e)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="contactdetails_resource"></a>
### ContactDetails
Contact details


<a name="plainjsonpostusingpost_2"></a>
#### Create a new resource
```
POST /api/rest/contactDetails
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**model**  <br>*required*|model|[ContactDetails](#contactdetails)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[Resource«ContactDetails»](#96603a995537a0046766cce56a0b041f)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsongetbyidsusingget_2"></a>
#### Search by pks
```
GET /api/rest/contactDetails
```


##### Description
Find the set of resources matching the given identifiers.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**pks**  <br>*required*||string|
|**Query**|**pks[]**  <br>*required*|pks[]|< string > array(multi)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResources«ContactDetails»](#8762ba8c004d6b87b95956d9f2b94a35)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="optionsusingoptions_2"></a>
#### Get CORS headers
```
OPTIONS /api/rest/contactDetails
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_2"></a>
#### Get JSON Schema
```
GET /api/rest/contactDetails/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_2"></a>
#### Get UI schema
```
GET /api/rest/contactDetails/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetbyidusingget_2"></a>
#### Find by pk
```
GET /api/rest/contactDetails/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResource«ContactDetails»](#9ef43d1ca8eac34613540d1bdf836772)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsonputusingput_2"></a>
#### Update a resource
```
PUT /api/rest/contactDetails/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[ContactDetails](#contactdetails)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«ContactDetails»](#96603a995537a0046766cce56a0b041f)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapideleteusingdelete_2"></a>
#### Delete a resource
```
DELETE /api/rest/contactDetails/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="jsonapipatchusingpatch_2"></a>
#### Patch (partially plainJsonPut) a resource given as a JSON API Document
```
PATCH /api/rest/contactDetails/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«ContactDetails,string»](#b25d6e708aa2c3e2be1df17d5e3a9251)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«ContactDetails,string»](#b25d6e708aa2c3e2be1df17d5e3a9251)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetrelatedusingget_4"></a>
#### Find related by root pk
```
GET /api/rest/contactDetails/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«ContactDetails»](#367e02371b461d9a61f89e4fbcc1b98d)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapigetrelatedusingget_5"></a>
#### Find related by root pk
```
GET /api/rest/contactDetails/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«ContactDetails»](#10fc875f5c1fa5b72937f5046c4ae1d8)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="continents_resource"></a>
### Continents
Operations about continent


<a name="jsonapipostusingpost_3"></a>
#### Create a new JSON API Resource
```
POST /api/rest/continents
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«Continent,string»](#a6420b18357b50929f7db6443f6fd529)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[JsonApiModelResourceDocument«Continent,string»](#a6420b18357b50929f7db6443f6fd529)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetallusingget_3"></a>
#### Get the full collection of resources (no paging or criteria)
```
GET /api/rest/continents
```


##### Description
Find all resources, and return the full collection (i.e. VS a page of the total results)


##### Parameters

|Type|Name|Schema|Default|
|---|---|---|---|
|**Query**|**page**  <br>*required*|enum (no)|`"no"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResources«Continent»](#0d94a4e6f9389b6a4d3f8ebcb0a245e8)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="optionsusingoptions_3"></a>
#### Get CORS headers
```
OPTIONS /api/rest/continents
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_3"></a>
#### Get JSON Schema
```
GET /api/rest/continents/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_3"></a>
#### Get UI schema
```
GET /api/rest/continents/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetbyidusingget_3"></a>
#### Find by pk
```
GET /api/rest/continents/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResource«Continent»](#4e940515b73bab5898fbcee055f5441b)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsonputusingput_3"></a>
#### Update a resource
```
PUT /api/rest/continents/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[Continent](#continent)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«Continent»](#7274c721e900aea845a066c08f101d27)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapideleteusingdelete_3"></a>
#### Delete a resource
```
DELETE /api/rest/continents/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="jsonapipatchusingpatch_3"></a>
#### Patch (partially plainJsonPut) a resource given as a JSON API Document
```
PATCH /api/rest/continents/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«Continent,string»](#a6420b18357b50929f7db6443f6fd529)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«Continent,string»](#a6420b18357b50929f7db6443f6fd529)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetrelatedusingget_6"></a>
#### Find related by root pk
```
GET /api/rest/continents/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«Continent»](#3b9a5ac152f43b0a2b0da5bdb016b8bd)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsongetrelatedusingget_7"></a>
#### Find related by root pk
```
GET /api/rest/continents/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«Continent»](#3b9a5ac152f43b0a2b0da5bdb016b8bd)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="countries_resource"></a>
### Countries
Operations about countries


<a name="jsonapipostusingpost_4"></a>
#### Create a new JSON API Resource
```
POST /api/rest/countries
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«Country,string»](#adbc7ab7820cfd03fefcaf75b93da36c)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[JsonApiModelResourceDocument«Country,string»](#adbc7ab7820cfd03fefcaf75b93da36c)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetallusingget_4"></a>
#### Get the full collection of resources (no paging or criteria)
```
GET /api/rest/countries
```


##### Description
Find all resources, and return the full collection (i.e. VS a page of the total results)


##### Parameters

|Type|Name|Schema|Default|
|---|---|---|---|
|**Query**|**page**  <br>*required*|enum (no)|`"no"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResources«Country»](#dca97e35bd8781395bc5a0e62b45d110)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="optionsusingoptions_4"></a>
#### Get CORS headers
```
OPTIONS /api/rest/countries
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_4"></a>
#### Get JSON Schema
```
GET /api/rest/countries/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_4"></a>
#### Get UI schema
```
GET /api/rest/countries/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetbyidusingget_4"></a>
#### Find by pk
```
GET /api/rest/countries/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResource«Country»](#9444a34c25d284655fc420cf1bfdef65)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsonputusingput_4"></a>
#### Update a resource
```
PUT /api/rest/countries/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[Country](#country)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«Country»](#0fd62beb279efbe66a254288f7881f70)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsondeleteusingdelete_4"></a>
#### Delete a resource
```
DELETE /api/rest/countries/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapipatchusingpatch_4"></a>
#### Patch (partially plainJsonPut) a resource given as a JSON API Document
```
PATCH /api/rest/countries/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«Country,string»](#adbc7ab7820cfd03fefcaf75b93da36c)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«Country,string»](#adbc7ab7820cfd03fefcaf75b93da36c)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetrelatedusingget_8"></a>
#### Find related by root pk
```
GET /api/rest/countries/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«Country»](#da51ebd4e4de29877f5e0803d9547dca)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsongetrelatedusingget_9"></a>
#### Find related by root pk
```
GET /api/rest/countries/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«Country»](#da51ebd4e4de29877f5e0803d9547dca)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="errorlogs_resource"></a>
### ErrorLogs
Stacktrace or other error log details.


<a name="plainjsonpostusingpost_5"></a>
#### Create a new resource
```
POST /api/rest/errorLogs
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**model**  <br>*required*|model|[ErrorLog](#errorlog)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[Resource«ErrorLog»](#95fdd628a6010e03a6f8067fc025ce95)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapigetallusingget_5"></a>
#### Get the full collection of resources (no paging or criteria)
```
GET /api/rest/errorLogs
```


##### Description
Find all resources, and return the full collection (i.e. VS a page of the total results)


##### Parameters

|Type|Name|Schema|Default|
|---|---|---|---|
|**Query**|**page**  <br>*required*|enum (no)|`"no"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceCollectionDocument«ErrorLog,string»](#6b4dd139b32c90fd009baad01b32c162)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="optionsusingoptions_5"></a>
#### Get CORS headers
```
OPTIONS /api/rest/errorLogs
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_5"></a>
#### Get JSON Schema
```
GET /api/rest/errorLogs/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_5"></a>
#### Get UI schema
```
GET /api/rest/errorLogs/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetbyidusingget_5"></a>
#### Find by pk
```
GET /api/rest/errorLogs/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResource«ErrorLog»](#a3c232177f9f6d89da7ea16a9309c29c)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsonputusingput_5"></a>
#### Update a resource
```
PUT /api/rest/errorLogs/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[ErrorLog](#errorlog)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«ErrorLog»](#95fdd628a6010e03a6f8067fc025ce95)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapideleteusingdelete_5"></a>
#### Delete a resource
```
DELETE /api/rest/errorLogs/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsonpatchusingpatch_5"></a>
#### Patch (partially update) a resource
```
PATCH /api/rest/errorLogs/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[ErrorLog](#errorlog)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«ErrorLog»](#95fdd628a6010e03a6f8067fc025ce95)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapigetrelatedusingget_10"></a>
#### Find related by root pk
```
GET /api/rest/errorLogs/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«ErrorLog»](#770e1184ef1d68a3758bfc6fa22bd444)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="jsonapigetrelatedusingget_11"></a>
#### Find related by root pk
```
GET /api/rest/errorLogs/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«ErrorLog»](#770e1184ef1d68a3758bfc6fa22bd444)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="errors_resource"></a>
### Errors
Generic error information (readonly)


<a name="jsonapipostusingpost"></a>
#### Create a new JSON API Resource
```
POST /api/rest/allErrors
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«BaseError,string»](#788c3bcb0cd8e8f495ab85014de843cb)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[JsonApiModelResourceDocument«BaseError,string»](#788c3bcb0cd8e8f495ab85014de843cb)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetbyidsusingget"></a>
#### Search by pks
```
GET /api/rest/allErrors
```


##### Description
Find the set of resources matching the given identifiers.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**pks**  <br>*required*||string|
|**Query**|**pks[]**  <br>*required*|pks[]|< string > array(multi)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResources«BaseError»](#25d85cda698c35bf1f8019cd4014ad85)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="optionsusingoptions"></a>
#### Get CORS headers
```
OPTIONS /api/rest/allErrors
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget"></a>
#### Get JSON Schema
```
GET /api/rest/allErrors/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget"></a>
#### Get UI schema
```
GET /api/rest/allErrors/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="jsonapigetbyidusingget"></a>
#### Find by pk
```
GET /api/rest/allErrors/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«BaseError,string»](#788c3bcb0cd8e8f495ab85014de843cb)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsonputusingput"></a>
#### Update a resource
```
PUT /api/rest/allErrors/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[BaseError](#baseerror)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«BaseError»](#ab8fee71cc97c8c2bd9bacf874a5f738)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsondeleteusingdelete"></a>
#### Delete a resource
```
DELETE /api/rest/allErrors/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsonpatchusingpatch"></a>
#### Patch (partially update) a resource
```
PATCH /api/rest/allErrors/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[BaseError](#baseerror)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«BaseError»](#ab8fee71cc97c8c2bd9bacf874a5f738)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapigetrelatedusingget"></a>
#### Find related by root pk
```
GET /api/rest/allErrors/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«BaseError»](#df365763e04325ca180e482d51c349a6)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetrelatedusingget_1"></a>
#### Find related by root pk
```
GET /api/rest/allErrors/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«BaseError»](#f688d2819c775fad0d39fa0b4dd5a5b3)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="friends_resource"></a>
### Friends
Friend searches


<a name="findmyfriendspaginatedusingget"></a>
#### Find all friends (paginated)
```
GET /api/rest/friends/my
```


##### Description
Find all friends of the current user. Returns paginated results


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Query**|**page**  <br>*optional*|page|integer(int32)|`"0"`|
|**Query**|**size**  <br>*optional*|size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|sort|string|`"pk"`|
|**Query**|**status**  <br>*optional*|status|< string > array(multi)|`"CONFIRMED"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Page«UserDTO»](#b23ca1d459fe9c814c752fba6d94fcbc)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/xml`
* `application/json`


<a name="findafriendsfriendspaginatedusingget"></a>
#### Find all friends of a friend (paginated)
```
GET /api/rest/friends/{friendId}
```


##### Description
Find all friends of a friend. Returns paginated results


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**friendId**  <br>*required*|string|string||
|**Query**|**page**  <br>*optional*|page|integer(int32)|`"0"`|
|**Query**|**size**  <br>*optional*|size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|sort|string|`"pk"`|
|**Query**|**status**  <br>*optional*|status|< string > array(multi)|`"CONFIRMED"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Page«UserDTO»](#b23ca1d459fe9c814c752fba6d94fcbc)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/xml`
* `application/json`


<a name="friendships_resource"></a>
### Friendships
Operations about friendships


<a name="plainjsonpostusingpost_6"></a>
#### Create a new resource
```
POST /api/rest/friendships
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**model**  <br>*required*|model|[Friendship](#friendship)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[Resource«Friendship»](#c71766d00247d5c219b0b6a94c678181)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsongetpageusingget_6"></a>
#### Search for resources (paginated).
```
GET /api/rest/friendships
```


##### Description
Find all resources matching the given criteria and return a paginated collection.Predefined paging properties are _pn (page number), _ps (page size) and sort. All serialized member names of the resource are supported as search criteria in the form of HTTP URL parameters.


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[PagedModelResources«Friendship»](#c1414046871706e5ea4ef047065642a4)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="optionsusingoptions_6"></a>
#### Get CORS headers
```
OPTIONS /api/rest/friendships
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_6"></a>
#### Get JSON Schema
```
GET /api/rest/friendships/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_6"></a>
#### Get UI schema
```
GET /api/rest/friendships/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetbyidusingget_6"></a>
#### Find by pk
```
GET /api/rest/friendships/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResource«Friendship»](#1f54f6b4564b09cb85c290aa5845aa94)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsonputusingput_6"></a>
#### Update a resource
```
PUT /api/rest/friendships/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[Friendship](#friendship)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«Friendship»](#c71766d00247d5c219b0b6a94c678181)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapideleteusingdelete_6"></a>
#### Delete a resource
```
DELETE /api/rest/friendships/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="jsonapipatchusingpatch_6"></a>
#### Patch (partially plainJsonPut) a resource given as a JSON API Document
```
PATCH /api/rest/friendships/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«Friendship,FriendshipIdentifier»](#162950cb6ec633865b848ca5d98c998c)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«Friendship,FriendshipIdentifier»](#162950cb6ec633865b848ca5d98c998c)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetrelatedusingget_12"></a>
#### Find related by root pk
```
GET /api/rest/friendships/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«Friendship»](#750e2f4000044b50b0d793cf0f21c8fa)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapigetrelatedusingget_13"></a>
#### Find related by root pk
```
GET /api/rest/friendships/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«Friendship»](#faf39d664962d27886c93cb8ef8ff68d)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="hosts_resource"></a>
### Hosts
Operations about hosts


<a name="jsonapipostusingpost_7"></a>
#### Create a new JSON API Resource
```
POST /api/rest/hosts
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«Host,string»](#fab1b612ff3f6f782c667482f5518953)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[JsonApiModelResourceDocument«Host,string»](#fab1b612ff3f6f782c667482f5518953)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetbyidsusingget_7"></a>
#### Search by pks
```
GET /api/rest/hosts
```


##### Description
Find the set of resources matching the given identifiers.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**pks**  <br>*required*||string|
|**Query**|**pks[]**  <br>*required*|pks[]|< string > array(multi)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResources«Host»](#4deb6df5e56fd3bc7d192a4623fd2beb)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="optionsusingoptions_7"></a>
#### Get CORS headers
```
OPTIONS /api/rest/hosts
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_7"></a>
#### Get JSON Schema
```
GET /api/rest/hosts/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_7"></a>
#### Get UI schema
```
GET /api/rest/hosts/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="jsonapigetbyidusingget_7"></a>
#### Find by pk
```
GET /api/rest/hosts/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«Host,string»](#fab1b612ff3f6f782c667482f5518953)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsonputusingput_7"></a>
#### Update a resource
```
PUT /api/rest/hosts/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[Host](#host)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«Host»](#4b3f1994c33e4350629300bebe438034)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapideleteusingdelete_7"></a>
#### Delete a resource
```
DELETE /api/rest/hosts/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsonpatchusingpatch_7"></a>
#### Patch (partially update) a resource
```
PATCH /api/rest/hosts/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[Host](#host)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«Host»](#4b3f1994c33e4350629300bebe438034)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapigetrelatedusingget_14"></a>
#### Find related by root pk
```
GET /api/rest/hosts/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«Host»](#19132450ec6514b538e0effd0aeffed6)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetrelatedusingget_15"></a>
#### Find related by root pk
```
GET /api/rest/hosts/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«Host»](#5b9ea71df74981f682abd627a91fb65d)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="invitations_resource"></a>
### Invitations
Invite new users


<a name="inviteusersusingpost"></a>
#### Invite users
```
POST /api/rest/invitations
```


##### Description
Invite users by email


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**invitations**  <br>*required*|invitations|[UserInvitationsDTO](#userinvitationsdto)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[UserInvitationResultsDTO](#userinvitationresultsdto)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/xml`
* `application/json`


<a name="localities_resource"></a>
### Localities
Locality operations


<a name="plainjsonpostusingpost_8"></a>
#### Create a new resource
```
POST /api/rest/localities
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**model**  <br>*required*|model|[Locality](#locality)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[Resource«Locality»](#0b6e798b627cbe8539a863efcb1a56ff)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsongetbyidsusingget_8"></a>
#### Search by pks
```
GET /api/rest/localities
```


##### Description
Find the set of resources matching the given identifiers.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**pks**  <br>*required*||string|
|**Query**|**pks[]**  <br>*required*|pks[]|< string > array(multi)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResources«Locality»](#eaaf18024132049fcf1a477995161123)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="optionsusingoptions_8"></a>
#### Get CORS headers
```
OPTIONS /api/rest/localities
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_8"></a>
#### Get JSON Schema
```
GET /api/rest/localities/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_8"></a>
#### Get UI schema
```
GET /api/rest/localities/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetbyidusingget_8"></a>
#### Find by pk
```
GET /api/rest/localities/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResource«Locality»](#6e3b861112e12ed5486863ddfdd870dc)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsonputusingput_8"></a>
#### Update a resource
```
PUT /api/rest/localities/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[Locality](#locality)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«Locality»](#0b6e798b627cbe8539a863efcb1a56ff)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsondeleteusingdelete_8"></a>
#### Delete a resource
```
DELETE /api/rest/localities/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsonpatchusingpatch_8"></a>
#### Patch (partially update) a resource
```
PATCH /api/rest/localities/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[Locality](#locality)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«Locality»](#0b6e798b627cbe8539a863efcb1a56ff)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapigetrelatedusingget_16"></a>
#### Find related by root pk
```
GET /api/rest/localities/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«Locality»](#dda25a325eb794dd742ae052439f5c07)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="jsonapigetrelatedusingget_17"></a>
#### Find related by root pk
```
GET /api/rest/localities/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«Locality»](#dda25a325eb794dd742ae052439f5c07)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="oauth_resource"></a>
### OAuth
Service provider connection operations


<a name="showrememberpageusingget"></a>
#### showRememberPage
```
GET /api/auth/oauth/signin/popup/remember
```


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|string|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `*/*`


<a name="signinusingpost"></a>
#### signIn
```
POST /api/auth/oauth/signin/{providerId}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**providerId**  <br>*required*|providerId|string|
|**Body**|**request**  <br>*optional*|request|[NativeWebRequest](#nativewebrequest)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RedirectView](#redirectview)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `*/*`


<a name="oauth1callbackusingget"></a>
#### oauth1Callback
```
GET /api/auth/oauth/signin/{providerId}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**providerId**  <br>*required*|providerId|string|
|**Query**|**oauth_token**  <br>*required*||string|
|**Body**|**request**  <br>*optional*|request|[NativeWebRequest](#nativewebrequest)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RedirectView](#redirectview)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `*/*`


<a name="personaldetails_resource"></a>
### PersonalDetails
Personal information operations


<a name="jsonapipostusingpost_9"></a>
#### Create a new JSON API Resource
```
POST /api/rest/personalDetails
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«PersonalDetails,string»](#5d260e67e7c78ea334bf945d3ceb4f43)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[JsonApiModelResourceDocument«PersonalDetails,string»](#5d260e67e7c78ea334bf945d3ceb4f43)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetallusingget_9"></a>
#### Get the full collection of resources (no paging or criteria)
```
GET /api/rest/personalDetails
```


##### Description
Find all resources, and return the full collection (i.e. VS a page of the total results)


##### Parameters

|Type|Name|Schema|Default|
|---|---|---|---|
|**Query**|**page**  <br>*required*|enum (no)|`"no"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResources«PersonalDetails»](#b5e062ca38ae416d4d7959f028c66bc0)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="optionsusingoptions_9"></a>
#### Get CORS headers
```
OPTIONS /api/rest/personalDetails
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_9"></a>
#### Get JSON Schema
```
GET /api/rest/personalDetails/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_9"></a>
#### Get UI schema
```
GET /api/rest/personalDetails/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="jsonapigetbyidusingget_9"></a>
#### Find by pk
```
GET /api/rest/personalDetails/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«PersonalDetails,string»](#5d260e67e7c78ea334bf945d3ceb4f43)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsonputusingput_9"></a>
#### Update a resource
```
PUT /api/rest/personalDetails/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[PersonalDetails](#personaldetails)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«PersonalDetails»](#d23157c33d48277bb1a1439dcb17bb87)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapideleteusingdelete_9"></a>
#### Delete a resource
```
DELETE /api/rest/personalDetails/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="jsonapipatchusingpatch_9"></a>
#### Patch (partially plainJsonPut) a resource given as a JSON API Document
```
PATCH /api/rest/personalDetails/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«PersonalDetails,string»](#5d260e67e7c78ea334bf945d3ceb4f43)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«PersonalDetails,string»](#5d260e67e7c78ea334bf945d3ceb4f43)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="jsonapigetrelatedusingget_18"></a>
#### Find related by root pk
```
GET /api/rest/personalDetails/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«PersonalDetails»](#a8a2d657326f1f93b5ef013d8a741471)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetrelatedusingget_19"></a>
#### Find related by root pk
```
GET /api/rest/personalDetails/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«PersonalDetails»](#00e3c94cb4b1ef15ade0489f11348786)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="postalcodes_resource"></a>
### PostalCodes
Postal code operations


<a name="plainjsonpostusingpost_10"></a>
#### Create a new resource
```
POST /api/rest/postalCodes
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**model**  <br>*required*|model|[PostalCode](#postalcode)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[Resource«PostalCode»](#8eb673589b8cfa5bf97689fe6c295786)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapigetbyidsusingget_10"></a>
#### Search by pks
```
GET /api/rest/postalCodes
```


##### Description
Find the set of resources matching the given identifiers.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**pks**  <br>*required*||string|
|**Query**|**pks[]**  <br>*required*|pks[]|< string > array(multi)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceCollectionDocument«PostalCode,string»](#85481637dd2dcf44000d1cef7f5884d5)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="optionsusingoptions_10"></a>
#### Get CORS headers
```
OPTIONS /api/rest/postalCodes
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_10"></a>
#### Get JSON Schema
```
GET /api/rest/postalCodes/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_10"></a>
#### Get UI schema
```
GET /api/rest/postalCodes/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="jsonapigetbyidusingget_10"></a>
#### Find by pk
```
GET /api/rest/postalCodes/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«PostalCode,string»](#948547db6b23e5934426b4fd77d2441b)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsonputusingput_10"></a>
#### Update a resource
```
PUT /api/rest/postalCodes/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[PostalCode](#postalcode)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«PostalCode»](#8eb673589b8cfa5bf97689fe6c295786)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsondeleteusingdelete_10"></a>
#### Delete a resource
```
DELETE /api/rest/postalCodes/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapipatchusingpatch_10"></a>
#### Patch (partially plainJsonPut) a resource given as a JSON API Document
```
PATCH /api/rest/postalCodes/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«PostalCode,string»](#948547db6b23e5934426b4fd77d2441b)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«PostalCode,string»](#948547db6b23e5934426b4fd77d2441b)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetrelatedusingget_20"></a>
#### Find related by root pk
```
GET /api/rest/postalCodes/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«PostalCode»](#a147ae80f9ce75c1efc703b60be50f30)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapigetrelatedusingget_21"></a>
#### Find related by root pk
```
GET /api/rest/postalCodes/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«PostalCode»](#b920b95da8ca7f7cc5fd465aead82495)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="registrationcodebatches_resource"></a>
### RegistrationCodeBatches
Codes management (admin, operator)


<a name="jsonapipostusingpost_18"></a>
#### Create a new JSON API Resource
```
POST /api/rest/registrationCodeBatches
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«UserRegistrationCodeBatch,string»](#fc8442e5fbd6124269c0d38f08edbaa4)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[JsonApiModelResourceDocument«UserRegistrationCodeBatch,string»](#fc8442e5fbd6124269c0d38f08edbaa4)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetbyidsusingget_18"></a>
#### Search by pks
```
GET /api/rest/registrationCodeBatches
```


##### Description
Find the set of resources matching the given identifiers.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**pks**  <br>*required*||string|
|**Query**|**pks[]**  <br>*required*|pks[]|< string > array(multi)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResources«UserRegistrationCodeBatch»](#c6c606ceb400df54bca43f11d9751b1e)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/xml`
* `application/json`


<a name="optionsusingoptions_18"></a>
#### Get CORS headers
```
OPTIONS /api/rest/registrationCodeBatches
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_18"></a>
#### Get JSON Schema
```
GET /api/rest/registrationCodeBatches/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_18"></a>
#### Get UI schema
```
GET /api/rest/registrationCodeBatches/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="exporttocsvusingget"></a>
#### Export batch to a spreadsheet (CSV) report
```
GET /api/rest/registrationCodeBatches/{id}/csv
```


##### Description
The filename will be [batch name]_[date: yyyyMMddHHmmss].csv


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|< [UserRegistrationCodeInfo](#userregistrationcodeinfo) > array|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `text/csv`


<a name="plainjsongetbyidusingget_18"></a>
#### Find by pk
```
GET /api/rest/registrationCodeBatches/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResource«UserRegistrationCodeBatch»](#b4e2d3021fa227250a9a5e254fce44c7)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/xml`
* `application/json`


<a name="plainjsonputusingput_18"></a>
#### Update a resource
```
PUT /api/rest/registrationCodeBatches/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[UserRegistrationCodeBatch](#userregistrationcodebatch)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«UserRegistrationCodeBatch»](#2ac0d0698c68f295c8cb1e92d27cf2d3)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/xml`
* `application/json`


<a name="plainjsondeleteusingdelete_18"></a>
#### Delete a resource
```
DELETE /api/rest/registrationCodeBatches/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/xml`
* `application/json`


<a name="jsonapipatchusingpatch_18"></a>
#### Patch (partially plainJsonPut) a resource given as a JSON API Document
```
PATCH /api/rest/registrationCodeBatches/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«UserRegistrationCodeBatch,string»](#fc8442e5fbd6124269c0d38f08edbaa4)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«UserRegistrationCodeBatch,string»](#fc8442e5fbd6124269c0d38f08edbaa4)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="jsonapigetrelatedusingget_36"></a>
#### Find related by root pk
```
GET /api/rest/registrationCodeBatches/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«UserRegistrationCodeBatch»](#29e1e912f10e19c7abb03c1e4774dedf)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="jsonapigetrelatedusingget_37"></a>
#### Find related by root pk
```
GET /api/rest/registrationCodeBatches/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«UserRegistrationCodeBatch»](#29e1e912f10e19c7abb03c1e4774dedf)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="roles_resource"></a>
### Roles
Operations about roles


<a name="jsonapipostusingpost_11"></a>
#### Create a new JSON API Resource
```
POST /api/rest/roles
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«Role,string»](#91f0906ce82be845edf829468cf32ff5)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[JsonApiModelResourceDocument«Role,string»](#91f0906ce82be845edf829468cf32ff5)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetpageusingget_11"></a>
#### Search for resources (paginated).
```
GET /api/rest/roles
```


##### Description
Find all resources matching the given criteria and return a paginated collection.Predefined paging properties are _pn (page number), _ps (page size) and sort. All serialized member names of the resource are supported as search criteria in the form of HTTP URL parameters.


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[PagedModelResources«Role»](#ce2d59bddeedb62dd390b8097f8f671c)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="optionsusingoptions_11"></a>
#### Get CORS headers
```
OPTIONS /api/rest/roles
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_11"></a>
#### Get JSON Schema
```
GET /api/rest/roles/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_11"></a>
#### Get UI schema
```
GET /api/rest/roles/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetbyidusingget_11"></a>
#### Find by pk
```
GET /api/rest/roles/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResource«Role»](#95d8c85f589cabfba73a82139092c1f8)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsonputusingput_11"></a>
#### Update a resource
```
PUT /api/rest/roles/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[Role](#role)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«Role»](#45f492ff7f2355dd9fe8670f966c13b1)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsondeleteusingdelete_11"></a>
#### Delete a resource
```
DELETE /api/rest/roles/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsonpatchusingpatch_11"></a>
#### Patch (partially update) a resource
```
PATCH /api/rest/roles/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[Role](#role)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«Role»](#45f492ff7f2355dd9fe8670f966c13b1)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapigetrelatedusingget_22"></a>
#### Find related by root pk
```
GET /api/rest/roles/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«Role»](#e47450c548f4c9b64835e5c9621f6d3a)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetrelatedusingget_23"></a>
#### Find related by root pk
```
GET /api/rest/roles/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«Role»](#2fa679034f2a67df9073aa8a2a8891a2)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="stomp-sessions_resource"></a>
### STOMP Sessions
STOMP Session Operations


<a name="jsonapipostusingpost_12"></a>
#### Create a new JSON API Resource
```
POST /api/rest/stompSessions
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«STOMP Session,string»](#88093c7bc43b97c1d9f0a75f9593843d)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[JsonApiModelResourceDocument«STOMP Session,string»](#88093c7bc43b97c1d9f0a75f9593843d)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="jsonapigetbyidsusingget_12"></a>
#### Search by pks
```
GET /api/rest/stompSessions
```


##### Description
Find the set of resources matching the given identifiers.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**pks**  <br>*required*||string|
|**Query**|**pks[]**  <br>*required*|pks[]|< string > array(multi)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceCollectionDocument«STOMP Session,string»](#790b74b0f37b62a564d09f14528b84f1)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="optionsusingoptions_12"></a>
#### Get CORS headers
```
OPTIONS /api/rest/stompSessions
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_12"></a>
#### Get JSON Schema
```
GET /api/rest/stompSessions/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_12"></a>
#### Get UI schema
```
GET /api/rest/stompSessions/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetbyidusingget_12"></a>
#### Find by pk
```
GET /api/rest/stompSessions/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResource«STOMP Session»](#777e9019408cb8fd5296f5da82b11549)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsonputusingput_12"></a>
#### Update a resource
```
PUT /api/rest/stompSessions/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[STOMP Session](#stomp-session)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«STOMP Session»](#444feda462dbd0497ce75c2ac7644b6e)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsondeleteusingdelete_12"></a>
#### Delete a resource
```
DELETE /api/rest/stompSessions/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapipatchusingpatch_12"></a>
#### Patch (partially plainJsonPut) a resource given as a JSON API Document
```
PATCH /api/rest/stompSessions/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«STOMP Session,string»](#88093c7bc43b97c1d9f0a75f9593843d)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«STOMP Session,string»](#88093c7bc43b97c1d9f0a75f9593843d)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetrelatedusingget_24"></a>
#### Find related by root pk
```
GET /api/rest/stompSessions/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«STOMP Session»](#402c7efa52ed52c3536cabcd23686a34)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsongetrelatedusingget_25"></a>
#### Find related by root pk
```
GET /api/rest/stompSessions/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«STOMP Session»](#402c7efa52ed52c3536cabcd23686a34)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="system-errors_resource"></a>
### System Errors
System Error Operations (readonly)


<a name="jsonapipostusingpost_13"></a>
#### Create a new JSON API Resource
```
POST /api/rest/systemErrors
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«SystemError,string»](#b7b83c54682cba591a226330ae27a07b)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[JsonApiModelResourceDocument«SystemError,string»](#b7b83c54682cba591a226330ae27a07b)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="jsonapigetallusingget_13"></a>
#### Get the full collection of resources (no paging or criteria)
```
GET /api/rest/systemErrors
```


##### Description
Find all resources, and return the full collection (i.e. VS a page of the total results)


##### Parameters

|Type|Name|Schema|Default|
|---|---|---|---|
|**Query**|**page**  <br>*required*|enum (no)|`"no"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceCollectionDocument«SystemError,string»](#5311255b1149577ff2838e7e6485ae46)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="optionsusingoptions_13"></a>
#### Get CORS headers
```
OPTIONS /api/rest/systemErrors
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_13"></a>
#### Get JSON Schema
```
GET /api/rest/systemErrors/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_13"></a>
#### Get UI schema
```
GET /api/rest/systemErrors/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="jsonapigetbyidusingget_13"></a>
#### Find by pk
```
GET /api/rest/systemErrors/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«SystemError,string»](#b7b83c54682cba591a226330ae27a07b)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsonputusingput_13"></a>
#### Update a resource
```
PUT /api/rest/systemErrors/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[SystemError](#systemerror)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«SystemError»](#699b494ee59baa225bb0487a393a205d)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapideleteusingdelete_13"></a>
#### Delete a resource
```
DELETE /api/rest/systemErrors/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsonpatchusingpatch_13"></a>
#### Patch (partially update) a resource
```
PATCH /api/rest/systemErrors/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[SystemError](#systemerror)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«SystemError»](#699b494ee59baa225bb0487a393a205d)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapigetrelatedusingget_26"></a>
#### Find related by root pk
```
GET /api/rest/systemErrors/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«SystemError»](#d38ff8b806dc84761874de3322b9f189)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetrelatedusingget_27"></a>
#### Find related by root pk
```
GET /api/rest/systemErrors/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«SystemError»](#d810224a8ed64e85885f525a3823587f)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="user-agents_resource"></a>
### User Agents
Collection of UA signatures


<a name="jsonapipostusingpost_14"></a>
#### Create a new JSON API Resource
```
POST /api/rest/userAgents
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«UserAgent,string»](#9107a0c6f1bedb59ed8c453e7f3e2c67)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[JsonApiModelResourceDocument«UserAgent,string»](#9107a0c6f1bedb59ed8c453e7f3e2c67)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="jsonapigetallusingget_14"></a>
#### Get the full collection of resources (no paging or criteria)
```
GET /api/rest/userAgents
```


##### Description
Find all resources, and return the full collection (i.e. VS a page of the total results)


##### Parameters

|Type|Name|Schema|Default|
|---|---|---|---|
|**Query**|**page**  <br>*required*|enum (no)|`"no"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceCollectionDocument«UserAgent,string»](#97534114b53ad68cb0e908c9dc6f5492)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="optionsusingoptions_14"></a>
#### Get CORS headers
```
OPTIONS /api/rest/userAgents
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_14"></a>
#### Get JSON Schema
```
GET /api/rest/userAgents/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_14"></a>
#### Get UI schema
```
GET /api/rest/userAgents/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetbyidusingget_14"></a>
#### Find by pk
```
GET /api/rest/userAgents/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResource«UserAgent»](#b87bcc078614ec1c9ce5b3a045b62fe8)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsonputusingput_14"></a>
#### Update a resource
```
PUT /api/rest/userAgents/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[UserAgent](#useragent)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«UserAgent»](#2c20f0eb88911650e8da8b52ec7a1cdd)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapideleteusingdelete_14"></a>
#### Delete a resource
```
DELETE /api/rest/userAgents/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsonpatchusingpatch_14"></a>
#### Patch (partially update) a resource
```
PATCH /api/rest/userAgents/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[UserAgent](#useragent)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«UserAgent»](#2c20f0eb88911650e8da8b52ec7a1cdd)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapigetrelatedusingget_28"></a>
#### Find related by root pk
```
GET /api/rest/userAgents/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«UserAgent»](#acf81c73a4b814d521aed28969421ff6)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetrelatedusingget_29"></a>
#### Find related by root pk
```
GET /api/rest/userAgents/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«UserAgent»](#bcae2dcf6ae7300aa2757d801a50ec47)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="user-credentials_resource"></a>
### User Credentials
Operations about user credentials


<a name="plainjsonpostusingpost_16"></a>
#### Create a new resource
```
POST /api/rest/userCredentials
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**model**  <br>*required*|model|[UserCredentials](#usercredentials)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[Resource«UserCredentials»](#f57cf6713880fc103ac4c8ef0370ba2a)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsongetpageusingget_16"></a>
#### Search for resources (paginated).
```
GET /api/rest/userCredentials
```


##### Description
Find all resources matching the given criteria and return a paginated collection.Predefined paging properties are _pn (page number), _ps (page size) and sort. All serialized member names of the resource are supported as search criteria in the form of HTTP URL parameters.


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[PagedModelResources«UserCredentials»](#733fcf7e619c2dceeeff0a720733c01d)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="optionsusingoptions_16"></a>
#### Get CORS headers
```
OPTIONS /api/rest/userCredentials
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_16"></a>
#### Get JSON Schema
```
GET /api/rest/userCredentials/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_16"></a>
#### Get UI schema
```
GET /api/rest/userCredentials/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="jsonapigetbyidusingget_16"></a>
#### Find by pk
```
GET /api/rest/userCredentials/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«UserCredentials,string»](#410073fa1e16c29e59a98561490d3e9a)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsonputusingput_16"></a>
#### Update a resource
```
PUT /api/rest/userCredentials/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[UserCredentials](#usercredentials)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«UserCredentials»](#f57cf6713880fc103ac4c8ef0370ba2a)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapideleteusingdelete_16"></a>
#### Delete a resource
```
DELETE /api/rest/userCredentials/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsonpatchusingpatch_16"></a>
#### Patch (partially update) a resource
```
PATCH /api/rest/userCredentials/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[UserCredentials](#usercredentials)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«UserCredentials»](#f57cf6713880fc103ac4c8ef0370ba2a)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsongetrelatedusingget_32"></a>
#### Find related by root pk
```
GET /api/rest/userCredentials/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«UserCredentials»](#0881d617dc56ac4c4272fd57dcefa184)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapigetrelatedusingget_33"></a>
#### Find related by root pk
```
GET /api/rest/userCredentials/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«UserCredentials»](#6090c6193e6c6e58f94588385b61d2e4)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="userregistrationcode_resource"></a>
### UserRegistrationCode
User registration codes (read-only)


<a name="jsonapipostusingpost_17"></a>
#### Create a new JSON API Resource
```
POST /api/rest/userRegistrationCodes
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«UserRegistrationCode,string»](#21bd7755aaf27c46fad29cb7e1ff25a1)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[JsonApiModelResourceDocument«UserRegistrationCode,string»](#21bd7755aaf27c46fad29cb7e1ff25a1)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetpageusingget_17"></a>
#### Search for resources (paginated).
```
GET /api/rest/userRegistrationCodes
```


##### Description
Find all resources matching the given criteria and return a paginated collection.Predefined paging properties are _pn (page number), _ps (page size) and sort. All serialized member names of the resource are supported as search criteria in the form of HTTP URL parameters.


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[PagedModelResources«UserRegistrationCode»](#c544285309913bb175c0baeb01c97af0)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="optionsusingoptions_17"></a>
#### Get CORS headers
```
OPTIONS /api/rest/userRegistrationCodes
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="plainjsongetjsonschemausingget_17"></a>
#### Get JSON Schema
```
GET /api/rest/userRegistrationCodes/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_17"></a>
#### Get UI schema
```
GET /api/rest/userRegistrationCodes/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="jsonapigetbyidusingget_17"></a>
#### Find by pk
```
GET /api/rest/userRegistrationCodes/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«UserRegistrationCode,string»](#21bd7755aaf27c46fad29cb7e1ff25a1)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsonputusingput_17"></a>
#### Update a resource
```
PUT /api/rest/userRegistrationCodes/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[UserRegistrationCode](#userregistrationcode)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«UserRegistrationCode»](#c59e2883024a16ce4b29f9822cd43edf)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsondeleteusingdelete_17"></a>
#### Delete a resource
```
DELETE /api/rest/userRegistrationCodes/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapipatchusingpatch_17"></a>
#### Patch (partially plainJsonPut) a resource given as a JSON API Document
```
PATCH /api/rest/userRegistrationCodes/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«UserRegistrationCode,string»](#21bd7755aaf27c46fad29cb7e1ff25a1)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«UserRegistrationCode,string»](#21bd7755aaf27c46fad29cb7e1ff25a1)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="jsonapigetrelatedusingget_34"></a>
#### Find related by root pk
```
GET /api/rest/userRegistrationCodes/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«UserRegistrationCode»](#380220dcbc5985a5e7ebdfb31d4db480)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="jsonapigetrelatedusingget_35"></a>
#### Find related by root pk
```
GET /api/rest/userRegistrationCodes/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«UserRegistrationCode»](#380220dcbc5985a5e7ebdfb31d4db480)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="users_resource"></a>
### Users
User management operations


<a name="jsonapipostusingpost_15"></a>
#### Create a new JSON API Resource
```
POST /api/rest/users
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«User,string»](#656dfd54c1eb474fdc8aadf368bc606e)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[JsonApiModelResourceDocument«User,string»](#656dfd54c1eb474fdc8aadf368bc606e)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetbyidsusingget_15"></a>
#### Search by pks
```
GET /api/rest/users
```


##### Description
Find the set of resources matching the given identifiers.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**pks**  <br>*required*||string|
|**Query**|**pks[]**  <br>*required*|pks[]|< string > array(multi)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResources«User»](#fc1ae26c5789cf20fb6269b6f59edd82)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="optionsusingoptions_15"></a>
#### Get CORS headers
```
OPTIONS /api/rest/users
```


##### Description
Get the CORS headers for the given path


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/vnd.api+json`


##### Produces

* `application/json`
* `application/vnd.api+json`


<a name="getbyusernameoremailusingget"></a>
#### Get one by username or email
```
GET /api/rest/users/byUserNameOrEmail/{userNameOrEmail}
```


##### Description
Get the single user with the given username or email.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**userNameOrEmail**  <br>*required*|userNameOrEmail|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«User»](#7cfe2818d2924b9c0f220f9e272581a5)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsongetjsonschemausingget_15"></a>
#### Get JSON Schema
```
GET /api/rest/users/jsonschema
```


##### Description
Get the JSON Schema for the controller entity type


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[RawJson](#rawjson)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="plainjsongetuischemausingget_15"></a>
#### Get UI schema
```
GET /api/rest/users/uischema
```

Caution : 
operation.deprecated


##### Description
Get the UI achema for the controller entity type, including fields, use-cases etc.


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[UiSchema](#uischema)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="updatefilesusingpost_1"></a>
#### Update files
```
POST /api/rest/users/{id}/files
```


##### Description
The files are saved using the parameter names of the multipart files contained in this request. These are the field names of the form (like with normal parameters), not the original file names.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**id**  <br>*required*|id|string|
|**Body**|**request**  <br>*optional*|request|[MultipartHttpServletRequest](#multiparthttpservletrequest)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[User](#user)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `multipart/*`


##### Produces

* `application/xml`
* `application/json`


<a name="plainjsongetbyidusingget_15"></a>
#### Find by pk
```
GET /api/rest/users/{pk}
```


##### Description
Find a resource by it's identifier


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ModelResource«User»](#9eaf6d0fbe011d785264d794923d4636)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsonputusingput_15"></a>
#### Update a resource
```
PUT /api/rest/users/{pk}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**model**  <br>*required*|model|[User](#user)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[Resource«User»](#7cfe2818d2924b9c0f220f9e272581a5)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="plainjsondeleteusingdelete_15"></a>
#### Delete a resource
```
DELETE /api/rest/users/{pk}
```


##### Description
Delete a resource by its identifier.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapipatchusingpatch_15"></a>
#### Patch (partially plainJsonPut) a resource given as a JSON API Document
```
PATCH /api/rest/users/{pk}
```


##### Description
Partial updates will apply all given properties (ignoring null values) to the persisted entity.


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string|
|**Body**|**document**  <br>*required*|document|[JsonApiModelResourceDocument«User,string»](#656dfd54c1eb474fdc8aadf368bc606e)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiModelResourceDocument«User,string»](#656dfd54c1eb474fdc8aadf368bc606e)|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="plainjsongetrelatedusingget_30"></a>
#### Find related by root pk
```
GET /api/rest/users/{pk}/relationships/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**_pn**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**_ps**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[ResponseEntity«User»](#cd8e556591494ccd572311ba3087e44a)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="jsonapigetrelatedusingget_31"></a>
#### Find related by root pk
```
GET /api/rest/users/{pk}/{relationName}
```


##### Description
Find the related resource for the given relation name and identifier


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Path**|**pk**  <br>*required*|string|string||
|**Path**|**relationName**  <br>*required*|string|string||
|**Query**|**filter**  <br>*optional*|The RSQL/FIQL query to use. Simply URL param based search will be used if missing.|string||
|**Query**|**page[number]**  <br>*optional*|The page number|integer(int32)|`"0"`|
|**Query**|**page[size]**  <br>*optional*|The page size|integer(int32)|`"10"`|
|**Query**|**sort**  <br>*optional*|Comma separated list of attribute names, descending for each one prefixed with a dash, ascending otherwise|string|`"pk"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[JsonApiDocument«User»](#edbd1b77d1868403783de6ea25379f49)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/vnd.api+json`


##### Produces

* `application/vnd.api+json`


<a name="addmetadatumusingput"></a>
#### Add metadatum
```
PUT /api/rest/users/{subjectId}/metadata
```


##### Description
Add or update a resource metadatum


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**subjectId**  <br>*required*|subjectId|string|
|**Body**|**dto**  <br>*required*|dto|[MetadatumDTO](#metadatumdto)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`
* `application/hal+json`


##### Produces

* `application/json`
* `application/hal+json`


<a name="endpoint-mvc-adapter_resource"></a>
### Endpoint-mvc-adapter
Endpoint Mvc Adapter


<a name="invokeusingget"></a>
#### invoke
```
GET /beans
```


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/vnd.spring-boot.actuator.v1+json`
* `application/json`


<a name="invokeusingget_1"></a>
#### invoke
```
GET /beans.json
```


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/vnd.spring-boot.actuator.v1+json`
* `application/json`


<a name="invokeusingget_2"></a>
#### invoke
```
GET /info
```


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/vnd.spring-boot.actuator.v1+json`
* `application/json`


<a name="invokeusingget_3"></a>
#### invoke
```
GET /info.json
```


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/vnd.spring-boot.actuator.v1+json`
* `application/json`


<a name="invokeusingget_4"></a>
#### invoke
```
GET /mappings
```


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/vnd.spring-boot.actuator.v1+json`
* `application/json`


<a name="invokeusingget_5"></a>
#### invoke
```
GET /mappings.json
```


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/vnd.spring-boot.actuator.v1+json`
* `application/json`


<a name="environment-mvc-endpoint_resource"></a>
### Environment-mvc-endpoint
Environment Mvc Endpoint


<a name="invokeusingget_6"></a>
#### invoke
```
GET /env
```


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/vnd.spring-boot.actuator.v1+json`
* `application/json`


<a name="invokeusingget_7"></a>
#### invoke
```
GET /env.json
```


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/vnd.spring-boot.actuator.v1+json`
* `application/json`


<a name="valueusingget"></a>
#### value
```
GET /env/{name}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**name**  <br>*required*|name|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/vnd.spring-boot.actuator.v1+json`
* `application/json`


<a name="health-mvc-endpoint_resource"></a>
### Health-mvc-endpoint
Health Mvc Endpoint


<a name="invokeusingget_8"></a>
#### invoke
```
GET /health
```


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/vnd.spring-boot.actuator.v1+json`
* `application/json`


<a name="invokeusingget_9"></a>
#### invoke
```
GET /health.json
```


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/vnd.spring-boot.actuator.v1+json`
* `application/json`


<a name="heapdump-mvc-endpoint_resource"></a>
### Heapdump-mvc-endpoint
Heapdump Mvc Endpoint


<a name="invokeusingget_10"></a>
#### invoke
```
GET /heapdump
```


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Query**|**live**  <br>*optional*|live|boolean|`"true"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/octet-stream`


<a name="invokeusingget_11"></a>
#### invoke
```
GET /heapdump.json
```


##### Parameters

|Type|Name|Description|Schema|Default|
|---|---|---|---|---|
|**Query**|**live**  <br>*optional*|live|boolean|`"true"`|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/octet-stream`


<a name="jwt-controller_resource"></a>
### Jwt-controller
Jwt Controller


<a name="createaccesstokenusingpost"></a>
#### createAccessToken
```
POST /api/auth/jwt/access
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**loginRequest**  <br>*required*|loginRequest|[SimpleLoginRequest](#simpleloginrequest)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[UserDetails](#userdetails)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="createrefreshtokenusingpost"></a>
#### createRefreshToken
```
POST /api/auth/jwt/refresh
```


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**201**|Created|[JwtToken](#jwttoken)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`


<a name="metrics-mvc-endpoint_resource"></a>
### Metrics-mvc-endpoint
Metrics Mvc Endpoint


<a name="invokeusingget_12"></a>
#### invoke
```
GET /metrics
```


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/vnd.spring-boot.actuator.v1+json`
* `application/json`


<a name="invokeusingget_13"></a>
#### invoke
```
GET /metrics.json
```


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/vnd.spring-boot.actuator.v1+json`
* `application/json`


<a name="valueusingget_1"></a>
#### value
```
GET /metrics/{name}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**name**  <br>*required*|name|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/vnd.spring-boot.actuator.v1+json`
* `application/json`




<a name="definitions"></a>
## Definitions

<a name="applicationcontext"></a>
### ApplicationContext

|Name|Schema|
|---|---|
|**applicationName**  <br>*optional*|string|
|**autowireCapableBeanFactory**  <br>*optional*|[AutowireCapableBeanFactory](#autowirecapablebeanfactory)|
|**beanDefinitionCount**  <br>*optional*|integer(int32)|
|**beanDefinitionNames**  <br>*optional*|< string > array|
|**classLoader**  <br>*optional*|[ClassLoader](#classloader)|
|**displayName**  <br>*optional*|string|
|**environment**  <br>*optional*|[Environment](#environment)|
|**id**  <br>*optional*|string|
|**parent**  <br>*optional*|[ApplicationContext](#applicationcontext)|
|**parentBeanFactory**  <br>*optional*|[BeanFactory](#beanfactory)|
|**startupDate**  <br>*optional*|integer(int64)|


<a name="autowirecapablebeanfactory"></a>
### AutowireCapableBeanFactory
*Type* : object


<a name="baseerror"></a>
### BaseError
Generic error superclass


|Name|Description|Schema|
|---|---|---|
|**createdBy**  <br>*optional*|The initial content author|[UserDTO](#userdto)|
|**createdDate**  <br>*optional*|Date created|[LocalDateTime](#localdatetime)|
|**errorLog**  <br>*optional*||[ErrorLog](#errorlog)|
|**lastModifiedDate**  <br>*optional*|Date last modified|[LocalDateTime](#localdatetime)|
|**message**  <br>*optional*|Message for users|string|
|**pk**  <br>*optional*||string|
|**remoteAddress**  <br>*optional*|The address the request originated from|string|
|**userAgent**  <br>*optional*|The UA string if provided with a request|[UserAgent](#useragent)|


<a name="baseerrorservice"></a>
### BaseErrorService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="beanfactory"></a>
### BeanFactory
*Type* : object


<a name="cellphonedetail"></a>
### CellphoneDetail
CellphoneDetail


|Name|Schema|
|---|---|
|**contactDetails**  <br>*optional*|[ContactDetails](#contactdetails)|
|**description**  <br>*optional*|string|
|**number**  <br>*optional*|string|
|**pk**  <br>*optional*|string|
|**primary**  <br>*optional*|boolean|
|**value**  <br>*optional*|string|
|**verificationToken**  <br>*optional*|string|
|**verified**  <br>*optional*|boolean|


<a name="character"></a>
### Character
*Type* : object


<a name="chronology"></a>
### Chronology

|Name|Schema|
|---|---|
|**calendarType**  <br>*optional*|string|
|**id**  <br>*optional*|string|


<a name="classloader"></a>
### ClassLoader

|Name|Schema|
|---|---|
|**parent**  <br>*optional*|[ClassLoader](#classloader)|


<a name="clienterror"></a>
### ClientError
Client errors are created upon client request and refer to exceptions occurred specifically within client application code.


|Name|Description|Schema|
|---|---|---|
|**createdBy**  <br>*optional*|The initial content author|[UserDTO](#userdto)|
|**createdDate**  <br>*optional*|Date created|[LocalDateTime](#localdatetime)|
|**description**  <br>*optional*|The error description provided by the user, if any.|string|
|**errorLog**  <br>*optional*||[ErrorLog](#errorlog)|
|**lastModifiedDate**  <br>*optional*|Date last modified|[LocalDateTime](#localdatetime)|
|**message**  <br>*optional*|Message for users|string|
|**pk**  <br>*optional*||string|
|**remoteAddress**  <br>*optional*|The address the request originated from|string|
|**screenshotUrl**  <br>*optional*|A client application screenshot demonstrating the issue.|string|
|**state**  <br>*optional*|A textual representation of the client application state, e.g. the DOM tree.|string|
|**userAgent**  <br>*optional*|The UA string if provided with a request|[UserAgent](#useragent)|
|**viewUrl**  <br>*optional*|The URL relevant to application view state during the error e.g. window.location.href, if any|string|


<a name="clienterrorservice"></a>
### ClientErrorService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="d80a5819530b6f6a1ed38f5baa9bd31b"></a>
### Collection«ErrorModel«BaseError»»
*Type* : object


<a name="0ffecb6b6e4d11271bdcadd7b617f7e7"></a>
### Collection«ErrorModel«ClientError»»
*Type* : object


<a name="4c94d3dbdbdd049bd0d0278202f83ee5"></a>
### Collection«ErrorModel«ContactDetails»»
*Type* : object


<a name="c570c7ce0e98127938697efeb180151f"></a>
### Collection«ErrorModel«Continent»»
*Type* : object


<a name="75873d802950225a02afef89c53d32ee"></a>
### Collection«ErrorModel«Country»»
*Type* : object


<a name="a4161e3c9f6cc90fd604bb34f21189af"></a>
### Collection«ErrorModel«ErrorLog»»
*Type* : object


<a name="6de8340d560a4195bb6e4493443b5fb5"></a>
### Collection«ErrorModel«Friendship»»
*Type* : object


<a name="bfce32545f8baddfb0af5c7bb7a8f8cd"></a>
### Collection«ErrorModel«Host»»
*Type* : object


<a name="f30263e5f13760f50e9dde320bf71cbc"></a>
### Collection«ErrorModel«Locality»»
*Type* : object


<a name="3faca06816065403a281df6d41af99f9"></a>
### Collection«ErrorModel«PersonalDetails»»
*Type* : object


<a name="dbc490fca4dc27db700194674b87ee7c"></a>
### Collection«ErrorModel«PostalCode»»
*Type* : object


<a name="76e1a50f233bbcd28bbbe21505e54a60"></a>
### Collection«ErrorModel«Role»»
*Type* : object


<a name="9b41aa499ad7488579ad5ccfd56e59b6"></a>
### Collection«ErrorModel«STOMP Session»»
*Type* : object


<a name="72dc27dfabf6f1b845b0ff0b5c17ee58"></a>
### Collection«ErrorModel«SystemError»»
*Type* : object


<a name="03e6a825c736188927b41f2f650446ed"></a>
### Collection«ErrorModel«UserAgent»»
*Type* : object


<a name="62a74aba2f3f725a08fde36126e4255b"></a>
### Collection«ErrorModel«UserCredentials»»
*Type* : object


<a name="c2e299415dc76cd852fa3a04b2eb068a"></a>
### Collection«ErrorModel«UserRegistrationCodeBatch»»
*Type* : object


<a name="935e73110e9623f434110bff0878e6d3"></a>
### Collection«ErrorModel«UserRegistrationCode»»
*Type* : object


<a name="4c14262041c5436d451d3e9cc7a3c5a0"></a>
### Collection«ErrorModel«User»»
*Type* : object


<a name="6726e8fb45e4d9ae05913655d3f4b3a3"></a>
### Collection«GrantedAuthority»
*Type* : object


<a name="be96f6a2aa35b1355d0079563f88c41d"></a>
### Collection«JsonApiResource«BaseError,string»»
*Type* : object


<a name="b8fff7e6c92a46261acf0084d9f565a7"></a>
### Collection«JsonApiResource«ClientError,string»»
*Type* : object


<a name="e89d346f6067e6947fb964cbdfce3183"></a>
### Collection«JsonApiResource«ContactDetails,string»»
*Type* : object


<a name="413dd3c3e51da305c0479e735907ba20"></a>
### Collection«JsonApiResource«Continent,string»»
*Type* : object


<a name="fc419c3f4f7eb167d0eb1612c0584bd5"></a>
### Collection«JsonApiResource«Country,string»»
*Type* : object


<a name="714120d040c131338716e518a244b3a0"></a>
### Collection«JsonApiResource«ErrorLog,string»»
*Type* : object


<a name="e0761df0eb6fb385a21f14620c9fb3e5"></a>
### Collection«JsonApiResource«Friendship,FriendshipIdentifier»»
*Type* : object


<a name="4a2e508922c7f34c2258bb1d72f4aebb"></a>
### Collection«JsonApiResource«Host,string»»
*Type* : object


<a name="1692355a4529facbc3c186ef47dbbb79"></a>
### Collection«JsonApiResource«Locality,string»»
*Type* : object


<a name="104cef64f4a977dae04c37f318cc95a0"></a>
### Collection«JsonApiResource«PersonalDetails,string»»
*Type* : object


<a name="cd57ab0dc665880e5434e2f3a3f308ee"></a>
### Collection«JsonApiResource«PostalCode,string»»
*Type* : object


<a name="73cfd63705d242691d62fdcff163e940"></a>
### Collection«JsonApiResource«Role,string»»
*Type* : object


<a name="7bc1395e14c0d5e2aea3c03e101d79c4"></a>
### Collection«JsonApiResource«STOMP Session,string»»
*Type* : object


<a name="84f4cff5b6497337696e9b745f52181b"></a>
### Collection«JsonApiResource«SystemError,string»»
*Type* : object


<a name="2b53832dc0c7400979e8fc951a06759a"></a>
### Collection«JsonApiResource«User,string»»
*Type* : object


<a name="91f080513fd8bc697b8ce8d3bf44f469"></a>
### Collection«JsonApiResource«UserAgent,string»»
*Type* : object


<a name="20a42c724b41a7e49385d4cd14c412ca"></a>
### Collection«JsonApiResource«UserCredentials,string»»
*Type* : object


<a name="beb91bd7486a9af5df2cb54eff571a7d"></a>
### Collection«JsonApiResource«UserRegistrationCode,string»»
*Type* : object


<a name="b5e6daf80b3785414b47672236d4fa01"></a>
### Collection«JsonApiResource«UserRegistrationCodeBatch,string»»
*Type* : object


<a name="70921457e8c302937155d90300761195"></a>
### Collection«ModelResource«BaseError»»
*Type* : object


<a name="00364d37f7531cb1bdde06fb47bdb5d0"></a>
### Collection«ModelResource«ClientError»»
*Type* : object


<a name="2b744efc0c0f2f385a08d81dd1a13486"></a>
### Collection«ModelResource«ContactDetails»»
*Type* : object


<a name="a1a1c7dae1f03005696e0ae8fd5eecd2"></a>
### Collection«ModelResource«Continent»»
*Type* : object


<a name="a6931c3d843f8c5b56325c303035a19e"></a>
### Collection«ModelResource«Country»»
*Type* : object


<a name="00e63ec4a07cc804db25ec2ff0c96b8d"></a>
### Collection«ModelResource«ErrorLog»»
*Type* : object


<a name="4508411b3b8be5641d1fc4aaf38a46c6"></a>
### Collection«ModelResource«Friendship»»
*Type* : object


<a name="88f30c2b9c670c0197c9d9a3d8e5e875"></a>
### Collection«ModelResource«Host»»
*Type* : object


<a name="57bc0bee0ddb758cb3c32f12d23a373e"></a>
### Collection«ModelResource«Locality»»
*Type* : object


<a name="5cca3d480d18635094def6d180509b25"></a>
### Collection«ModelResource«PersonalDetails»»
*Type* : object


<a name="ea570866899cd292df9e25d038486345"></a>
### Collection«ModelResource«PostalCode»»
*Type* : object


<a name="78b6b7fe52f91ae431fa58fba65817c8"></a>
### Collection«ModelResource«Role»»
*Type* : object


<a name="6df143473f1b30c6e6a168eb5bb2f9a7"></a>
### Collection«ModelResource«STOMP Session»»
*Type* : object


<a name="adfd3d94f4ba9178746fd11d6759f8b1"></a>
### Collection«ModelResource«SystemError»»
*Type* : object


<a name="6b950887fd22022021c5ce263de0ac3d"></a>
### Collection«ModelResource«UserAgent»»
*Type* : object


<a name="15e05ad15be3da397044fbccc90bf87e"></a>
### Collection«ModelResource«UserCredentials»»
*Type* : object


<a name="96cb0a334fb4ea62c1b31c165d23f0e9"></a>
### Collection«ModelResource«UserRegistrationCodeBatch»»
*Type* : object


<a name="cf7ab9d28e0eb49ec72ba0a8f0374565"></a>
### Collection«ModelResource«UserRegistrationCode»»
*Type* : object


<a name="cc5ab41b8db0ab4cb2c0986459e7a9b6"></a>
### Collection«ModelResource«User»»
*Type* : object


<a name="constraintviolationentry"></a>
### ConstraintViolationEntry
DTO class for serialization of bean validation violations.


|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**propertyPath**  <br>*optional*|string|


<a name="contactdetails"></a>
### ContactDetails
ContactDetails


|Name|Schema|
|---|---|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|
|**primaryAddress**  <br>*optional*|[PostalAddressDetail](#postaladdressdetail)|
|**primaryCellphone**  <br>*optional*|[CellphoneDetail](#cellphonedetail)|
|**primaryEmail**  <br>*optional*|[EmailDetail](#emaildetail)|
|**user**  <br>*optional*|[User](#user)|


<a name="contactdetailsservice"></a>
### ContactDetailsService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="continent"></a>
### Continent

|Name|Schema|
|---|---|
|**name**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**parent**  <br>*optional*|[Continent](#continent)|
|**path**  <br>*optional*|string|
|**pathLevel**  <br>*optional*|integer(int32)|
|**pk**  <br>*optional*|string|


<a name="continentservice"></a>
### ContinentService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="conversionservice"></a>
### ConversionService
*Type* : object


<a name="country"></a>
### Country
A model representing a country, meaning a region that is identified as a distinct entity in political geography.


|Name|Schema|
|---|---|
|**callingCode**  <br>*optional*|string|
|**capital**  <br>*optional*|string|
|**currency**  <br>*optional*|string|
|**languages**  <br>*optional*|string|
|**name**  <br>*optional*|string|
|**nativeName**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**parent**  <br>*optional*|[Continent](#continent)|
|**path**  <br>*optional*|string|
|**pathLevel**  <br>*optional*|integer(int32)|
|**pk**  <br>*optional*|string|


<a name="countryservice"></a>
### CountryService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="emailconfirmationorpasswordresetrequest"></a>
### EmailConfirmationOrPasswordResetRequest
Submitting a password reset request triggers a reset token and email if resetPasswordToken is null, updates the password if resetPasswordToken is not null and valid,


|Name|Schema|
|---|---|
|**currentPassword**  <br>*optional*|string|
|**email**  <br>*optional*|string|
|**password**  <br>*optional*|string|
|**passwordConfirmation**  <br>*optional*|string|
|**resetPasswordToken**  <br>*optional*|string|
|**username**  <br>*optional*|string|


<a name="emaildetail"></a>
### EmailDetail
EmailDetail


|Name|Schema|
|---|---|
|**contactDetails**  <br>*optional*|[ContactDetails](#contactdetails)|
|**description**  <br>*optional*|string|
|**email**  <br>*optional*|string|
|**pk**  <br>*optional*|string|
|**primary**  <br>*optional*|boolean|
|**value**  <br>*optional*|string|
|**verificationToken**  <br>*optional*|string|
|**verified**  <br>*optional*|boolean|


<a name="environment"></a>
### Environment

|Name|Schema|
|---|---|
|**activeProfiles**  <br>*optional*|< string > array|
|**defaultProfiles**  <br>*optional*|< string > array|


<a name="era"></a>
### Era

|Name|Schema|
|---|---|
|**value**  <br>*optional*|integer(int32)|


<a name="errorlog"></a>
### ErrorLog
Used to persist error stacktraces using the corresponding hash as the ID. The generated hash ignores line numbers (in case of SystemError) and is thus tolerant of small code changes, like adding a comment line.


|Name|Description|Schema|
|---|---|---|
|**errorCount**  <br>*optional*|The number of errors corresponding to this stacktrace.|integer(int32)|
|**firstOccurred**  <br>*required*|First occurrence date|[LocalDateTime](#localdatetime)|
|**lastOccurred**  <br>*required*|Last occurrence date|[LocalDateTime](#localdatetime)|
|**name**  <br>*optional*||string|
|**pk**  <br>*optional*||string|
|**rootCauseMessage**  <br>*optional*|The root cause message.|string|
|**stacktrace**  <br>*required*|The actual stacktrace.|string|


<a name="errorlogservice"></a>
### ErrorLogService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="6cffa7616f5c54c0efb258ab9dde2709"></a>
### ErrorModel«BaseError»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="02870edeadba481f5ba192a5143868cc"></a>
### ErrorModel«ClientError»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="f98a5df33ee08c5dced9998ed6b3bf72"></a>
### ErrorModel«ContactDetails»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="9beab2754ed1eb5b0d82a5be284027bf"></a>
### ErrorModel«Continent»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="992f93e3a981d28a2ff4d8c0cea0cad6"></a>
### ErrorModel«Country»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="219862de4d8446871c3017f3332b0413"></a>
### ErrorModel«ErrorLog»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="c59d4aed91beb0d8f73fa19423050bde"></a>
### ErrorModel«Friendship»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|[FriendshipIdentifier](#friendshipidentifier)|


<a name="d2e5941465155ef0706b9a72a8ab825d"></a>
### ErrorModel«Host»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="a312b9b8ea3df484b6aab199f7bad52c"></a>
### ErrorModel«Locality»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="417d78209d0f07df79a0fe5f8f6e3572"></a>
### ErrorModel«PersonalDetails»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="63d2e12fde3140c7357846da222cbe71"></a>
### ErrorModel«PostalCode»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="e1cd8b8e8f8807597329d7827e36c951"></a>
### ErrorModel«Role»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="74284c0248c321ad4462f7b678ff4705"></a>
### ErrorModel«STOMP Session»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="9eebef0fcc1e33a413568bb5be9af7d6"></a>
### ErrorModel«SystemError»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="dfb9cc23b5152b4a846b2bc4b8cd546e"></a>
### ErrorModel«UserAgent»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="716e08c2b42d7bf06da95516e57f68ff"></a>
### ErrorModel«UserCredentials»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="d87c23ccdca5b9c7ad2e8dad74631149"></a>
### ErrorModel«UserRegistrationCodeBatch»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="01e4b5e3c8a7475affb2b2f0a2e61380"></a>
### ErrorModel«UserRegistrationCode»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="2ff76e58d22da2c44a07004cc74fef41"></a>
### ErrorModel«User»

|Name|Schema|
|---|---|
|**message**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|


<a name="filepersistenceservice"></a>
### FilePersistenceService
*Type* : object


<a name="friendship"></a>
### Friendship
A model representing a directional connection between two users.


|Name|Schema|
|---|---|
|**pk**  <br>*required*|[FriendshipIdentifier](#friendshipidentifier)|
|**status**  <br>*required*|enum (NEW, CONFIRMED, BLOCK, DELETE)|


<a name="friendshipidentifier"></a>
### FriendshipIdentifier
An {@link javax.persistence;Embeddable} JPA composite key. The custom implementation provides support to all relevant de)serialization components (JSON, request mappings, pathFragment/param variables etc.) for both [ownerId_friendId] and [friendId] string representations.


|Name|Schema|
|---|---|
|**left**  <br>*required*|[User](#user)|
|**right**  <br>*required*|[User](#user)|


<a name="friendshipservice"></a>
### FriendshipService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="grantedauthority"></a>
### GrantedAuthority

|Name|Schema|
|---|---|
|**authority**  <br>*optional*|string|


<a name="host"></a>
### Host

|Name|Schema|
|---|---|
|**aliases**  <br>*optional*|< string > array|
|**country**  <br>*optional*|[Country](#country)|
|**description**  <br>*optional*|string|
|**name**  <br>*optional*|string|
|**pk**  <br>*optional*|string|


<a name="hostservice"></a>
### HostService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="isochronology"></a>
### IsoChronology

|Name|Schema|
|---|---|
|**calendarType**  <br>*optional*|string|
|**id**  <br>*optional*|string|


<a name="df365763e04325ca180e482d51c349a6"></a>
### JsonApiDocument«BaseError»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«BaseError»»](#d80a5819530b6f6a1ed38f5baa9bd31b)|
|**included**  <br>*optional*|[Collection«JsonApiResource«BaseError,string»»](#be96f6a2aa35b1355d0079563f88c41d)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="8f95abb5eec91b5f71ff884b7ed28591"></a>
### JsonApiDocument«ClientError»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«ClientError»»](#0ffecb6b6e4d11271bdcadd7b617f7e7)|
|**included**  <br>*optional*|[Collection«JsonApiResource«ClientError,string»»](#b8fff7e6c92a46261acf0084d9f565a7)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="10fc875f5c1fa5b72937f5046c4ae1d8"></a>
### JsonApiDocument«ContactDetails»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«ContactDetails»»](#4c94d3dbdbdd049bd0d0278202f83ee5)|
|**included**  <br>*optional*|[Collection«JsonApiResource«ContactDetails,string»»](#e89d346f6067e6947fb964cbdfce3183)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="0f036f114b5e05511e9588f1408dff12"></a>
### JsonApiDocument«Continent»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«Continent»»](#c570c7ce0e98127938697efeb180151f)|
|**included**  <br>*optional*|[Collection«JsonApiResource«Continent,string»»](#413dd3c3e51da305c0479e735907ba20)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="589300b57e2957860933fc5165b96d74"></a>
### JsonApiDocument«Country»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«Country»»](#75873d802950225a02afef89c53d32ee)|
|**included**  <br>*optional*|[Collection«JsonApiResource«Country,string»»](#fc419c3f4f7eb167d0eb1612c0584bd5)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="770e1184ef1d68a3758bfc6fa22bd444"></a>
### JsonApiDocument«ErrorLog»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«ErrorLog»»](#a4161e3c9f6cc90fd604bb34f21189af)|
|**included**  <br>*optional*|[Collection«JsonApiResource«ErrorLog,string»»](#714120d040c131338716e518a244b3a0)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="faf39d664962d27886c93cb8ef8ff68d"></a>
### JsonApiDocument«Friendship»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«Friendship»»](#6de8340d560a4195bb6e4493443b5fb5)|
|**included**  <br>*optional*|[Collection«JsonApiResource«Friendship,FriendshipIdentifier»»](#e0761df0eb6fb385a21f14620c9fb3e5)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="19132450ec6514b538e0effd0aeffed6"></a>
### JsonApiDocument«Host»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«Host»»](#bfce32545f8baddfb0af5c7bb7a8f8cd)|
|**included**  <br>*optional*|[Collection«JsonApiResource«Host,string»»](#4a2e508922c7f34c2258bb1d72f4aebb)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="dda25a325eb794dd742ae052439f5c07"></a>
### JsonApiDocument«Locality»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«Locality»»](#f30263e5f13760f50e9dde320bf71cbc)|
|**included**  <br>*optional*|[Collection«JsonApiResource«Locality,string»»](#1692355a4529facbc3c186ef47dbbb79)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="a8a2d657326f1f93b5ef013d8a741471"></a>
### JsonApiDocument«PersonalDetails»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«PersonalDetails»»](#3faca06816065403a281df6d41af99f9)|
|**included**  <br>*optional*|[Collection«JsonApiResource«PersonalDetails,string»»](#104cef64f4a977dae04c37f318cc95a0)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="b920b95da8ca7f7cc5fd465aead82495"></a>
### JsonApiDocument«PostalCode»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«PostalCode»»](#dbc490fca4dc27db700194674b87ee7c)|
|**included**  <br>*optional*|[Collection«JsonApiResource«PostalCode,string»»](#cd57ab0dc665880e5434e2f3a3f308ee)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="e47450c548f4c9b64835e5c9621f6d3a"></a>
### JsonApiDocument«Role»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«Role»»](#76e1a50f233bbcd28bbbe21505e54a60)|
|**included**  <br>*optional*|[Collection«JsonApiResource«Role,string»»](#73cfd63705d242691d62fdcff163e940)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="daadde0b097fcb9670306080ceb6101d"></a>
### JsonApiDocument«STOMP Session»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«STOMP Session»»](#9b41aa499ad7488579ad5ccfd56e59b6)|
|**included**  <br>*optional*|[Collection«JsonApiResource«STOMP Session,string»»](#7bc1395e14c0d5e2aea3c03e101d79c4)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="d38ff8b806dc84761874de3322b9f189"></a>
### JsonApiDocument«SystemError»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«SystemError»»](#72dc27dfabf6f1b845b0ff0b5c17ee58)|
|**included**  <br>*optional*|[Collection«JsonApiResource«SystemError,string»»](#84f4cff5b6497337696e9b745f52181b)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="acf81c73a4b814d521aed28969421ff6"></a>
### JsonApiDocument«UserAgent»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«UserAgent»»](#03e6a825c736188927b41f2f650446ed)|
|**included**  <br>*optional*|[Collection«JsonApiResource«UserAgent,string»»](#91f080513fd8bc697b8ce8d3bf44f469)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="6090c6193e6c6e58f94588385b61d2e4"></a>
### JsonApiDocument«UserCredentials»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«UserCredentials»»](#62a74aba2f3f725a08fde36126e4255b)|
|**included**  <br>*optional*|[Collection«JsonApiResource«UserCredentials,string»»](#20a42c724b41a7e49385d4cd14c412ca)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="29e1e912f10e19c7abb03c1e4774dedf"></a>
### JsonApiDocument«UserRegistrationCodeBatch»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«UserRegistrationCodeBatch»»](#c2e299415dc76cd852fa3a04b2eb068a)|
|**included**  <br>*optional*|[Collection«JsonApiResource«UserRegistrationCodeBatch,string»»](#b5e6daf80b3785414b47672236d4fa01)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="380220dcbc5985a5e7ebdfb31d4db480"></a>
### JsonApiDocument«UserRegistrationCode»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«UserRegistrationCode»»](#935e73110e9623f434110bff0878e6d3)|
|**included**  <br>*optional*|[Collection«JsonApiResource«UserRegistrationCode,string»»](#beb91bd7486a9af5df2cb54eff571a7d)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="edbd1b77d1868403783de6ea25379f49"></a>
### JsonApiDocument«User»

|Name|Schema|
|---|---|
|**data**  <br>*optional*|object|
|**errors**  <br>*optional*|[Collection«ErrorModel«User»»](#4c14262041c5436d451d3e9cc7a3c5a0)|
|**included**  <br>*optional*|[Collection«JsonApiResource«User,string»»](#2b53832dc0c7400979e8fc951a06759a)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|


<a name="jsonapilink"></a>
### JsonApiLink

|Name|Schema|
|---|---|
|**href**  <br>*optional*|string|
|**meta**  <br>*optional*|< string, [Serializable](#serializable) > map|
|**rel**  <br>*optional*|string|


<a name="781d3bedec9b50d8aa79ce3ac7ab2b2a"></a>
### JsonApiModelResourceCollectionDocument«BaseError,string»
*Type* : object


<a name="bee4c46293bd3d3f46adbe84243098f4"></a>
### JsonApiModelResourceCollectionDocument«ClientError,string»
*Type* : object


<a name="3f67ea91d41d9f0def3f8c7801ad2953"></a>
### JsonApiModelResourceCollectionDocument«ContactDetails,string»
*Type* : object


<a name="d785db3dd071808d80533a03ee94339d"></a>
### JsonApiModelResourceCollectionDocument«Continent,string»
*Type* : object


<a name="2586e9815a1d3770bbba9e02a22c0b39"></a>
### JsonApiModelResourceCollectionDocument«Country,string»
*Type* : object


<a name="6b4dd139b32c90fd009baad01b32c162"></a>
### JsonApiModelResourceCollectionDocument«ErrorLog,string»
*Type* : object


<a name="af8536276cc4e9bdedacfcf316edd89a"></a>
### JsonApiModelResourceCollectionDocument«Friendship,FriendshipIdentifier»
*Type* : object


<a name="31e0dba02ee9d4ed5d5b436e5c85abed"></a>
### JsonApiModelResourceCollectionDocument«Host,string»
*Type* : object


<a name="58b3a03951f87f54c3774f5458328fa9"></a>
### JsonApiModelResourceCollectionDocument«Locality,string»
*Type* : object


<a name="3f0fafc749403f392d91618c84d81c90"></a>
### JsonApiModelResourceCollectionDocument«PersonalDetails,string»
*Type* : object


<a name="85481637dd2dcf44000d1cef7f5884d5"></a>
### JsonApiModelResourceCollectionDocument«PostalCode,string»
*Type* : object


<a name="cd608af1be5799d86f6f7b01a20ec137"></a>
### JsonApiModelResourceCollectionDocument«Role,string»
*Type* : object


<a name="790b74b0f37b62a564d09f14528b84f1"></a>
### JsonApiModelResourceCollectionDocument«STOMP Session,string»
*Type* : object


<a name="5311255b1149577ff2838e7e6485ae46"></a>
### JsonApiModelResourceCollectionDocument«SystemError,string»
*Type* : object


<a name="6e13d001e2dbeda7256e896a52a2daec"></a>
### JsonApiModelResourceCollectionDocument«User,string»
*Type* : object


<a name="97534114b53ad68cb0e908c9dc6f5492"></a>
### JsonApiModelResourceCollectionDocument«UserAgent,string»
*Type* : object


<a name="a0f6ed6cfde1dc93e23c62522bc52901"></a>
### JsonApiModelResourceCollectionDocument«UserCredentials,string»
*Type* : object


<a name="f64fb1023590f72a0911993f753660fe"></a>
### JsonApiModelResourceCollectionDocument«UserRegistrationCode,string»
*Type* : object


<a name="1045f3a14c3c751283625462618e2fcf"></a>
### JsonApiModelResourceCollectionDocument«UserRegistrationCodeBatch,string»
*Type* : object


<a name="788c3bcb0cd8e8f495ab85014de843cb"></a>
### JsonApiModelResourceDocument«BaseError,string»
*Type* : object


<a name="c9eabaff21c531951357eb93831ff776"></a>
### JsonApiModelResourceDocument«ClientError,string»
*Type* : object


<a name="b25d6e708aa2c3e2be1df17d5e3a9251"></a>
### JsonApiModelResourceDocument«ContactDetails,string»
*Type* : object


<a name="a6420b18357b50929f7db6443f6fd529"></a>
### JsonApiModelResourceDocument«Continent,string»
*Type* : object


<a name="adbc7ab7820cfd03fefcaf75b93da36c"></a>
### JsonApiModelResourceDocument«Country,string»
*Type* : object


<a name="f52d744c821370dc62d23d2b1e0b0f67"></a>
### JsonApiModelResourceDocument«ErrorLog,string»
*Type* : object


<a name="162950cb6ec633865b848ca5d98c998c"></a>
### JsonApiModelResourceDocument«Friendship,FriendshipIdentifier»
*Type* : object


<a name="fab1b612ff3f6f782c667482f5518953"></a>
### JsonApiModelResourceDocument«Host,string»
*Type* : object


<a name="6545fb1d04c5b234bd345fbe2d503db8"></a>
### JsonApiModelResourceDocument«Locality,string»
*Type* : object


<a name="5d260e67e7c78ea334bf945d3ceb4f43"></a>
### JsonApiModelResourceDocument«PersonalDetails,string»
*Type* : object


<a name="948547db6b23e5934426b4fd77d2441b"></a>
### JsonApiModelResourceDocument«PostalCode,string»
*Type* : object


<a name="91f0906ce82be845edf829468cf32ff5"></a>
### JsonApiModelResourceDocument«Role,string»
*Type* : object


<a name="88093c7bc43b97c1d9f0a75f9593843d"></a>
### JsonApiModelResourceDocument«STOMP Session,string»
*Type* : object


<a name="b7b83c54682cba591a226330ae27a07b"></a>
### JsonApiModelResourceDocument«SystemError,string»
*Type* : object


<a name="656dfd54c1eb474fdc8aadf368bc606e"></a>
### JsonApiModelResourceDocument«User,string»
*Type* : object


<a name="9107a0c6f1bedb59ed8c453e7f3e2c67"></a>
### JsonApiModelResourceDocument«UserAgent,string»
*Type* : object


<a name="410073fa1e16c29e59a98561490d3e9a"></a>
### JsonApiModelResourceDocument«UserCredentials,string»
*Type* : object


<a name="21bd7755aaf27c46fad29cb7e1ff25a1"></a>
### JsonApiModelResourceDocument«UserRegistrationCode,string»
*Type* : object


<a name="fc8442e5fbd6124269c0d38f08edbaa4"></a>
### JsonApiModelResourceDocument«UserRegistrationCodeBatch,string»
*Type* : object


<a name="59eb277998c1b999ecb58862af311c2b"></a>
### JsonApiResource«BaseError,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="44086315d1f7905a0044b07142e9d771"></a>
### JsonApiResource«ClientError,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="1547db767fc982f650d379577c5cf0b6"></a>
### JsonApiResource«ContactDetails,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="a7d277cb0df454f3c9958d15318cd1c9"></a>
### JsonApiResource«Continent,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="4354951268fb84a7eeac690c06e68c3a"></a>
### JsonApiResource«Country,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="af82ca26e32b495e91705157972eb3fa"></a>
### JsonApiResource«ErrorLog,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="30aac45dc6bb6260a4f1424578b3c4cd"></a>
### JsonApiResource«Friendship,FriendshipIdentifier»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="2011a2feb9aa8441a526b64a29638d7a"></a>
### JsonApiResource«Host,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="db493917c953a275f2d3ea5fb38278e2"></a>
### JsonApiResource«Locality,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="8bf43272176941b65c94e06fe751c498"></a>
### JsonApiResource«PersonalDetails,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="f59b14de15c9b9a72ebb60e75356279b"></a>
### JsonApiResource«PostalCode,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="684560b6440e0c5f9549b511be5a82ab"></a>
### JsonApiResource«Role,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="e31a994ecec1f47d0dd388c3de5c9259"></a>
### JsonApiResource«STOMP Session,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="59044b47a48c9c82e0524247ad9793e8"></a>
### JsonApiResource«SystemError,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="7045a9017d0a2899ea197b57dbe8056b"></a>
### JsonApiResource«User,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="461c55ad8a9ea15f0879e485ebf85f4c"></a>
### JsonApiResource«UserAgent,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="12901a55eb375c46db882bd60cefda19"></a>
### JsonApiResource«UserCredentials,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="15b608a6056c881952ae750795345b34"></a>
### JsonApiResource«UserRegistrationCode,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="93d95dcf41c3afb9ce6aa2960d957d04"></a>
### JsonApiResource«UserRegistrationCodeBatch,string»

|Name|Schema|
|---|---|
|**attributes**  <br>*optional*|[Serializable](#serializable)|
|**links**  <br>*optional*|< string, [JsonApiLink](#jsonapilink) > map|


<a name="jwttoken"></a>
### JwtToken

|Name|Schema|
|---|---|
|**token**  <br>*optional*|string|


<a name="link"></a>
### Link

|Name|Schema|
|---|---|
|**href**  <br>*optional*|string|
|**rel**  <br>*optional*|string|
|**templated**  <br>*optional*|boolean|


<a name="localdate"></a>
### LocalDate

|Name|Schema|
|---|---|
|**chronology**  <br>*optional*|[IsoChronology](#isochronology)|
|**dayOfMonth**  <br>*optional*|integer(int32)|
|**dayOfWeek**  <br>*optional*|enum (MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY)|
|**dayOfYear**  <br>*optional*|integer(int32)|
|**era**  <br>*optional*|[Era](#era)|
|**leapYear**  <br>*optional*|boolean|
|**month**  <br>*optional*|enum (JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER)|
|**monthValue**  <br>*optional*|integer(int32)|
|**year**  <br>*optional*|integer(int32)|


<a name="localdatetime"></a>
### LocalDateTime

|Name|Schema|
|---|---|
|**chronology**  <br>*optional*|[Chronology](#chronology)|
|**dayOfMonth**  <br>*optional*|integer(int32)|
|**dayOfWeek**  <br>*optional*|enum (MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY)|
|**dayOfYear**  <br>*optional*|integer(int32)|
|**hour**  <br>*optional*|integer(int32)|
|**minute**  <br>*optional*|integer(int32)|
|**month**  <br>*optional*|enum (JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER)|
|**monthValue**  <br>*optional*|integer(int32)|
|**nano**  <br>*optional*|integer(int32)|
|**second**  <br>*optional*|integer(int32)|
|**year**  <br>*optional*|integer(int32)|


<a name="locale"></a>
### Locale

|Name|Schema|
|---|---|
|**country**  <br>*optional*|string|
|**displayCountry**  <br>*optional*|string|
|**displayLanguage**  <br>*optional*|string|
|**displayName**  <br>*optional*|string|
|**displayScript**  <br>*optional*|string|
|**displayVariant**  <br>*optional*|string|
|**extensionKeys**  <br>*optional*|< [Character](#character) > array|
|**iso3Country**  <br>*optional*|string|
|**iso3Language**  <br>*optional*|string|
|**language**  <br>*optional*|string|
|**script**  <br>*optional*|string|
|**unicodeLocaleAttributes**  <br>*optional*|< string > array|
|**unicodeLocaleKeys**  <br>*optional*|< string > array|
|**variant**  <br>*optional*|string|


<a name="locality"></a>
### Locality
A model representing an incorporated city or town political entity


|Name|Schema|
|---|---|
|**name**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**parent**  <br>*optional*|[Country](#country)|
|**path**  <br>*optional*|string|
|**pathLevel**  <br>*optional*|integer(int32)|
|**pk**  <br>*optional*|string|


<a name="localityservice"></a>
### LocalityService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="login-submission"></a>
### Login Submission
A DTO representing a login request


|Name|Schema|
|---|---|
|**email**  <br>*optional*|string|
|**password**  <br>*optional*|string|
|**resetPasswordToken**  <br>*optional*|string|
|**username**  <br>*optional*|string|


<a name="metadatumdto"></a>
### MetadatumDTO

|Name|Schema|
|---|---|
|**object**  <br>*optional*|string|
|**predicate**  <br>*optional*|string|


<a name="25d85cda698c35bf1f8019cd4014ad85"></a>
### ModelResources«BaseError»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«BaseError»»](#70921457e8c302937155d90300761195)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="f574653cd2faa5212cea56d9342c29bc"></a>
### ModelResources«ClientError»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«ClientError»»](#00364d37f7531cb1bdde06fb47bdb5d0)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="8762ba8c004d6b87b95956d9f2b94a35"></a>
### ModelResources«ContactDetails»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«ContactDetails»»](#2b744efc0c0f2f385a08d81dd1a13486)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="0d94a4e6f9389b6a4d3f8ebcb0a245e8"></a>
### ModelResources«Continent»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«Continent»»](#a1a1c7dae1f03005696e0ae8fd5eecd2)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="dca97e35bd8781395bc5a0e62b45d110"></a>
### ModelResources«Country»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«Country»»](#a6931c3d843f8c5b56325c303035a19e)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="6f296eb06faba963ac81007e5afcf596"></a>
### ModelResources«ErrorLog»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«ErrorLog»»](#00e63ec4a07cc804db25ec2ff0c96b8d)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="f0cc34d71ff6505ccd7a3b9bd8d19ee4"></a>
### ModelResources«Friendship»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«Friendship»»](#4508411b3b8be5641d1fc4aaf38a46c6)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="4deb6df5e56fd3bc7d192a4623fd2beb"></a>
### ModelResources«Host»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«Host»»](#88f30c2b9c670c0197c9d9a3d8e5e875)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="eaaf18024132049fcf1a477995161123"></a>
### ModelResources«Locality»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«Locality»»](#57bc0bee0ddb758cb3c32f12d23a373e)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="b5e062ca38ae416d4d7959f028c66bc0"></a>
### ModelResources«PersonalDetails»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«PersonalDetails»»](#5cca3d480d18635094def6d180509b25)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="fa8906727cade0eec7dd5815a1f85352"></a>
### ModelResources«PostalCode»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«PostalCode»»](#ea570866899cd292df9e25d038486345)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="55082ee791cbcae25d6e08acb4a12069"></a>
### ModelResources«Role»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«Role»»](#78b6b7fe52f91ae431fa58fba65817c8)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="9f569ad1e1140ab5c7d46966a59b44d2"></a>
### ModelResources«STOMP Session»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«STOMP Session»»](#6df143473f1b30c6e6a168eb5bb2f9a7)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="e01a2e29666837e6c67ca53b4c34d4cb"></a>
### ModelResources«SystemError»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«SystemError»»](#adfd3d94f4ba9178746fd11d6759f8b1)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="ccf8c8d3b2e5667d4cf1df279345b929"></a>
### ModelResources«UserAgent»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«UserAgent»»](#6b950887fd22022021c5ce263de0ac3d)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="f36a79dc91547f07c86ebf0cd3c3440a"></a>
### ModelResources«UserCredentials»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«UserCredentials»»](#15e05ad15be3da397044fbccc90bf87e)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="c6c606ceb400df54bca43f11d9751b1e"></a>
### ModelResources«UserRegistrationCodeBatch»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«UserRegistrationCodeBatch»»](#96cb0a334fb4ea62c1b31c165d23f0e9)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="211fafa1061b92660c0272af8826e275"></a>
### ModelResources«UserRegistrationCode»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«UserRegistrationCode»»](#cf7ab9d28e0eb49ec72ba0a8f0374565)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="fc1ae26c5789cf20fb6269b6f59edd82"></a>
### ModelResources«User»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«User»»](#cc5ab41b8db0ab4cb2c0986459e7a9b6)|
|**_links**  <br>*optional*|< [Link](#link) > array|


<a name="b42c3de17b4d667bee2211eda9bc4253"></a>
### ModelResource«BaseError»

|Name|Description|Schema|
|---|---|---|
|**_links**  <br>*optional*||< [Link](#link) > array|
|**createdBy**  <br>*optional*|The initial content author|[UserDTO](#userdto)|
|**createdDate**  <br>*optional*|Date created|[LocalDateTime](#localdatetime)|
|**lastModifiedDate**  <br>*optional*|Date last modified|[LocalDateTime](#localdatetime)|
|**message**  <br>*optional*|Message for users|string|
|**remoteAddress**  <br>*optional*|The address the request originated from|string|
|**userAgent**  <br>*optional*|The UA string if provided with a request|[UserAgent](#useragent)|


<a name="0bd4ef83d5db2c471c3469c19a8501cd"></a>
### ModelResource«ClientError»

|Name|Description|Schema|
|---|---|---|
|**_links**  <br>*optional*||< [Link](#link) > array|
|**createdBy**  <br>*optional*|The initial content author|[UserDTO](#userdto)|
|**createdDate**  <br>*optional*|Date created|[LocalDateTime](#localdatetime)|
|**description**  <br>*optional*|The error description provided by the user, if any.|string|
|**lastModifiedDate**  <br>*optional*|Date last modified|[LocalDateTime](#localdatetime)|
|**message**  <br>*optional*|Message for users|string|
|**remoteAddress**  <br>*optional*|The address the request originated from|string|
|**screenshotUrl**  <br>*optional*|A client application screenshot demonstrating the issue.|string|
|**state**  <br>*optional*|A textual representation of the client application state, e.g. the DOM tree.|string|
|**userAgent**  <br>*optional*|The UA string if provided with a request|[UserAgent](#useragent)|
|**viewUrl**  <br>*optional*|The URL relevant to application view state during the error e.g. window.location.href, if any|string|


<a name="9ef43d1ca8eac34613540d1bdf836772"></a>
### ModelResource«ContactDetails»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|
|**primaryAddress**  <br>*optional*|[PostalAddressDetail](#postaladdressdetail)|
|**primaryCellphone**  <br>*optional*|[CellphoneDetail](#cellphonedetail)|
|**primaryEmail**  <br>*optional*|[EmailDetail](#emaildetail)|
|**user**  <br>*optional*|[User](#user)|


<a name="4e940515b73bab5898fbcee055f5441b"></a>
### ModelResource«Continent»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**name**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**parent**  <br>*optional*|[Continent](#continent)|
|**path**  <br>*optional*|string|
|**pathLevel**  <br>*optional*|integer(int32)|
|**pk**  <br>*optional*|string|


<a name="9444a34c25d284655fc420cf1bfdef65"></a>
### ModelResource«Country»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**callingCode**  <br>*optional*|string|
|**capital**  <br>*optional*|string|
|**currency**  <br>*optional*|string|
|**languages**  <br>*optional*|string|
|**name**  <br>*optional*|string|
|**nativeName**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**parent**  <br>*optional*|[Continent](#continent)|
|**path**  <br>*optional*|string|
|**pathLevel**  <br>*optional*|integer(int32)|
|**pk**  <br>*optional*|string|


<a name="a3c232177f9f6d89da7ea16a9309c29c"></a>
### ModelResource«ErrorLog»

|Name|Description|Schema|
|---|---|---|
|**_links**  <br>*optional*||< [Link](#link) > array|
|**errorCount**  <br>*optional*|The number of errors corresponding to this stacktrace.|integer(int32)|
|**firstOccurred**  <br>*required*|First occurrence date|[LocalDateTime](#localdatetime)|
|**lastOccurred**  <br>*required*|Last occurrence date|[LocalDateTime](#localdatetime)|
|**name**  <br>*optional*||string|
|**new**  <br>*optional*||boolean|
|**pk**  <br>*optional*||string|
|**rootCauseMessage**  <br>*optional*|The root cause message.|string|
|**stacktrace**  <br>*required*|The actual stacktrace.|string|


<a name="1f54f6b4564b09cb85c290aa5845aa94"></a>
### ModelResource«Friendship»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**pk**  <br>*required*|[FriendshipIdentifier](#friendshipidentifier)|
|**status**  <br>*required*|enum (NEW, CONFIRMED, BLOCK, DELETE)|


<a name="824ddf492b95eb8b8faeac5de0199644"></a>
### ModelResource«Host»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**aliases**  <br>*optional*|< string > array|
|**country**  <br>*optional*|[Country](#country)|
|**description**  <br>*optional*|string|
|**name**  <br>*optional*|string|
|**pk**  <br>*optional*|string|


<a name="6e3b861112e12ed5486863ddfdd870dc"></a>
### ModelResource«Locality»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**name**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**parent**  <br>*optional*|[Country](#country)|
|**path**  <br>*optional*|string|
|**pathLevel**  <br>*optional*|integer(int32)|
|**pk**  <br>*optional*|string|


<a name="f65fc8a3d3b9dbcc4c520d7b46d4d79a"></a>
### ModelResource«PersonalDetails»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**birthDay**  <br>*optional*|[LocalDate](#localdate)|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|
|**user**  <br>*optional*|[User](#user)|


<a name="56554b423f7cd30ce6032abd32cc0885"></a>
### ModelResource«PostalCode»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**name**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**parent**  <br>*optional*|[Locality](#locality)|
|**path**  <br>*optional*|string|
|**pathLevel**  <br>*optional*|integer(int32)|
|**pk**  <br>*optional*|string|


<a name="95d8c85f589cabfba73a82139092c1f8"></a>
### ModelResource«Role»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**authority**  <br>*optional*|string|
|**description**  <br>*optional*|string|
|**name**  <br>*optional*|string|
|**pk**  <br>*optional*|string|


<a name="777e9019408cb8fd5296f5da82b11549"></a>
### ModelResource«STOMP Session»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|
|**user**  <br>*optional*|[User](#user)|


<a name="30725f4000d6402f4fe50b5e3d9b054e"></a>
### ModelResource«SystemError»

|Name|Description|Schema|
|---|---|---|
|**_links**  <br>*optional*||< [Link](#link) > array|
|**createdBy**  <br>*optional*|The initial content author|[UserDTO](#userdto)|
|**createdDate**  <br>*optional*|Date created|[LocalDateTime](#localdatetime)|
|**errorLogId**  <br>*optional*|The corresponding log/stacktrace ID, if any|string|
|**httpStatusCode**  <br>*optional*|The HTTP response status code|integer(int32)|
|**httpStatusMessage**  <br>*optional*|The phrase corresponding to the HTTP status|string|
|**lastModifiedDate**  <br>*optional*|Date last modified|[LocalDateTime](#localdatetime)|
|**message**  <br>*optional*|Message for users|string|
|**remoteAddress**  <br>*optional*|The address the request originated from|string|
|**requestMethod**  <br>*optional*|The HTTP request method|string|
|**requestUrl**  <br>*optional*|The HTTP request URL, relative to system base URL|string|
|**userAgent**  <br>*optional*|The UA string if provided with a request|[UserAgent](#useragent)|
|**validationErrors**  <br>*optional*|Failed constraint validation errors, if any|< [ConstraintViolationEntry](#constraintviolationentry) > array|


<a name="b87bcc078614ec1c9ce5b3a045b62fe8"></a>
### ModelResource«UserAgent»

|Name|Description|Schema|
|---|---|---|
|**_links**  <br>*optional*||< [Link](#link) > array|
|**new**  <br>*optional*||boolean|
|**pk**  <br>*optional*||string|
|**value**  <br>*optional*|UA signature string pathFragment|string|


<a name="bc828b4c6668756b81161ae51cc73f6c"></a>
### ModelResource«UserCredentials»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**active**  <br>*optional*|boolean|
|**inactivationDate**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**inactivationReason**  <br>*optional*|string|
|**lastLogin**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**lastPassWordChangeDate**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**loginAttempts**  <br>*optional*|integer(int32)|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|
|**resetPasswordToken**  <br>*optional*|string|
|**resetPasswordTokenCreated**  <br>*optional*|[LocalDateTime](#localdatetime)|


<a name="b4e2d3021fa227250a9a5e254fce44c7"></a>
### ModelResource«UserRegistrationCodeBatch»

|Name|Description|Schema|
|---|---|---|
|**_links**  <br>*optional*||< [Link](#link) > array|
|**available**  <br>*optional*  <br>*read-only*|The number of available codes in the batch|integer(int32)|
|**batchSize**  <br>*required*|The number of codes to generate (1 to 20), non-updatable.  <br>**Example** : `10`|integer(int32)|
|**createdDate**  <br>*optional*|Date created|[LocalDateTime](#localdatetime)|
|**description**  <br>*required*|The batch description.  <br>**Example** : `"A batch for CompanyName"`|string|
|**expirationDate**  <br>*optional*|Expiration date.|[LocalDate](#localdate)|
|**lastModifiedDate**  <br>*optional*|Date last modified|[LocalDateTime](#localdatetime)|
|**name**  <br>*required*|Unique short code, non-updatable.  <br>**Example** : `"CompanyName01"`|string|
|**pk**  <br>*optional*||string|


<a name="12710f2f416cc9270df00aec31896a55"></a>
### ModelResource«UserRegistrationCode»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**available**  <br>*optional*|boolean|
|**batch**  <br>*optional*|[UserRegistrationCodeBatch](#userregistrationcodebatch)|
|**pk**  <br>*optional*|string|


<a name="9eaf6d0fbe011d785264d794923d4636"></a>
### ModelResource«User»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**active**  <br>*optional*|boolean|
|**avatarUrl**  <br>*optional*|string|
|**bannerUrl**  <br>*optional*|string|
|**description**  <br>*optional*|string|
|**emailHash**  <br>*optional*|string|
|**firstName**  <br>*optional*|string|
|**fullName**  <br>*optional*|string|
|**lastName**  <br>*optional*|string|
|**lastVisit**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**locale**  <br>*optional*|string|
|**metadata**  <br>*optional*|< string, [UserMetadatum](#usermetadatum) > map|
|**name**  <br>*optional*|string|
|**pk**  <br>*optional*|string|
|**roles**  <br>*optional*|< [GrantedAuthority](#grantedauthority) > array|
|**stringCount**  <br>*optional*|integer(int32)|
|**strings**  <br>*optional*|< string > array|
|**username**  <br>*optional*|string|


<a name="pagemetadata"></a>
### PageMetadata

|Name|Schema|
|---|---|
|**number**  <br>*optional*|integer(int64)|
|**size**  <br>*optional*|integer(int64)|
|**totalElements**  <br>*optional*|integer(int64)|
|**totalPages**  <br>*optional*|integer(int64)|


<a name="ab479de493641c88b8075c84b9cb03fa"></a>
### PagedModelResources«BaseError»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«BaseError»»](#70921457e8c302937155d90300761195)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="7433da03f116cb2139ff79212f01129d"></a>
### PagedModelResources«ClientError»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«ClientError»»](#00364d37f7531cb1bdde06fb47bdb5d0)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="152617fdade611e092292718372a079c"></a>
### PagedModelResources«ContactDetails»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«ContactDetails»»](#2b744efc0c0f2f385a08d81dd1a13486)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="59de2a28287ff7a5e19ef514a4ed55f9"></a>
### PagedModelResources«Continent»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«Continent»»](#a1a1c7dae1f03005696e0ae8fd5eecd2)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="5c841f349c7a8eeb28a7fd267d2030c2"></a>
### PagedModelResources«Country»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«Country»»](#a6931c3d843f8c5b56325c303035a19e)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="ace50273581979bc0b71a8182a1d950c"></a>
### PagedModelResources«ErrorLog»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«ErrorLog»»](#00e63ec4a07cc804db25ec2ff0c96b8d)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="c1414046871706e5ea4ef047065642a4"></a>
### PagedModelResources«Friendship»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«Friendship»»](#4508411b3b8be5641d1fc4aaf38a46c6)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="fbe3e1314babf77c20db68c47052fdd4"></a>
### PagedModelResources«Host»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«Host»»](#88f30c2b9c670c0197c9d9a3d8e5e875)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="d2fa7ad3c926470650ae966793ebdd54"></a>
### PagedModelResources«Locality»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«Locality»»](#57bc0bee0ddb758cb3c32f12d23a373e)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="f1d50e188ddd05e694c5e173039c733f"></a>
### PagedModelResources«PersonalDetails»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«PersonalDetails»»](#5cca3d480d18635094def6d180509b25)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="cce6456cf2811657cea201fba0c45f0c"></a>
### PagedModelResources«PostalCode»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«PostalCode»»](#ea570866899cd292df9e25d038486345)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="ce2d59bddeedb62dd390b8097f8f671c"></a>
### PagedModelResources«Role»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«Role»»](#78b6b7fe52f91ae431fa58fba65817c8)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="78a8faf52abf6e7adea1a156a27308eb"></a>
### PagedModelResources«STOMP Session»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«STOMP Session»»](#6df143473f1b30c6e6a168eb5bb2f9a7)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="cf955e20bb945301bc7db0ad25021355"></a>
### PagedModelResources«SystemError»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«SystemError»»](#adfd3d94f4ba9178746fd11d6759f8b1)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="16438bfbf7d4109bc691b031c5a16d65"></a>
### PagedModelResources«UserAgent»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«UserAgent»»](#6b950887fd22022021c5ce263de0ac3d)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="733fcf7e619c2dceeeff0a720733c01d"></a>
### PagedModelResources«UserCredentials»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«UserCredentials»»](#15e05ad15be3da397044fbccc90bf87e)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="285b96aea9995ceb35f8674d5ced28d0"></a>
### PagedModelResources«UserRegistrationCodeBatch»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«UserRegistrationCodeBatch»»](#96cb0a334fb4ea62c1b31c165d23f0e9)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="c544285309913bb175c0baeb01c97af0"></a>
### PagedModelResources«UserRegistrationCode»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«UserRegistrationCode»»](#cf7ab9d28e0eb49ec72ba0a8f0374565)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="13cf52e6b6a56702e9ac5b35818a70ae"></a>
### PagedModelResources«User»

|Name|Schema|
|---|---|
|**_embedded**  <br>*optional*|[Collection«ModelResource«User»»](#cc5ab41b8db0ab4cb2c0986459e7a9b6)|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**page**  <br>*optional*|[PageMetadata](#pagemetadata)|
|**urlParameters**  <br>*optional*|< string, [Array](#array) > map|


<a name="b23ca1d459fe9c814c752fba6d94fcbc"></a>
### Page«UserDTO»

|Name|Schema|
|---|---|
|**content**  <br>*optional*|< [UserDTO](#userdto) > array|
|**first**  <br>*optional*|boolean|
|**last**  <br>*optional*|boolean|
|**number**  <br>*optional*|integer(int32)|
|**numberOfElements**  <br>*optional*|integer(int32)|
|**size**  <br>*optional*|integer(int32)|
|**sort**  <br>*optional*|[Sort](#sort)|
|**totalElements**  <br>*optional*|integer(int64)|
|**totalPages**  <br>*optional*|integer(int32)|


<a name="personaldetails"></a>
### PersonalDetails
PersonalDetails


|Name|Schema|
|---|---|
|**birthDay**  <br>*optional*|[LocalDate](#localdate)|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|
|**user**  <br>*optional*|[User](#user)|


<a name="personaldetailsservice"></a>
### PersonalDetailsService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="postaladdressdetail"></a>
### PostalAddressDetail
PostalAddressDetail


|Name|Schema|
|---|---|
|**addressLine1**  <br>*optional*|string|
|**addressLine2**  <br>*optional*|string|
|**contactDetails**  <br>*optional*|[ContactDetails](#contactdetails)|
|**description**  <br>*optional*|string|
|**pk**  <br>*optional*|string|
|**postCode**  <br>*optional*|[PostalCode](#postalcode)|
|**postOfficeBoxNumber**  <br>*optional*|string|
|**primary**  <br>*optional*|boolean|
|**value**  <br>*optional*|string|
|**verificationToken**  <br>*optional*|string|
|**verified**  <br>*optional*|boolean|


<a name="postalcode"></a>
### PostalCode
A model representing apostal code


|Name|Schema|
|---|---|
|**name**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**parent**  <br>*optional*|[Locality](#locality)|
|**path**  <br>*optional*|string|
|**pathLevel**  <br>*optional*|integer(int32)|
|**pk**  <br>*optional*|string|


<a name="postalcodeservice"></a>
### PostalCodeService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="rawjson"></a>
### RawJson
*Type* : object


<a name="redirectview"></a>
### RedirectView

|Name|Schema|
|---|---|
|**applicationContext**  <br>*optional*|[ApplicationContext](#applicationcontext)|
|**attributesMap**  <br>*optional*|object|
|**beanName**  <br>*optional*|string|
|**contentType**  <br>*optional*|string|
|**exposePathVariables**  <br>*optional*|boolean|
|**hosts**  <br>*optional*|< string > array|
|**propagateQueryProperties**  <br>*optional*|boolean|
|**redirectView**  <br>*optional*|boolean|
|**requestContextAttribute**  <br>*optional*|string|
|**staticAttributes**  <br>*optional*|object|
|**url**  <br>*optional*|string|


<a name="ab8fee71cc97c8c2bd9bacf874a5f738"></a>
### Resource«BaseError»

|Name|Description|Schema|
|---|---|---|
|**_links**  <br>*optional*||< [Link](#link) > array|
|**createdBy**  <br>*optional*|The initial content author|[UserDTO](#userdto)|
|**createdDate**  <br>*optional*|Date created|[LocalDateTime](#localdatetime)|
|**lastModifiedDate**  <br>*optional*|Date last modified|[LocalDateTime](#localdatetime)|
|**message**  <br>*optional*|Message for users|string|
|**remoteAddress**  <br>*optional*|The address the request originated from|string|
|**userAgent**  <br>*optional*|The UA string if provided with a request|[UserAgent](#useragent)|


<a name="73b8cd46e02faa249f33a8c68c3216b8"></a>
### Resource«ClientError»

|Name|Description|Schema|
|---|---|---|
|**_links**  <br>*optional*||< [Link](#link) > array|
|**createdBy**  <br>*optional*|The initial content author|[UserDTO](#userdto)|
|**createdDate**  <br>*optional*|Date created|[LocalDateTime](#localdatetime)|
|**description**  <br>*optional*|The error description provided by the user, if any.|string|
|**lastModifiedDate**  <br>*optional*|Date last modified|[LocalDateTime](#localdatetime)|
|**message**  <br>*optional*|Message for users|string|
|**remoteAddress**  <br>*optional*|The address the request originated from|string|
|**screenshotUrl**  <br>*optional*|A client application screenshot demonstrating the issue.|string|
|**state**  <br>*optional*|A textual representation of the client application state, e.g. the DOM tree.|string|
|**userAgent**  <br>*optional*|The UA string if provided with a request|[UserAgent](#useragent)|
|**viewUrl**  <br>*optional*|The URL relevant to application view state during the error e.g. window.location.href, if any|string|


<a name="96603a995537a0046766cce56a0b041f"></a>
### Resource«ContactDetails»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|
|**primaryAddress**  <br>*optional*|[PostalAddressDetail](#postaladdressdetail)|
|**primaryCellphone**  <br>*optional*|[CellphoneDetail](#cellphonedetail)|
|**primaryEmail**  <br>*optional*|[EmailDetail](#emaildetail)|
|**user**  <br>*optional*|[User](#user)|


<a name="7274c721e900aea845a066c08f101d27"></a>
### Resource«Continent»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**name**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**parent**  <br>*optional*|[Continent](#continent)|
|**path**  <br>*optional*|string|
|**pathLevel**  <br>*optional*|integer(int32)|
|**pk**  <br>*optional*|string|


<a name="0fd62beb279efbe66a254288f7881f70"></a>
### Resource«Country»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**callingCode**  <br>*optional*|string|
|**capital**  <br>*optional*|string|
|**currency**  <br>*optional*|string|
|**languages**  <br>*optional*|string|
|**name**  <br>*optional*|string|
|**nativeName**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**parent**  <br>*optional*|[Continent](#continent)|
|**path**  <br>*optional*|string|
|**pathLevel**  <br>*optional*|integer(int32)|
|**pk**  <br>*optional*|string|


<a name="95fdd628a6010e03a6f8067fc025ce95"></a>
### Resource«ErrorLog»

|Name|Description|Schema|
|---|---|---|
|**_links**  <br>*optional*||< [Link](#link) > array|
|**errorCount**  <br>*optional*|The number of errors corresponding to this stacktrace.|integer(int32)|
|**firstOccurred**  <br>*required*|First occurrence date|[LocalDateTime](#localdatetime)|
|**lastOccurred**  <br>*required*|Last occurrence date|[LocalDateTime](#localdatetime)|
|**name**  <br>*optional*||string|
|**new**  <br>*optional*||boolean|
|**pk**  <br>*optional*||string|
|**rootCauseMessage**  <br>*optional*|The root cause message.|string|
|**stacktrace**  <br>*required*|The actual stacktrace.|string|


<a name="c71766d00247d5c219b0b6a94c678181"></a>
### Resource«Friendship»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**pk**  <br>*required*|[FriendshipIdentifier](#friendshipidentifier)|
|**status**  <br>*required*|enum (NEW, CONFIRMED, BLOCK, DELETE)|


<a name="4b3f1994c33e4350629300bebe438034"></a>
### Resource«Host»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**aliases**  <br>*optional*|< string > array|
|**country**  <br>*optional*|[Country](#country)|
|**description**  <br>*optional*|string|
|**name**  <br>*optional*|string|
|**pk**  <br>*optional*|string|


<a name="0b6e798b627cbe8539a863efcb1a56ff"></a>
### Resource«Locality»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**name**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**parent**  <br>*optional*|[Country](#country)|
|**path**  <br>*optional*|string|
|**pathLevel**  <br>*optional*|integer(int32)|
|**pk**  <br>*optional*|string|


<a name="d23157c33d48277bb1a1439dcb17bb87"></a>
### Resource«PersonalDetails»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**birthDay**  <br>*optional*|[LocalDate](#localdate)|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|
|**user**  <br>*optional*|[User](#user)|


<a name="8eb673589b8cfa5bf97689fe6c295786"></a>
### Resource«PostalCode»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**name**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**parent**  <br>*optional*|[Locality](#locality)|
|**path**  <br>*optional*|string|
|**pathLevel**  <br>*optional*|integer(int32)|
|**pk**  <br>*optional*|string|


<a name="45f492ff7f2355dd9fe8670f966c13b1"></a>
### Resource«Role»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**authority**  <br>*optional*|string|
|**description**  <br>*optional*|string|
|**name**  <br>*optional*|string|
|**pk**  <br>*optional*|string|


<a name="444feda462dbd0497ce75c2ac7644b6e"></a>
### Resource«STOMP Session»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|
|**user**  <br>*optional*|[User](#user)|


<a name="699b494ee59baa225bb0487a393a205d"></a>
### Resource«SystemError»

|Name|Description|Schema|
|---|---|---|
|**_links**  <br>*optional*||< [Link](#link) > array|
|**createdBy**  <br>*optional*|The initial content author|[UserDTO](#userdto)|
|**createdDate**  <br>*optional*|Date created|[LocalDateTime](#localdatetime)|
|**errorLogId**  <br>*optional*|The corresponding log/stacktrace ID, if any|string|
|**httpStatusCode**  <br>*optional*|The HTTP response status code|integer(int32)|
|**httpStatusMessage**  <br>*optional*|The phrase corresponding to the HTTP status|string|
|**lastModifiedDate**  <br>*optional*|Date last modified|[LocalDateTime](#localdatetime)|
|**message**  <br>*optional*|Message for users|string|
|**remoteAddress**  <br>*optional*|The address the request originated from|string|
|**requestMethod**  <br>*optional*|The HTTP request method|string|
|**requestUrl**  <br>*optional*|The HTTP request URL, relative to system base URL|string|
|**userAgent**  <br>*optional*|The UA string if provided with a request|[UserAgent](#useragent)|
|**validationErrors**  <br>*optional*|Failed constraint validation errors, if any|< [ConstraintViolationEntry](#constraintviolationentry) > array|


<a name="2c20f0eb88911650e8da8b52ec7a1cdd"></a>
### Resource«UserAgent»

|Name|Description|Schema|
|---|---|---|
|**_links**  <br>*optional*||< [Link](#link) > array|
|**new**  <br>*optional*||boolean|
|**pk**  <br>*optional*||string|
|**value**  <br>*optional*|UA signature string pathFragment|string|


<a name="f57cf6713880fc103ac4c8ef0370ba2a"></a>
### Resource«UserCredentials»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**active**  <br>*optional*|boolean|
|**inactivationDate**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**inactivationReason**  <br>*optional*|string|
|**lastLogin**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**lastPassWordChangeDate**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**loginAttempts**  <br>*optional*|integer(int32)|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|
|**resetPasswordToken**  <br>*optional*|string|
|**resetPasswordTokenCreated**  <br>*optional*|[LocalDateTime](#localdatetime)|


<a name="2ac0d0698c68f295c8cb1e92d27cf2d3"></a>
### Resource«UserRegistrationCodeBatch»

|Name|Description|Schema|
|---|---|---|
|**_links**  <br>*optional*||< [Link](#link) > array|
|**available**  <br>*optional*  <br>*read-only*|The number of available codes in the batch|integer(int32)|
|**batchSize**  <br>*required*|The number of codes to generate (1 to 20), non-updatable.  <br>**Example** : `10`|integer(int32)|
|**createdDate**  <br>*optional*|Date created|[LocalDateTime](#localdatetime)|
|**description**  <br>*required*|The batch description.  <br>**Example** : `"A batch for CompanyName"`|string|
|**expirationDate**  <br>*optional*|Expiration date.|[LocalDate](#localdate)|
|**lastModifiedDate**  <br>*optional*|Date last modified|[LocalDateTime](#localdatetime)|
|**name**  <br>*required*|Unique short code, non-updatable.  <br>**Example** : `"CompanyName01"`|string|
|**pk**  <br>*optional*||string|


<a name="c59e2883024a16ce4b29f9822cd43edf"></a>
### Resource«UserRegistrationCode»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**available**  <br>*optional*|boolean|
|**batch**  <br>*optional*|[UserRegistrationCodeBatch](#userregistrationcodebatch)|
|**pk**  <br>*optional*|string|


<a name="7cfe2818d2924b9c0f220f9e272581a5"></a>
### Resource«User»

|Name|Schema|
|---|---|
|**_links**  <br>*optional*|< [Link](#link) > array|
|**active**  <br>*optional*|boolean|
|**avatarUrl**  <br>*optional*|string|
|**bannerUrl**  <br>*optional*|string|
|**description**  <br>*optional*|string|
|**emailHash**  <br>*optional*|string|
|**firstName**  <br>*optional*|string|
|**fullName**  <br>*optional*|string|
|**lastName**  <br>*optional*|string|
|**lastVisit**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**locale**  <br>*optional*|string|
|**metadata**  <br>*optional*|< string, [UserMetadatum](#usermetadatum) > map|
|**name**  <br>*optional*|string|
|**pk**  <br>*optional*|string|
|**roles**  <br>*optional*|< [GrantedAuthority](#grantedauthority) > array|
|**stringCount**  <br>*optional*|integer(int32)|
|**strings**  <br>*optional*|< string > array|
|**username**  <br>*optional*|string|


<a name="f688d2819c775fad0d39fa0b4dd5a5b3"></a>
### ResponseEntity«BaseError»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[BaseError](#baseerror)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="118c438bfd8447ff0a28faaf0ab0a63e"></a>
### ResponseEntity«ClientError»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[ClientError](#clienterror)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="367e02371b461d9a61f89e4fbcc1b98d"></a>
### ResponseEntity«ContactDetails»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[ContactDetails](#contactdetails)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="3b9a5ac152f43b0a2b0da5bdb016b8bd"></a>
### ResponseEntity«Continent»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[Continent](#continent)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="da51ebd4e4de29877f5e0803d9547dca"></a>
### ResponseEntity«Country»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[Country](#country)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="738267715eb2a1eeff2cb3719ec1edd5"></a>
### ResponseEntity«ErrorLog»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[ErrorLog](#errorlog)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="750e2f4000044b50b0d793cf0f21c8fa"></a>
### ResponseEntity«Friendship»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[Friendship](#friendship)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="5b9ea71df74981f682abd627a91fb65d"></a>
### ResponseEntity«Host»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[Host](#host)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="b3fa64852529c339beaf935edb0caab6"></a>
### ResponseEntity«Locality»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[Locality](#locality)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="00e3c94cb4b1ef15ade0489f11348786"></a>
### ResponseEntity«PersonalDetails»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[PersonalDetails](#personaldetails)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="a147ae80f9ce75c1efc703b60be50f30"></a>
### ResponseEntity«PostalCode»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[PostalCode](#postalcode)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="2fa679034f2a67df9073aa8a2a8891a2"></a>
### ResponseEntity«Role»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[Role](#role)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="402c7efa52ed52c3536cabcd23686a34"></a>
### ResponseEntity«STOMP Session»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[STOMP Session](#stomp-session)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="d810224a8ed64e85885f525a3823587f"></a>
### ResponseEntity«SystemError»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[SystemError](#systemerror)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="bcae2dcf6ae7300aa2757d801a50ec47"></a>
### ResponseEntity«UserAgent»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[UserAgent](#useragent)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="0881d617dc56ac4c4272fd57dcefa184"></a>
### ResponseEntity«UserCredentials»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[UserCredentials](#usercredentials)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="7a6504c2b073be7e7942c672d6237d33"></a>
### ResponseEntity«UserRegistrationCodeBatch»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[UserRegistrationCodeBatch](#userregistrationcodebatch)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="2ef7f3f8a8ada0eda5790aca743e70cd"></a>
### ResponseEntity«UserRegistrationCode»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[UserRegistrationCode](#userregistrationcode)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="cd8e556591494ccd572311ba3087e44a"></a>
### ResponseEntity«User»

|Name|Schema|
|---|---|
|**body**  <br>*optional*|[User](#user)|
|**statusCode**  <br>*optional*|enum (100, 101, 102, 103, 200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 302, 303, 304, 305, 307, 308, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 413, 414, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 426, 428, 429, 431, 451, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511)|
|**statusCodeValue**  <br>*optional*|integer(int32)|


<a name="role"></a>
### Role
User principal roles. Roles are principals themselves and can be assigned to users.


|Name|Schema|
|---|---|
|**description**  <br>*optional*|string|
|**name**  <br>*optional*|string|
|**pk**  <br>*optional*|string|


<a name="roleservice"></a>
### RoleService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="stomp-session"></a>
### STOMP Session
A model representing a websocket STOMP session


|Name|Schema|
|---|---|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|
|**user**  <br>*optional*|[User](#user)|


<a name="serializable"></a>
### Serializable
*Type* : object


<a name="simpleloginrequest"></a>
### SimpleLoginRequest

|Name|Schema|
|---|---|
|**password**  <br>*required*|string|
|**username**  <br>*required*|string|


<a name="sort"></a>
### Sort
*Type* : object


<a name="stompsessionservice"></a>
### StompSessionService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="systemerror"></a>
### SystemError
SystemErrors are created exclusively by the system (i.e. without manual intervention) to handle and inform the user about runtime exceptions. They may be persisted automatically according to restdude.validationErrors.system.persist* configuration properties. System validationErrors have a many-to-one relationship with ErrorLog records, as those are shared based on their hash to save space.


|Name|Description|Schema|
|---|---|---|
|**createdBy**  <br>*optional*|The initial content author|[UserDTO](#userdto)|
|**createdDate**  <br>*optional*|Date created|[LocalDateTime](#localdatetime)|
|**errorLog**  <br>*optional*||[ErrorLog](#errorlog)|
|**errorLogId**  <br>*optional*|The corresponding log/stacktrace ID, if any|string|
|**httpStatusCode**  <br>*optional*|The HTTP response status code|integer(int32)|
|**httpStatusMessage**  <br>*optional*|The phrase corresponding to the HTTP status|string|
|**lastModifiedDate**  <br>*optional*|Date last modified|[LocalDateTime](#localdatetime)|
|**message**  <br>*optional*|Message for users|string|
|**pk**  <br>*optional*||string|
|**remoteAddress**  <br>*optional*|The address the request originated from|string|
|**requestMethod**  <br>*optional*|The HTTP request method|string|
|**requestUrl**  <br>*optional*|The HTTP request URL, relative to system base URL|string|
|**userAgent**  <br>*optional*|The UA string if provided with a request|[UserAgent](#useragent)|
|**validationErrors**  <br>*optional*|Failed constraint validation errors, if any|< [ConstraintViolationEntry](#constraintviolationentry) > array|


<a name="systemerrorservice"></a>
### SystemErrorService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="uischema"></a>
### UiSchema

|Name|Schema|
|---|---|
|**json**  <br>*optional*|string|


<a name="user"></a>
### User
Human users


|Name|Schema|
|---|---|
|**active**  <br>*optional*|boolean|
|**avatarUrl**  <br>*optional*|string|
|**bannerUrl**  <br>*optional*|string|
|**description**  <br>*optional*|string|
|**emailHash**  <br>*optional*|string|
|**firstName**  <br>*optional*|string|
|**fullName**  <br>*optional*|string|
|**lastName**  <br>*optional*|string|
|**lastVisit**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**locale**  <br>*optional*|string|
|**metadata**  <br>*optional*|< string, [UserMetadatum](#usermetadatum) > map|
|**name**  <br>*optional*|string|
|**pk**  <br>*optional*|string|
|**roles**  <br>*optional*|< [GrantedAuthority](#grantedauthority) > array|
|**stringCount**  <br>*optional*|integer(int32)|
|**strings**  <br>*optional*|< string > array|
|**username**  <br>*optional*|string|


<a name="useraccountregistration"></a>
### UserAccountRegistration
User registration


|Name|Schema|
|---|---|
|**email**  <br>*optional*|string|
|**firstName**  <br>*optional*|string|
|**lastName**  <br>*optional*|string|
|**locale**  <br>*optional*|string|
|**password**  <br>*optional*|string|
|**passwordConfirmation**  <br>*optional*|string|
|**redirectUrl**  <br>*optional*|string|
|**registrationCode**  <br>*optional*|string|
|**username**  <br>*optional*|string|


<a name="useragent"></a>
### UserAgent
UA signatures


|Name|Description|Schema|
|---|---|---|
|**new**  <br>*optional*||boolean|
|**pk**  <br>*optional*||string|
|**value**  <br>*optional*|UA signature string pathFragment|string|


<a name="useragentservice"></a>
### UserAgentService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="usercredentials"></a>
### UserCredentials
User login information


|Name|Schema|
|---|---|
|**active**  <br>*optional*|boolean|
|**inactivationDate**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**inactivationReason**  <br>*optional*|string|
|**lastLogin**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**lastPassWordChangeDate**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**loginAttempts**  <br>*optional*|integer(int32)|
|**new**  <br>*optional*|boolean|
|**pk**  <br>*optional*|string|
|**resetPasswordToken**  <br>*optional*|string|
|**resetPasswordTokenCreated**  <br>*optional*|[LocalDateTime](#localdatetime)|


<a name="usercredentialsservice"></a>
### UserCredentialsService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="userdto"></a>
### UserDTO
UserDTO is a lightweight DTO version of User


|Name|Schema|
|---|---|
|**avatarUrl**  <br>*optional*|string|
|**bannerUrl**  <br>*optional*|string|
|**emailHash**  <br>*optional*|string|
|**firstName**  <br>*optional*|string|
|**lastName**  <br>*optional*|string|
|**name**  <br>*optional*|string|
|**pk**  <br>*optional*|string|
|**stompSessionCount**  <br>*optional*|integer(int32)|
|**username**  <br>*optional*|string|


<a name="userdetails"></a>
### UserDetails

|Name|Schema|
|---|---|
|**accountNonExpired**  <br>*optional*|boolean|
|**accountNonLocked**  <br>*optional*|boolean|
|**active**  <br>*optional*|boolean|
|**admin**  <br>*optional*|boolean|
|**authorities**  <br>*optional*|[Collection«GrantedAuthority»](#6726e8fb45e4d9ae05913655d3f4b3a3)|
|**avatarUrl**  <br>*optional*|string|
|**birthDay**  <br>*optional*|[LocalDate](#localdate)|
|**cellphone**  <br>*optional*|string|
|**credentialsNonExpired**  <br>*optional*|boolean|
|**dateFormat**  <br>*optional*|string|
|**emailHash**  <br>*optional*|string|
|**enabled**  <br>*optional*|boolean|
|**firstName**  <br>*optional*|string|
|**inactivationDate**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**inactivationReason**  <br>*optional*|string|
|**lastName**  <br>*optional*|string|
|**lastPassWordChangeDate**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**lastPost**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**lastVisit**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**locale**  <br>*optional*|string|
|**loginAttempts**  <br>*optional*|integer(int32)|
|**metadata**  <br>*optional*|< string, string > map|
|**name**  <br>*optional*|string|
|**new**  <br>*optional*|boolean|
|**password**  <br>*optional*|string|
|**pk**  <br>*optional*|string|
|**redirectUrl**  <br>*optional*|string|
|**siteAdmin**  <br>*optional*|boolean|
|**telephone**  <br>*optional*|string|
|**userId**  <br>*optional*|string|
|**username**  <br>*optional*|string|


<a name="userinvitationresultsdto"></a>
### UserInvitationResultsDTO
Data transfer object for user invitation resultss


|Name|Description|Schema|
|---|---|---|
|**duplicate**  <br>*optional*|A list of ignored duplicate addressess.|< string > array|
|**existing**  <br>*optional*|A list of ignored, pre-existing addressess.|< string > array|
|**invalid**  <br>*optional*|A list of invalid addressess.|< string > array|
|**invited**  <br>*optional*|A list of invited addressess.|< string > array|


<a name="userinvitationsdto"></a>
### UserInvitationsDTO
Data transfer object for user invitations


|Name|Description|Schema|
|---|---|---|
|**addressLines**  <br>*optional*|A line and/or comma delimeted string of RFC 2822 email addresses.|string|
|**recepients**  <br>*optional*|A list of UserDTO instances.|< [UserDTO](#userdto) > array|


<a name="usermetadatum"></a>
### UserMetadatum

|Name|Schema|
|---|---|
|**object**  <br>*optional*|string|
|**pk**  <br>*optional*|string|
|**predicate**  <br>*optional*|string|


<a name="usermodel"></a>
### UserModel

|Name|Schema|
|---|---|
|**active**  <br>*optional*|boolean|
|**avatarUrl**  <br>*optional*|string|
|**bannerUrl**  <br>*optional*|string|
|**description**  <br>*optional*|string|
|**emailHash**  <br>*optional*|string|
|**firstName**  <br>*optional*|string|
|**fullName**  <br>*optional*|string|
|**lastName**  <br>*optional*|string|
|**lastVisit**  <br>*optional*|[LocalDateTime](#localdatetime)|
|**locale**  <br>*optional*|string|
|**localeObject**  <br>*optional*|[Locale](#locale)|
|**name**  <br>*optional*|string|
|**roles**  <br>*optional*|< [GrantedAuthority](#grantedauthority) > array|
|**username**  <br>*optional*|string|


<a name="userregistrationcode"></a>
### UserRegistrationCode
UserRegistrationCode


|Name|Schema|
|---|---|
|**available**  <br>*optional*|boolean|
|**batch**  <br>*optional*|[UserRegistrationCodeBatch](#userregistrationcodebatch)|
|**pk**  <br>*optional*|string|


<a name="userregistrationcodebatch"></a>
### UserRegistrationCodeBatch
UserRegistrationCodeBatch


|Name|Description|Schema|
|---|---|---|
|**available**  <br>*optional*  <br>*read-only*|The number of available codes in the batch|integer(int32)|
|**batchSize**  <br>*required*|The number of codes to generate (1 to 20), non-updatable.  <br>**Example** : `10`|integer(int32)|
|**createdDate**  <br>*optional*|Date created|[LocalDateTime](#localdatetime)|
|**description**  <br>*required*|The batch description.  <br>**Example** : `"A batch for CompanyName"`|string|
|**expirationDate**  <br>*optional*|Expiration date.|[LocalDate](#localdate)|
|**lastModifiedDate**  <br>*optional*|Date last modified|[LocalDateTime](#localdatetime)|
|**name**  <br>*required*|Unique short code, non-updatable.  <br>**Example** : `"CompanyName01"`|string|
|**pk**  <br>*optional*||string|


<a name="userregistrationcodebatchservice"></a>
### UserRegistrationCodeBatchService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="userregistrationcodeinfo"></a>
### UserRegistrationCodeInfo

|Name|Schema|
|---|---|
|**available**  <br>*optional*|boolean|
|**batchId**  <br>*optional*|string|
|**batchName**  <br>*optional*|string|
|**pk**  <br>*optional*|string|
|**username**  <br>*optional*|string|


<a name="userregistrationcodeservice"></a>
### UserRegistrationCodeService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="userservice"></a>
### UserService

|Name|Schema|
|---|---|
|**conversionService**  <br>*optional*|[ConversionService](#conversionservice)|
|**filePersistenceService**  <br>*optional*|[FilePersistenceService](#filepersistenceservice)|
|**principal**  <br>*optional*|[UserDetails](#userdetails)|
|**principalLocalUser**  <br>*optional*|[UserModel](#usermodel)|


<a name="usernamechangerequest"></a>
### UsernameChangeRequest
A request to change one's username,


|Name|Schema|
|---|---|
|**password**  <br>*optional*|string|
|**username**  <br>*optional*|string|





