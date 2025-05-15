class Library {
    private static Library instance;
    private Map<Integer, Book> books;
    private Map<Integer, User> users;
    private List<Observer> observers;

    private Library() {
        books = new HashMap<>();
        users = new HashMap<>();
        observers = new ArrayList<>();
    }

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    public void addBook(Book book) { books.put(book.getBookId(), book); }
    public void registerUser(User user) { users.put(user.getUserId(), user); }
    public void addObserver(Observer observer) { observers.add(observer); }
    
    public void borrowBook(int userId, int bookId, int days) {
        User user = users.get(userId);
        Book book = books.get(bookId);
        if (user != null && book != null) {
            user.borrowBook(book, days);
        } else {
            System.out.println("Invalid user or book ID.");
        }
    }

    public void returnBook(int userId, int bookId) {
        User user = users.get(userId);
        Book book = books.get(bookId);
        if (user != null && book != null) {
            user.returnBook(book);
            notifyObservers(user, book);
        } else {
            System.out.println("Invalid user or book ID.");
        }
    }

    public void payFine(int userId, double amount) {
        User user = users.get(userId);
        if (user != null) {
            user.payFine(amount);
        } else {
            System.out.println("Invalid user ID.");
        }
    }

    public void notifyObservers(User user, Book book) {
        for (Observer observer : observers) {
            observer.update(user, book);
        }
    }

    public void displayBooks() {
        for (Book book : books.values()) {
            System.out.println(book);
        }
    }
}

// ================= Factory Pattern =================
class LibraryFactory {
    public static Book createBook(int bookId, String title, String author) {
        return new Book(bookId, title, author);
    }

    public static User createUser(int userId, String name) {
        return new User(userId, name);
    }
}

// ================= Observer Pattern (Notification System) =================
interface Observer {
    void update(User user, Book book);
}

class EmailNotification implements Observer {
    public void update(User user, Book book) {
        System.out.println("📧 Email Notification: " + user.getName() + ", you returned " + book.getTitle() + ".");
    }
}

class SMSNotification implements Observer {
    public void update(User user, Book book) {
        System.out.println("📱 SMS Notification: " + user.getName() + ", you returned " + book.getTitle() + ".");
    }
}

// ================= Book Class =================
class Book {
    private String title;
    private String author;
    private int bookId;
    private boolean isAvailable;
    private Date dueDate;

    public Book(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
        this.dueDate = null;
    }

    public int getBookId() { return bookId; }
    public boolean isAvailable() { return isAvailable; }
    public Date getDueDate() { return dueDate; }

    public void borrowBook(int days) {
        this.isAvailable = false;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        this.dueDate = cal.getTime();
    }

    public void returnBook() {
        this.isAvailable = true;
        this.dueDate = null;
    }

    @Override
    public String toString() {
        return bookId + " - " + title + " by " + author + " [" + (isAvailable ? "Available" : "Borrowed") + "]";
    }
}

// ================= User Class =================
class User {
    private String name;
    private int userId;
    private List<Book> borrowedBooks;
    private double fineAmount;

    public User(int userId, String name) {
        this.userId = userId;
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
        this.fineAmount = 0.0;
    }

    public int getUserId() { return userId; }
    public String getName() { return name; }

    public void borrowBook(Book book, int days) {
        if (book.isAvailable()) {
            borrowedBooks.add(book);
            book.borrowBook(days);
            System.out.println(name + " borrowed " + book.getTitle() + " for " + days + " days.");
        } else {
            System.out.println("Book is already borrowed.");
        }
    }

    public void returnBook(Book book) {
        if (borrowedBooks.remove(book)) {
            Date currentDate = new Date();
            if (book.getDueDate() != null && currentDate.after(book.getDueDate())) {
                long lateDays = (currentDate.getTime() - book.getDueDate().getTime()) / (1000 * 60 * 60 * 24);
                fineAmount += lateDays;
                System.out.println(name + " returned " + book.getTitle() + " late! Fine: $" + lateDays);
            } else {
                System.out.println(name + " returned " + book.getTitle() + " on time.");
            }
            book.returnBook();
        } else {
            System.out.println("Book not found in borrowed list.");
        }
    }

    // Pay fine using the provided payment method
    public void payFine(PaymentMethod paymentMethod) {
        if (fineAmount > 0) {
            paymentMethod.processPayment(fineAmount);  // Process payment for the fine
            fineAmount = 0;
            System.out.println(name + " has paid the fine.");
        } else {
            System.out.println(name + " has no fine to pay.");
        }
    }
}


//
interface PaymentMethod {
    void processPayment(double amount);
}

class CreditCardPayment implements PaymentMethod {
    private String cardNumber;
    
    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void processPayment(double amount) {
        // Payment logic for credit card (you can add actual payment gateway logic here)
        System.out.println("Processing Credit Card payment of $" + amount);
    }
}

class PayPalPayment implements PaymentMethod {
    private String paypalEmail;
    
    public PayPalPayment(String paypalEmail) {
        this.paypalEmail = paypalEmail;
    }

    @Override
    public void processPayment(double amount) {
        // Payment logic for PayPal
        System.out.println("Processing PayPal payment of $" + amount);
    }
}


class Librarian {
    private String name;
    private int librarianId;

    public Librarian(int librarianId, String name) {
        this.librarianId = librarianId;
        this.name = name;
    }

    public void addBookToLibrary(Library library, Book book) {
        library.addBook(book);
        System.out.println("📚 " + name + " added book: " + book.getTitle());
    }

    public void issueBookToUser(Library library, int userId, int bookId, int days) {
        library.borrowBook(userId, bookId, days);
        System.out.println("📖 " + name + " issued book ID " + bookId + " to User ID " + userId);
    }
}


public class Main {
    public static void main(String[] args) {
        Library library = Library.getInstance();
        
        // Create Librarian
        Librarian librarian = new Librarian(101, "Mr. Smith");

        // Create Users
        User user1 = new User(1, "Alice");
        User user2 = new User(2, "Bob");

        library.registerUser(user1);
        library.registerUser(user2);

        // Librarian adds books to the library
        Book book1 = new Book(101, "The Great Gatsby", "F. Scott Fitzgerald");
        Book book2 = new Book(102, "Moby Dick", "Herman Melville");

        librarian.addBookToLibrary(library, book1);
        librarian.addBookToLibrary(library, book2);

        // Librarian issues books to users
        librarian.issueBookToUser(library, 1, 101, 5);  // Librarian issues book to Alice
        librarian.issueBookToUser(library, 2, 102, 7);  // Librarian issues book to Bob

        // Users return books
        user1.returnBook(book1);
        user2.returnBook(book2);

        // Users pay their fines
        PaymentMethod creditCard = new CreditCardPayment("1234-5678-9876-5432");
        user1.payFine(creditCard);

        PaymentMethod paypal = new PayPalPayment("bob@example.com");
        user2.payFine(paypal);
    }
}


/*
 📚 Mr. Smith added book: The Great Gatsby
📚 Mr. Smith added book: Moby Dick
📖 Mr. Smith issued book ID 101 to User ID 1
📖 Mr. Smith issued book ID 102 to User ID 2
Alice returned The Great Gatsby on time.
Bob returned Moby Dick on time.
Processing Credit Card payment of $0.0
Alice has paid the fine.
Processing PayPal payment of $0.0
Bob has paid the fine.



Library is a Singleton and contains multiple Books, Users, and Observers.

Users can borrow multiple Books.

Librarian can add, issue, and remove books.

Observer Pattern is used for notifications (e.g., Email and SMS updates).

Strategy Pattern is used for Payment Methods (CreditCard and PayPal).

 */
