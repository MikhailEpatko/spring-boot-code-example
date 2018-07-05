package ru.emi.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MyRuleTest {

    @Test
    public void createJson() throws Exception {
        String expected = "{\"id\":0,\"name\":null,\"priority\":0,\"description\":null,\"userCreator\":null," +
                "\"created\":null,\"updated\":null,\"viewsPerSession\":0,\"sessionsCount\":0,\"daysAfterCreate\":0," +
                "\"daysAfterPreviousUpdate\":0,\"b2b\":null,\"haveEmail\":null,\"myHtml\":{\"id\":0,\"name\":null," +
                "\"html\":null,\"mayBeShown\":0}}";
        MyRule wr = new MyRule();
        wr.setMyHtml(new MyHtml());
        ObjectMapper mapper = new ObjectMapper();
        String actual = mapper.writeValueAsString(wr);
        System.out.println(actual);

        assertThat(actual, is(expected));
    }
}