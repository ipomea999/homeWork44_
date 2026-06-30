package kg.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import kg.attractor.java.lesson44.model.Book;
import kg.attractor.java.lesson44.model.Employee;
import kg.attractor.java.lesson44.model.MockData;
import kg.attractor.java.server.BasicServer;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.ResponseCodes;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Lesson44Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();
    private static final Map<String, Integer> sessions = new HashMap<>();

    public Lesson44Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/sample", this::freemarkerSampleHandler);
        registerGet("/books", this::booksHandler);
        registerGet("/book", this::bookHandler);
        registerGet("/employee", this::employeeHandler);
        registerGet("/register", this::registerGetHandler);
        registerPost("/register", this::registerPostHandler);
        registerGet("/login", this::loginGetHandler);
        registerPost("/login", this::loginPostHandler);
        registerGet("/profile", this::profileGetHandler);
        registerGet("/logout", this::logoutHandler);
        registerPost("/books/borrow", this::borrowBookHandler);
        registerPost("/books/return", this::returnBookHandler);
    }

    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            cfg.setDirectoryForTemplateLoading(new File("data"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void freemarkerSampleHandler(HttpExchange exchange) {
        renderTemplate(exchange, "sample.html", getSampleDataModel());
    }

    protected void renderTemplate(HttpExchange exchange, String templateFile, Object dataModel) {
        try {
            Template temp = freemarker.getTemplate(templateFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try (OutputStreamWriter writer = new OutputStreamWriter(stream)) {
                temp.process(dataModel, writer);
                writer.flush();
                var data = stream.toByteArray();
                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    private Integer getPathId(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        if (parts.length > 2) {
            try {
                return Integer.parseInt(parts[parts.length - 1]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private String getSessionId(HttpExchange exchange) {
        var headers = exchange.getRequestHeaders();
        var cookieHeader = headers.getFirst("Cookie");
        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split(";");
            for (String cookie : cookies) {
                String[] pair = cookie.trim().split("=");
                if (pair.length == 2 && pair[0].equals("sessionId")) {
                    return pair[1];
                }
            }
        }
        return null;
    }

    private Employee getAuthorizedEmployee(HttpExchange exchange) {
        String sessionId = getSessionId(exchange);
        if (sessionId != null && sessions.containsKey(sessionId)) {
            return MockData.getEmployeeById(sessions.get(sessionId));
        }
        return null;
    }

    private void employeeHandler(HttpExchange exchange) {
        Employee user = getAuthorizedEmployee(exchange);
        if (user == null) {
            sendRedirect(exchange, "/login");
            return;
        }

        Integer id = getPathId(exchange);
        Employee employee = null;

        if (id != null) {
            employee = MockData.getEmployeeById(id);
        }

        if (employee == null && !MockData.getEmployees().isEmpty()) {
            employee = MockData.getEmployees().get(0);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("employee", employee);
        renderTemplate(exchange, "employee.html", data);
    }

    private void booksHandler(HttpExchange exchange) {
        Employee user = getAuthorizedEmployee(exchange);
        Map<String, Object> data = new HashMap<>();
        data.put("books", MockData.getBooks());
        data.put("user", user);
        renderTemplate(exchange, "books.html", data);
    }

    private void bookHandler(HttpExchange exchange) {
        Employee user = getAuthorizedEmployee(exchange);
        Integer id = getPathId(exchange);
        Book book = null;

        if (id != null) {
            book = MockData.getBookById(id);
        }

        if (book == null && !MockData.getBooks().isEmpty()) {
            book = MockData.getBooks().get(0);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("book", book);
        data.put("user", user);

        boolean canBorrow = false;
        boolean canReturn = false;

        if (user != null && book != null) {
            canBorrow = !book.isBorrowed() && user.getCurrentBooks().size() < 2;
            canReturn = book.isBorrowed() && book.getCurrentHolder() != null && book.getCurrentHolder().getId() == user.getId();
        }

        data.put("canBorrow", canBorrow);
        data.put("canReturn", canReturn);

        renderTemplate(exchange, "book.html", data);
    }

    private void registerGetHandler(HttpExchange exchange) {
        renderTemplate(exchange, "register.html", new HashMap<>());
    }

    private void registerPostHandler(HttpExchange exchange) {
        Map<String, String> body = parsePostBody(exchange);
        String email = body.get("email");
        String name = body.get("name");
        String password = body.get("password");

        Map<String, Object> data = new HashMap<>();

        if (email == null || name == null || password == null || email.isBlank() || name.isBlank() || password.isBlank()) {
            data.put("success", false);
            data.put("message", "Все поля должны быть заполнены!");
            renderTemplate(exchange, "register_result.html", data);
            return;
        }

        boolean userExists = false;
        for (Employee emp : MockData.getEmployees()) {
            if (emp.getEmail() != null && emp.getEmail().equalsIgnoreCase(email.trim())) {
                userExists = true;
                break;
            }
        }

        if (userExists) {
            data.put("success", false);
            data.put("message", "Пользователь с таким email уже зарегистрирован!");
        } else {
            int newId = MockData.getEmployees().size() + 1;
            Employee newEmp = new Employee(newId, name.trim(), email.trim().toLowerCase(), password);
            MockData.addEmployee(newEmp);

            data.put("success", true);
            data.put("message", "Пользователь успешно зарегистрирован!");
        }

        renderTemplate(exchange, "register_result.html", data);
    }

    private void loginGetHandler(HttpExchange exchange) {
        renderTemplate(exchange, "login.html", new HashMap<>());
    }

    private void loginPostHandler(HttpExchange exchange) {
        Map<String, String> body = parsePostBody(exchange);
        String email = body.get("email");
        String password = body.get("password");

        if (email != null && password != null) {
            for (Employee emp : MockData.getEmployees()) {
                if (emp.getEmail() != null && emp.getEmail().equalsIgnoreCase(email.trim())
                        && emp.getPassword() != null && emp.getPassword().equals(password)) {

                    String sessionId = UUID.randomUUID().toString();
                    sessions.put(sessionId, emp.getId());
                    exchange.getResponseHeaders().add("Set-Cookie", "sessionId=" + sessionId + "; Max-Age=600; HttpOnly; Path=/");
                    sendRedirect(exchange, "/profile");
                    return;
                }
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("error", "Авторизоваться не удалось, неверный идентификатор или пароль");
        renderTemplate(exchange, "login.html", data);
    }

    private void profileGetHandler(HttpExchange exchange) {
        Employee user = getAuthorizedEmployee(exchange);
        if (user == null) {
            sendRedirect(exchange, "/login");
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("employee", user);
        renderTemplate(exchange, "profile.html", data);
    }

    private void logoutHandler(HttpExchange exchange) {
        String sessionId = getSessionId(exchange);
        if (sessionId != null) {
            sessions.remove(sessionId);
        }
        exchange.getResponseHeaders().add("Set-Cookie", "sessionId=; Max-Age=0; HttpOnly; Path=/");
        sendRedirect(exchange, "/login");
    }

    private void borrowBookHandler(HttpExchange exchange) {
        Employee user = getAuthorizedEmployee(exchange);
        if (user == null) {
            sendRedirect(exchange, "/login");
            return;
        }

        Map<String, String> body = parsePostBody(exchange);
        String bookIdStr = body.get("bookId");
        if (bookIdStr != null) {
            try {
                int bookId = Integer.parseInt(bookIdStr);
                Book book = MockData.getBookById(bookId);
                if (book != null && !book.isBorrowed() && user.getCurrentBooks().size() < 2) {
                    MockData.borrowBook(user.getId(), bookId);
                }
                sendRedirect(exchange, "/book/" + bookId);
                return;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        sendRedirect(exchange, "/books");
    }

    private void returnBookHandler(HttpExchange exchange) {
        Employee user = getAuthorizedEmployee(exchange);
        if (user == null) {
            sendRedirect(exchange, "/login");
            return;
        }

        Map<String, String> body = parsePostBody(exchange);
        String bookIdStr = body.get("bookId");
        if (bookIdStr != null) {
            try {
                int bookId = Integer.parseInt(bookIdStr);
                Book book = MockData.getBookById(bookId);
                if (book != null && book.isBorrowed() && book.getCurrentHolder() != null && book.getCurrentHolder().getId() == user.getId()) {
                    MockData.returnBook(user.getId(), bookId);
                }
                sendRedirect(exchange, "/book/" + bookId);
                return;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        sendRedirect(exchange, "/books");
    }

    private Map<String, String> parsePostBody(HttpExchange exchange) {
        Map<String, String> result = new HashMap<>();
        try {
            InputStream is = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String body = sb.toString();
            if (body.isEmpty()) {
                return result;
            }
            String[] pairs = body.split("&");
            for (String pair : pairs) {
                String[] keyVal = pair.split("=");
                if (keyVal.length > 1) {
                    String key = URLDecoder.decode(keyVal[0], StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(keyVal[1], StandardCharsets.UTF_8);
                    result.put(key, value);
                } else if (keyVal.length == 1) {
                    String key = URLDecoder.decode(keyVal[0], StandardCharsets.UTF_8);
                    result.put(key, "");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void sendRedirect(HttpExchange exchange, String location) {
        try {
            exchange.getResponseHeaders().set("Location", location);
            exchange.sendResponseHeaders(303, -1);
            exchange.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> getQueryParams(HttpExchange exchange) {
        Map<String, String> result = new HashMap<>();
        String query = exchange.getRequestURI().getQuery();
        if (query == null) {
            return result;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyVal = pair.split("=");
            if (keyVal.length > 1) {
                result.put(keyVal[0], keyVal[1]);
            } else if (keyVal.length == 1) {
                result.put(keyVal[0], "");
            }
        }
        return result;
    }

    private SampleDataModel getSampleDataModel() {
        return new SampleDataModel();
    }
}