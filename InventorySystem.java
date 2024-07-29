import java.io.*;
import java.util.*;

public class InventorySystem {
    private HashMap<Integer, InventoryItem> items;
    private Scanner scanner;
    private static final String FILE_NAME = "inventory.dat";
    private Stack<String> undoStack;

    public InventorySystem() {
        items = new HashMap<>();
        scanner = new Scanner(System.in);
        undoStack = new Stack<>();
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
            validateQuantity(quantity);
            System.out.println("Enter Price (in Rupees):");
            double price = scanner.nextDouble();
            validatePrice(price);
            items.put(id, new InventoryItem(id, name, quantity, price));
            System.out.println("Item added successfully.");
            saveItems();
            undoStack.push("ADD:" + id);
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
            validateQuantity(quantity);
            System.out.println("Enter new Price (in Rupees):");
            double price = scanner.nextDouble();
            validatePrice(price);
            InventoryItem item = items.get(id);
            item.setQuantity(quantity);
            item.setPrice(price);
            System.out.println("Item updated successfully.");
            saveItems();
            undoStack.push("UPDATE:" + id);
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
            undoStack.push("REMOVE:" + id);
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
        System.out.println("1. Display Items Sorted by ID");
        System.out.println("2. Display Items Sorted by Name");
        System.out.println("3. Display Items Sorted by Quantity");
        System.out.println("4. Display Items Sorted by Price");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        List<InventoryItem> itemList = new ArrayList<>(items.values());

        switch (choice) {
            case 1:
                itemList.sort(Comparator.comparingInt(InventoryItem::getId));
                break;
            case 2:
                itemList.sort(Comparator.comparing(InventoryItem::getName));
                break;
            case 3:
                itemList.sort(Comparator.comparingInt(InventoryItem::getQuantity));
                break;
            case 4:
                itemList.sort(Comparator.comparingDouble(InventoryItem::getPrice));
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        for (InventoryItem item : itemList) {
            System.out.println(item);
        }
    }

    public void searchItem() {
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                searchById();
                break;
            case 2:
                searchByName();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void searchById() {
        System.out.println("Enter Item ID to search:");
        int id = scanner.nextInt();
        InventoryItem item = items.get(id);
        if (item != null) {
            System.out.println(item);
        } else {
            System.out.println("Item with ID " + id + " not found.");
        }
    }

    private void searchByName() {
        System.out.println("Enter Item Name to search:");
        String name = scanner.nextLine().toLowerCase();
        boolean found = false;
        for (InventoryItem item : items.values()) {
            if (item.getName().toLowerCase().contains(name)) {
                System.out.println(item);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No items found with name containing " + name + ".");
        }
    }

    public void generateReports() {
        System.out.println("1. Low Stock Report");
        System.out.println("2. Value of Inventory");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                generateLowStockReport();
                break;
            case 2:
                calculateInventoryValue();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void generateLowStockReport() {
        System.out.println("Enter quantity threshold:");
        int threshold = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Items with quantity below " + threshold + ":");
        boolean found = false;
        for (InventoryItem item : items.values()) {
            if (item.getQuantity() < threshold) {
                System.out.println(item);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No items below the threshold.");
        }
    }

    private void calculateInventoryValue() {
        double totalValue = 0;
        for (InventoryItem item : items.values()) {
            totalValue += item.getPrice() * item.getQuantity();
        }
        System.out.println("Total value of inventory: " + totalValue + "/-");
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
    }

    private void validatePrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
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

    private void undoLastOperation() {
        if (undoStack.isEmpty()) {
            System.out.println("No operations to undo.");
            return;
        }
        String lastOperation = undoStack.pop();
        String[] parts = lastOperation.split(":");
        String action = parts[0];
        int id = Integer.parseInt(parts[1]);

        switch (action) {
            case "ADD":
                items.remove(id);
                System.out.println("Undo Add Operation. Item removed.");
                break;
            case "UPDATE":
                System.out.println("Undo Update Operation: Previous values not supported.");
                break;
            case "REMOVE":
                System.out.println("Undo Remove Operation: Restore not supported fully.");
                break;
            default:
                System.out.println("Unknown action for undo.");
        }
        saveItems();
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
            System.out.println("6. Generate Reports");
            System.out.println("7. Undo Last Operation");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
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
                        system.generateReports();
                        break;
                    case 7:
                        system.undoLastOperation();
                        break;
                    case 8:
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
        } while (choice != 8);
        scanner.close();
    }
}
