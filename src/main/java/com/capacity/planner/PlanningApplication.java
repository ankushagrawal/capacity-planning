package com.capacity.planner;


import com.capacity.planner.entity.*;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by ankush.a on 22/03/17.
 */
public class PlanningApplication extends Application<PlanningConfiguration> {

    @Override
    public String getName() {
        return "Sample application";
    }

    public static void main(String[] args) throws Exception {
        new PlanningApplication().run(args);
    }

    private final HibernateBundle<PlanningConfiguration> hibernate =
            new HibernateBundle<PlanningConfiguration>(Demand.class, PlanOutcome.class, Skill.class, SupplyChannel.class, SupplyData.class,
                    Usecase.class,UsecaseSkills.class,UsecaseWiseSupplyChannel.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(PlanningConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<PlanningConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(PlanningConfiguration configuration, Environment environment) throws Exception {
//        final SampleController controller = new SampleController(configuration.getTemplate(),configuration.getDefaultName());
//
//        final ApplicationHealthCheck healthCheck = new ApplicationHealthCheck(configuration.getTemplate());
//        environment.healthChecks().register("template", healthCheck);
//
//        environment.admin().addTask(new StopServerTask(1));
//
//        final PersonDao personDao = new PersonDao(hibernate.getSessionFactory());
//        final ParentDao parentDao = new ParentDao(hibernate.getSessionFactory());
//
//        environment.jersey().register(controller);
//        environment.jersey().register(new PersonController(personDao));
//        environment.jersey().register(new ParentController(parentDao));
    }


}
