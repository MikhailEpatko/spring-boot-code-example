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
import ru.emi.repository.IdPostgresRepository;
import ru.emi.repository.UserActivityTarantoolRepository;
import ru.emi.util.MyDate;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WidgetServiceTest {

    @Autowired
    private MyService service;
    @Autowired
    private UserActivityTarantoolRepository userActivityRepository;
    @Autowired
    private IdPostgresRepository idRepository;
    private IdPostgres idPostgres;
    private final static String SESSION = "nts-11111";
    private final static String OTHER_SESSION = "nts-22222";


    @Before
    public void initializeTest() {
        idPostgres = new IdPostgres("id-12345", false, "email", "phone", null, null);
        idRepository.save(idPostgres);
        this.service.getHtmlByActivity("Mozilla", "google.com", "id-12345", SESSION, "ntfp-12345", "127.0.0.1");
    }

    @Test
    public void test_getHtml() {
        UserActivity ua = userActivityRepository.getUserActivity("id-12345");

        assertThat(ua.getDaysAfterCreate(), is(0L));
        assertThat(ua.getDaysAfterPreviousUpdate(), is(0L));
        assertThat(ua.getCreated(), is(MyDate.now()));
        assertThat(ua.getUpdated(), is(MyDate.now()));
        assertThat(ua.getNtSessionId(), is(SESSION));
        assertThat(ua.getIp(), is("127.0.0.1"));
        assertThat(ua.getNtFingerPrint(), is("ntfp-12345"));
        assertThat(ua.getId(), is("id-12345"));
        assertThat(ua.getReferer(), is("google.com"));
        assertThat(ua.getUserAgent(), is("Mozilla"));
        assertThat(ua.getSessionsCount(), is(1));
        assertThat(ua.getViewsPerSession(), is(1));
    }

    @Test
    public void test_getHtmlAgainInSameSession() {
        this.service.getHtmlByActivity("Mozilla", "google.com", "id-12345", SESSION, "ntfp-12345", "127.0.0.1");
        UserActivity ua = userActivityRepository.getUserActivity("id-12345");

        assertThat(ua.getSessionsCount(), is(1));
        assertThat(ua.getViewsPerSession(), is(2));
    }

    @Test
    public void test_getHtmlAgainInOtherSession() {
        this.service.getHtmlByActivity("Mozilla", "google.com", "id-12345", OTHER_SESSION, "ntfp-12345", "127.0.0.1");
        UserActivity ua = userActivityRepository.getUserActivity("id-12345");

        assertThat(ua.getSessionsCount(), is(2));
        assertThat(ua.getViewsPerSession(), is(1));
    }

    @After
    public void completeTest() {
        userActivityRepository.delete("id-12345");
        idRepository.delete(idPostgres);
    }
}