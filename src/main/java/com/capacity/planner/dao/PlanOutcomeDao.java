package com.capacity.planner.dao;

import com.capacity.planner.dto.ResultDto;
import com.capacity.planner.entity.outcome.PlanOutcome;
import com.capacity.planner.entity.supply.Supply;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by ankush.a on 07/04/17.
 */
public class PlanOutcomeDao extends AbstractDAO<PlanOutcome> {
    private SessionFactory sessionFactory;
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public PlanOutcomeDao(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public void save(List<PlanOutcome> planOutcomeList) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        for ( int i=0; i<planOutcomeList.size(); i++ ) {
            PlanOutcome PlanOutcome = planOutcomeList.get(i);
            session.save(PlanOutcome);
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

    public ResultDto searchOutcome(Long requestId) throws Exception{
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(PlanOutcome.class);
        criteria.add(Restrictions.eq("requestId", requestId));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<PlanOutcome> outcomes =  criteria.list();
        session.close();
        ResultDto resultDto = new ObjectMapper().readValue(outcomes.get(0).getOutcome(),ResultDto.class);
        return resultDto;
    }
}
