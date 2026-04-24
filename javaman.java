import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class javaman extends Application {

    @Override
    public void start(Stage stage) {
        // Method dropdown
        ComboBox<String> methodBox = new ComboBox<>();
        methodBox.getItems().addAll("GET", "POST");
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
        topBar.setPadding(new javafx.geometry.Insets(10));

        // Main layout
        VBox root = new VBox(10, topBar, responseArea);

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Mini Postman");
        stage.setScene(scene);
        stage.show();

        // Button action (dummy for now)
        sendButton.setOnAction(e -> {
            String method = methodBox.getValue();
            String url = urlField.getText();

            responseArea.setText("Sending " + method + " request to:\n" + url);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}