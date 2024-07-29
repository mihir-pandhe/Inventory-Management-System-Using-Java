import java.util.ArrayList;
import java.util.Scanner;

public class InventorySystem {
    private ArrayList<InventoryItem> items;
    private Scanner scanner;

    public InventorySystem() {
        items = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void addItem() {
        System.out.println("Enter Item ID:");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter Item Name:");
        String name = scanner.nextLine();
        System.out.println("Enter Quantity:");
        int quantity = scanner.nextInt();
        System.out.println("Enter Price (in Rupees):");
        double price = scanner.nextDouble();
        items.add(new InventoryItem(id, name, quantity, price));
        System.out.println("Item added successfully.");
    }

    public void displayItems() {
        if (items.isEmpty()) {
            System.out.println("No items in inventory.");
            return;
        }
        for (InventoryItem item : items) {
            System.out.println(item);
        }
    }

    public static void main(String[] args) {
        InventorySystem system = new InventorySystem();
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("1. Add Item");
            System.out.println("2. Display Items");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    system.addItem();
                    break;
                case 2:
                    system.displayItems();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 3);
        scanner.close();
    }
}
