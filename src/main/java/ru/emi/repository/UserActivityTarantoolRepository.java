package ru.emi.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.tarantool.TarantoolClientOps;
import ru.emi.domain.UserActivity;
import ru.emi.service.TarantoolSerializer;
import java.util.List;

@Repository
public class UserActivityTarantoolRepository {

    private TarantoolSerializer serializer;
    private TarantoolClientOps<Integer, List<?>, Object, List<?>> syncOps;

    @Autowired
    public UserActivityTarantoolRepository(TarantoolSerializer serializer, TarantoolClientOps<Integer, List<?>, Object, List<?>> syncOps) {
        this.serializer = serializer;
        this.syncOps = syncOps;
    }

    public UserActivity getUserActivity(String id) {
        return serializer.deserialize(syncOps.call("selectById", id));
    }

    public String save(UserActivity lastActivity) {
        List<?> res = syncOps.call("upsert", lastActivity.getId(), serializer.serialize(lastActivity));
        return res.get(0).toString();
    }

    public UserActivity delete(String id) {
        return serializer.deserialize(syncOps.call("deleteById", id));
    }
}
