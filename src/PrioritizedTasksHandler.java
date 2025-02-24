import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
public class PrioritizedTasksHandler extends BaseHttpHandler {
    private final Gson gson = new Gson();
    private final TaskManager taskManager = Managers.getDefault();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            if ("GET".equals(method)) {
                handleGet(exchange);
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
            exchange.close();
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getPrioritizedTasks()));
    }
}
