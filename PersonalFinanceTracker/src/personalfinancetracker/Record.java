package personalfinancetracker;

// The Record class represents a financial record in the personal finance tracker.
public class Record {
    // Private member variables to store the details of the record.
    private String date;          // The date of the record.
    private String type;          // The type of the record (e.g., income, expense).
    private String category;      // The category of the record (e.g., groceries, salary).
    private String description;   // A brief description of the record.
    private double amount;        // The amount of money involved in the record.

    // Constructor to initialize a new Record object with given values.
    public Record(String date, String type, String category, String description, double amount) {
        this.date = date;               // Set the date.
        this.type = type;               // Set the type.
        this.category = category;       // Set the category.
        this.description = description; // Set the description.
        this.amount = amount;           // Set the amount.
    }

    // Getter method to retrieve the date of the record.
    public String getDate() {
        return date;
    }

    // Getter method to retrieve the type of the record.
    public String getType() {
        return type;
    }

    // Getter method to retrieve the category of the record.
    public String getCategory() {
        return category;
    }

    // Getter method to retrieve the description of the record.
    public String getDescription() {
        return description;
    }

    // Getter method to retrieve the amount of the record.
    public double getAmount() {
        return amount;
    }

    // Setter method to update the type of the record.
    public void setType(String type) {
        this.type = type;
    }

    // Setter method to update the category of the record.
    public void setCategory(String category) {
        this.category = category;
    }

    // Setter method to update the description of the record.
    public void setDescription(String description) {
        this.description = description;
    }

    // Setter method to update the amount of the record.
    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Override the toString method to provide a string representation of the record.
    @Override
    public String toString() {
        return "Record{" +
                "date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                '}';
    }
}
