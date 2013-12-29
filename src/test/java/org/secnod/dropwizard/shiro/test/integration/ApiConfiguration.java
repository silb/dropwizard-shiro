package org.secnod.dropwizard.shiro.test.integration;
import org.secnod.dropwizard.shiro.ShiroConfiguration;

import com.yammer.dropwizard.config.Configuration;

/**
 * An example Dropwizard configuration.
 */
public class ApiConfiguration extends Configuration {

    public ShiroConfiguration shiro;
}