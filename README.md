# Synopsis

This is a simple client to build and execute REST calls. It bases on JAX-RS and JSON.

Its main goals:

- provide an easy way to execute REST calls
- hide the most of HTTP logic
- use an object-oriented approach

  - accept business objects as input for requests
  - return business objects as response

# Usage

## `RestClient` Class

`RestClient` represents the client it self. To get a new client Usage

## Create a new Client

```java
String restUri = "https://myresturi.tld/api";
RestClient restClient = new RestClient(restUri);
```

This will create a very basic `RestClient` object which can be customized later on.

## Default Headers

Many REST APIs expect a certain set of headers set to all calls they receive. Such header elements can be added to the `RestClient` object and will be automatically applied to every new request created.

```java
restClient.header("Authorization", "Basic abcxyz").header("another-header", "42");
```

## Cookies

Some REST APIs use cookies to authenicate clients or to persist data locally. At the moment this `RestClient` supports cookies to be used in requests. These need to be set by the `RestClient`, too.

```java
restClient.cookie("name-of-my-cookie", "a-magic-value", "valid/path", ".myresturi.tld/api");
```

## Processing REST Calls

To invoke a REST call the `RestClient` expects a `Request` object to be provided which contains the actual business logic of the request. The `RestClient` provides helper methods that create such `Request` objects based on the settings made to the `RestClient` before.

### Requests

As already mentioned above, there are three types of `Request` at the moment:

1. `Request` ... which is also the super class of the following
2. `RequestWithBody`
3. `RequestWithUrlEncodedData`

The difference between these types is if an entity is added to the request and which representation is used to send the entity to the API.

`Request` does not have an entity at all. Typically this is used for GET requests.

`RequestWithBody` contains an entity that is represented as a JSON body.

`RequestWithUrlEncodedData` contains an entity that is represented in several
url-encoded parameters. For a better understanding, Curl sets such parameters using the `-d` flag: ``curl [...] -d key=value [...]``

The latter two `Request` objects can be created out of a serializeble business
object.

#### Creating Requests

The preferred way to create `Request` objects is to use one of the following helper methods.

```java
Request request1 =
  restClient.newRequest("request/endpoint");

RequestWithBody request2 =
  restClient.newRequestWithBody("request/endpoint", serializableBody);

RequestWithUrlEncodedData request3 =
  restClient.newRequestWithUrlEncodedData("request/endpoint", serializableBody);
```

#### Configuring Requests
Apart from those properties `Request` objects receive from `RestClient`'s defaults when created by one of the `newRequest...()` methods further attributes can be set up.

To add URI parameters to a request the following can be used.

```java
RestClient restClient new RestClient("https://myresturi.tld/api");
Request request = restClient.newRequest("my/endpoint");
request.restParameter("paramKey", "paramValue").restParameter("anotherKey", "withAValue");
```
The result: `https://myresturi.tld/api/my/endpoint?paramKey=paramValue&anotherKey=anotherValue`

Furthermore `Request.path(...)` can be used to new elements to the path. Let's extend the example from above:

```java
request.path("returns").path("useless/data");
```

This would extend the URI from above to:
`https://myresturi.tld/api/my/endpoint/returns/useless/data?paramKey=paramValue&anotherKey=anotherValue`

#### `RequestWithBody` & `RequestWithUrlEncodedData`
These `Request` types expect a serializable body on creation. This body is mapped to a certain entity representation in the aftermath. To influence the marshalling a custom mapper can be setup for these requests.

```java
import com.fasterxml.jackson.databind.ObjectMapper;
/*...*/
RequestWithBody request = restClient.newRequestWithBody(endpoint, body);

ObjectMapper requestMapper = new ObjectMapper();
// some example setups
requestMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
requestMapper.setSerializationInclusion(Include.NON_NULL);

request.setBodyMapper(requestMapper);
```

### Responses
`Response` objects are used to encapsulate HTTP responses received in return to
a REST request and make them easiely accessible. There are two different types:
1. `Response` ... which is also the super class of the second
1. `ResponseWithBody<T>` ... where `T` is the type the body entity shall be unmarshalled to.

Both types have attributes to store HTTP status, status code and status phrase. Values are copied from corresponding HTTP response directly.

Apart from that `ResponseWithBody<T>` contains a body entity which can be returned deserialized to the type of `T`.

```java
ResponseWithBody<Person> response;
// let's assume response is being filled with life:
createRealResponse(response);

// then this would work:
List<Person> listOfPersons = response.getResults();
Person person = response.getSingleResult();
```

To manipulate the object mapper setup which is used to unmarshall body entities to business objects a custom mapper can be set. This works similar to request mappers.

```java
import com.fasterxml.jackson.databind.ObjectMapper;
/*...*/
ResponseWithBody<Person> response;
createRealResponse(response);

ObjectMapper responseMapper = new ObjectMapper();
// some example setups
responseMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);

response.setBodyMapper(responseMapper);
```

### Invoking REST Calls

Currently `RestClient` supports `GET`, `POST` and `PUT` requests. This is how they work in general:

```java
Response response1 = restClient.get(...);
Response response2 = restClient.post(...);
Response response3 = restClient.put(...);
```
Depending on what the called API expects as request and what you expect to be returned these calls can be used with different parameters.

#### Get Data
A typical `GET` call is works without request entity but expectes an entity in return. E.g.:

```java
Request request = restClient.newRequest(endpoint);
// set a parameter
request.restParameter("name", "bob");

ResponseWithBody<Person> response = restClient.get(request, Person.class);
Person person = response.getSingleResult();

log.info(person.getName());
// result: bob

```

#### Post Data
When `POST`ing data the situation is usually the other way around. The request does not contain an entity but the response is expected to have one.

```java
Person person = new Person();
person.setName("Bob");
person.setAge(42);

RequestWithBody request = restClient.newRequestWithBody(endpoint, person);

Response response = restClient.post(request);

if(response.getStatus == 201) {
  log.info("201 - Created")
} else {
  log.error("POST request returned an error");
}
```

Anyway, also `POST` Requests can invoke entities to be returned. In this case it is also possible to use `post()` similar to the `GET` example.

```java
ResponseWithBody<Person> response = restClient.post(request, Person.class);

Person thePersonCreated = response.getSingleResult();
```
