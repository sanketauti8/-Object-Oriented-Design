// Step 1: Create an Interface
interface Shape {
    void getShapeName();
}

// Step 2: Implement different shape classes
class Circle implements Shape {
    public void getShapeName() {
        System.out.println("Circle class");
    }
}

class Rectangle implements Shape {
    public void getShapeName() {
        System.out.println("Rectangle class");
    }
}

class Square implements Shape {
    public void getShapeName() {
        System.out.println("Square class");
    }
}

// Step 3: Create the Factory class
class ShapeFactory {
    // Factory Method to return appropriate shape object
    public Shape getObject(String type) {
        if (type == null) {
            return null;
        }
        if (type.equalsIgnoreCase("circle")) {
            return new Circle();
        } else if (type.equalsIgnoreCase("rectangle")) {
            return new Rectangle();
        } else if (type.equalsIgnoreCase("square")) {
            return new Square();
        }
        return null; // Return null if shape type is unknown
    }
}

// Step 4: Main class to test Factory Pattern
class Main {
    public static void main(String[] args) {
        ShapeFactory shapeFactory = new ShapeFactory();

        // Get Circle object
        Shape c1 = shapeFactory.getObject("circle");
        if (c1 != null) {
            c1.getShapeName();  // Outputs: Circle class
        }

        // Get Rectangle object
        Shape r1 = shapeFactory.getObject("rectangle");
        if (r1 != null) {
            r1.getShapeName();  // Outputs: Rectangle class
        }

        // Get Square object
        Shape s1 = shapeFactory.getObject("square");
        if (s1 != null) {
            s1.getShapeName();  // Outputs: Square class
        }
    }
}
