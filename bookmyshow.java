

enum SeatStatus{AVAILABLE,BOOKED}
enum BookigStatus{CONFIRMED,CANCELLED}

abstract class Account{
    String name;
    String email;
    String password;
    public Account(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
    abstract void login();
    abstract void logout();
}

class Admin extends Account{
    public Admin(){

    }

    public void addMovie(Movie movie){

    }
    public void addShow(Show show){

    }


    @Override
    void login() { System.out.println("Admin Logged In"); }

    @Override
    void logout() { System.out.println("Admin Logged Out"); }

}

class Customer extends Account{

    public Customer(){

    }
    public List<Movie>searchMovie(String title){
        //print(title);
        return new ArrayList<>();
    }
    Booking bookTicket(Show show,List<showSeat>seats,Payment paymentmethod){
        Booking booking=BookingService.getInstance().createBooking(this,show,seats);
        paymentmethod.pay(booking);
        NotificationService.getInstance().notifyUser(this, "Your booking is confirmed!");
        return booking;
    }
    @Override
    void login() { System.out.println("Customer Logged In"); }

    @Override
    void logout() { System.out.println("Customer Logged Out"); }

}

class Movie{
    String title,genre,langage;
    public movie(){

    }
}

class Cinema{
    String name,city;
    List<CinemaHall>halls;
}
class CinemaHall{
    int hallno;
    List<ShowSeat>seats;
}

class ShowSeat{
    int seatno;
    SeatStatus status;
    public ShowSeat(int seatNumber) {
        this.seatNumber = seatNumber;
        this.status = SeatStatus.AVAILABLE;
    }

    public void bookSeat() {
        this.status = SeatStatus.BOOKED;
    }
}
class show{
    Date showTime;
    CinemaHall hall;

}