import java.util.*;

enum TransactionType {
    BALANCE_INQUIRY, DEPOSIT_CASH, DEPOSIT_CHECK, WITHDRAW, TRANSFER
}

enum TransactionStatus {
    SUCCESS, FAILURE, BLOCKED, FULL, PARTIAL, NONE
}

enum CustomerStatus {
    ACTIVE, BLOCKED, BANNED, COMPROMISED, ARCHIVED, CLOSED, UNKNOWN
}

class Address {
    private String street, city, state, zipCode, country;

    public Address(String street, String city, String state, String zipCode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }
}

class Customer {
    private String name, email, phone;
    private Address address;
    private CustomerStatus status;
    private Card card;
    private Account account;

    public Customer(String name, Address address, String email, String phone, CustomerStatus status) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

    public void makeTransaction(Transaction transaction) {}
}

class Card {
    private String cardNumber, customerName, expiry;
    private int pin;

    public Card(String number, String customerName, String expiry, int pin) {
        this.cardNumber = number;
        this.customerName = customerName;
        this.expiry = expiry;
        this.pin = pin;
    }
}

class Account {
    protected String accountNumber;
    protected double totalBalance = 0.0;
    protected double availableBalance = 0.0;

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public double getAvailableBalance() {
        return availableBalance;
    }
}

class SavingAccount extends Account {
    private double withdrawLimit;

    public SavingAccount(String accountNumber, double withdrawLimit) {
        super(accountNumber);
        this.withdrawLimit = withdrawLimit;
    }
}

class CheckingAccount extends Account {
    private String debitCardNumber;

    public CheckingAccount(String accountNumber, String debitCardNumber) {
        super(accountNumber);
        this.debitCardNumber = debitCardNumber;
    }
}

class Bank {
    private String name, bankCode;
    private List<ATM> atms;

    public Bank(String name, String bankCode) {
        this.name = name;
        this.bankCode = bankCode;
        this.atms = new ArrayList<>();
    }
}

class ATM {
    private String atmId, location;
    private CashDispenser cashDispenser;
    private Keypad keypad;
    private Screen screen;
    private Printer printer;
    private DepositSlot cashDepositSlot, checkDepositSlot;

    public ATM(String id, String location) {
        this.atmId = id;
        this.location = location;
        this.cashDispenser = new CashDispenser();
        this.keypad = new Keypad();
        this.screen = new Screen();
        this.printer = new Printer();
        this.cashDepositSlot = new CashDepositSlot();
        this.checkDepositSlot = new CheckDepositSlot();
    }
}

class CashDispenser {
    private int totalFiveDollarBills, totalTwentyDollarBills;
}

class Keypad {
    public void getInput() {}
}

class Screen {
    public void showMessage(String message) {}
    public void getInput() {}
}

class Printer {
    public void printReceipt(Transaction transaction) {}
}

abstract class DepositSlot {
    protected double totalAmount;
    public double getTotalAmount() { return totalAmount; }
}

class CheckDepositSlot extends DepositSlot {
    public void getCheckAmount() {}
}

class CashDepositSlot extends DepositSlot {
    public void receiveDollarBill() {}
}

abstract class Transaction {
    protected String transactionId;
    protected Date creationDate;
    protected TransactionStatus status;

    public Transaction(String id, Date creationDate, TransactionStatus status) {
        this.transactionId = id;
        this.creationDate = creationDate;
        this.status = status;
    }
    
    public abstract void makeTransaction();
}

class BalanceInquiry extends Transaction {
    private String accountId;

    public BalanceInquiry(String id, Date creationDate, TransactionStatus status, String accountId) {
        super(id, creationDate, status);
        this.accountId = accountId;
    }
}

class Deposit extends Transaction {
    protected double amount;

    public Deposit(String id, Date creationDate, TransactionStatus status, double amount) {
        super(id, creationDate, status);
        this.amount = amount;
    }
}

class CheckDeposit extends Deposit {
    private String checkNumber, bankCode;

    public CheckDeposit(String id, Date creationDate, TransactionStatus status, double amount, String checkNumber, String bankCode) {
        super(id, creationDate, status, amount);
        this.checkNumber = checkNumber;
        this.bankCode = bankCode;
    }
}

class CashDeposit extends Deposit {
    private double cashDepositLimit;

    public CashDeposit(String id, Date creationDate, TransactionStatus status, double amount, double cashDepositLimit) {
        super(id, creationDate, status, amount);
        this.cashDepositLimit = cashDepositLimit;
    }
}

class Withdraw extends Transaction {
    private double amount;

    public Withdraw(String id, Date creationDate, TransactionStatus status, double amount) {
        super(id, creationDate, status);
        this.amount = amount;
    }
}

class Transfer extends Transaction {
    private String destinationAccountNumber;

    public Transfer(String id, Date creationDate, TransactionStatus status, String destinationAccountNumber) {
        super(id, creationDate, status);
        this.destinationAccountNumber = destinationAccountNumber;
    }
}


import java.util.Date;

public class Main {
    public static void main(String[] args) {
        // Create a bank
        Bank bank = new Bank("Global Bank", "GB001");

        // Create an ATM
        ATM atm = new ATM("ATM123", "Downtown");

        // Create a customer
        Address address = new Address("123 Main St", "Los Angeles", "CA", "90001", "USA");
        Customer customer = new Customer("Sanket Auti", address, "sanketauti01@gmail.com", "714-622-8315", CustomerStatus.ACTIVE);

        // Create a checking account
        CheckingAccount account = new CheckingAccount("CHK123456", "1234-5678-9012-3456");
        
        // Assign account to customer
        customer.makeTransaction(new BalanceInquiry("TXN001", new Date(), TransactionStatus.SUCCESS, "CHK123456"));

        // Create a card
        Card card = new Card("1234-5678-9876-5432", "Sanket Auti", "12/26", 1234);

        // Perform a deposit transaction
        Deposit cashDeposit = new CashDeposit("TXN002", new Date(), TransactionStatus.SUCCESS, 500.0, 2000.0);
        customer.makeTransaction(cashDeposit);

        // Perform a withdrawal transaction
        Withdraw withdraw = new Withdraw("TXN003", new Date(), TransactionStatus.SUCCESS, 100.0);
        customer.makeTransaction(withdraw);

        System.out.println("ATM transactions simulated successfully.");
    }
}
