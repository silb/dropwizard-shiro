package org.secnod.dropwizard.shiro.test.integration;

import org.secnod.example.webapp.UserInjectableProvider;
import org.secnod.shiro.jaxrs.ShiroExceptionMapper;
import org.secnod.shiro.test.integration.webapp.IntegrationTestApplication;
import org.eclipse.jetty.server.session.SessionHandler;
import org.secnod.dropwizard.shiro.ShiroBundle;
import org.secnod.dropwizard.shiro.ShiroConfiguration;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * An example Dropwizard application.
 */
public class ApiApplication extends Application<ApiConfiguration> {

    private final ShiroBundle<ApiConfiguration> shiro = new ShiroBundle<ApiConfiguration>() {

        @Override
        protected ShiroConfiguration narrow(ApiConfiguration configuration) {
            return configuration.shiro;
        }
    };

    ApiApplication() {}

    @Override
    public void initialize(Bootstrap<ApiConfiguration> bootstrap) {
        bootstrap.addBundle(shiro);
    }

    @Override
    public void run(ApiConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().setUrlPattern("/api/*");
        environment.jersey().register(new UserInjectableProvider());
        environment.jersey().register(new ShiroExceptionMapper());

        environment.getApplicationContext().setSessionHandler(new SessionHandler());

        for (Object resource : IntegrationTestApplication.createAllIntegrationTestResources()) {
            environment.jersey().register(resource);
        }
    }

    @Override
    public String getName() {
        return "api";
    }

    public static void main(String[] args) throws Exception {
        new ApiApplication().run(args.length > 0 ? args : new String[] { "server", "src/test/resources/api.yml"});
    }
}