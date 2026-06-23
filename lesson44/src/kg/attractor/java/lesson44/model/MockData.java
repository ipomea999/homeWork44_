package kg.attractor.java.lesson44.model;

import java.util.ArrayList;
import java.util.List;

public class MockData {
    private static final List<Book> books = new ArrayList<>();
    private static final List<Employee> employees = new ArrayList<>();

    static {
        Employee emp1 = new Employee(1, "Иван Иванов");
        Employee emp2 = new Employee(2, "Анна Петрова");
        employees.add(emp1);
        employees.add(emp2);

        Book b1 = new Book(1, "Преступление и наказание", "Ф. Достоевский", "1.jpg", "Студент убил старуху, потом раскаялся.");
        Book b2 = new Book(2, "Гарри Поттеры", "Дж. Роулинг", "1.jpg", "Мальчик со шрамом победил безносого.");
        Book b3 = new Book(3, "Ромео и Джульетта", "У. Шекспир", "1.jpg", "Подростки влюбились, повраждовали и умерли.");

        b1.setBorrowed(true);
        b1.setCurrentHolder(emp1);
        emp1.getCurrentBooks().add(b1);

        b2.setBorrowed(true);
        b2.setCurrentHolder(emp2);
        emp2.getCurrentBooks().add(b2);

        emp1.getPastBooks().add(b3);

        books.add(b1);
        books.add(b2);
        books.add(b3);
    }

    public static List<Book> getBooks() {
        return books;
    }

    public static List<Employee> getEmployees() {
        return employees;
    }

    public static Book getBookById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    public static Employee getEmployeeById(int id) {
        for (Employee emp : employees) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        return null;
    }
}