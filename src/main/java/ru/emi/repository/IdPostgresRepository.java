package ru.emi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.emi.domain.IdPostgres;
import java.util.Optional;

@Repository
public interface IdPostgresRepository extends CrudRepository<IdPostgres, String> {

    Optional<IdPostgres> findById(String id);
}
