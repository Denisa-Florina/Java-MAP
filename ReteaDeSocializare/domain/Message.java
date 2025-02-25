package src.lab7.domain;

import java.time.LocalDateTime;

public class Message extends Entity<Long> {
    private Long id1;
    private Long id2;
    private String message;
    private LocalDateTime datetime;
    private String reply = null;

    public Message(Long id2, Long id1, String message, LocalDateTime datetime, String reply) {
        this.id2 = id2;
        this.reply = reply;
        this.id1 = id1;
        this.message = message;
        this.datetime = datetime;
    }


    public Message(Long id1, Long id2, String message, LocalDateTime datetime) {
        this.id1 = id1;
        this.id2 = id2;
        this.message = message;
        this.datetime = datetime;
        reply = null;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
