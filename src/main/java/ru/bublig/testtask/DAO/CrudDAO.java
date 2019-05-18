package ru.bublig.testtask.DAO;

import ru.bublig.testtask.model.Entity;

import java.util.List;

public abstract class CrudDAO<E extends Entity, K extends Number> {

    public abstract List<E> getAll();
    public abstract E getEntityById(K id);
    protected abstract void update(E entity);
    public abstract boolean delete(K id);
    protected abstract void create(E entity);
    public abstract List<E> getAll(String filter);
    protected abstract List<E> getAllHelper(String sql);

    public void save(E entity){
        if (entity == null) {
            System.out.println("Entity is null");
            return;
        }
        if (entity.getId() == null) {
            create(entity);
        } else {
            update(entity);
        }
    }
}
