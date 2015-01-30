package org.secnod.dropwizard.shiro.test.integration;

import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit.DropwizardAppRule;

import java.io.IOException;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.secnod.shiro.test.integration.AnnotationAuthTest;
import org.secnod.shiro.test.integration.webapp.JettyServer;

@RunWith(Suite.class)
@SuiteClasses({AnnotationAuthTest.class})
public class IntegrationTestSuite {

    private static int findPort() {
        try {
            int port = JettyServer.allocatePort();
            System.setProperty("org.secnod.shiro.test.port", Integer.toString(port));
            return port;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ClassRule
    public static DropwizardAppRule<ApiConfiguration> app = new DropwizardAppRule<ApiConfiguration>(
            ApiApplication.class,
            "src/test/resources/api.yml",
            ConfigOverride.config("server.applicationConnectors[0].port", Integer.toString(findPort())));
}
