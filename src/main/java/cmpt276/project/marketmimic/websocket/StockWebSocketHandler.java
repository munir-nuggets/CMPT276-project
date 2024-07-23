package cmpt276.project.marketmimic.websocket;

import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class StockWebSocketHandler extends TextWebSocketHandler {

    private String finnhubWebSocketUrl = "wss://ws.finnhub.io?token=cq1jlm1r01qjh3d5pt90cq1jlm1r01qjh3d5pt9g";

    private WebSocketSession webSocketSession;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.webSocketSession = session;
        connectToFinnhubWebSocket();
    }

    private void connectToFinnhubWebSocket() {
        scheduler.scheduleAtFixedRate(() -> {
            if (webSocketSession != null && webSocketSession.isOpen()) {
                try {
                    WebSocketClient client = new StandardWebSocketClient();
                    client.execute(this, finnhubWebSocketUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5, TimeUnit.MINUTES);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle messages from Finnhub WebSocket
        if (webSocketSession != null && webSocketSession.isOpen()) {
            webSocketSession.sendMessage(new TextMessage(message.getPayload()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        this.webSocketSession = null;
        scheduler.shutdown();
    }
}