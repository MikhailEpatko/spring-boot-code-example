package ru.emi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import ru.emi.domain.UserActivity;
import java.util.Base64;
import java.util.List;

/**
 * @author Mikhail Epatko
 */

@Slf4j
@Service
public class TarantoolSerializer {

    private ObjectMapper mapper = new ObjectMapper();

    public String serialize(UserActivity value) {
        if(value != null) {
            try {
                byte[] data = SerializationUtils.serialize(value);
                return Base64.getEncoder().encodeToString(data);
            } catch (Exception e) {
                log.error("Cannot serialize", e);
            }
        }
        return null;
    }

    public UserActivity deserialize(List value) {
        if (!value.isEmpty() && value.get(0) != null) {
            try {
                String onlyValue = (String) ((List) value.get(0)).get(1);
                byte[] data = Base64.getDecoder().decode(onlyValue);
                return (UserActivity) SerializationUtils.deserialize(data);
            } catch (Exception e) {
                log.error("Cannot deserialize:  ", e);
            }
        }
        return null;
    }
}
