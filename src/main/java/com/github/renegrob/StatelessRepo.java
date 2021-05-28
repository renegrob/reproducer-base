package com.github.renegrob;

import java.time.LocalDateTime;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.query.Query;
import com.github.renegrob.entity.Fruit;

@Singleton
@Transactional
public class StatelessRepo {

    @Inject
    SessionFactory sessionFactory;

    public void upsertApple() {
        try (StatelessSession session = sessionFactory.openStatelessSession()) {
            final String QUERY = "UPDATE Fruit o SET o.color = :color WHERE o.name = :name";
            final Query<?> query = session.createQuery(QUERY);
            query.setParameter("name", "apple");
            query.setParameter("color", "green");
            final int cnt = query.executeUpdate();
            if (cnt == 0) {
                session.insert(Fruit.create("apple", "green"));
            }
        }
    }
}
