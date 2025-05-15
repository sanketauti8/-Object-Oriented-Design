import java.util.*;

// Person class representing common attributes of a person
class Person {
    private String name;
    private String contactInfo;

    public Person(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }
}

// Customer class representing restaurant customers
class Customer extends Person {
    public Customer(String name, String contactInfo) {
        super(name, contactInfo);
    }

    public Order createOrder(Menu menu) {
        // Customer selects items from the menu
        Scanner scanner = new Scanner(System.in);
        Order order = new Order(101);  // Create order with ID 101

        System.out.println("Please select items from the menu:");
        for (MenuSection section : menu.getSections()) {
            System.out.println("Section: " + section.getName());
            for (int i = 0; i < section.getItems().size(); i++) {
                System.out.println((i + 1) + ". " + section.getItems().get(i));
            }
            System.out.print("Enter the number of the item you want to order: ");
            int itemNumber = scanner.nextInt();
            MenuItem item = section.getItems().get(itemNumber - 1);
            order.addItem(item);
        }
        return order;
    }

    public void makeReservation(Reservation reservation) {
        System.out.println(getName() + " made a reservation.");
    }
}

// Employee class representing restaurant employees
class Employee extends Person {
    private String role;

    public Employee(String name, String contactInfo, String role) {
        super(name, contactInfo);
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}

// Waiter class representing a specific type of employee (Waiter)
class Waiter extends Employee {
    public Waiter(String name, String contactInfo) {
        super(name, contactInfo, "Waiter");
    }

    public void takeOrder(Customer customer, Order order) {
        // Waiter takes the order from the customer
       
    }

    public void serveOrder(Order order, Table table) {
        // Waiter serves the order to the table
        System.out.println(getName() + " is serving order " + order + " to table " + table);
    }
}

// Chef class representing a specific type of employee (Chef)
class Chef extends Employee {
    public Chef(String name, String contactInfo) {
        super(name, contactInfo, "Chef");
    }

    public void prepareOrder(Order order) {
        // Chef prepares the order
        System.out.println(getName() + " is preparing the order: " + order);
    }
}

// Receptionist class representing a specific type of employee (Receptionist)
class Receptionist extends Employee {
    public Receptionist(String name, String contactInfo) {
        super(name, contactInfo, "Receptionist");
    }

    public void searchTable() {
        System.out.println(getName() + " is searching for available tables...");
    }

    public void makeReservation(Reservation reservation) {
        System.out.println(getName() + " made a reservation: " + reservation);
    }
}

// Restaurant class representing a restaurant entity
class Restaurant {
    private String name;
    private boolean isActive;
    private List<Employee> employees;
    private List<Branch> branches;

    public Restaurant(String name) {
        this.name = name;
        this.isActive = true;
        this.employees = new ArrayList<>();
        this.branches = new ArrayList<>();
    }

    public void addBranch(Branch branch) {
        branches.add(branch);
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }
}

// Branch class representing a branch of a restaurant
class Branch {
    private String location;
    private List<Employee> employees;
    private Menu menu;

    public Branch(String location) {
        this.location = location;
        this.employees = new ArrayList<>();
        this.menu = new Menu();
    }

    public Menu getMenu() {
        return menu;
    }
}

// Menu class containing menu sections and items
class Menu {
    private List<MenuSection> sections;

    public Menu() {
        this.sections = new ArrayList<>();
    }

    public void addSection(MenuSection section) {
        sections.add(section);
    }

    public List<MenuSection> getSections() {
        return sections;
    }
}

// Menu Section containing multiple menu items
class MenuSection {
    private String name;
    private List<MenuItem> items;

    public MenuSection(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }
}

// Menu Item representing individual food items
class MenuItem {
    private String name;
    private double price;

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " ($" + price + ")";
    }
}

// Table class representing restaurant tables
class Table {
    private int tableId;
    private int maxCapacity;

    public Table(int tableId, int maxCapacity) {
        this.tableId = tableId;
        this.maxCapacity = maxCapacity;
    }

    @Override
    public String toString() {
        return "Table " + tableId + " (Capacity: " + maxCapacity + ")";
    }
}

// Order class representing an order placed by customers
class Order {
    private int orderId;
    private List<MenuItem> menuItems;

    public Order(int orderId) {
        this.orderId = orderId;
        this.menuItems = new ArrayList<>();
    }

    public void addItem(MenuItem item) {
        menuItems.add(item);
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId + ", Items: " + menuItems;
    }
}

// Reservation class handling table reservations
class Reservation {
    private int reservationId;
    private Table table;
    private String customerName;
    private Date reservationTime;

    public Reservation(int reservationId, Table table, String customerName, Date reservationTime) {
        this.reservationId = reservationId;
        this.table = table;
        this.customerName = customerName;
        this.reservationTime = reservationTime;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId + ", Table: " + table + ", Customer: " + customerName + ", Time: " + reservationTime;
    }
}

// Main class to test the system
public class RestaurantManagementSystem {
    public static void main(String[] args) {
        // Creating a restaurant
        Restaurant restaurant = new Restaurant("Gourmet Delight");

        // Adding a branch
        Branch branch1 = new Branch("Downtown");
        restaurant.addBranch(branch1);

        // Creating menu and adding items
        Menu menu = branch1.getMenu();
        MenuSection appetizers = new MenuSection("Appetizers");
        appetizers.addItem(new MenuItem("Spring Rolls", 5.99));
        menu.addSection(appetizers);

        // Creating a customer and making a reservation
        Customer customer = new Customer("John Doe", "123-456-7890");
        Table table = new Table(1, 4);
        Reservation reservation = new Reservation(1001, table, customer.getName(), new Date());
        customer.makeReservation(reservation);

        // Creating a waiter and taking an order
        Waiter waiter = new Waiter("Alice", "987-654-3210");
        Order order = new Order(101);
        order.addItem(new MenuItem("Spring Rolls", 5.99));
        waiter.takeOrder(customer, order);  // Waiter takes order from customer

        // Creating an order
        

        // Waiter serving the order
        waiter.serveOrder(order, table);

        // Sending notification (for simplicity, we assume notification is sent here)
        System.out.println("Sending notification: Your order is being prepared.");
    }
}
