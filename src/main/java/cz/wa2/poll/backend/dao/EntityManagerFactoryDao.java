package cz.wa2.poll.backend.dao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by Nell on 11.4.2015.
 */
public class EntityManagerFactoryDao {

    private static EntityManagerFactory entityManagerFactory;

    protected EntityManagerFactoryDao() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if(entityManagerFactory == null){
            entityManagerFactory = Persistence.createEntityManagerFactory("persist-unit");
        }
        return entityManagerFactory;
    }
}
