package com.hodbenor.project.eventsservice.dao;

import com.hodbenor.project.eventsservice.dao.beans.EventUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class EventUserDaoImpl implements EventUserDao {

    private final SessionFactory sessionFactory;

    public EventUserDaoImpl() {

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(EventUser.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        this.sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    public void signupToEvent(long eventId, long userId) {
        EventUser eventUser = new EventUser();
        eventUser.setEventId(eventId);
        eventUser.setUserId(userId);

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(eventUser);
        session.getTransaction().commit();
    }

    @Override
    public List<EventUser> findEventUser(long eventId) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<EventUser> criteriaQuery = criteriaBuilder.createQuery(EventUser.class);
        Root<EventUser> root = criteriaQuery.from(EventUser.class);

        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get("eventId"), eventId));

        Query<EventUser> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
