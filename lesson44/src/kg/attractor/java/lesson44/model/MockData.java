package kg.attractor.java.lesson44.model;

import kg.attractor.java.lesson44.util.FileUtil;
import java.util.ArrayList;
import java.util.List;

public class MockData {
    private static List<Book> books = new ArrayList<>();
    private static List<Employee> employees = new ArrayList<>();

    static {
        loadData();
    }

    public static void loadData() {
        books = FileUtil.readBooks();
        employees = FileUtil.readEmployees();
        List<BorrowRecord> history = FileUtil.readHistory();

        if (books.isEmpty() && employees.isEmpty()) {
            initDefaultData();
            books = FileUtil.readBooks();
            employees = FileUtil.readEmployees();
            history = FileUtil.readHistory();
        }

        for (Book b : books) {
            b.setBorrowed(false);
            b.setCurrentHolder(null);
        }
        for (Employee emp : employees) {
            emp.initBooks();
        }

        for (BorrowRecord record : history) {
            Book book = getBookById(record.getBookId());
            Employee emp = getEmployeeById(record.getEmployeeId());

            if (book != null && emp != null) {
                if (!record.isReturned()) {
                    book.setBorrowed(true);
                    book.setCurrentHolder(emp);
                    emp.getCurrentBooks().add(book);
                } else {
                    emp.getPastBooks().add(book);
                }
            }
        }
    }

    private static void initDefaultData() {
        List<Employee> defaultEmployees = new ArrayList<>();
        defaultEmployees.add(new Employee(1, "Иван Иванов", "ivan@test.mail", "123"));
        defaultEmployees.add(new Employee(2, "Анна Петрова", "anna@test.mail", "456"));
        FileUtil.writeEmployees(defaultEmployees);

        List<Book> defaultBooks = new ArrayList<>();
        defaultBooks.add(new Book(1, "Преступление и наказание", "Ф. Достоевский", "Dostoev.jpg", "Студент убил старуху, потом раскаялся."));
        defaultBooks.add(new Book(2, "Гарри Поттер", "Дж. Роулинг", "Royling.jpg", "Мальчик со шрамом победил безносого."));
        defaultBooks.add(new Book(3, "Ромео и Джульетта", "У. Шекспир", "1.jpg", "Подростки влюбились, повраждовали и умерли."));
        FileUtil.writeBooks(defaultBooks);

        List<BorrowRecord> defaultHistory = new ArrayList<>();
        defaultHistory.add(new BorrowRecord(1, 1, false));
        defaultHistory.add(new BorrowRecord(2, 2, false));
        defaultHistory.add(new BorrowRecord(1, 3, true));
        FileUtil.writeHistory(defaultHistory);
    }

    public static List<Book> getBooks() { return books; }
    public static List<Employee> getEmployees() { return employees; }

    public static Book getBookById(int id) {
        for (Book book : books) {
            if (book.getId() == id) return book;
        }
        return null;
    }

    public static Employee getEmployeeById(int id) {
        for (Employee emp : employees) {
            if (emp.getId() == id) return emp;
        }
        return null;
    }

    public static Employee getEmployeeByEmail(String email) {
        for (Employee emp : employees) {
            if (emp.getEmail() != null && emp.getEmail().equalsIgnoreCase(email)) {
                return emp;
            }
        }
        return null;
    }

    public static void addEmployee(Employee newEmp) {
        employees.add(newEmp);
        FileUtil.writeEmployees(employees);
    }
}