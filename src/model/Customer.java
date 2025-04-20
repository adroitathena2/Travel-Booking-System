package model;

public class Customer extends User {
    private String phone, gender, address, dob;

    public Customer(int id, String name, String email, String password, String phone, String gender, String address, String dob) {
        super(id, name, email, password);
        this.phone = phone;
        this.gender = gender;
        this.address = address;
        this.dob = dob;
    }

    public String getPhone() { return phone; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getDob() { return dob; }
}