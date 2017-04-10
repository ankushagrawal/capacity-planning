package com.capacity.planner;


import com.capacity.planner.controller.PlanController;
import com.capacity.planner.dao.*;
import com.capacity.planner.dto.PlanDto;
import com.capacity.planner.entity.demand.Demand;
import com.capacity.planner.entity.metadata.Skill;
import com.capacity.planner.entity.metadata.Usecase;
import com.capacity.planner.entity.metadata.UsecaseSkills;
import com.capacity.planner.entity.outcome.PlanOutcome;
import com.capacity.planner.entity.supply.Supply;
import com.capacity.planner.entity.supply.SupplyChannel;
import com.capacity.planner.entity.supply.UsecaseWiseSupplyChannel;
import com.capacity.planner.service.PlanService;
import com.capacity.planner.service.impl.PlanServiceImpl;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.List;

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
            new HibernateBundle<PlanningConfiguration>(Demand.class, PlanOutcome.class, Skill.class, SupplyChannel.class, Supply.class,
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
//
        final ApplicationHealthCheck healthCheck = new ApplicationHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
//
//        environment.admin().addTask(new StopServerTask(1));
//
        final DemandDao demandDao = new DemandDao(hibernate.getSessionFactory());
        final UsecaseDao usecaseDao = new UsecaseDao(hibernate.getSessionFactory());
        final SupplyChannelDao supplyChannelDao = new SupplyChannelDao(hibernate.getSessionFactory());
        final SupplyDao supplyDao = new SupplyDao(hibernate.getSessionFactory());
        final PlanOutcomeDao planOutcomeDao = new PlanOutcomeDao(hibernate.getSessionFactory());

        final PlanServiceImpl planService = new PlanServiceImpl(demandDao,usecaseDao,supplyChannelDao,
                supplyDao,planOutcomeDao);
//
        environment.jersey().register(new PlanController(planService));
    }


}
