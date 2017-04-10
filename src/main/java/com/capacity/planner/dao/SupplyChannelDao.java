package com.capacity.planner.dao;

import com.capacity.planner.entity.metadata.Usecase;
import com.capacity.planner.entity.supply.SupplyChannel;
import com.capacity.planner.enums.UsecaseEnum;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by ankush.a on 06/04/17.
 */
public class SupplyChannelDao extends AbstractDAO<SupplyChannel> {

    private SessionFactory sessionFactory;
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public SupplyChannelDao(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public SupplyChannel getSupplyChannel(String supplyChannel){

        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(SupplyChannel.class);
        criteria.add(Restrictions.eq("supplyChannel", supplyChannel));
        List<SupplyChannel> results = criteria.list();
        session.close();
        return (results!=null && results.size()!=0) ? results.get(0) : null ;
    }
}
