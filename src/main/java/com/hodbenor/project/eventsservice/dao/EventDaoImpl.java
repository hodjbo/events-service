package com.hodbenor.project.eventsservice.dao;

import com.hodbenor.project.eventsservice.dao.beans.Event;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class EventDaoImpl implements EventDao {

    private final SessionFactory sessionFactory;

    public EventDaoImpl() {

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(Event.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        this.sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    public long insertEvent(Event event) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(event);
        session.getTransaction().commit();

        return event.getId();
    }

    @Override
    public List<Event> findAllEvents() {

        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);
        criteriaQuery.select(root);
        Query<Event> query = session.createQuery(criteriaQuery);

        return query.getResultList();
    }
    @Override
    public List<Event> findAllEvents(LocalDateTime fromDate, LocalDateTime toDate) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);
        criteriaQuery.select(root)
                .where(criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("dateTime"), fromDate),
                        criteriaBuilder.lessThan(root.get("dateTime"), toDate)));

        Query<Event> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public Optional<Event> findById(long eventId) {
        Session session = sessionFactory.openSession();

        return Optional.ofNullable(session.get(Event.class, eventId));
    }

    @Override
    public List<Event> findEventsByVenue(String venue) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);

        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get("venue"), venue));

        Query<Event> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public List<Event> findEventsByLocation(String location) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);

        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get("location"), location));

        Query<Event> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public List<Event> findSortedEvents(String orderBy) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);

        criteriaQuery.select(root).orderBy(criteriaBuilder.asc(root.get(orderBy)));
        Query<Event> query = session.createQuery(criteriaQuery);

        return query.getResultList();
    }

    @Override
    public boolean updateEvent(Event event) {
        Transaction transaction = null;
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.update(event);
            transaction.commit();

            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            return false;
        }
    }

    @Override
    public Optional<Event> removeEvent(long eventId) {
        Optional<Event> optionalEvent = findById(eventId);

        optionalEvent.ifPresent(event -> {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(event);
            transaction.commit();
        });

        return optionalEvent;
    }
}
