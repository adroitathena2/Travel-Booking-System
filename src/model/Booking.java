package model;

public class Booking {
    private int id;
    private int status; 
    private int packageId;
    private int paymentId;
    
    public Booking(int id, int status, int packageId, int paymentId) {
        this.id = id;
        this.status = status;
        this.packageId = packageId;
        this.paymentId = paymentId;
    }
    
    public int getId() { return id; }
    public int getStatus() { return status; }
    public int getPackageId() { return packageId; }
    public int getPaymentId() { return paymentId; }
    
    public boolean isConfirmed() { return status == 1; }
}
