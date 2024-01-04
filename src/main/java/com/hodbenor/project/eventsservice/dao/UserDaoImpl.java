package com.hodbenor.project.eventsservice.dao;

import com.hodbenor.project.eventsservice.dao.beans.User;
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
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final SessionFactory sessionFactory;

    public UserDaoImpl() {

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(User.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        this.sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    public long insertUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(user);
        session.getTransaction().commit();

        return user.getId();
    }

    @Override
    public Optional<User> findUserById(long userId) {
       Session session = sessionFactory.openSession();

        return Optional.ofNullable(session.get(User.class, userId));
    }

    @Override
    public Optional<User> findUser(String username, String password) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        criteriaQuery.select(root)
                .where(criteriaBuilder.and(criteriaBuilder.equal(root.get("username"), username),
                        criteriaBuilder.equal(root.get("password"), password)));

        Query<User> query = session.createQuery(criteriaQuery);

        return Optional.ofNullable(query.uniqueResult());
    }
}