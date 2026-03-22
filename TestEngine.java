import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Handles the test logic, including timing, stimulus presentation, and trial management.
 */
public class TestEngine {

    private MainApp app;
    private List<Long> reactionTimes = new ArrayList<>();
    private int trial = 0;
    private long startTime;
    private boolean stimulusShown = false;
    private Random random = new Random();

    public TestEngine(MainApp app) {
        this.app = app;
    }

    /**
     * Starts a new test session or continues to the next trial.
     */
    public void startTest() {
        if (trial < 10) {
            stimulusShown = false;
            app.updateUI("Wait for the color change...", Color.GRAY);
            double delay = 2 + random.nextDouble() * 3; // Random delay between 2-5 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(delay));
            pause.setOnFinished(e -> showStimulus());
            pause.play();
        } else {
            app.showResults(reactionTimes);
        }
    }

    /**
     * Shows the stimulus (color change) and starts timing.
     */
    private void showStimulus() {
        stimulusShown = true;
        startTime = System.nanoTime();
        app.updateUI("Press SPACE now!", Color.GREEN);
    }

    /**
     * Handles spacebar press from the user.
     */
    public void onSpacePressed() {
        if (!stimulusShown) {
            app.updateUI("Too Early! Wait for green.", Color.RED);
            // Restart the current trial
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> startTest());
            pause.play();
            return;
        }

        long endTime = System.nanoTime();
        long reactionTime = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
        reactionTimes.add(reactionTime);
        trial++;
        stimulusShown = false;

        app.updateUI("Reaction time: " + reactionTime + " ms", Color.GRAY);

        // Brief pause before next trial
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> startTest());
        pause.play();
    }

    /**
     * Resets the test engine for a new session.
     */
    public void reset() {
        reactionTimes.clear();
        trial = 0;
        stimulusShown = false;
    }
}