package ru.emi.repository;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.emi.domain.MyHtml;
import ru.emi.domain.MyRule;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostgresRepositoryTest {

    @Autowired
    private RulesPostgresRepository rulesRepository;
    @Autowired
    private HtmlPostgresRepository htmlRepository;
    private MyRule rule = new MyRule();
    private MyRule rule2 = new MyRule();
    private MyHtml wh = new MyHtml();
    private MyHtml wh2 = new MyHtml();


    @Test
    public void test_Context() {
        assertNotNull(rulesRepository);
    }

    @Test
    public void test_Save() {
        wh.setHtml("=text here=");
        wh = htmlRepository.save(wh);
        rule.setMyHtml(wh);

        MyRule actual = rulesRepository.save(rule);

        assertThat(actual, is(rule));

        rulesRepository.delete(actual);
        Optional<MyRule> deleted = rulesRepository.findById(actual.getId());

        assertFalse(deleted.isPresent());
    }

    @Test
    public void test_Get() {
        wh.setHtml("=text here=");
        wh = htmlRepository.save(wh);
        rule.setCreated(LocalDate.now());
        rule.setPriority(10);
        rule.setMyHtml(wh);
        rule = rulesRepository.save(rule);

        wh2.setHtml("=text here 2=");
        wh2 = htmlRepository.save(wh2);
        rule2.setCreated(LocalDate.now());
        rule2.setPriority(10);
        rule2.setMyHtml(wh2);
        rule2 = rulesRepository.save(rule2);

        List<MyRule> rules = rulesRepository.findRules(1, 1, 0, 0, 1, 1);

        assertThat(rules.size(), is(1));
        assertThat(rules.get(0), is(rule));

        rules = rulesRepository.findRules(1, 0, 0, 0, 1, 1);

        assertThat(rules.size(), is(1));
        assertThat(rules.get(0), is(rule2));

        rulesRepository.save(rule);
        rules = rulesRepository.findRules(1, 1, 0, 0, 1, 1);

        assertThat(rules.size(), is(1));
        assertThat(rules.get(0), is(rule));

        rules = rulesRepository.findRules(1, 0, 0, 0, 1, 1);

        assertThat(rules.size(), is(2));
        assertThat(rules, is(Arrays.asList(rule2, rule)));
    }

    @After
    public void cleanDatabases() {
        rulesRepository.delete(rule);
        rulesRepository.delete(rule2);
        htmlRepository.delete(wh);
        htmlRepository.delete(wh2);
    }
}