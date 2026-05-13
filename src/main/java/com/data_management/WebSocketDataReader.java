package com.data_management;

import java.io.IOException;
import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Reads patient data from a WebSocket server
 */
public class WebSocketDataReader implements DataReader {

    private String serverAddress;
    private WebSocketClient client;
    public WebSocketDataReader(String serverAddress)
    {
        this.serverAddress = serverAddress;
    }

    /**
     * Connects to the WebSocket server and starts reading data
     *
     * @param dataStorage where the received data is saved
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        try {
            client = new WebSocketClient(new URI(serverAddress)) {

                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("Connected to WebSocket server");
                }

                @Override
                public void onMessage(String message)
                {
                    try {
                    saveMessage(message, dataStorage);
                    }
                    catch(Exception e)
                    {
                        System.err.println("Bad message" + message);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote)
                {
                    System.out.println("WebSocket closed: " + reason);
                }

                @Override
                public void onError(Exception e) {
                    System.err.println("WebSocket error: " + e.getMessage());
                }
            };
            client.connect();
        }
        catch(Exception e)
        {
            throw new IOException("Cant connect to WebSocket server", e);
        }
    }

    /**
     * Closes the WebSocket connection
     */
    @Override
    public void close() throws IOException
    {
        if (client != null)
            client.close();
    }

    /**
     * Parses one message and saves it
     */
    public void saveMessage(String message, DataStorage dataStorage)
    {
        String[] parts = message.split(",");
        int patientId = Integer.parseInt(parts[0].trim());
        long timestamp = Long.parseLong(parts[1].trim());
        String label = parts[2].trim();
        String data = parts[3].trim();
        double value;
        if(data.equals("triggered"))
            value = 1.0;
        else if(data.equals("resolved"))
            value = 0.0;
        else
            value = Double.parseDouble(data.replace("%", ""));

        dataStorage.addPatientData(patientId, value, label, timestamp);
    }
}