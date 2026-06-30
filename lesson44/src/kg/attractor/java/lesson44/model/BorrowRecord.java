package kg.attractor.java.lesson44.model;

public class BorrowRecord {
    private int employeeId;
    private int bookId;
    private boolean returned;

    public BorrowRecord(int employeeId, int bookId, boolean returned) {
        this.employeeId = employeeId;
        this.bookId = bookId;
        this.returned = returned;
    }

    public int getEmployeeId() { return employeeId; }
    public int getBookId() { return bookId; }
    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }
}