package src.lab7.domain;

import jdk.jfr.Event;
import src.lab7.utils.Observer;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FriendshipRequest extends Entity<Long>{
    Long userIdR1;
    Long userIdR2;
    LocalDateTime dateTimeR;

    public FriendshipRequest(Long userIdR1, Long userIdR2, LocalDateTime dateTimeR) {
        this.userIdR1 = userIdR1;
        this.userIdR2 = userIdR2;
        this.dateTimeR = dateTimeR;
    }

    public Long getUserIdR1() {
        return userIdR1;
    }

    public void setUserIdR1(Long userIdR1) {
        this.userIdR1 = userIdR1;
    }

    public Long getUserIdR2() {
        return userIdR2;
    }

    public void setUserIdR2(Long userIdR2) {
        this.userIdR2 = userIdR2;
    }

    public LocalDateTime getDateTimeR() {
        return dateTimeR;
    }

    public void setDateTimeR(LocalDateTime dateTimeR) {
        this.dateTimeR = dateTimeR;
    }

}
