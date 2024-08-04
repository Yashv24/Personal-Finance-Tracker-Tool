
package personalfinancetracker;

public class Record {
    private String date;
    private String type;
    private String category;
    private String description;
    private double amount;

    public Record(String date, String type, String category, String description, double amount) {
        this.date = date;
        this.type = type;
        this.category = category;
        this.description = description;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

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

