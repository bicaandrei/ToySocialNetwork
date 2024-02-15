package ubb_221.toysocialnetworkgui.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {
    private User from;

    private User to;
    private String message;
    private LocalDateTime date;
    private Message reply = null;
    public Message(User from, User to, String message, LocalDateTime date){

        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;

    }

    public User getFrom() { return this.from; }
    public void addTo(User user) { this.to = user; }
    public User getTo() { return this.to; }

    public void setMessage(String message) { this.message = message; }

    public String getMessage() { return this.message; }
    public LocalDateTime getDate() { return this.date; }
    public void setReply(Message reply) { this.reply = reply; }
    public Message getReply() { return this.reply; }

    @Override
    public String toString(){

        return "MessageId=" + id + " | from " + from.toString() + " | to " + this.to + " | message { " + message  + " }" + " | at " + date.toString() + " |";

    }

}
