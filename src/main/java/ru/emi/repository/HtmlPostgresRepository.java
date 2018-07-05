package ru.emi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.emi.domain.MyHtml;

@Repository
public interface HtmlPostgresRepository extends CrudRepository<MyHtml, Integer> {
}
