
package personalfinancetracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class RecordManager {
    private List<Record> records;
    private Queue<Record> recentRecordsQueue;
    private SimpleDateFormat dateFormat;
    private static final int DAYS_30 = 30;

    public RecordManager() {
        this.records = new ArrayList<>();
        this.recentRecordsQueue = new LinkedList<>();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public void addRecord(String date, String type, String category, String description, double amount) throws ParseException {
        Record newRecord = new Record(date, type, category, description, amount);
        records.add(newRecord);
        updateRecentRecordsQueue(newRecord);
    }

    public List<Record> getAllRecords() {
        return records;
    }

    public void printAllRecords(List<Record> records) {
        for (Record record : records) {
            System.out.println(record);
        }
        double balance = calculateBalance();
        System.out.println("Balance: " + balance);
    }

    public List<Record> searchRecordsByDate(String date) {
        return records.stream()
                .filter(record -> record.getDate().equals(date))
                .collect(Collectors.toList());
    }

    public boolean editRecord(String date, String type, String category, String description, double amount) {
        for (Record record : records) {
            if (record.getDate().equals(date)) {
                if (type != null && !type.isEmpty()) {
                    record.setType(type);
                }
                if (category != null && !category.isEmpty()) {
                    record.setCategory(category);
                }
                if (description != null && !description.isEmpty()) {
                    record.setDescription(description);
                }
                if (amount >= 0) {
                    record.setAmount(amount);
                }
                return true;
            }
        }
        return false;
    }

    public boolean deleteRecord(String date) {
        return records.removeIf(record -> record.getDate().equals(date));
    }

    // Filtering methods
    public List<Record> filterByType(List<Record> records, String type) {
        return records.stream()
                .filter(record -> record.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public List<Record> filterByCategory(List<Record> records, String category) {
        return records.stream()
                .filter(record -> record.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Record> filterByDateRange(List<Record> records, String startDate, String endDate) {
        return records.stream()
                .filter(record -> {
                    try {
                        Date recordDate = dateFormat.parse(record.getDate());
                        Date start = dateFormat.parse(startDate);
                        Date end = dateFormat.parse(endDate);
                        return !recordDate.before(start) && !recordDate.after(end);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    // Sorting methods
    public List<Record> sortByDate(List<Record> records) {
        return records.stream()
                .sorted((r1, r2) -> {
                    try {
                        return dateFormat.parse(r2.getDate()).compareTo(dateFormat.parse(r1.getDate())); // Latest to oldest
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                })
                .collect(Collectors.toList());
    }

    public List<Record> sortByAmount(List<Record> records) {
        return records.stream()
                .sorted((r1, r2) -> Double.compare(r2.getAmount(), r1.getAmount())) // Highest to lowest
                .collect(Collectors.toList());
    }

    // Search method
    public List<Record> searchByDescription(List<Record> records, String description) {
        return records.stream()
                .filter(record -> record.getDescription().contains(description))
                .collect(Collectors.toList());
    }

    // Method to calculate balance
    public double calculateBalance() {
        return records.stream()
                .mapToDouble(record -> record.getType().equalsIgnoreCase("INCOME") ? record.getAmount() : -record.getAmount())
                .sum();
    }

    // Method to update the recent records queue
    private void updateRecentRecordsQueue(Record newRecord) throws ParseException {
        Date newRecordDate = dateFormat.parse(newRecord.getDate());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -DAYS_30);
        Date cutoffDate = calendar.getTime();

        recentRecordsQueue.add(newRecord);

        while (!recentRecordsQueue.isEmpty()) {
            try {
                Date oldestDate = dateFormat.parse(recentRecordsQueue.peek().getDate());
                if (oldestDate.before(cutoffDate)) {
                    recentRecordsQueue.poll();
                } else {
                    break; // The queue is now up-to-date
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to get summary for the last 7 or 30 days using the queue
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