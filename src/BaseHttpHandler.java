import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        String response = "Not Found";
        exchange.sendResponseHeaders(404, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        String response = "Not Acceptable";
        exchange.sendResponseHeaders(406, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}
