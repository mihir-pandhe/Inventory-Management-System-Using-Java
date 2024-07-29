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

    public void updateItem() {
        System.out.println("Enter Item ID to update:");
        int id = scanner.nextInt();
        scanner.nextLine();
        for (InventoryItem item : items) {
            if (item.getId() == id) {
                System.out.println("Enter new Quantity:");
                int quantity = scanner.nextInt();
                System.out.println("Enter new Price (in Rupees):");
                double price = scanner.nextDouble();
                item.setQuantity(quantity);
                item.setPrice(price);
                System.out.println("Item updated successfully.");
                return;
            }
        }
        System.out.println("Item with ID " + id + " not found.");
    }

    public void removeItem() {
        System.out.println("Enter Item ID to remove:");
        int id = scanner.nextInt();
        for (InventoryItem item : items) {
            if (item.getId() == id) {
                items.remove(item);
                System.out.println("Item removed successfully.");
                return;
            }
        }
        System.out.println("Item with ID " + id + " not found.");
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
            System.out.println("2. Update Item");
            System.out.println("3. Remove Item");
            System.out.println("4. Display Items");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    system.addItem();
                    break;
                case 2:
                    system.updateItem();
                    break;
                case 3:
                    system.removeItem();
                    break;
                case 4:
                    system.displayItems();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 5);
        scanner.close();
    }
}
