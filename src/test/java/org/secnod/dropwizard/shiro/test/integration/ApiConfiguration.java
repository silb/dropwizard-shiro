package org.secnod.dropwizard.shiro.test.integration;
import org.secnod.dropwizard.shiro.ShiroConfiguration;

import io.dropwizard.Configuration;

/**
 * An example Dropwizard configuration.
 */
public class ApiConfiguration extends Configuration {

    public ShiroConfiguration shiro;
}