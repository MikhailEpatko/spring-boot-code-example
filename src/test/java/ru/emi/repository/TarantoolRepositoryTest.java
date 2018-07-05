package ru.emi.repository;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tarantool.TarantoolClientOps;
import ru.emi.domain.UserActivity;
import ru.emi.service.TarantoolSerializer;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/* To start tests need special tarantool application started. */

@RunWith(SpringRunner.class)
@SpringBootTest
public class TarantoolRepositoryTest {

    @Autowired
    private TarantoolSerializer serializer;
    @Autowired
    private UserActivityTarantoolRepository userActivityRepository;
    @Autowired
    private TarantoolClientOps<Integer, List<?>, Object, List<?>> syncOps;
    private UserActivity expected = new UserActivity("Mozilla", "google.com", "id-12345", "nts-12345", "ntfp-12345",
                                                     "127.0.0.1");

    @Test
    public void testContextStarted() {
        assertNotNull(syncOps);
        assertNotNull(serializer);
        syncOps.ping();
    }

    @Test
    public void test_Upsert_And_Select_in_DB() {

        List<?> tuple = syncOps.call("upsert", "id-12345", serializer.serialize(expected));
        assertTrue("true".equals(tuple.get(0).toString()));

        tuple = syncOps.call("selectByid", "id-12345");
        UserActivity actual = serializer.deserialize(tuple);
        assertEquals(expected, actual);
    }

    @Test
    public void test_Delete_in_DB() {
        syncOps.call("upsert", "id-12345", serializer.serialize(this.expected));
        List<?> expectedTuple = syncOps.call("selectById", "id-12345");
        List<?> actualTuple = syncOps.call("deleteById", "id-12345");

        assertEquals(expectedTuple, actualTuple);
    }

    @Test
    public void test_Upsert_in_TarantoolRepository_class() {
        assertTrue(Boolean.valueOf(userActivityRepository.save(this.expected)));

    }

    @Test
    public void test_Select_in_TarantoolRepository_class() {
        userActivityRepository.save(this.expected);
        UserActivity actual = userActivityRepository.getUserActivity(expected.getId());

        assertThat(expected, is(actual));
    }

    @Test
    public void test_Delete_in_TarantoolRepository_class() {
        userActivityRepository.save(this.expected);
        UserActivity actual = userActivityRepository.delete(expected.getId());

        assertThat(expected, is(actual));
    }

    @After
    public void executAfterTest() {
        userActivityRepository.delete("id-12345");
    }
}
