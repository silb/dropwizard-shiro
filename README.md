A bundle for securing [Dropwizard](http://dropwizard.codahale.com) with [Apache Shiro](http://shiro.apache.org).

# Adding the dropwizard-shiro dependency

Add the following dependencies to `pom.xml` in an existing project already using Dropwizard:

```xml
<dependency>
  <groupId>org.secnod.dropwizard</groupId>
  <artifactId>dropwizard-shiro</artifactId>
  <version>0.2.0</version>
</dependency>
```

Version compatibility:

|Dropwizard|Dropwizard Shiro|
|----------|----------------|
|2.0.x     |0.3.0           |
|1.0.x     |0.2.0           |
|0.9.x     |0.2.0           |
|0.8.x     |0.2.0           |
|0.7.x     |0.1.1           |

# Adding and configuring the Shiro bundle

Add the bundle to the Dropwizard environment:

```java
public class ApiApplication extends Application<ApiConfiguration> {

    private final ShiroBundle<ApiConfiguration> shiro = new ShiroBundle<ApiConfiguration>() {

        @Override
        protected ShiroConfiguration narrow(ApiConfiguration configuration) {
            return configuration.shiro;
        }
    };

    @Override
    public void initialize(Bootstrap<ApiConfiguration> bootstrap) {
        bootstrap.addBundle(shiro);
    }
}
```

Edit the Dropwizard YAML config file:

```yaml
shiro:
  iniConfigs: ["classpath:shiro.ini"]
```

Now create and [configure](http://github.com/silb/shiro-jersey#configure-shiro) `shiro.ini` in the default package
on the classpath.

## Optionally enable the exception mapper

Shiro exceptions can be mapped to HTTP responses by adding the exception mapper for Shiro exceptions.

```java
public class ApiApplication extends Application<ApiConfiguration> {

    @Override
    public void run(ApiConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new ShiroExceptionMapper());
    }
}
```

It is not added by default as most applications will need to customize how Shiro exceptions are mapped to HTTP responses.

## Optionally using a custom user class

The `TypeFactory` for a [custom user class](http://github.com/silb/shiro-jersey#custom-user) must be added to Dropwizard:

```java
public class ApiApplication extends Application<ApiConfiguration> {

    @Override
    public void run(ApiConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new UserFactory());
    }
}
```

# Using Shiro for securing JAX-RS resources

See <http://github.com/silb/shiro-jersey#using-shiro>.

# Optional configuration options and their defaults

```yaml
shiro:
  filterUrlPattern = "/*" # The URL pattern for the Shiro servlet filter
```

# FAQ

### How do I take complete control over the Shiro initialization?

Override `ShiroBundle.createFilter(T)`.

### How do I add a realm that requires configuration parameters?

Override `ShiroBundle.createRealms(T)`.

### One of my realms has a dependency to an object that is constructed in Application.run(T, Environment). How do I pass it to the realm?

Store the object in a field in the application class:

```java
public class ApiApplication extends Application<ApiConfiguration> {

    MyObject myObject;

    private final ShiroBundle<ApiConfiguration> shiro = new ShiroBundle<ApiConfiguration>() {

        @Override
        protected Collection<Realm> createRealms(ApiConfiguration configuration) {
            Realm r = new SomeRealm(myObject);
            return Collections.singleton(r);
        }
    };

    @Override
    public void run(ApiConfiguration configuration, Environment environment) throws Exception {
        myObject = new MyObject(configuration);
    }
}
```

### How do I enable sessions?

Add a `SessionHandler` to Jetty:

```java
environment.getApplicationContext().setSessionHandler(new SessionHandler());
```

See the supplied [example application](src/test/java/org/secnod/dropwizard/shiro/test/integration/ApiApplication.java).

# Development

## Running the integration tests

    mvn -Pintegration-tests test

### Running the integration tests against other Dropwizard versions

First, compile against the default Dropwizard version in the POM:

    mvn clean compile

Then run the tests by overriding the dependencies on the command line:

    mvn -Pintegration-tests -Ddropwizard.version=0.8.0 -Djersey.version=2.16 surefire:test
    mvn -Pintegration-tests -Ddropwizard.version=0.8.1 -Djersey.version=2.17 surefire:test
    mvn -Pintegration-tests -Ddropwizard.version=0.8.2 -Djersey.version=2.19 surefire:test
    mvn -Pintegration-tests -Ddropwizard.version=0.8.3 -Djersey.version=2.19 surefire:test
    mvn -Pintegration-tests -Ddropwizard.version=0.8.4 -Djersey.version=2.21 surefire:test
    mvn -Pintegration-tests -Ddropwizard.version=0.9.0 -Djersey.version=2.22.1 surefire:test
    mvn -Pintegration-tests -Ddropwizard.version=0.9.1 -Djersey.version=2.22.1 surefire:test
    mvn -Pintegration-tests -Ddropwizard.version=0.9.2 -Djersey.version=2.22.1 surefire:test
    mvn -Pintegration-tests -Ddropwizard.version=1.0.0 -Djersey.version=2.23.1 surefire:test
    mvn -Pintegration-tests -Ddropwizard.version=2.0.0 -Djersey.version=2.29.1 surefire:test
    mvn -Pintegration-tests -Ddropwizard.version=2.0.20 -Djersey.version=2.33 surefire:test

Note that running the integration tests against DropWizard 1.0 requires Java 8.
