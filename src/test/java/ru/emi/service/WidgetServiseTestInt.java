package ru.emi.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.emi.domain.IdPostgres;
import ru.emi.domain.UserActivity;
import ru.emi.domain.MyHtml;
import ru.emi.domain.MyRule;
import ru.emi.repository.HtmlPostgresRepository;
import ru.emi.repository.IdPostgresRepository;
import ru.emi.repository.RulesPostgresRepository;
import ru.emi.repository.UserActivityTarantoolRepository;
import java.time.LocalDate;
import java.time.Month;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WidgetServiseTestInt {

    @Autowired
    private MyService service;
    @Autowired
    private UserActivityTarantoolRepository userActivityRepository;
    @Autowired
    private IdPostgresRepository idRepository;
    @Autowired
    private RulesPostgresRepository rulesRepository;
    @Autowired
    private HtmlPostgresRepository htmlRepository;
    private IdPostgres idPostgres;
    private final static String SESSION = "nts-11111";
    private MyRule rule1;
    private MyRule rule2;
    private MyRule rule3;
    private MyRule rule4;
    private MyRule rule5;

    @Before
    public void initializeTest() {
        idPostgres = new IdPostgres("id-12345", false, "email", "phone", null, null);
        idRepository.save(idPostgres);
        MyHtml wh1 = htmlRepository.save(new MyHtml(1, "name1", "11111", 1));
        MyHtml wh2 = htmlRepository.save(new MyHtml(2, "name2", "22222", 2));
        MyHtml wh3 = htmlRepository.save(new MyHtml(3, "name3", "33333", 3));
        rule1 = new MyRule();
        rule2 = new MyRule();
        rule3 = new MyRule();
        rule4 = new MyRule();
        rule5 = new MyRule();
        rule1.setCreated(LocalDate.of(2018, Month.MARCH, 1));
        rule2.setCreated(LocalDate.of(2018, Month.FEBRUARY, 27));
        rule3.setCreated(LocalDate.of(2018, Month.MARCH, 3));
        rule1.setPriority(10);
        rule2.setPriority(9);
        rule3.setPriority(8);
        rule1.setMyHtml(wh1);
        rule2.setMyHtml(wh2);
        rule3.setMyHtml(wh3);
        rule1 = rulesRepository.save(rule1);
        rule2 = rulesRepository.save(rule2);
        rule3 = rulesRepository.save(rule3);
    }

    @Test
    public void test_shouldGetHtmlFromRuleWithHighestPriority() {
        String html = service.getHtmlByActivity("Mozilla", "google.com", "id-12345", SESSION, "ntfp-12345", "127.0.0.1");

        assertThat(html, is("11111"));
    }

    @Test
    public void test_shouldGetHtmlFromRuleWithEqualPriorityAndOldestDate() {
        MyHtml wh4 = htmlRepository.save(new MyHtml(4, "name4", "44444", 1));
        rule4.setCreated(LocalDate.of(2018, Month.FEBRUARY, 28));
        rule4.setPriority(10);
        rule4.setMyHtml(wh4);
        rule4 = rulesRepository.save(rule4);

        String html = service.getHtmlByActivity("Mozilla", "google.com", "id-12345", SESSION, "ntfp-12345", "127.0.0.1");

        assertThat(html, is("44444"));
    }

    @Test
    public void test_shouldGetHtmlFromRuleWithSmallerPriorityAndBiggestShowCounter() {
        service.getHtmlByActivity("Mozilla", "google.com", "id-12345", SESSION, "ntfp-12345", "127.0.0.1");
        String html = service.getHtmlByActivity("Mozilla", "google.com", "id-12345", SESSION, "ntfp-12345", "127.0.0.1");

        assertThat(html, is("22222"));
    }

    @Test
    public void test_shouldGetHtmlFromRuleWithEqualPriorityEqualShowCounterAndOldestDate() {
        MyHtml wh4 = htmlRepository.save(new MyHtml(4, "name4", "44444", 1));
        rule4.setCreated(LocalDate.of(2018, Month.FEBRUARY, 28));
        rule4.setPriority(10);
        rule4.setMyHtml(wh4);
        rule4 = rulesRepository.save(rule4);

        MyHtml wh5 = htmlRepository.save(new MyHtml(5, "name5", "55555", 1));
        rule5.setCreated(LocalDate.of(2018, Month.MARCH, 1));
        rule5.setPriority(10);
        rule5.setMyHtml(wh5);
        rule5 = rulesRepository.save(rule5);

        service.getHtmlByActivity("Mozilla", "google.com", "id-12345", SESSION, "ntfp-12345", "127.0.0.1");
        String html = service.getHtmlByActivity("Mozilla", "google.com", "id-12345", SESSION, "ntfp-12345", "127.0.0.1");

        assertThat(html, is("44444"));
    }

    @Test
    public void test_shouldGetUserActivityWithUpdatedFieldHaveEmail() {
        UserActivity ua1 = userActivityRepository.getUserActivity("id-12345");
        assertNull(ua1);

        service.getHtmlByActivity("Mozilla", "google.com", "id-12345", SESSION, "ntfp-12345", "127.0.0.1");
        ua1 = userActivityRepository.getUserActivity("id-12345");

        assertNotNull(ua1);
        assertThat(ua1.getHaveEmail(), is(1));

        service.getHtmlByActivity("Mozilla", "google.com", "id-00000", SESSION, "ntfp-12345", "127.0.0.1");
        UserActivity ua2 = userActivityRepository.getUserActivity("id-00000");

        assertNotNull(ua2);
        assertThat(ua2.getHaveEmail(), is(0));

    }

    @After
    public void completeTest() {
        userActivityRepository.delete("id-12345");
        userActivityRepository.delete("id-00000");
        idRepository.delete(idPostgres);
        rulesRepository.delete(rule1);
        rulesRepository.delete(rule2);
        rulesRepository.delete(rule3);
        rulesRepository.delete(rule4);
        rulesRepository.delete(rule5);
    }
}
