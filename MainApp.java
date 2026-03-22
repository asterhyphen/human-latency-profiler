import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.List;

/**
 * Main application class for the Human Latency Profiler.
 * Sets up the JavaFX UI and handles the main window.
 */
public class MainApp extends Application {

    private TestEngine testEngine;
    private VBox root;
    private Button startButton;
    private Text resultText;

    @Override
    public void start(Stage primaryStage) {
        testEngine = new TestEngine(this);

        root = new VBox(20); // spacing between elements
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        startButton = new Button("Start Test");
        startButton.setOnAction(e -> testEngine.startTest());

        resultText = new Text("Click Start Test to begin");
        resultText.setStyle("-fx-font-size: 16px;");

        root.getChildren().addAll(startButton, resultText);

        Scene scene = new Scene(root, 400, 300);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                testEngine.onSpacePressed();
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Human Latency Profiler");
        primaryStage.show();
    }

    /**
     * Updates the UI with new text and background color.
     * Must be called on JavaFX Application Thread.
     */
    public void updateUI(String text, Color color) {
        Platform.runLater(() -> {
            resultText.setText(text);
            String colorHex = String.format("#%02x%02x%02x",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
            root.setStyle("-fx-background-color: " + colorHex + "; -fx-padding: 20; -fx-alignment: center;");
        });
    }

    /**
     * Displays the final results after all trials are completed.
     */
    public void showResults(List<Long> times) {
        double avg = ResultCalculator.average(times);
        long min = ResultCalculator.min(times);
        long max = ResultCalculator.max(times);
        updateUI(String.format("Session Complete!\nAverage: %.2f ms\nFastest: %d ms\nSlowest: %d ms", avg, min, max), Color.LIGHTGRAY);
        startButton.setText("Start New Session");
        startButton.setOnAction(e -> {
            testEngine.reset();
            updateUI("Click Start Test to begin", Color.WHITE);
            startButton.setText("Start Test");
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}