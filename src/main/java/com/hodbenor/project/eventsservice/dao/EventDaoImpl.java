package com.hodbenor.project.eventsservice.dao;

import com.hodbenor.project.eventsservice.dao.beans.Event;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class EventDaoImpl implements EventDao {

    private SessionFactory sessionFactory;

    public EventDaoImpl() {

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(Event.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        this.sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    public void insertEvent(Event event) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            session.persist(event);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("error " + e);
        }
    }

    @Override
    public List<Event> fetchAllScheduledEvents(LocalDateTime fromDate, LocalDateTime toDate) {

        try {
            Session session = sessionFactory.openSession();
           Query query = session.createQuery("FROM Event", Event.class);
            return query.getResultList();

        } catch (Exception e) {

        }

        return List.of();
    }

    @Override
    public Optional<Event> findById(long eventId) {
        try {
            Session session = sessionFactory.openSession();
            //return Optional.of(session.get(Event.class, eventId));
        } catch (Exception e) {

        } finally {
            sessionFactory.close();
        }

        return Optional.empty();
    }

    @Override
    public void updateEvent(Event event) {
        Transaction transaction = null;
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.update(event);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            sessionFactory.close();
        }
    }

    @Override
    public void removeEvent(long eventId) {

    }
}
