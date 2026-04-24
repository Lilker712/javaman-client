import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class javaman extends Application {
    
    static VBox rightContainer = new VBox();

    @Override
    public void start(Stage stage) {
        rightContainer.getChildren().setAll(rightMenu(1));

        HBox root = new HBox(leftMenu(), rightContainer);
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
        methodBox.getItems().addAll("GET", "POST", "PUT", "DELETE");
        methodBox.setValue("GET");

        // URL input
        TextField urlField = new TextField();
        urlField.setPromptText("Enter request URL");

        // Send button
        Button sendButton = new Button("Send");

        // Response area
        TextArea responseArea = new TextArea();
        responseArea.setPromptText("Response will appear here...");
        responseArea.setWrapText(true);

        // Top bar layout (method + URL + button)
        HBox topBar = new HBox(10, methodBox, urlField, sendButton);
        topBar.setPadding(new javafx.geometry.Insets(0));

        // Main in Tab requests        
        VBox rightMenu = new VBox(10, topBar, responseArea);
        rightMenu.setPadding(new javafx.geometry.Insets(10));

        // Button action (dummy for now)
        sendButton.setOnAction(e -> {
            String method = methodBox.getValue();
            String url = urlField.getText();

            responseArea.setText("Sending " + method + " request to:\n" + url);
        });

        VBox root = new VBox(10, topBar, responseArea);

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