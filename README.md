# webclient-starter

This starter provides simple app to configure `WebClient` and provides different methods
to call services synchronously and asynchronously.

It can be deployed as a standalone app

## Application

> **NOTE**: The project is configured for Java 8 in `build.gradle`.

You can build the project using the provided gradle wrapper.

```bash
./gradlew clean build -x test
```


### Run the app locally

To run the app locally using the embedded Tomcat server you can run this command:

```bash
./gradlew  bootRun
```

You can access the function using `curl`:

```bash
curl http://localhost:9090/customer/1
```

You should see below output in the console of the app:

```text
{"customerId":"1","firstName":"alex","lastName":"smith","phoneNumber":null}
```

you can access sse endpoint using `curl`:
```bash
curl http://localhost:9090/stream-flux
```

You should see bwlo output in the console of the app:

```text
data:Flux - 17:12:41.350
data:Flux - 17:12:42.353
data:Flux - 17:12:43.342
data:Flux - 17:12:44.347
data:Flux - 17:12:45.350
data:Flux - 17:12:46.354
data:Flux - 17:12:47.342
data:Flux - 17:12:48.347
```
