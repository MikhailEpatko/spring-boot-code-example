package ru.emi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.emi.domain.MyRule;
import java.util.List;

@Repository
public interface RulesPostgresRepository extends CrudRepository<MyRule, Integer> {

    @Query(/* custom query here */)
    List<MyRule> findRules(int b2b, int haveEmail, long daysAfterCreate, long daysAfterPreviousUpdate,
                           int sessionsCount, int viewsPerSession);
}