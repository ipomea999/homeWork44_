package kg.attractor.java.lesson44;
import kg.attractor.java.lesson44.model.Book;
import kg.attractor.java.lesson44.model.Employee;
import kg.attractor.java.lesson44.model.MockData;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import kg.attractor.java.server.BasicServer;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.ResponseCodes;


import java.io.*;

public class Lesson44Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();

    public Lesson44Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/sample", this::freemarkerSampleHandler);
        registerGet("/books", this::booksHandler);
        registerGet("/book", this::bookHandler);
        registerGet("/employee", this::employeeHandler);
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

    private void employeeHandler(HttpExchange exchange) {
        Map<String, String> params = getQueryParams(exchange);
        String idStr = params.get("id");
        Employee employee = null;

        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                employee = MockData.getEmployeeById(id);
            } catch (NumberFormatException e) {
            }
        }

        if (employee == null && !MockData.getEmployees().isEmpty()) {
            employee = MockData.getEmployees().get(0);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("employee", employee);
        renderTemplate(exchange, "employee.html", data);
    }

    private void booksHandler(HttpExchange exchange) {
        Map<String, Object> data = new HashMap<>();
        data.put("books", MockData.getBooks());
        renderTemplate(exchange, "books.html", data);
    }

    private void bookHandler(HttpExchange exchange) {
        Map<String, String> params = getQueryParams(exchange);
        String idStr = params.get("id");
        Book book = null;

        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                book = MockData.getBookById(id);
            } catch (NumberFormatException e) {
            }
        }

        if (book == null && !MockData.getBooks().isEmpty()) {
            book = MockData.getBooks().get(0);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("book", book);
        renderTemplate(exchange, "book.html", data);
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
