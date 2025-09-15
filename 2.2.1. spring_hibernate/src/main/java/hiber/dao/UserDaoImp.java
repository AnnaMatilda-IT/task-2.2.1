package hiber.dao;

import hiber.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {

       Session session = sessionFactory.getCurrentSession();
       // Если у пользователя есть машина, сохраняем ее сначала
       if (user.getCar() != null) {
           session.save(user.getCar());
       }
       // Затем сохраняем пользователя
       session.save(user);
   }

   @Override
   public List<User> listUsers() {
       TypedQuery<User> query =
               sessionFactory.getCurrentSession().createQuery("from User", User.class);
      return query.getResultList();
   }

    @Override
    public User findByCarModelAndSeries(String model, int series) {
        TypedQuery<User> query =
                sessionFactory.getCurrentSession().createQuery(
                "SELECT u FROM User u JOIN u.car c WHERE c.model = :model AND c.series = :series",
                User.class
        );
        query.setParameter("model", model);
        query.setParameter("series", series);
        query.setMaxResults(1); // явно ограничила выборку одним результатом

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            // если все же найдено несколько автомобилей, возвращаем первый найденный
            List<User> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        }
    }
}
