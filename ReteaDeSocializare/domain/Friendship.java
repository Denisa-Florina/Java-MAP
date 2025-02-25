package src.lab7.domain;

import java.time.LocalDateTime;

public class Friendship extends Entity<Long> {
    Long userId1;
    Long userId2;
    LocalDateTime dateTime;

    public Friendship(Long userId1, Long userId2, LocalDateTime dateTime) {
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "userId1=" + userId1 +
                ", userId2=" + userId2 +
                ", dateTime=" + dateTime +
                '}';
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Long getUserId1() {
        return userId1;
    }

    public Long getUserId2() {
        return userId2;
    }
}
