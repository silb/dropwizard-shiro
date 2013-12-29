A bundle for securing DropWizard with Apache Shiro.

# Adding the dropwizard-shiro dependency

1. Clone this repository.
2. Run `mvn clean install`
3. Add the following dependencies to an existing DropWizard project:

```xml
<dependency>
  <groupId>org.secnod.dropwizard</groupId>
  <artifactId>dropwizard-shiro</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

# Adding and configuring the Shiro bundle

Add the bundle to the DropWizard environment:

```java
public class ApiService extends Service<ApiConfiguration> {

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

Edit the DropWizard YAML config file:

```yaml
shiro:
  iniConfigs: ["classpath:shiro.ini"]
```

Now create and [configure](http://github.com/silb/shiro-jersey#configure-shiro) `shiro.ini` in the default package
on the classpath.

## Optionally enable the exception mapper

Shiro exceptions can be mapped to HTTP responses by adding the exception mapper for Shiro exceptions.

```java
public class ApiService extends Service<ApiConfiguration> {

    @Override
    public void run(ApiConfiguration configuration, Environment environment) throws Exception {
        environment.addProvider(new ShiroExceptionMapper());
    }
}
```

It is not added by default as most applications will need to customize how Shiro exception are mapped to HTTP responses.

## Optionally using a custom user class

The `InjectableProvider` for a [custom user class](http://github.com/silb/shiro-jersey#custom-user) must be added to
DropWizard:

```java
public class ApiService extends Service<ApiConfiguration> {

    @Override
    public void run(ApiConfiguration configuration, Environment environment) throws Exception {
        environment.addProvider(new UserInjectableProvider());
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

Override `ShiroBundle.buildShiroFilter(T)`.

