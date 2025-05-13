import java.util.*;

// ATM class representing the ATM machine
class ATM {
    private String atmID;
    private String location;
    private CashDispenser cashDispenser;
    private CardReader cardReader;
    private Screen screen;
    private Keypad keypad;
    private Printer printer;
    private DepositSlot depositSlot;
    private Bank bank;

    public ATM(String atmID, String location, Bank bank) {
        this.atmID = atmID;
        this.location = location;
        this.bank = bank;
        this.cashDispenser = new CashDispenser();
        this.cardReader = new CardReader();
        this.screen = new Screen();
        this.keypad = new Keypad();
        this.printer = new Printer();
        this.depositSlot = new DepositSlot();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        screen.displayMessage("Welcome to ATM!");

        // Simulate card reading
        Card card = cardReader.readCard();
        screen.displayMessage("Card read successfully: " + card.getCardNumber());

        // Retrieve account from bank
        Account account = bank.getAccount(card.getCardNumber());
        if (account == null) {
            screen.displayMessage("Invalid card. No account found.");
            return;
        }

        while (true) {
            screen.displayMessage("\nSelect an option:\n1. Balance Inquiry\n2. Withdraw\n3. Deposit\n4. Exit");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    new BalanceInquiry(account).execute();
                    break;
                case 2:
                    screen.displayMessage("Enter withdrawal amount: ");
                    double withdrawAmount = scanner.nextDouble();
                    new Withdrawal(account, withdrawAmount, cashDispenser).execute();
                    break;
                case 3:
                    screen.displayMessage("Enter deposit amount: ");
                    double depositAmount = scanner.nextDouble();
                    new Deposit(account, depositAmount, depositSlot).execute();
                    break;
                case 4:
                    screen.displayMessage("Thank you for using the ATM!");
                    return;
                default:
                    screen.displayMessage("Invalid option. Please try again.");
            }
        }
    }
}

// Card Reader class to handle ATM card input
class CardReader {
    public Card readCard() {
        return new Card("1234-5678-9876-5432"); // Simulated card reading
    }
}

// Cash Dispenser class to dispense cash
class CashDispenser {
    public void dispenseCash(double amount) {
        System.out.println("Dispensing cash: $" + amount);
    }
}

// Keypad class to take user input
class Keypad {
    public String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}

// Screen class to display messages to users
class Screen {
    public void displayMessage(String message) {
        System.out.println(message);
    }
}

// Printer class to print receipts
class Printer {
    public void printReceipt(String details) {
        System.out.println("Printing receipt: " + details);
    }
}

// Deposit Slot class to accept deposits
class DepositSlot {
    public void deposit(double amount) {
        System.out.println("Deposited: $" + amount);
    }
}

// Bank class to manage customer accounts
class Bank {
    private Map<String, Account> accounts = new HashMap<>();

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
}

// Abstract Account class representing bank accounts
abstract class Account {
    protected String accountNumber;
    protected double balance;

    public Account(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

// Checking Account class
class CheckingAccount extends Account {
    public CheckingAccount(String accountNumber, double balance) {
        super(accountNumber, balance);
    }
}

// Saving Account class
class SavingAccount extends Account {
    public SavingAccount(String accountNumber, double balance) {
        super(accountNumber, balance);
    }
}

// Card class representing an ATM card
class Card {
    private String cardNumber;

    public Card(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }
}

// Abstract Transaction class representing ATM transactions
abstract class Transaction {
    protected Account account;

    public Transaction(Account account) {
        this.account = account;
    }

    abstract void execute();
}

// Balance Inquiry transaction
class BalanceInquiry extends Transaction {
    public BalanceInquiry(Account account) {
        super(account);
    }

    @Override
    void execute() {
        System.out.println("Your balance: $" + account.getBalance());
    }
}

// Withdrawal transaction
class Withdrawal extends Transaction {
    private double amount;
    private CashDispenser cashDispenser;

    public Withdrawal(Account account, double amount, CashDispenser cashDispenser) {
        super(account);
        this.amount = amount;
        this.cashDispenser = cashDispenser;
    }

    @Override
    void execute() {
        if (account.withdraw(amount)) {
            cashDispenser.dispenseCash(amount);
            System.out.println("Withdrawal successful.");
        } else {
            System.out.println("Insufficient funds.");
        }
    }
}

// Deposit transaction
class Deposit extends Transaction {
    private double amount;
    private DepositSlot depositSlot;

    public Deposit(Account account, double amount, DepositSlot depositSlot) {
        super(account);
        this.amount = amount;
        this.depositSlot = depositSlot;
    }

    @Override
    void execute() {
        account.deposit(amount);
        depositSlot.deposit(amount);
        System.out.println("Deposit successful.");
    }
}

// Main class to run the ATM system
public class Main {
    public static void main(String[] args) {
        // Initialize bank and accounts
        Bank bank = new Bank();
        bank.addAccount(new CheckingAccount("1234-5678-9876-5432", 1000.0));
        bank.addAccount(new SavingAccount("9876-5432-1234-5678", 5000.0));

        // Initialize ATM
        ATM atm = new ATM("ATM001", "Downtown", bank);
        atm.start();
    }
}
