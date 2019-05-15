package ru.bublig.testtask.DAO;

import java.util.List;

public abstract class CrudDAO<E, K> {

    public abstract List<E> getAll();
    public abstract E getEntityById(K id);
    protected abstract E update(E entity);
    public abstract boolean delete(K id);
    protected abstract boolean create(E entity);
    public abstract boolean save(E entity);
    public abstract List<E> getAll(String filter);
    protected abstract List<E> getAllHelper(String sql);
}
