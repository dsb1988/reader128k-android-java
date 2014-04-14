package net.reader128k.network;

import net.reader128k.util.LogUtils;

import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketOptions;

import static net.reader128k.util.LogUtils.LOGD;

class WSConnection {
	private static final String TAG = LogUtils.makeLogTag(WSConnection.class);
    private WebSocketConnection mConnection;
	private ObjectMapper mapper = new ObjectMapper();

	private int lastMessageNumber = 0;
	private final HashMap<String, Object> closureHandlers = new HashMap<String, Object>();

	private class Handler {
		public Method method;
		public Class<?> expectedType;
	}

	private HashMap<String, Handler> handlers = new HashMap<String, Handler>();

	private void findHandlers() {
		for (Method method : this.getClass().getMethods()) {
		    if (method.isAnnotationPresent(WSAnswerHandler.class)) {
		    	String m = method.getAnnotation(WSAnswerHandler.class).method();

	    		Handler handler = new Handler();
	    		handler.method = method;
	    		handler.expectedType = method.getParameterTypes()[0];

		    	handlers.put(m, handler);
		    }
		}
	}

	public WSConnection() {
		findHandlers();
	}

    protected void connect(String host, List<BasicNameValuePair> headers)  throws WebSocketException {
        WebSocketOptions options = new WebSocketOptions();
        options.setReceiveTextMessagesRaw(true);

        for (BasicNameValuePair cookie : headers)
            LOGD(TAG, cookie.getName() + " " + cookie.getValue());

        mConnection = new WebSocketConnection();
        mConnection.connect("ws://" + host, new String[0], new WSHandler(), options, headers);
    }

	private String generateMessageId() {
		synchronized (this) {
			++lastMessageNumber;
			return String.valueOf(lastMessageNumber);
		}
	}

	protected void onConnect() {
	}

	protected void onDisconnect(int code, String reason) {
	}

	public void send(String method, Map<String, Object> params, Object userData) {
        final String messageId = generateMessageId();

		HashMap<String, Object> datagram;
		if (params != null)
			datagram = new HashMap<String, Object>(params);
		else
			datagram = new HashMap<String, Object>();

		datagram.put("m", method);
		datagram.put("n", messageId);

		byte[] bytes;
		try {
			bytes = mapper.writeValueAsBytes(datagram);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			return;
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		synchronized (closureHandlers) {
			closureHandlers.put(messageId, userData);
		}

        mConnection.sendRawTextMessage(bytes);
	}

    public void disconnect() {
        mConnection.disconnect();
    }

	private void dispatch(byte[] payload) throws Exception {
		JsonNode answer = mapper.readTree(payload);

		final String method = getTextField(answer, "m");
		if (!handlers.containsKey(method))
			throw new RuntimeException("Received answer for unknown method '" + method + "'.");

		Handler handler = handlers.get(method);
		final String messageId = getTextField(answer, "n");

		Object userData = null;
		synchronized (closureHandlers) {
			if (closureHandlers.containsKey(messageId)) {
				userData = closureHandlers.get(messageId);
				closureHandlers.remove(messageId);
			}
		}

		JsonNode r = answer.get("r");
		Object response = mapper.readValue(r, handler.expectedType);
		handler.method.invoke(this, response, userData);
	}

    public boolean isConnected() {
        return mConnection != null && mConnection.isConnected();
    }

	final class WSHandler extends WebSocketConnectionHandler {
		@Override
		public void onOpen() {
			onConnect();
		}

		@Override
		public void onRawTextMessage(byte[] payload) {
			try {
				dispatch(payload);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onClose(int code, String reason) {
			onDisconnect(code, reason);
		}
	}

	private static String getTextField(JsonNode node, String fieldName) {
		JsonNode child = node.get(fieldName);
		return child != null ? child.getTextValue() : null;
	}
}
