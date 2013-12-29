package org.secnod.dropwizard.shiro;

import java.util.List;

import javax.servlet.Filter;

import org.secnod.shiro.jersey.ShiroResourceFilterFactory;
import org.secnod.shiro.jersey.SubjectInjectableProvider;

import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.servlet.AbstractShiroFilter;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.ResourceFilterFactory;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

/**
 * A DropWizard bundle for Apache Shiro.
 */
public abstract class ShiroBundle<T> implements ConfiguredBundle<T> {

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(T configuration, Environment environment) {
        ShiroConfiguration shiroConfig = narrow(configuration);
        ResourceConfig resourceConfig = environment.getJerseyResourceConfig();

        @SuppressWarnings("unchecked")
        List<ResourceFilterFactory> resourceFilterFactories = resourceConfig.getResourceFilterFactories();
        resourceFilterFactories.add(new ShiroResourceFilterFactory());

        environment.addProvider(new SubjectInjectableProvider());

        Filter shiroFilter = buildShiroFilter(configuration);
        environment.addFilter(shiroFilter, shiroConfig.getFilterUrlPattern()).setName("ShiroFilter");
    }

    /**
     * Narrow down the complete configuration to just the Shiro configuration.
     */
    protected abstract ShiroConfiguration narrow(T configuration);

    /**
     * Build the Shiro filter. Overriding this method allows for complete customization of how Shiro is initialized.
     */
    protected Filter buildShiroFilter(T configuration) {
        ShiroConfiguration shiroConfig = narrow(configuration);
        final IniWebEnvironment shiroEnv = new IniWebEnvironment();
        shiroEnv.setConfigLocations(shiroConfig.getIniConfigs());
        shiroEnv.init();

        AbstractShiroFilter shiroFilter = new AbstractShiroFilter() {
            @Override
            public void init() throws Exception {
                setSecurityManager(shiroEnv.getWebSecurityManager());
                setFilterChainResolver(shiroEnv.getFilterChainResolver());
            }
        };
        return shiroFilter;
    }
}
