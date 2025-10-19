package org.example.Manager;


import jakarta.inject.Singleton;
import lombok.Getter;
import org.example.DbModels.ShopUser;
import org.example.DbModels.Shopkeeper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Singleton
public class SessionManager {

    private static final Map<String, SessionData> sessions = new ConcurrentHashMap<>();
    private static final Map<String, ShopkeeperSessionData> shopKeeperSessions = new ConcurrentHashMap<>();
    private static final long SESSION_DURATION = 10 * 60; // 10 minutes in seconds

    public static String createSession(final ShopUser user) {
        String sessionId = UUID.randomUUID().toString();
        SessionData data = new SessionData(user, Instant.now().plusSeconds(SESSION_DURATION));
        sessions.put(sessionId, data);
        return sessionId;
    }

    public static String createShopKeeperSession(final Shopkeeper shopkeeper) {
        String sessionId = UUID.randomUUID().toString();
        ShopkeeperSessionData data = new ShopkeeperSessionData(shopkeeper, Instant.now().plusSeconds(SESSION_DURATION));
        shopKeeperSessions.put(sessionId, data);
        return sessionId;
    }

    public static ShopUser validateSession(String sessionId) {
        SessionData data = sessions.get(sessionId);
        if (data == null || Instant.now().isAfter(data.getExpiry())) {
            sessions.remove(sessionId);
            return null;
        }
        return data.getUser();
    }

    public static Shopkeeper validateShopKeeperSession(String sessionId) {
        ShopkeeperSessionData data = shopKeeperSessions.get(sessionId);
        if (data == null || Instant.now().isAfter(data.getExpiry())) {
            shopKeeperSessions.remove(sessionId);
            return null;
        }
        return data.getShopkeeper();
    }

    @Getter
    private static class SessionData {
        private final ShopUser user;
        private final Instant expiry;

        public SessionData(ShopUser user, Instant expiry) {
            this.user = user;
            this.expiry = expiry;
        }
    }

    @Getter
    private static class ShopkeeperSessionData {
        private final Shopkeeper shopkeeper;
        private final Instant expiry;

        public ShopkeeperSessionData(Shopkeeper shopkeeper, Instant expiry) {
            this.shopkeeper = shopkeeper;
            this.expiry = expiry;
        }
    }
}
