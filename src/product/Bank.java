package product;

public class Bank {

    private int id;

    public Bank() {

    }

    public Bank(int id) {
        this.id = id;
    }

    public String verify() {
        return "success";
    }

    public int getId() {
        return id;
    }
}
