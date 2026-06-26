package kg.attractor.java.lesson44.model;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private int id;
    private String name;
    private String email;
    private String password;
    private List<Book> currentBooks;
    private List<Book> pastBooks;

    public Employee(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.currentBooks = new ArrayList<>();
        this.pastBooks = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public List<Book> getCurrentBooks() { return currentBooks; }
    public List<Book> getPastBooks() { return pastBooks; }
}