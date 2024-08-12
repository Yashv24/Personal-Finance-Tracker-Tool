package personalfinancetracker;

import java.text.ParseException;
import java.util.Scanner;

public class PersonalFinanceTracker {

    public static void main(String[] args) {
        RecordManager manager = new RecordManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Display the main menu
            System.out.println("Main Menu:");
            System.out.println("1. Add Record");
            System.out.println("2. View Records");
            System.out.println("3. Edit Record");
            System.out.println("4. Delete Record");
            System.out.println("5. Summarize Records");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Add Record
                    try {
                        System.out.print("Enter date (yyyy-MM-dd): ");
                        String date = scanner.nextLine();
                        System.out.print("Enter type (INCOME/EXPENSE): ");
                        String type = scanner.nextLine();
                        System.out.print("Enter category: ");
                        String category = scanner.nextLine();
                        System.out.print("Enter description: ");
                        String description = scanner.nextLine();
                        System.out.print("Enter amount: ");
                        double amount = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline

                        manager.addRecord(date, type, category, description, amount);
                        System.out.println("Record added successfully!");
                    } catch (ParseException e) {
                        System.out.println("Invalid date format.");
                    }
                    break;
                case 2:
                    // View Records
                    manager.printAllRecords(manager.getAllRecords());
                    viewMenu(manager, scanner);
                    break;
                case 3:
                    // Edit Record
                    System.out.print("Enter date of the record to edit: ");
                    String editDate = scanner.nextLine();
                    System.out.print("Enter new type (or leave blank): ");
                    String editType = scanner.nextLine();
                    System.out.print("Enter new category (or leave blank): ");
                    String editCategory = scanner.nextLine();
                    System.out.print("Enter new description (or leave blank): ");
                    String editDescription = scanner.nextLine();
                    System.out.print("Enter new amount (or enter -1 to leave unchanged): ");
                    double editAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline

                    boolean edited = manager.editRecord(editDate, editType, editCategory, editDescription, editAmount);
                    System.out.println(edited ? "Record edited successfully!" : "Record not found.");
                    break;
                case 4:
                    // Delete Record
                    System.out.print("Enter date of the record to delete: ");
                    String deleteDate = scanner.nextLine();
                    boolean deleted = manager.deleteRecord(deleteDate);
                    System.out.println(deleted ? "Record deleted successfully!" : "Record not found.");
                    break;
                case 5:
                    // Summarize Records
                    summarizeMenu(manager, scanner);
                    break;
                case 6:
                    // Exit
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Method for displaying and handling the view menu
    private static void viewMenu(RecordManager manager, Scanner scanner) {
        while (true) {
            System.out.println("View Menu:");
            System.out.println("1. Filter Records");
            System.out.println("2. Sort Records");
            System.out.println("3. Search Records");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choose an option: ");
            int viewChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (viewChoice) {
                case 1:
                    filterMenu(manager, scanner);
                    break;
                case 2:
                    sortMenu(manager, scanner);
                    break;
                case 3:
                    // Search Records
                    System.out.print("Enter description to search: ");
                    String searchDesc = scanner.nextLine();
                    manager.printAllRecords(manager.searchByDescription(manager.getAllRecords(), searchDesc));
                    break;
                case 4:
                    // Back to Main Menu
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Method for displaying and handling the filter menu
    private static void filterMenu(RecordManager manager, Scanner scanner) {
        System.out.println("Filter Options:");
        System.out.println("1. By Type");
        System.out.println("2. By Category");
        System.out.println("3. By Date Range");
        System.out.println("4. Back to View Menu");
        System.out.print("Choose an option: ");
        int filterChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (filterChoice) {
            case 1:
                System.out.print("Enter type (INCOME/EXPENSE): ");
                String filterType = scanner.nextLine();
                manager.printAllRecords(manager.filterByType(manager.getAllRecords(), filterType));
                break;
            case 2:
                System.out.print("Enter category: ");
                String filterCategory = scanner.nextLine();
                manager.printAllRecords(manager.filterByCategory(manager.getAllRecords(), filterCategory));
                break;
            case 3:
                System.out.print("Enter start date (yyyy-MM-dd): ");
                String startDate = scanner.nextLine();
                System.out.print("Enter end date (yyyy-MM-dd): ");
                String endDate = scanner.nextLine();
                manager.printAllRecords(manager.filterByDateRange(manager.getAllRecords(), startDate, endDate));
                break;
            case 4:
                // Back to View Menu
                return;
            default:
                System.out.println("Invalid option.");
        }
    }

    // Method for displaying and handling the sort menu
    private static void sortMenu(RecordManager manager, Scanner scanner) {
        System.out.println("Sort Options:");
        System.out.println("1. By Date (Latest to Oldest)");
        System.out.println("2. By Amount (Highest to Lowest)");
        System.out.println("3. Back to View Menu");
        System.out.print("Choose an option: ");
        int sortChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (sortChoice) {
            case 1:
                manager.printAllRecords(manager.sortByDate(manager.getAllRecords()));
                break;
            case 2:
                manager.printAllRecords(manager.sortByAmount(manager.getAllRecords()));
                break;
            case 3:
                // Back to View Menu
                return;
            default:
                System.out.println("Invalid option.");
        }
    }

    // Method for displaying and handling the summarize menu
    private static void summarizeMenu(RecordManager manager, Scanner scanner) {
        System.out.println("Summarize Options:");
        System.out.println("1. Last 7 Days");
        System.out.println("2. Last 30 Days");
        System.out.print("Choose an option: ");
        int summarizeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        int days = (summarizeChoice == 1) ? 7 : 30;
        manager.printSummary(days);
    }
}
