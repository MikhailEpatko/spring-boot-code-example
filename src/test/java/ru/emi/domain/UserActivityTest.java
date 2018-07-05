package ru.emi.domain;

import org.junit.Test;
import java.time.LocalDate;
import java.time.Month;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UserActivityTest {

    @Test
    public void test_date_calculate() {
        UserActivity ua = new UserActivity();
        ua.setCreated(LocalDate.of(2018, Month.MARCH, 22));
        ua.update(LocalDate.of(2018, Month.MARCH, 23));

        assertThat(ua.getDaysAfterCreate(), is(1L));
        assertThat(ua.getDaysAfterPreviousUpdate(), is(1L));

        ua.update(LocalDate.of(2018, Month.MARCH, 28));

        assertThat(ua.getDaysAfterCreate(), is(6L));
        assertThat(ua.getDaysAfterPreviousUpdate(), is(5L));
    }
}