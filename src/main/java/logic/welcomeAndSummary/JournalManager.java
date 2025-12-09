
package welcomelogic;

import java.util.HashMap;
import java.util.Map;

public class JournalManager {

    private Map<String, String> journals = new HashMap<>();

    // Create or overwrite journal
    public void createOrUpdateJournal(String title, String content) {
        journals.put(title, content);
    }

    // View a journal (returns null if not found)
    public String viewJournal(String title) {
        return journals.get(title);
    }

    // Get all journal titles
    public Map<String, String> getAllJournals() {
        return journals;
    }
}
