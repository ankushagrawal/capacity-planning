package com.capacity.planner.dao;

import com.capacity.planner.entity.demand.Demand;
import com.capacity.planner.entity.metadata.Usecase;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by ankush.a on 05/04/17.
 */
public class DemandDao extends AbstractDAO<Demand> {

    private SessionFactory sessionFactory;
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public DemandDao(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public List<Demand> searchDemand(Long requestId){
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Demand.class);
        criteria.add(Restrictions.eq("requestId", requestId));

        List<Demand> demandList =  criteria.list();
        session.close();
        return demandList;
    }

    public Long save(List<Demand> demandList) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        final Long requestId = generateRequestId();
        for ( int i=0; i<demandList.size(); i++ ) {
            Demand demand = demandList.get(i);
            demand.setRequestId(requestId);
            session.save(demand);
            if ( i % 200 == 0 ) { //50, same as the JDBC batch size
                //flush a batch of inserts and release memory:
                session.flush();
                session.clear();
            }
        }
        session.flush();
        session.clear();
        tx.commit();
        session.close();
        return requestId;
    }

    private Long generateRequestId() {
        return System.currentTimeMillis();
    }
}
