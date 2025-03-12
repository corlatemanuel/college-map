package Repo;

import Entity.*;
import Exceptions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositoryImpl<T extends Entitate> implements Repository<T> {
    private List<T> entities = new ArrayList<>();

    @Override
    public void add(T entity) throws Exception {
        if (findById(entity.getId()).isPresent()) {
            throw new DuplicateObjectException("ID-ul trebuie să fie unic!");
        }
        entities.add(entity);
    }

    @Override
    public Optional<T> findById(int id) {
        return entities.stream().filter(e -> e.getId() == id).findFirst();
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(entities);
    }

    @Override
    public void update(T entity) throws Exception {
        Optional<T> existingEntity = findById(entity.getId());
        if (existingEntity.isEmpty()) {
            throw new NullObjectException("Entitatea nu a fost găsită!");
        }
        delete(entity.getId());
        add(entity);
    }

    @Override
    public void delete(int id) throws Exception {
        T entity = findById(id).orElseThrow(() -> new NullObjectException("Entitatea nu a fost găsită!"));
        entities.remove(entity);
    }
}