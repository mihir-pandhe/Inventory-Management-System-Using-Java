import java.io.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InventorySystem {
    private HashMap<Integer, InventoryItem> items;
    private Scanner scanner;
    private static final String FILE_NAME = "inventory.dat";

    public InventorySystem() {
        items = new HashMap<>();
        scanner = new Scanner(System.in);
        loadItems();
    }

    public void addItem() {
        try {
            System.out.println("Enter Item ID:");
            int id = scanner.nextInt();
            scanner.nextLine();
            if (items.containsKey(id)) {
                System.out.println("Item with this ID already exists.");
                return;
            }
            System.out.println("Enter Item Name:");
            String name = scanner.nextLine();
            System.out.println("Enter Quantity:");
            int quantity = scanner.nextInt();
            System.out.println("Enter Price (in Rupees):");
            double price = scanner.nextDouble();
            items.put(id, new InventoryItem(id, name, quantity, price));
            System.out.println("Item added successfully.");
            saveItems();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter the correct data type.");
            scanner.nextLine();
        }
    }

    public void updateItem() {
        try {
            System.out.println("Enter Item ID to update:");
            int id = scanner.nextInt();
            scanner.nextLine();
            if (!items.containsKey(id)) {
                System.out.println("Item with ID " + id + " not found.");
                return;
            }
            System.out.println("Enter new Quantity:");
            int quantity = scanner.nextInt();
            System.out.println("Enter new Price (in Rupees):");
            double price = scanner.nextDouble();
            InventoryItem item = items.get(id);
            item.setQuantity(quantity);
            item.setPrice(price);
            System.out.println("Item updated successfully.");
            saveItems();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter the correct data type.");
            scanner.nextLine();
        }
    }

    public void removeItem() {
        try {
            System.out.println("Enter Item ID to remove:");
            int id = scanner.nextInt();
            if (!items.containsKey(id)) {
                System.out.println("Item with ID " + id + " not found.");
                return;
            }
            items.remove(id);
            System.out.println("Item removed successfully.");
            saveItems();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter the correct data type.");
            scanner.nextLine();
        }
    }

    public void displayItems() {
        if (items.isEmpty()) {
            System.out.println("No items in inventory.");
            return;
        }
        for (InventoryItem item : items.values()) {
            System.out.println(item);
        }
    }

    public void searchItem() {
        System.out.println("Enter Item ID to search:");
        int id = scanner.nextInt();
        InventoryItem item = items.get(id);
        if (item != null) {
            System.out.println(item);
        } else {
            System.out.println("Item with ID " + id + " not found.");
        }
    }

    private void saveItems() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(items);
        } catch (IOException e) {
            System.out.println("Error saving inventory items: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadItems() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Object obj = ois.readObject();
            if (obj instanceof HashMap) {
                items = (HashMap<Integer, InventoryItem>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous inventory data found.");
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
            System.out.println("5. Search Item");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            try {
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
                        system.searchItem();
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice. Please enter a number.");
                scanner.nextLine();
                choice = 0;
            }
        } while (choice != 6);
        scanner.close();
    }
}
