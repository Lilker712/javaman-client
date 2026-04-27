import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.swing.JOptionPane;


public class javaman extends Application {
    
    static VBox rightContainer = new VBox();

    @Override
    public void start(Stage stage) {
        rightContainer.getChildren().setAll(rightMenu(1));

        HBox root = new HBox(leftMenu(), rightContainer);
        HBox.setHgrow(rightContainer, Priority.ALWAYS);
        root.setPadding(new javafx.geometry.Insets(0));

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("javaman");
        stage.setScene(scene);
        stage.show();

    }
    
    public static HBox leftMenu() {
        Button GotoButtonRequests = new Button("1");
        Button GotoButtonLogs = new Button("2");
        Button GotoButtonSavedRequests = new Button("3");

        // Button action (dummy for now)
        GotoButtonRequests.setOnAction(e -> {
            rightContainer.getChildren().setAll(rightMenu(1));
        });

        // Button action (dummy for now)
        GotoButtonLogs.setOnAction(e -> {
            rightContainer.getChildren().setAll(rightMenu(2));
        });

        // Button action (dummy for now)
        GotoButtonSavedRequests.setOnAction(e -> {
            rightContainer.getChildren().setAll(rightMenu(3));
        });

        VBox leftMenu = new VBox(10, GotoButtonRequests, GotoButtonLogs, GotoButtonSavedRequests);  
        leftMenu.setPadding(new javafx.geometry.Insets(10));
        
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: gray;");
        separator.setPrefHeight(2);
        separator.setOrientation(Orientation.VERTICAL);

        HBox leftMenuBox = new HBox(0, leftMenu, separator);

        return leftMenuBox;
    }

    public static VBox rightMenu(int tabId) {
        VBox root = new VBox();
        switch(tabId) {
            case 2:
                root = logsTab();
                break;
            case 3:
                root = savedRequestsTab();
                break;
            default:
                root = requestsTab();
            }
        
        root.setPadding(new javafx.geometry.Insets(10));

        return root;
    }

    public static VBox requestsTab() {
        // Method dropdown
        ComboBox<String> methodBox = new ComboBox<>();
        methodBox.getItems().addAll("GET", "POST", "PUT", "DELETE", "PATCH");
        methodBox.setValue("GET");

        // URL input
        TextField urlField = new TextField();
        urlField.setPromptText("Enter request URL");

        // Send button
        Button sendButton = new Button("Send");

        // Input area
        TextArea inputArea = new TextArea();
        inputArea.setPromptText("{\n  \"title\": \"Hello\",\n  \"body\": \"World\"\n}");
        inputArea.setWrapText(true);

        // Response area
        TextArea responseArea = new TextArea();
        responseArea.setPromptText("");
        responseArea.setWrapText(true);
        responseArea.setEditable(false);

        // Bottom bar layout (input + response)
        VBox bottomBar = new VBox(10, inputArea, responseArea);
        bottomBar.setPadding(new javafx.geometry.Insets(0));
        
        VBox.setVgrow(inputArea, Priority.ALWAYS);
        VBox.setVgrow(responseArea, Priority.ALWAYS);
        
        bottomBar.setMaxWidth(Double.MAX_VALUE);


        // Top bar layout (method + URL + button)
        HBox topBar = new HBox(10, methodBox, urlField, sendButton);
        topBar.setPadding(new javafx.geometry.Insets(0));

        // Main in Tab requests    
        VBox rightMenu = new VBox(10, topBar, bottomBar);
        rightMenu.setPadding(new javafx.geometry.Insets(10));

        // Button action (dummy for now)
        sendButton.setOnAction(e -> {
            String method = methodBox.getValue();
            String url = urlField.getText();

            String message = inputArea.getText();

            HttpClient client = HttpClient.newHttpClient();

            switch(method) {
            case "GET":
                message = methodGET(url, client);
                break;
            case "POST":
                System.out.println(message);
                message = methodPOST(url, client, message);
                break;
            case "PUT":
                message = methodPUT(url, client, message);
                break;
            case "DELETE":
                message = methodDELETE(url, client);
                break;
            case "PATCH":
                message = methodPATCHE(url, client, message);
                break;
            }

            responseArea.appendText(message + "\n");
        });

        VBox.setVgrow(bottomBar, Priority.ALWAYS);

        VBox root = new VBox(10, rightMenu);
        VBox.setVgrow(rightMenu, Priority.ALWAYS);

        return root;
    }

    public static String methodGET(String url, HttpClient client) {
        String root = "";
        
        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print status code and body
            String x = "Status Code: " + response.statusCode();
            System.out.println(x);
            root = x;
            x = "Response Body: " + response.body();
            root = root + "\n" + x;
            System.out.println(x);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return root;
    }

    public static String methodPOST(String url, HttpClient client, String jsonBody) {
        String root = "";

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json") // Tell the server it's JSON
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody)) // Attach the body
                .build();

        try {
            // Send the request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print status code and body
            String x = "Status Code: " + response.statusCode();
            System.out.println(x);
            root = x;
            x = "Response Body: " + response.body();
            root = root + "\n" + x;
            System.out.println(x);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return root;
    }

    public static String methodPUT(String url, HttpClient client, String jsonBody) {
        String root = "";

        // Build the PUT request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            String id = url.substring(url.lastIndexOf("/"));
            root = "Updating resource " + id + " ...";
            System.out.println(root);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Show the result
            String x = "Status Code: " + response.statusCode();
            System.out.println(x);
            root += x;
            x = "Response Body: " + response.body();
            root = root + "\n" + x;
            System.out.println(x);
                
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }

        return root;
    }

    public static String methodDELETE(String url, HttpClient client) {
        String root = "";

        // Build the DELETE request
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .DELETE()
            .build();

        try {
            String id = url.substring(url.lastIndexOf("/"));
            root = "Deleting resource " + id + "...";
            System.out.println(root);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Show the result
            // 200 OK or 204 No Content are the usual success codes for DELETE
            String x = "Status Code: " + response.statusCode();
            System.out.println(x);
            root += x;
            x = "Response Body: " + response.body();
            root = root + "\n" + x;
            System.out.println(x);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }

        return root;
    }

    private static String methodPATCHE(String url, HttpClient client, String jsonBody) {
        String root = "";

        // Build the request using .method("PATCH", ...)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            String id = url.substring(url.lastIndexOf("/"));
            String x = "Updating resource " + id + " ...";
            System.out.println(x);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            x = "Status Code: " + response.statusCode();
            System.out.println(x);
            root = x;
            x = "Updated Content: " + response.body();
            root = root + "\n" + x;
            System.out.println(x);
                
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }

        return root;

    }

    public static VBox logsTab() {
        VBox root = new VBox(10);

        return root;
    }

    public static VBox savedRequestsTab() {
        VBox root = new VBox(10);

        return root;
    }

    public static void main(String[] args) {
        launch();
    }
}