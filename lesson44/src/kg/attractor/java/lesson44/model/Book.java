package kg.attractor.java.lesson44.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String image;
    private String description;
    private boolean isBorrowed;
    private Employee currentHolder;

    public Book(int id, String title, String author, String image, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.image = image;
        this.description = description;
        this.isBorrowed = false;
        this.currentHolder = null;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getImage() { return image; }
    public String getDescription() { return description; }
    public boolean isBorrowed() { return isBorrowed; }
    public void setBorrowed(boolean borrowed) { isBorrowed = borrowed; }
    public Employee getCurrentHolder() { return currentHolder; }
    public void setCurrentHolder(Employee currentHolder) { this.currentHolder = currentHolder; }
}