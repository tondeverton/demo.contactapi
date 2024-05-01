package com.tondeverton.demo.contactapi.providers;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EnvironmentVariableProvider implements VariableProvider {

    private final Environment environment;

    public EnvironmentVariableProvider(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Integer getValueAsInt(Variables variable) {
        var property = environment.getProperty(variable.getName(), Integer.class);
        return Optional.ofNullable(property).orElse(variable.getFallbackValue());
    }
}
