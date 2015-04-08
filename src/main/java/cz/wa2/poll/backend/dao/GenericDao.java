package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.exception.DaoException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nell on 25.2.2015.
 */
public interface GenericDao<T, PK extends Serializable>  {

    T find(PK id) throws DaoException;
    List<T> findAll(Integer offset, Integer base) throws DaoException;
    T create(T object) throws DaoException;
    void update(T object) throws DaoException;
    void delete(PK id) throws DaoException;

}
