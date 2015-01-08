package org.secnod.dropwizard.shiro.test.integration;

import io.dropwizard.testing.junit.DropwizardAppRule;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.secnod.shiro.test.integration.AnnotationAuthTest;

@RunWith(Suite.class)
@SuiteClasses({AnnotationAuthTest.class})
public class IntegrationTestSuite {

    @ClassRule
    public static DropwizardAppRule<ApiConfiguration> app = new DropwizardAppRule<>(
            ApiApplication.class,
            "src/test/resources/api.yml");
}
