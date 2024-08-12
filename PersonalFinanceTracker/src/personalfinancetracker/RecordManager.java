package personalfinancetracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

// The RecordManager class handles the operations on financial records.
public class RecordManager {
    // Member variables
    private List<Record> records;                   // List to store all records.
    private Queue<Record> recentRecordsQueue;       // Queue to store recent records for quick access.
    private SimpleDateFormat dateFormat;            // Date format for parsing and formatting dates.
    private static final int DAYS_30 = 30;          // Constant for 30 days.

    // Constructor to initialize the RecordManager.
    public RecordManager() {
        this.records = new ArrayList<>();           // Initialize the records list.
        this.recentRecordsQueue = new LinkedList<>(); // Initialize the recent records queue.
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Initialize the date format.
    }

    // Method to add a new record.
    public void addRecord(String date, String type, String category, String description, double amount) throws ParseException {
        Record newRecord = new Record(date, type, category, description, amount); // Create a new record.
        records.add(newRecord);             // Add the new record to the list.
        updateRecentRecordsQueue(newRecord); // Update the recent records queue.
    }

    // Method to get all records.
    public List<Record> getAllRecords() {
        return records;
    }

    // Method to print all records along with the balance.
    public void printAllRecords(List<Record> records) {
        for (Record record : records) {
            System.out.println(record);     // Print each record.
        }
        double balance = calculateBalance(); // Calculate the balance.
        System.out.println("Balance: " + balance); // Print the balance.
    }

    // Method to search records by date.
    public List<Record> searchRecordsByDate(String date) {
        return records.stream()
                .filter(record -> record.getDate().equals(date)) // Filter records by date.
                .collect(Collectors.toList());
    }

    // Method to edit a record.
    public boolean editRecord(String date, String type, String category, String description, double amount) {
        for (Record record : records) {
            if (record.getDate().equals(date)) { // Find the record by date.
                if (type != null && !type.isEmpty()) {
                    record.setType(type);         // Update type if provided.
                }
                if (category != null && !category.isEmpty()) {
                    record.setCategory(category); // Update category if provided.
                }
                if (description != null && !description.isEmpty()) {
                    record.setDescription(description); // Update description if provided.
                }
                if (amount >= 0) {
                    record.setAmount(amount);     // Update amount if provided.
                }
                return true; // Return true if record is edited.
            }
        }
        return false; // Return false if record is not found.
    }

    // Method to delete a record by date.
    public boolean deleteRecord(String date) {
        return records.removeIf(record -> record.getDate().equals(date)); // Remove records by date.
    }

    // Method to filter records by type.
    public List<Record> filterByType(List<Record> records, String type) {
        return records.stream()
                .filter(record -> record.getType().equalsIgnoreCase(type)) // Filter records by type.
                .collect(Collectors.toList());
    }

    // Method to filter records by category.
    public List<Record> filterByCategory(List<Record> records, String category) {
        return records.stream()
                .filter(record -> record.getCategory().equalsIgnoreCase(category)) // Filter records by category.
                .collect(Collectors.toList());
    }

    // Method to filter records by date range.
    public List<Record> filterByDateRange(List<Record> records, String startDate, String endDate) {
        return records.stream()
                .filter(record -> {
                    try {
                        Date recordDate = dateFormat.parse(record.getDate());
                        Date start = dateFormat.parse(startDate);
                        Date end = dateFormat.parse(endDate);
                        return !recordDate.before(start) && !recordDate.after(end); // Check if date is within range.
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    // Method to sort records by date.
    public List<Record> sortByDate(List<Record> records) {
        return records.stream()
                .sorted((r1, r2) -> {
                    try {
                        return dateFormat.parse(r2.getDate()).compareTo(dateFormat.parse(r1.getDate())); // Sort from latest to oldest.
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                })
                .collect(Collectors.toList());
    }

    // Method to sort records by amount.
    public List<Record> sortByAmount(List<Record> records) {
        return records.stream()
                .sorted((r1, r2) -> Double.compare(r2.getAmount(), r1.getAmount())) // Sort from highest to lowest.
                .collect(Collectors.toList());
    }

    // Method to search records by description.
    public List<Record> searchByDescription(List<Record> records, String description) {
        return records.stream()
                .filter(record -> record.getDescription().contains(description)) // Filter records by description.
                .collect(Collectors.toList());
    }

    // Method to calculate the balance.
    public double calculateBalance() {
        return records.stream()
                .mapToDouble(record -> record.getType().equalsIgnoreCase("INCOME") ? record.getAmount() : -record.getAmount()) // Calculate income and expenses.
                .sum(); // Sum the values to get the balance.
    }

    // Method to update the recent records queue.
    private void updateRecentRecordsQueue(Record newRecord) throws ParseException {
        Date newRecordDate = dateFormat.parse(newRecord.getDate());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -DAYS_30);
        Date cutoffDate = calendar.getTime();

        recentRecordsQueue.add(newRecord); // Add the new record to the queue.

        // Remove records older than 30 days from the queue.
        while (!recentRecordsQueue.isEmpty()) {
            try {
                Date oldestDate = dateFormat.parse(recentRecordsQueue.peek().getDate());
                if (oldestDate.before(cutoffDate)) {
                    recentRecordsQueue.poll();
                } else {
                    break; // The queue is now up-to-date.
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to print summary for the last 7 or 30 days using the queue.
    public void printSummary(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        Date cutoffDate = calendar.getTime();

        double totalIncome = 0;
        double totalExpenses = 0;
        int recordCount = 0;

        for (Record record : recentRecordsQueue) {
            try {
                Date recordDate = dateFormat.parse(record.getDate());
                if (!recordDate.before(cutoffDate)) {
                    recordCount++;
                    if (record.getType().equalsIgnoreCase("INCOME")) {
                        totalIncome += record.getAmount();
                    } else if (record.getType().equalsIgnoreCase("EXPENSE")) {
                        totalExpenses += record.getAmount();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        double averageDailySpending = recordCount > 0 ? totalExpenses / recordCount : 0;
        System.out.println("Summary for the last " + days + " days:");
        System.out.println("Total Income: " + totalIncome);
        System.out.println("Total Expenses: " + totalExpenses);
        System.out.println("Average Daily Spending: " + averageDailySpending);
    }
}
