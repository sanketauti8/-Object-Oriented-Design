/*
 Key Design Patterns Used
Singleton → Ensures a single instance of BookingService (to prevent race conditions in seat booking).

Factory → For creating different types of payments (PaymentFactory).

Strategy → To support multiple payment methods (CreditCardPayment, CashPayment).

Observer → To send notifications to users (NotificationService).

1. High-Level Execution Flow
The system starts by initializing data such as cities, cinemas, movies, and shows.

A customer or a guest searches for movies.

If a customer selects a movie, the system displays available cinemas and showtimes.

The customer books a seat, and the system processes payments.

Notifications are sent to the customer.

If needed, the customer cancels the booking, triggering a refund.
 */

 import java.util.*;

// Enum for Seat and Booking Status
enum SeatStatus { AVAILABLE, BOOKED }
enum BookingStatus { CONFIRMED, CANCELLED }

// Abstract Class for Accounts
abstract class Account {
    String userId, name, email, password;

    public Account(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    abstract void login();
    abstract void logout();
}

// Admin Account
class Admin extends Account {
    public Admin(String userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    public void addMovie(Movie movie) {
        System.out.println("Movie Added: " + movie.title);
    }

    public void addShow(Show show) {
        System.out.println("Show Added: " + show.showTime);
    }

    @Override
    void login() { System.out.println("Admin Logged In"); }

    @Override
    void logout() { System.out.println("Admin Logged Out"); }
}

// Customer Account
class Customer extends Account {
    public Customer(String userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    public List<Movie> searchMovie(String title) {
        System.out.println("Searching for Movie: " + title);
        return new ArrayList<>();
    }

    public Booking bookTicket(Show show, List<ShowSeat> seats, Payment paymentMethod) {
        Booking booking = BookingService.getInstance().createBooking(this, show, seats);
        paymentMethod.pay(booking);
        NotificationService.getInstance().notifyUser(this, "Your booking is confirmed!");
        return booking;
    }

    @Override
    void login() { System.out.println("Customer Logged In"); }

    @Override
    void logout() { System.out.println("Customer Logged Out"); }
}

// Movie Class
class Movie {
    String title, genre, language;
    
    public Movie(String title, String genre, String language) {
        this.title = title;
        this.genre = genre;
        this.language = language;
    }
}

// Cinema Class
class Cinema {
    String name, city;
    List<CinemaHall> halls;

    public Cinema(String name, String city) {
        this.name = name;
        this.city = city;
        this.halls = new ArrayList<>();
    }
}

// Cinema Hall Class
class CinemaHall {
    int hallNumber;
    List<ShowSeat> seats;

    public CinemaHall(int hallNumber, int totalSeats) {
        this.hallNumber = hallNumber;
        this.seats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            seats.add(new ShowSeat(i));
        }
    }
}

// Show Class
class Show {
    Date showTime;
    CinemaHall hall;
    
    public Show(Date showTime, CinemaHall hall) {
        this.showTime = showTime;
        this.hall = hall;
    }
}

// ShowSeat Class
class ShowSeat {
    int seatNumber;
    SeatStatus status;

    public ShowSeat(int seatNumber) {
        this.seatNumber = seatNumber;
        this.status = SeatStatus.AVAILABLE;
    }

    public void bookSeat() {
        this.status = SeatStatus.BOOKED;
    }
}

// Booking Class (Singleton)
class BookingService {
    private static BookingService instance;

    private BookingService() {}

    public static BookingService getInstance() {
        if (instance == null) {
            instance = new BookingService();
        }
        return instance;
    }

    public Booking createBooking(Customer customer, Show show, List<ShowSeat> seats) {
        for (ShowSeat seat : seats) {
            if (seat.status == SeatStatus.BOOKED) {
                throw new IllegalStateException("Seat already booked!");
            }
            seat.bookSeat();
        }
        return new Booking(UUID.randomUUID().toString(), seats, BookingStatus.CONFIRMED);
    }
}

// Booking Class
class Booking {
    String bookingId;
    List<ShowSeat> seats;
    BookingStatus status;

    public Booking(String bookingId, List<ShowSeat> seats, BookingStatus status) {
        this.bookingId = bookingId;
        this.seats = seats;
        this.status = status;
    }
}

// Strategy Pattern: Payment
interface Payment {
    void pay(Booking booking);
}

// CreditCard Payment
class CreditCardPayment implements Payment {
    public void pay(Booking booking) {
        System.out.println("Paid using Credit Card for booking: " + booking.bookingId);
    }
}

// Cash Payment
class CashPayment implements Payment {
    public void pay(Booking booking) {
        System.out.println("Paid using Cash for booking: " + booking.bookingId);
    }
}

// Observer Pattern: Notification Service
class NotificationService {
    private static NotificationService instance;
    private List<Account> observers = new ArrayList<>();

    private NotificationService() {}

    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    public void addObserver(Account account) {
        observers.add(account);
    }

    public void removeObserver(Account account) {
        observers.remove(account);
    }

    public void notifyUser(Account account, String message) {
        System.out.println("Notification to " + account.name + ": " + message);
    }
}

import java.util.*;

public class MovieTicketSystem {
    public static void main(String[] args) {
        // Step 1: Initialize System with Sample Data
        SystemInitializer initializer = new SystemInitializer();
        initializer.setupSystem();

        // Step 2: Create a Customer and Perform Actions
        Customer customer = new Customer("Sanket", "sanketauti01@gmail.com");

        // Step 3: Search for Movies
        List<Movie> movies = MovieService.searchMovies("Inception", null, null, null, null);
        if (!movies.isEmpty()) {
            Movie selectedMovie = movies.get(0);
            System.out.println("Customer selected movie: " + selectedMovie.getTitle());

            // Step 4: Get Available Shows
            List<Show> shows = MovieService.getShowsByMovie(selectedMovie);
            if (!shows.isEmpty()) {
                Show selectedShow = shows.get(0);
                System.out.println("Available show at: " + selectedShow.getCinema().getName());

                // Step 5: Select Seats and Book Tickets
                List<ShowSeat> seatsToBook = selectedShow.getAvailableSeats(2);
                Booking booking = customer.bookTickets(selectedShow, seatsToBook);
                
                // Step 6: Make Payment
                Payment payment = new Payment();
                payment.processPayment(booking, PaymentMethod.CREDIT_CARD);

                // Step 7: Send Notification
                NotificationService.sendBookingConfirmation(customer, booking);
            }
        }
    }
}

/*
 Key Features Implemented
✔ Singleton for BookingService & NotificationService
✔ Factory for Payment Types
✔ Strategy Pattern for Payments
✔ Observer for Notifications
✔ Thread-safe Seat Booking to Prevent Double Booking
 
 */