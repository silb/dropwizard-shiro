package org.secnod.dropwizard.shiro;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShiroConfiguration {

    @NotNull
    @JsonProperty("iniConfigs")
    private String[] iniConfigs;

    @JsonProperty("filterUrlPattern")
    private String filterUrlPattern = "/*";

    public String[] iniConfigs() {
        return iniConfigs;
    }

    public String filterUrlPattern() {
        return filterUrlPattern;
    }
}
