package com.major.k1.resturant.DTO;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OtpUserStore {
    private final Map<String, PendingUser> store = new ConcurrentHashMap<>();

    public void save(String email, PendingUser user) {
        store.put(email, user);
    }

    public PendingUser get(String email) {
        return store.get(email);
    }

    public void remove(String email) {
        store.remove(email);
    }

    public void removeExpiredUsers(long maxAgeMillis) {
        long now = System.currentTimeMillis();
        store.entrySet().removeIf(entry ->
                (now - entry.getValue().getTimestamp()) > maxAgeMillis
        );
    }
}