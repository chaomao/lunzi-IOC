package product;

public class Bank {

    private int id;
    private String name;
    private int amount;

    public Bank() {

    }

    public Bank(int id) {
        this.id = id;
    }

    public Bank(String name) {
        this.name = name;
    }

    public String verify() {
        return "success";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
