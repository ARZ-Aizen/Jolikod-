package first_project;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class SystemInterface {

    private Scanner scanner;
    
    // --- MENU ITEMS & PRICES (Converted from C arrays) ---
    private static final String[] BUCKET_SET = {"Chicken Bucket", "Chicken Bucket (Spicy)", "Porkchop Bucket",
                                                "Porkchop Bucket (Spicy)", "Whole Mini Bangus Bucket", "Tilapia Bucket"};
    private static final String[] BILAO_SET = {"Spaghetti Bilao", "Pancit Bato Bilao", "Palabok Bilao"};
    private static final String[] ALA_CARTE = {"Spaghetti", "Pancit Bato", "Palabok"};
    private static final String[] BURGER = {"Pork Burger", "Double Patty Pork Burger", "Overload Pork Burger",
                                            "Chicken Burger", "Double Patty Chicken Burger", "Overload Chicken Burger"};
    private static final String[] ALA_CARTE_RICE = {"Fried Chicken", "Fried Porkchop", "Fried Half Bangus", "Fried Tilapia"};
    private static final String[] DRINKS = {"Coke", "Gulaman", "Pineapple Juice"};
    private static final String[] DESSERT = {"Chocolate Sundae", "Strawberry Sundae", "Caramel Sundae",
                                             "Coke float", "Saging con yelo", "Halo-Halo"};
    
    // Total 31 items. Index 0 corresponds to item #1.
    private static final double[] PRICES = {
        400.00, 400.00, 450.00, 450.00, 380.00, 350.00,  // Bucket Set (1-6)
        300.00, 280.00, 280.00,                          // Bilao Set (7-9)
        80.00, 75.00, 75.00,                             // Ala Carte (10-12)
        65.00, 85.00, 105.00, 60.00, 80.00, 100.00,      // Burger (13-18)
        65.00, 80.00, 75.00, 60.00,                      // Ala Carte Rice (19-22)
        35.00, 25.00, 30.00,                             // Drinks (23-25)
        45.00, 45.00, 45.00, 50.00, 40.00, 60.00         // Dessert (26-31)
    };
    
    // Single array of all item names
    private static final String[] ALL_MENU_ITEMS;
    
    // Static block to initialize ALL_MENU_ITEMS once
    static {
        ALL_MENU_ITEMS = new String[31];
        int index = 0;
        for (String item : BUCKET_SET) ALL_MENU_ITEMS[index++] = item;
        for (String item : BILAO_SET) ALL_MENU_ITEMS[index++] = item;
        for (String item : ALA_CARTE) ALL_MENU_ITEMS[index++] = item;
        for (String item : BURGER) ALL_MENU_ITEMS[index++] = item;
        for (String item : ALA_CARTE_RICE) ALL_MENU_ITEMS[index++] = item;
        for (String item : DRINKS) ALL_MENU_ITEMS[index++] = item;
        for (String item : DESSERT) ALL_MENU_ITEMS[index++] = item;
    }
    // --- END MENU ITEMS ---


    public SystemInterface(Scanner sharedScanner) {
        this.scanner = sharedScanner;
    }

    
    public void runInterface() {
        
        // Loop control variable for the entire POS system
        boolean runPosSystem = true;

        do {
            // --- ORDER STORAGE (Resets for each customer) ---
            List<Integer> orderChoice = new ArrayList<>();   // Stores item index (1-31)
            List<Integer> orderQuantity = new ArrayList<>(); // Stores quantity
            int maxMenuOption = ALL_MENU_ITEMS.length + 1; // 31 items + 1 for Exit
            
            // Order details
            int orderType = 0;      // 1 = Dine-in, 2 = Takeout
            int tableNumber = 0;    // Table number if Dine-in
            String tableDisplay = ""; 

            // ================= 1. GET ORDER TYPE & TABLE =================
            OrderDetails details = getOrderDetails();
            if (details == null) { // User chose to exit before placing order
                runPosSystem = false;
                break; 
            }
            orderType = details.orderType;
            tableNumber = details.tableNumber;
            tableDisplay = details.tableDisplay;

            // ================= 2. DISPLAY MENU =================
            displayMenu(tableDisplay);
            
            // ================= 3. TAKE ORDER LOOP =================
            takeCustomerOrder(orderChoice, orderQuantity, maxMenuOption);

            if (orderChoice.isEmpty()) {
                System.out.println("\n🚫 No items ordered. Returning to main menu.");
                continue; // Skip payment and go to next customer/exit prompt
            }

            // ================= 4. PROCESS PAYMENT & RECEIPT =================
            processPaymentAndReceipt(orderChoice, orderQuantity, tableDisplay);

            // ================= 5. NEXT CUSTOMER / EXIT PROMPT =================
            runPosSystem = askForNextCustomer();

        } while (runPosSystem);
    }
    
    // --- HELPER CLASSES AND METHODS ---

    // Simple class to return multiple order details
    private static class OrderDetails {
        int orderType;
        int tableNumber;
        String tableDisplay;
        
        OrderDetails(int type, int number, String display) {
            this.orderType = type;
            this.tableNumber = number;
            this.tableDisplay = display;
        }
    }

    private OrderDetails getOrderDetails() {
        int orderType = 0;
        int tableNumber = 0;
        
        // ASK FOR ORDER TYPE (DINE-IN OR TAKEOUT)
        while (orderType != 1 && orderType != 2) {
            System.out.println("===================================================");
            System.out.println("            SELECT ORDER TYPE");
            System.out.println("===================================================");
            System.out.println("[1] Dine-in");
            System.out.println("[2] Takeout");
            System.out.println("[3] Return to Login Menu");
            System.out.println("===================================================");
            System.out.print("Choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("❌ Invalid input. Please enter a number.");
                scanner.next(); 
                continue;
            }
            orderType = scanner.nextInt();
            scanner.nextLine();
            
            if (orderType == 3) {
                return null; // Signal to exit POS
            } else if (orderType != 1 && orderType != 2) {
                System.out.println("❌ Invalid option. Please select 1, 2, or 3.");
            }
        }
        
        String tableDisplay;

        // IF DINE-IN, ASK FOR TABLE NUMBER
        if (orderType == 1) {
            while (tableNumber <= 0) {
                System.out.println("===================================================");
                System.out.print("Enter Table Number: ");
                if (!scanner.hasNextInt()) {
                    System.out.println("❌ Invalid input. Please enter a valid table number.");
                    scanner.next();
                } else {
                    tableNumber = scanner.nextInt();
                    scanner.nextLine();
                    if (tableNumber <= 0) {
                        System.out.println("❌ Table number must be greater than 0.");
                    }
                }
            }
            System.out.println("===================================================");
            System.out.printf("✅ Table #%d assigned.\n", tableNumber);
            tableDisplay = String.format("Table #%d", tableNumber);
        } else {
            // TAKEOUT
            System.out.println("===================================================");
            System.out.println("✅ Takeout order confirmed.");
            tableDisplay = "TAKEOUT";
        }
        
        return new OrderDetails(orderType, tableNumber, tableDisplay);
    }
    
    private void displayMenu(String tableDisplay) {
        System.out.println("\n[==================== JOLIKOD ====================]");
        System.out.println("[===================== MENU ======================]");
        System.out.printf("[================= %-15s =================]\n\n", tableDisplay);

        int number = 1;

        // BUCKET SET
        System.out.println("[=================★ Bucket Set ★=================]");
        for (String item : BUCKET_SET) {
            System.out.printf("[%2d] %-30s ₱%7.2f\n", number, item, PRICES[number - 1]);
            number++;
        }

        // BILAO SET
        System.out.println("\n[=================★ Bilao Set ★==================]");
        for (String item : BILAO_SET) {
            System.out.printf("[%2d] %-30s ₱%7.2f\n", number, item, PRICES[number - 1]);
            number++;
        }

        // ALA CARTE
        System.out.println("\n[=================★ Ala Carte ★==================]");
        for (String item : ALA_CARTE) {
            System.out.printf("[%2d] %-30s ₱%7.2f\n", number, item, PRICES[number - 1]);
            number++;
        }

        // BURGER
        System.out.println("\n[=================★ Burger ★====================]");
        for (String item : BURGER) {
            System.out.printf("[%2d] %-30s ₱%7.2f\n", number, item, PRICES[number - 1]);
            number++;
        }

        // ALA CARTE RICE
        System.out.println("\n[===============★ Ala Carte Rice ★===============]");
        for (String item : ALA_CARTE_RICE) {
            System.out.printf("[%2d] %-30s ₱%7.2f\n", number, item, PRICES[number - 1]);
            number++;
        }

        // DRINKS
        System.out.println("\n[=================★ Drinks ★====================]");
        for (String item : DRINKS) {
            System.out.printf("[%2d] %-30s ₱%7.2f\n", number, item, PRICES[number - 1]);
            number++;
        }

        // DESSERT
        System.out.println("\n[=================★ Dessert ★===================]");
        for (String item : DESSERT) {
            System.out.printf("[%2d] %-30s ₱%7.2f\n", number, item, PRICES[number - 1]);
            number++;
        }

        // EXIT option (to stop ordering and proceed to payment)
        System.out.println("\n[===============================================]");
        System.out.printf("[%2d] %-30s\n", number, "Proceed to Checkout (Finish Order)");
        System.out.println("[===============================================]");
    }
    
    private void takeCustomerOrder(List<Integer> orderChoice, List<Integer> orderQuantity, int maxMenuOption) {
        int loopOption;
        
        do {
            int choice = 0;
            int quantity = 0;
            
            // CHOOSE ORDER
            while (choice < 1 || choice > maxMenuOption) {
                System.out.println("\n===================================================");
                System.out.print("Choose your Order: ");
                if (!scanner.hasNextInt()) {
                    System.out.println("\n❌ Invalid input. Please enter a number.\n");
                    scanner.next();
                    choice = 0; 
                } else {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice < 1 || choice > maxMenuOption) {
                        System.out.println("\n❌ Invalid option. Please enter again.\n");
                    }
                }
            }
            
            // PROCEED TO CHECKOUT OPTION
            if (choice == maxMenuOption) {
                break; // Exit the order-taking loop
            }

            // QUANTITY
            while (quantity <= 0) {
                System.out.println("===================================================");
                System.out.print("Enter Quantity: ");
                if (!scanner.hasNextInt()) {
                    System.out.println("\n❌ Invalid input. Please enter a number.\n");
                    scanner.next();
                    quantity = 0;
                } else {
                    quantity = scanner.nextInt();
                    scanner.nextLine();
                    if (quantity <= 0) {
                        System.out.println("\n❌ Quantity must be greater than 0.\n");
                    }
                }
            }

            // Store the item index (choice is 1-based, we store it as is)
            orderChoice.add(choice);
            orderQuantity.add(quantity);
            
            System.out.println("===================================================");
            System.out.println("\n✅ Order Added! Item #" + choice);

            // ADD MORE ORDERS?
            loopOption = 0;
            while (loopOption != 1 && loopOption != 2) {
                System.out.println("\n===================================================");
                System.out.println("Do you want to select more orders?");
                System.out.print("[1] Yes \n[2] No\n===================================================\nChoice: ");
                
                if (!scanner.hasNextInt()) {
                    System.out.println("\n❌ Invalid input. Please enter 1 or 2.\n");
                    scanner.next();
                    loopOption = 0;
                } else {
                    loopOption = scanner.nextInt();
                    scanner.nextLine();
                    if (loopOption != 1 && loopOption != 2) {
                        System.out.println("\n❌ Invalid option.\n");
                    }
                }
            }
        } while (loopOption == 1);
    }
    
    private double calculateGrandTotal(List<Integer> orderChoice, List<Integer> orderQuantity) {
        double grandTotal = 0.0;
        for (int i = 0; i < orderChoice.size(); i++) {
            int itemIndex = orderChoice.get(i) - 1; // Convert 1-based choice to 0-based index
            int qty = orderQuantity.get(i);
            double unitPrice = PRICES[itemIndex];
            grandTotal += unitPrice * qty;
        }
        return grandTotal;
    }
    
    private void processPaymentAndReceipt(List<Integer> orderChoice, List<Integer> orderQuantity, String tableDisplay) {
        
        double grandTotal = calculateGrandTotal(orderChoice, orderQuantity);
        
        // ================= 1. ORDER SUMMARY =================
        System.out.println("\n===================================================");
        System.out.println("             📝 ORDER SUMMARY");
        System.out.printf("               %s\n", tableDisplay);
        System.out.println("===================================================");
        System.out.printf("%-25s %-8s %-8s %-8s\n", "Item", "Qty", "Price", "Total");
        System.out.println("---------------------------------------------------");

        for (int i = 0; i < orderChoice.size(); i++) {
            int itemIndex = orderChoice.get(i) - 1;
            int qty = orderQuantity.get(i);
            double unitPrice = PRICES[itemIndex];
            double totalPrice = unitPrice * qty;

            System.out.printf("%-25s %-8d ₱%6.2f ₱%6.2f\n", ALL_MENU_ITEMS[itemIndex], qty, unitPrice, totalPrice);
        }

        System.out.println("---------------------------------------------------");
        System.out.printf("%-25s %-8s %-7s ₱%6.2f\n", "GRAND TOTAL", "", "", grandTotal);
        System.out.println("===================================================");

        // ================= 2. PAYMENT =================
        double payment = 0.0;
        while (payment < grandTotal) {
            System.out.printf("\nEnter Payment Amount: ₱");
            if (!scanner.hasNextDouble()) {
                System.out.println("❌ Invalid input. Please enter a number.\n");
                scanner.next();
                payment = 0.0; 
            } else {
                payment = scanner.nextDouble();
                scanner.nextLine();
                if (payment < grandTotal) {
                    System.out.printf("❌ Insufficient payment. Please enter at least ₱%.2f\n", grandTotal);
                }
            }
        }

        double change = payment - grandTotal;

        // ================= 3. RECEIPT =================
        System.out.println("\n\n===================================================");
        System.out.println("             JOLIKOD RECEIPT");
        System.out.printf("               %s\n", tableDisplay);
        System.out.println("===================================================");
        System.out.printf("%-25s %-8s %-8s %-8s\n", "Item", "Qty", "Price", "Total");
        System.out.println("---------------------------------------------------");

        for (int i = 0; i < orderChoice.size(); i++) {
            int itemIndex = orderChoice.get(i) - 1;
            int qty = orderQuantity.get(i);
            double unitPrice = PRICES[itemIndex];
            double totalPrice = unitPrice * qty;
            System.out.printf("%-25s %-8d ₱%6.2f ₱%6.2f\n", ALL_MENU_ITEMS[itemIndex], qty, unitPrice, totalPrice);
        }

        System.out.println("---------------------------------------------------");
        System.out.printf("%-25s %-8s %-7s ₱%6.2f\n", "TOTAL", "", "", grandTotal);
        System.out.printf("%-25s %-8s %-7s ₱%6.2f\n", "PAYMENT", "", "", payment);
        System.out.printf("%-25s %-8s %-7s ₱%6.2f\n", "CHANGE", "", "", change);
        System.out.println("===================================================");
        System.out.println("     THANK YOU FOR ORDERING AT JOLIKOD ❤️");
        System.out.println("===================================================");
    }
    
    private boolean askForNextCustomer() {
        int nextCustomer = 0;
        while (nextCustomer != 1 && nextCustomer != 2) {
            System.out.println("\n===================================================");
            System.out.println("[1] Next Customer");
            System.out.println("[2] Logout (Return to Login Menu)");
            System.out.println("===================================================");
            System.out.print("Choice: ");
            
            if (!scanner.hasNextInt()) {
                System.out.println("❌ Invalid input. Please enter 1 or 2.");
                scanner.next();
                nextCustomer = 0;
            } else {
                nextCustomer = scanner.nextInt();
                scanner.nextLine();
                if (nextCustomer != 1 && nextCustomer != 2) {
                    System.out.println("❌ Invalid option.");
                }
            }
        }
        
        if (nextCustomer == 2) {
            System.out.println("\n👋 Logging out of POS system. Returning to main login menu.");
            return false; // Exit the runInterface loop, returning control to JolikodSystem
        }
        
        // Clear buffer and return to the start of POS loop
        return true; 
    }
}