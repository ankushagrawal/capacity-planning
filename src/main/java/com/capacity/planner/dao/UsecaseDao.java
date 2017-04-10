package com.capacity.planner.dao;

import com.capacity.planner.entity.metadata.Usecase;
import com.capacity.planner.enums.UsecaseEnum;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by ankush.a on 05/04/17.
 */
public class UsecaseDao extends AbstractDAO<Usecase> {
    private SessionFactory sessionFactory;
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public UsecaseDao(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public Usecase getUsecase(UsecaseEnum usecaseEnum){

        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Usecase.class);
        criteria.add(Restrictions.eq("usecase", usecaseEnum));
        List<Usecase> results = criteria.list();
        session.close();
        return (results!=null && results.size()!=0) ? results.get(0) : null ;
    }

    public UsecaseEnum getUsecase(Long id){
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Usecase.class);
        criteria.add(Restrictions.idEq(id));
        List<Usecase> usecases = criteria.list();
        return usecases.get(0).getUsecase();
    }
}
