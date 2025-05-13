/*Notification (Abstract Class)

EmailNotification

SMSNotification

PushNotification

User

NotificationManager

NotificationPreference

NotificationService (Main Entry Point)

 */

 import java.util.*;

// Enum for Notification Types
enum NotificationType {
    EMAIL, SMS, PUSH
}

// NotificationPreference Class to store user preferences
class NotificationPreference {
    private final Set<NotificationType> preferences;

    public NotificationPreference() {
        preferences = new HashSet<>();
    }

    public void enableNotification(NotificationType type) {
        preferences.add(type);
    }

    public void disableNotification(NotificationType type) {
        preferences.remove(type);
    }

    public boolean isEnabled(NotificationType type) {
        return preferences.contains(type);
    }

    public Set<NotificationType> getPreferences() {
        return preferences;
    }
}

// User Class
class User {
    private final String userId;
    private final String email;
    private final String phoneNumber;
    private final NotificationPreference notificationPreference;

    public User(String userId, String email, String phoneNumber) {
        this.userId = userId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.notificationPreference = new NotificationPreference();
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public NotificationPreference getNotificationPreference() {
        return notificationPreference;
    }
}

// Abstract Notification Class
abstract class Notification {
    protected final User user;
    protected final String message;

    public Notification(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public abstract void send();
}

// EmailNotification Class
class EmailNotification extends Notification {

    public EmailNotification(User user, String message) {
        super(user, message);
    }

    @Override
    public void send() {
        System.out.println("Sending Email to " + user.getEmail() + ": " + message);
    }
}

// SMSNotification Class
class SMSNotification extends Notification {

    public SMSNotification(User user, String message) {
        super(user, message);
    }

    @Override
    public void send() {
        System.out.println("Sending SMS to " + user.getPhoneNumber() + ": " + message);
    }
}

// PushNotification Class
class PushNotification extends Notification {

    public PushNotification(User user, String message) {
        super(user, message);
    }

    @Override
    public void send() {
        System.out.println("Sending Push Notification to User " + user.userId + ": " + message);
    }
}

// NotificationManager Class
class NotificationManager {

    public void sendNotification(User user, String message) {
        NotificationPreference preference = user.getNotificationPreference();

        if (preference.isEnabled(NotificationType.EMAIL)) {
            Notification emailNotification = new EmailNotification(user, message);
            emailNotification.send();
        }

        if (preference.isEnabled(NotificationType.SMS)) {
            Notification smsNotification = new SMSNotification(user, message);
            smsNotification.send();
        }

        if (preference.isEnabled(NotificationType.PUSH)) {
            Notification pushNotification = new PushNotification(user, message);
            pushNotification.send();
        }
    }
}

// NotificationService Class (Main Entry Point)
public class NotificationService {
    public static void main(String[] args) {
        User user = new User("U001", "sanketauti01@gmail.com", "714-622-8315");

        // Setting user preferences
        user.getNotificationPreference().enableNotification(NotificationType.EMAIL);
        user.getNotificationPreference().enableNotification(NotificationType.SMS);

        NotificationManager manager = new NotificationManager();

        // Sending notification
        manager.sendNotification(user, "Your order has been shipped!");

        // Changing preferences
        user.getNotificationPreference().disableNotification(NotificationType.SMS);
        user.getNotificationPreference().enableNotification(NotificationType.PUSH);

        manager.sendNotification(user, "Your order has been delivered!");
    }
}
