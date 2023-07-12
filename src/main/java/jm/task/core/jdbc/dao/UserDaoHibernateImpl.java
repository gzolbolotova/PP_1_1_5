package jm.task.core.jdbc.dao;

import org.hibernate.Session;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import jakarta.persistence.*;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class UserDaoHibernateImpl implements UserDao {
    private final String TABLE = "users";

    Transaction transaction = null;

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery(String.format("CREATE TABLE IF NOT EXISTS %s " +
                    "(id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(20), lastName VARCHAR(25), age INT)", TABLE));
            transaction.commit();
        } catch (Exception e) {
            System.out.println("Table exists");
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void dropUsersTable() {
            try (Session session = Util.getSessionFactory().openSession()){
                transaction = session.beginTransaction();
                session.createNativeQuery(String.format("DROP TABLE IF EXISTS %s ", TABLE));
                transaction.commit();
            }catch (Exception e) {
                System.out.println("Table exists");
                if (transaction != null) {
                    transaction.rollback();
                }
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try(Session session = Util.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.persist(user);
            transaction.commit();
        }catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

    }

    @Override
    public void removeUserById(long id) {
        try(Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if(user != null) {
                session.remove(user);
            }
            transaction.commit();
        }catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

    }

    @Override
    public List<User> getAllUsers() {
       List<User> users = null;
        try (Session session = Util.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            String hql = String.format("FROM %s", TABLE);
            Query query = session.createQuery(hql);
            users = query.list();
            transaction.commit();
        }catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.createQuery(String.format("DELETE FROM %s", TABLE)).executeUpdate();
            /*for(User user: getAllUsers()){
                session.remove(user);
            }  */
            transaction.commit();
        }catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

    }
}
