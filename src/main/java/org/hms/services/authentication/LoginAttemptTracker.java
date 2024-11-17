package org.hms.services.authentication;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is responsible for tracking login attempts and managing account lockouts.
 * It maintains a count of failed login attempts for each user and locks the account
 * if the number of failed attempts exceeds a configured threshold.
 */
public class LoginAttemptTracker {
    /**
     * The duration in minutes for which an account is locked out after exceeding the allowed number of failed login attempts.
     * Once the account is locked, no further login attempts will be permitted until this duration has passed.
     */
    private static final int LOCKOUT_DURATION_MINUTES = 30;
    /**
     * A map to track the number of failed login attempts for each user.
     * The key is a user ID, and the value is the count of failed attempts.
     * This helps in determining when to lock an account after a certain number of failed login attempts.
     */
    private final Map<String, Integer> attemptCount = new ConcurrentHashMap<>();
    /**
     * A map that tracks the lockout time for each user.
     * <p>
     * The keys are user IDs, and the values are the {@link LocalDateTime}
     * instances indicating when the user's account was locked out due to
     * excessive failed login attempts.
     * <p>
     * This is used to determine if a user's account is currently locked
     * and when the lockout period will expire.
     */
    private final Map<String, LocalDateTime> lockoutTime = new ConcurrentHashMap<>();

    /**
     * Records a failed login attempt for the specified user. If the number of failed attempts
     * reaches the defined threshold, the user's account is marked as locked.
     *
     * @param userId The unique identifier of the user who made the failed login attempt.
     */
    public void recordFailedAttempt(String userId) {
        attemptCount.merge(userId, 1, Integer::sum);
        if (attemptCount.get(userId) >= 3) {
            lockoutTime.put(userId, LocalDateTime.now());
        }
    }

    /**
     * Clears the record of login attempts and lockout time for the specified user.
     *
     * @param userId The unique identifier of the user whose attempts are to be cleared.
     */
    public void clearAttempts(String userId) {
        attemptCount.remove(userId);
        lockoutTime.remove(userId);
    }

    /**
     * Checks if a user's account is locked due to too many failed login attempts.
     * An account is considered locked if the current time is within the lockout duration
     * since the last failed login attempt that triggered the lockout.
     *
     * @param userId The unique identifier of the user whose account lock status is being checked.
     * @return {@code true} if the user's account is currently locked, {@code false} otherwise.
     */
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

    /**
     * Retrieves the number of remaining login attempts for a given user before their account is locked.
     *
     * @param userId The unique identifier of the user whose remaining login attempts are being queried.
     * @return The number of remaining login attempts before the account is locked.
     */
    public int getRemainingAttempts(String userId) {
        return 3 - attemptCount.getOrDefault(userId, 0);
    }

    /**
     * Retrieves the end time of the lockout period for a given user.
     *
     * @param userId The unique identifier of the user whose lockout end time is to be retrieved.
     * @return The {@code LocalDateTime} representing the end time of the lockout period, or {@code null} if the user is not currently locked out.
     */
    public LocalDateTime getLockoutEndTime(String userId) {
        LocalDateTime lockTime = lockoutTime.get(userId);
        return lockTime != null ? lockTime.plusMinutes(LOCKOUT_DURATION_MINUTES) : null;
    }
}
