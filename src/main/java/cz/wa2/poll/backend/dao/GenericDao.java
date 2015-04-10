package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.entities.EntitiesList;
import cz.wa2.poll.backend.exception.DaoException;
import cz.wa2.poll.backend.exception.InputException;

import java.io.Serializable;

/**
 * Created by Nell on 25.2.2015.
 */
public interface GenericDao<T, PK extends Serializable>  {

    T find(PK id) throws DaoException;
    EntitiesList<T> findAll(Integer offset, Integer base, String order) throws DaoException, InputException;
    T create(T object) throws DaoException;
    void update(T object) throws DaoException;
    void delete(PK id) throws DaoException;

}
