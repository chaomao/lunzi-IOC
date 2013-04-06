package product;

public class User {
    private Bank bank;

    public User() {
    }

    public User(Bank bank) {
        this.bank = bank;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
}
