//pizza
https://medium.com/@prashant558908/solving-top-10-low-level-design-lld-interview-questions-in-2024-302b6177c869
/*
 
base pizza-(margarita, cheese,panner)
toppping-(cheese,chicken,bacon)

 */
enum PIZZA_SIZE {SMALL, MEDIUM, LARGE};

abstract class BasePizza {
    PIZZA_SIZE size;
    int price;

    public BasePizza(PIZZA_SIZE size) {
        this.size = size;
    }
    abstract int getPrice();
}

class CheesePizza extends BasePizza {
    public CheesePizza(PIZZA_SIZE size) {
        super(size);  // Call parent constructor
    }
    int getPrice() {
        if (size == PIZZA_SIZE.SMALL) {
            return 10;
        } else if (size == PIZZA_SIZE.MEDIUM) {
            return 12;
        } else {
            return 15;
        }
    }
}

class PannerPizza extends BasePizza {
    public PannerPizza(PIZZA_SIZE size) {
        super(size);  // Call parent constructor
    }
    int getPrice() {
        if (size == PIZZA_SIZE.SMALL) {
            return 12;
        } else if (size == PIZZA_SIZE.MEDIUM) {
            return 14;
        } else {
            return 18;
        }
    }
}

abstract class Toppings extends BasePizza {
    BasePizza pizza;
    public Toppings(BasePizza pizza) {
        super(pizza.size);  // Call parent constructor with size
        this.pizza = pizza;
    }
    int getPrice() {
        return pizza.getPrice();
    }
}

class ExtraCheese extends Toppings {
    public ExtraCheese(BasePizza obj) {
        super(obj);
    }   
    int getPrice() {
        return pizza.getPrice() + 2;
    }
}

class ExtraChicken extends Toppings {
    public ExtraChicken(BasePizza obj) {
        super(obj);
    }
    int getPrice() {
        return pizza.getPrice() + 4;
    }
}

class Main {
    public static void main(String[] args) {
        BasePizza cheese = new PannerPizza(PIZZA_SIZE.LARGE);  // Fixed typo 'chesse' to 'cheese'
        Toppings t1 = new ExtraCheese(cheese);
        Toppings t2 = new ExtraChicken(t1);

        System.out.println(t2.getPrice());  // Added output to see result
    }
}




