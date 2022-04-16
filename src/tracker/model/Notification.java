package tracker.model;

public class Notification {
    private String recipient;
    private String subject;
    private String message;


    public Notification(String recipient, String subject, String message) {
        this.subject = subject;
        this.recipient = recipient;
        this.message = message;
    }

    public Notification() {

    }

    public String getRecipient() {
        return this.recipient;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getMessage() {
        return this.message;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
