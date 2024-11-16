package org.hms.services.authentication;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginAttemptTracker {
    private static final int LOCKOUT_DURATION_MINUTES = 30;
    private final Map<String, Integer> attemptCount = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lockoutTime = new ConcurrentHashMap<>();

    public void recordFailedAttempt(String userId) {
        attemptCount.merge(userId, 1, Integer::sum);
        if (attemptCount.get(userId) >= 3) {
            lockoutTime.put(userId, LocalDateTime.now());
        }
    }

    public void clearAttempts(String userId) {
        attemptCount.remove(userId);
        lockoutTime.remove(userId);
    }

    public boolean isAccountLocked(String userId) {
        LocalDateTime lockTime = lockoutTime.get(userId);
        if (lockTime == null) return false;

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(lockTime.plusMinutes(LOCKOUT_DURATION_MINUTES))) {
            clearAttempts(userId);
            return false;
        }
        return true;
    }

    public int getRemainingAttempts(String userId) {
        return 3 - attemptCount.getOrDefault(userId, 0);
    }

    public LocalDateTime getLockoutEndTime(String userId) {
        LocalDateTime lockTime = lockoutTime.get(userId);
        return lockTime != null ? lockTime.plusMinutes(LOCKOUT_DURATION_MINUTES) : null;
    }
}
