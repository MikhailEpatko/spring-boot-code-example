package ru.emi.service;

import org.junit.Test;
import ru.emi.domain.UserActivity;

import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.*;

public class TarantoolSerializerTest {


    @Test
    public void test_Serialize_And_Deserialize() {
        TarantoolSerializer serializer = new TarantoolSerializer();
        UserActivity expected = new UserActivity("Mozilla", "google.com", "id-12345", "nts-12345", "ntfp-12345", "127.0.0.1");

        String value = serializer.serialize(expected);
        UserActivity actual = serializer.deserialize(Collections.singletonList(Arrays.asList("id-12345", value)));

        assertEquals(expected, actual);
    }
}