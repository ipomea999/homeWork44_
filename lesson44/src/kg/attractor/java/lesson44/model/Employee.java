package kg.attractor.java.lesson44.model;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private int id;
    private String name;
    private List<Book> currentBooks;
    private List<Book> pastBooks;

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
        this.currentBooks = new ArrayList<>();
        this.pastBooks = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public List<Book> getCurrentBooks() { return currentBooks; }
    public List<Book> getPastBooks() { return pastBooks; }
}