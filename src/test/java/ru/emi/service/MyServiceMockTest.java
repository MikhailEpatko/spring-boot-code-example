package ru.emi.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.emi.domain.IdPostgres;
import ru.emi.domain.MyHtml;
import ru.emi.domain.UserActivity;
import ru.emi.domain.MyRule;
import ru.emi.repository.IdPostgresRepository;
import ru.emi.repository.RulesPostgresRepository;
import ru.emi.repository.UserActivityTarantoolRepository;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MyServiceMockTest {

    @Mock
    private UserActivityTarantoolRepository userActivityRepository;
    @Mock
    private RulesPostgresRepository rulesRepository;
    @Mock
    private IdPostgresRepository idRepository;
    @InjectMocks
    private MyService service;
    private List<MyRule> rules;

    @Before
    public void initializeTest() {
        MyHtml wh1 = new MyHtml(1, "name1", "12345", 1);
        MyHtml wh2 = new MyHtml(2, "name2", "22222", 2);
        MyHtml wh3 = new MyHtml(3, "name3", "33333", 3);
        MyRule wr1 = new MyRule();
        MyRule wr2 = new MyRule();
        MyRule wr3 = new MyRule();
        wr1.setMyHtml(wh1);
        wr2.setMyHtml(wh2);
        wr3.setMyHtml(wh3);
        wr1.setPriority(10);
        wr2.setPriority(9);
        wr3.setPriority(8);
        wr1.setCreated(LocalDate.of(2018, Month.MARCH, 22));
        wr2.setCreated(LocalDate.of(2018, Month.MARCH, 23));
        wr3.setCreated(LocalDate.of(2018, Month.MARCH, 24));
        rules = Arrays.asList(wr1, wr2, wr3);
    }

    @Test
    public void testGettingMorePriorityHtml() {
        String expectedHtml = "12345";

        when(userActivityRepository.getUserActivity("id-12345")).thenReturn(null);
        when(userActivityRepository.save(any(UserActivity.class))).thenReturn("true");
        when(rulesRepository.findRules(1, 1, 0L, 0L, 1, 1)).thenReturn(rules);
        when(idRepository.findById("id-12345")).thenReturn(Optional.of(new IdPostgres("id-12345", true, "email",
                "phone", null, null)));

        String html = this.service.getHtmlByActivity("Mozilla", "google.com", "id-12345", "nts-12345", "ntfp-12345", "127.0.0.1");

        assertThat(html, is(expectedHtml));
    }

    @Test
    public void testGettingNextMorePriorityHtml() {
        String expectedHtml = "22222";
        UserActivity ua = new UserActivity("Mozilla", "google.com", "id-12345", "nts-12345", "ntfp-12345", "127.0.0.1");
        ua.getHtmlViewsCounters().put(1, 1);
        ua.incrementSessionsCount();
        ua.setB2b(0);
        ua.setHaveEmail(1);

        when(userActivityRepository.getUserActivity("id-12345")).thenReturn(ua);
        when(userActivityRepository.save(any(UserActivity.class))).thenReturn("true");
        when(idRepository.findById("id-12345")).thenReturn(Optional.of(new IdPostgres("id-12345", false, "email",
                "phone", null, null)));
        when(rulesRepository.findRules(0, 1, 0, 0, 1, 1)).thenReturn(rules);

        String html = this.service.getHtmlByActivity("Mozilla", "google.com", "id-12345", "nts-12345", "ntfp-12345", "127.0.0.1");

        assertThat(html, is(expectedHtml));
        assertThat(ua.getHtmlViewsCounters().get(2), is(1));
    }
}
