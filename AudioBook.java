/*
User

Audiobook

Purchase

Review

Payment

Search

StoreDatabase (Singleton Class */

import java.util.*;

// Enum for audiobook genres
enum Genre {
    FICTION, NON_FICTION, MYSTERY, FANTASY, BIOGRAPHY, SCIENCE, HISTORY
}

// User Class
class User {
    private String userId;
    private String name;
    private String email;
    private List<Purchase> purchaseHistory;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.purchaseHistory = new ArrayList<>();
    }

    public void purchaseAudiobook(Audiobook audiobook, Payment paymentMethod) {
        if (paymentMethod.processPayment(audiobook.getPrice())) {
            Purchase purchase = new Purchase(this, audiobook);
            purchaseHistory.add(purchase);
            System.out.println("Purchase successful for audiobook: " + audiobook.getTitle());
        } else {
            System.out.println("Payment failed. Purchase unsuccessful.");
        }
    }

    public List<Purchase> getPurchaseHistory() {
        return purchaseHistory;
    }
}

// Audiobook Class
class Audiobook {
    private String title;
    private String author;
    private Genre genre;
    private double price;
    private List<Review> reviews;

    public Audiobook(String title, String author, Genre genre, double price) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.price = price;
        this.reviews = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Genre getGenre() {
        return genre;
    }

    public double getPrice() {
        return price;
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public List<Review> getReviews() {
        return reviews;
    }
}

// Purchase Class
class Purchase {
    private User user;
    private Audiobook audiobook;
    private Date purchaseDate;

    public Purchase(User user, Audiobook audiobook) {
        this.user = user;
        this.audiobook = audiobook;
        this.purchaseDate = new Date();
    }

    public Audiobook getAudiobook() {
        return audiobook;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }
}

// Review Class
class Review {
    private User user;
    private String comment;
    private int rating;

    public Review(User user, String comment, int rating) {
        this.user = user;
        this.comment = comment;
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }
}

// Payment Interface
interface Payment {
    boolean processPayment(double amount);
}

// CreditCard Payment Implementation
class CreditCardPayment implements Payment {
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;

    public CreditCardPayment(String cardNumber, String cardHolderName, String expiryDate) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing payment of $" + amount + " via Credit Card.");
        return true; // Assume payment is always successful
    }
}

// Store Database Singleton Class
class StoreDatabase {
    private static StoreDatabase instance;
    private final List<Audiobook> audiobooks;

    private StoreDatabase() {
        audiobooks = new ArrayList<>();
    }

    public static StoreDatabase getInstance() {
        if (instance == null) {
            instance = new StoreDatabase();
        }
        return instance;
    }

    public void addAudiobook(Audiobook audiobook) {
        audiobooks.add(audiobook);
    }

    public List<Audiobook> searchByTitle(String title) {
        List<Audiobook> result = new ArrayList<>();
        for (Audiobook book : audiobooks) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Audiobook> searchByAuthor(String author) {
        List<Audiobook> result = new ArrayList<>();
        for (Audiobook book : audiobooks) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Audiobook> searchByGenre(Genre genre) {
        List<Audiobook> result = new ArrayList<>();
        for (Audiobook book : audiobooks) {
            if (book.getGenre() == genre) {
                result.add(book);
            }
        }
        return result;
    }
}

// Main Class for Testing
public class Main {
    public static void main(String[] args) {
        StoreDatabase db = StoreDatabase.getInstance();
        
        Audiobook book1 = new Audiobook("The Mystery of the Night", "Arthur Conan Doyle", Genre.MYSTERY, 19.99);
        Audiobook book2 = new Audiobook("A Brief History of Time", "Stephen Hawking", Genre.SCIENCE, 24.99);
        Audiobook book3 = new Audiobook("Harry Potter", "J.K. Rowling", Genre.FANTASY, 29.99);

        db.addAudiobook(book1);
        db.addAudiobook(book2);
        db.addAudiobook(book3);

        User user = new User("U001", "Sanket", "sanketauti01@gmail.com");
        Payment payment = new CreditCardPayment("1234567890123456", "Sanket", "12/25");

        user.purchaseAudiobook(book1, payment);

        List<Audiobook> searchResults = db.searchByAuthor("J.K. Rowling");
        for (Audiobook book : searchResults) {
            System.out.println("Found Audiobook: " + book.getTitle());
        }
    }
}
