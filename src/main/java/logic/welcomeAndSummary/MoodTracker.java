
package welcomelogic;

import java.util.ArrayList;
import java.util.List;

public class MoodTracker {

    private List<String> weeklyMoods = new ArrayList<>();

    public void addMood(String mood) {
        if (weeklyMoods.size() < 7) {
            weeklyMoods.add(mood);
        }
    }

    public List<String> getWeeklySummary() {
        return weeklyMoods;
    }
}
