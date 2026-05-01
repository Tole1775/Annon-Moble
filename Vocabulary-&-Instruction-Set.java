import java.util.HashMap;
import java.util.Map;

public class AnnonVocabularyEngine {

    // The "Instruction Set" - Clean, Short, Urgent
    private enum Command {
        OBSTACLE_LEFT, OBSTACLE_RIGHT, OBSTACLE_CENTER,
        STEP_UP, STEP_DOWN, 
        TURN_LEFT, TURN_RIGHT, 
        PATH_CLEAR, ARRIVED, DANGER_STOP
    }

    private Map<Command, String> lexicon = new HashMap<>();

    public AnnonVocabularyEngine() {
        // Mapping complex states to 4th-grade auditory cues
        lexicon.put(Command.OBSTACLE_LEFT, "Object left.");
        lexicon.put(Command.OBSTACLE_RIGHT, "Object right.");
        lexicon.put(Command.OBSTACLE_CENTER, "Directly ahead.");
        lexicon.put(Command.STEP_UP, "Step up.");
        lexicon.put(Command.STEP_DOWN, "Step down.");
        lexicon.put(Command.TURN_LEFT, "Turn left.");
        lexicon.put(Command.TURN_RIGHT, "Turn right.");
        lexicon.put(Command.PATH_CLEAR, "Clear.");
        lexicon.put(Command.ARRIVED, "You are here.");
        lexicon.put(Command.DANGER_STOP, "Stop. Danger.");
    }

    // This converts LiDAR spatial data into one of our "Safe" words
    public void translateSpatialData(float distance, float angle, String obstacleType) {
        if (distance < 0.5) {
            broadcast(Command.DANGER_STOP);
            return;
        }

        // Logic to determine direction based on LiDAR angle
        if (angle < -15) broadcast(Command.OBSTACLE_LEFT);
        else if (angle > 15) broadcast(Command.OBSTACLE_RIGHT);
        else broadcast(Command.OBSTACLE_CENTER);
    }

    private void broadcast(Command cmd) {
        String phrase = lexicon.get(cmd);
        // Handshake with the Audio Engine
        AnnonAudio.speak(phrase);
        // Sync with Haptics (Vibration on the side of the command)
        AnnonHaptics.sync(cmd);
    }
}
