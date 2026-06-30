package kg.attractor.java.lesson44.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kg.attractor.java.lesson44.model.Book;
import kg.attractor.java.lesson44.model.Employee;
import kg.attractor.java.lesson44.model.BorrowRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path BOOKS_PATH = Paths.get("data/books.json");
    private static final Path EMPLOYEES_PATH = Paths.get("data/employees.json");
    private static final Path HISTORY_PATH = Paths.get("data/history.json.json");

    public static List<Book> readBooks() {
        try {
            if (!Files.exists(BOOKS_PATH)) return new ArrayList<>();
            String json = Files.readString(BOOKS_PATH);
            Book[] arr = GSON.fromJson(json, Book[].class);
            return arr != null ? new ArrayList<>(Arrays.asList(arr)) : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void writeBooks(List<Book> books) {
        try {
            Files.writeString(BOOKS_PATH, GSON.toJson(books));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> readEmployees() {
        try {
            if (!Files.exists(EMPLOYEES_PATH)) return new ArrayList<>();
            String json = Files.readString(EMPLOYEES_PATH);
            Employee[] arr = GSON.fromJson(json, Employee[].class);
            return arr != null ? new ArrayList<>(Arrays.asList(arr)) : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void writeEmployees(List<Employee> employees) {
        try {
            Files.writeString(EMPLOYEES_PATH, GSON.toJson(employees));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<BorrowRecord> readHistory() {
        try {
            if (!Files.exists(HISTORY_PATH)) return new ArrayList<>();
            String json = Files.readString(HISTORY_PATH);
            BorrowRecord[] arr = GSON.fromJson(json, BorrowRecord[].class);
            return arr != null ? new ArrayList<>(Arrays.asList(arr)) : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void writeHistory(List<BorrowRecord> history) {
        try {
            Files.writeString(HISTORY_PATH, GSON.toJson(history));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}