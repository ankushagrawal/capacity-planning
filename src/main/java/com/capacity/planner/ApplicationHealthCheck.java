package com.capacity.planner;

import com.codahale.metrics.health.HealthCheck;

/**
 * Created by ankush.a on 22/03/17.
 */
public class ApplicationHealthCheck extends HealthCheck {

    private final String template;

    public ApplicationHealthCheck(String template) {
        this.template = template;
    }

    @Override
    protected Result check() throws Exception {
        final String saying = String.format(template, "TEST");
        if (!saying.contains("TEST")) {
            return Result.unhealthy("template doesn't include a name");
        }
        return Result.healthy();
    }
}
