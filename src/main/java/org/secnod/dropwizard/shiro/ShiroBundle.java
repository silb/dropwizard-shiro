package org.secnod.dropwizard.shiro;

import io.dropwizard.core.ConfiguredBundle;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.secnod.shiro.jersey.SubjectFactory;

/**
 * A Dropwizard bundle for Apache Shiro.
 */
public abstract class ShiroBundle<T> implements ConfiguredBundle<T> {

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(T configuration, Environment environment) {
        ShiroConfiguration shiroConfig = narrow(configuration);
        ResourceConfig resourceConfig = environment.jersey().getResourceConfig();

        resourceConfig.register(org.apache.shiro.web.jaxrs.ShiroAnnotationFilterFeature.class);
        resourceConfig.register(org.apache.shiro.web.jaxrs.SubjectPrincipalRequestFilter.class);
        resourceConfig.register(new SubjectFactory());

        Filter shiroFilter = createFilter(configuration);
        environment.servlets()
            .addFilter("ShiroFilter", shiroFilter)
            .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, shiroConfig.filterUrlPattern());
    }

    /**
     * Narrow down the complete configuration to just the Shiro configuration.
     */
    protected abstract ShiroConfiguration narrow(T configuration);


    /**
     * Create the Shiro filter. Overriding this method allows for complete customization of how Shiro is initialized.
     */
    protected Filter createFilter(final T configuration) {
        ShiroConfiguration shiroConfig = narrow(configuration);
        final IniWebEnvironment shiroEnv = new IniWebEnvironment();
        shiroEnv.setConfigLocations(shiroConfig.iniConfigs());
        shiroEnv.init();

        AbstractShiroFilter shiroFilter = new AbstractShiroFilter() {
            @Override
            public void init() throws Exception {
                Collection<Realm> realms = createRealms(configuration);
                WebSecurityManager securityManager = realms.isEmpty()
                        ? shiroEnv.getWebSecurityManager()
                        : new DefaultWebSecurityManager(realms);
                setSecurityManager(securityManager);
                setFilterChainResolver(shiroEnv.getFilterChainResolver());
            }
        };
        return shiroFilter;
    }

    /**
     * Create and configure the Shiro realms. Override this method in order to
     * add realms that require configuration.
     *
     * @return a non-null list of realms. If empty, no realms will be added, but depending on the content of the INI
     *         file Shiro might still add its automatic IniRealm.
     */
    protected Collection<Realm> createRealms(T configuration) {
        return Collections.emptyList();
    }
}
