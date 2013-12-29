package org.secnod.dropwizard.shiro.test.integration;

import org.secnod.example.webapp.UserInjectableProvider;
import org.secnod.shiro.jaxrs.ShiroExceptionMapper;
import org.secnod.shiro.test.integration.webapp.IntegrationTestApplication;

import org.eclipse.jetty.server.session.SessionHandler;
import org.secnod.dropwizard.shiro.ShiroBundle;
import org.secnod.dropwizard.shiro.ShiroConfiguration;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

/**
 * An example DropWizard service.
 */
public class ApiService extends Service<ApiConfiguration> {

    private final ShiroBundle<ApiConfiguration> shiro = new ShiroBundle<ApiConfiguration>() {

        @Override
        protected ShiroConfiguration narrow(ApiConfiguration configuration) {
            return configuration.shiro;
        }
    };

    ApiService() {}

    @Override
    public void initialize(Bootstrap<ApiConfiguration> bootstrap) {
        bootstrap.setName("api");
        bootstrap.addBundle(shiro);
    }

    @Override
    public void run(ApiConfiguration configuration, Environment environment) throws Exception {

        environment.addProvider(new UserInjectableProvider());
        environment.addProvider(new ShiroExceptionMapper());

        environment.setSessionHandler(new SessionHandler());

        for (Object resource : IntegrationTestApplication.createAllIntegrationTestResources()) {
            environment.addResource(resource);
        }
    }

    public static void main(String[] args) throws Exception {
        new ApiService().run(args.length > 0 ? args : new String[] { "server", "src/test/resources/api.yml"});
    }
}