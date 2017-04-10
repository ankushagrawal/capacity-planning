package com.capacity.planner.dao;

import com.capacity.planner.entity.demand.Demand;
import com.capacity.planner.entity.supply.Supply;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by ankush.a on 06/04/17.
 */
public class SupplyDao extends AbstractDAO<Supply> {
    private SessionFactory sessionFactory;

    public SupplyDao(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public List<Supply> searchSupply(Long requestId){
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Supply.class);
        criteria.add(Restrictions.eq("requestId", requestId));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Supply> supplyList =  criteria.list();
        session.close();
        return supplyList;
    }

    public void save(List<Supply> supplyList) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        for ( int i=0; i<supplyList.size(); i++ ) {
            Supply supply = supplyList.get(i);
            session.save(supply);
            session.flush();
            session.clear();
            //TODO: bulk giving error here.
//            if ( i % 200 == 0 ) { //50, same as the JDBC batch size
//                //flush a batch of inserts and release memory:
//                session.flush();
//                session.clear();
//            }
        }

        tx.commit();
        session.close();
    }
}
