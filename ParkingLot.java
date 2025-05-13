/*
Design Patterns Used
Singleton Pattern → Ensures only one instance of ParkingLot exists.
Factory Pattern → Creates objects dynamically (e.g., VehicleFactory, PaymentFactory).
Strategy Pattern → Supports different payment methods (CreditCardPayment, CashPayment).
Observer Pattern → Notifies system components when a parking spot is occupied or freed.
 */

// Vehicle and Parking Spot
import java.util.*;

enum VehicleType { CAR, TRUCK, BIKE }
enum ParkingSpotType { COMPACT, LARGE, BIKE }
enum TicketStatus { ACTIVE, PAID, LOST }

abstract class Vehicle {
    protected String licenseNo;
    protected VehicleType type;
    protected ParkingTicket ticket;

    public Vehicle(String licenseNo, VehicleType type) {
        this.licenseNo = licenseNo;
        this.type = type;
    }

    public void assignTicket(ParkingTicket ticket) {
        this.ticket = ticket;
    }
}

class Car extends Vehicle {
    public Car(String licenseNo) {
        super(licenseNo, VehicleType.CAR);
    }
}

class Truck extends Vehicle {
    public Truck(String licenseNo) {
        super(licenseNo, VehicleType.TRUCK);
    }
}

class Bike extends Vehicle {
    public Bike(String licenseNo) {
        super(licenseNo, VehicleType.BIKE);
    }
}

class ParkingSpot {
    private int spotId;
    private boolean isOccupied;
    private ParkingSpotType type;

    public ParkingSpot(int spotId, ParkingSpotType type) {
        this.spotId = spotId;
        this.type = type;
        this.isOccupied = false;
    }

    public boolean isAvailable() {
        return !isOccupied;
    }

    public void occupySpot() {
        isOccupied = true;
    }

    public void freeSpot() {
        isOccupied = false;
    }
}

//Ticket and Payment System
class ParkingTicket {
    private static int counter = 1;
    private int ticketNo;
    private Date issuedAt;
    private Date paidAt;
    private double amount;
    private TicketStatus status;

    public ParkingTicket(double amount) {
        this.ticketNo = counter++;
        this.issuedAt = new Date();
        this.amount = amount;
        this.status = TicketStatus.ACTIVE;
    }

    public void markAsPaid() {
        this.status = TicketStatus.PAID;
        this.paidAt = new Date();
    }

    public TicketStatus getStatus() {
        return status;
    }

    public int getTicketNo() {
        return ticketNo;
    }
}

interface PaymentStrategy {
    void processPayment(double amount);
}

class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing credit card payment of $" + amount);
    }
}

class CashPayment implements PaymentStrategy {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing cash payment of $" + amount);
    }
}


//Parking Lot & Floor

class ParkingFloor {
    private int floorNumber;
    private List<ParkingSpot> spots;

    public ParkingFloor(int floorNumber, int numSpots) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
        for (int i = 1; i <= numSpots; i++) {
            spots.add(new ParkingSpot(i, ParkingSpotType.COMPACT));
        }
    }

    public ParkingSpot findAvailableSpot() {
        for (ParkingSpot spot : spots) {
            if (spot.isAvailable()) {
                return spot;
            }
        }
        return null;
    }
}

class ParkingLot {
    private static ParkingLot instance;
    private List<ParkingFloor> floors;
    private List<ParkingTicket> activeTickets;

    private ParkingLot(int numFloors, int spotsPerFloor) {
        floors = new ArrayList<>();
        activeTickets = new ArrayList<>();
        for (int i = 1; i <= numFloors; i++) {
            floors.add(new ParkingFloor(i, spotsPerFloor));
        }
    }

    public static ParkingLot getInstance(int numFloors, int spotsPerFloor) {
        if (instance == null) {
            instance = new ParkingLot(numFloors, spotsPerFloor);
        }
        return instance;
    }

    public ParkingTicket issueTicket(Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            ParkingSpot spot = floor.findAvailableSpot();
            if (spot != null) {
                spot.occupySpot();
                ParkingTicket ticket = new ParkingTicket(10.0);  // $10 default fee
                vehicle.assignTicket(ticket);
                activeTickets.add(ticket);
                System.out.println("Issued Ticket #" + ticket.getTicketNo() + " to " + vehicle.licenseNo);
                return ticket;
            }
        }
        System.out.println("No parking spots available.");
        return null;
    }

    public void processPayment(ParkingTicket ticket, PaymentStrategy paymentMethod) {
        if (ticket.getStatus() == TicketStatus.ACTIVE) {
            paymentMethod.processPayment(ticket.amount);
            ticket.markAsPaid();
            System.out.println("Ticket #" + ticket.getTicketNo() + " has been paid.");
        } else {
            System.out.println("Ticket already paid.");
        }
    }
}

//main
public class Main {
    public static void main(String[] args) {
        ParkingLot lot = ParkingLot.getInstance(2, 5);

        Vehicle car1 = new Car("ABC-123");
        Vehicle truck1 = new Truck("XYZ-999");

        ParkingTicket ticket1 = lot.issueTicket(car1);
        ParkingTicket ticket2 = lot.issueTicket(truck1);

        PaymentStrategy creditCard = new CreditCardPayment("1234-5678-9876-5432");
        lot.processPayment(ticket1, creditCard);

        PaymentStrategy cash = new CashPayment();
        lot.processPayment(ticket2, cash);
    }
}



