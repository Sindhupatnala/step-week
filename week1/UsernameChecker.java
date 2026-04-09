import java.util.*;

class UsernameChecker {

    // Stores username -> userId
    private HashMap<String, Integer> usernameMap;

    // Stores username -> attempt frequency
    private HashMap<String, Integer> attemptCount;

    public UsernameChecker() {
        usernameMap = new HashMap<>();
        attemptCount = new HashMap<>();
    }

    // Register a username
    public void registerUser(String username, int userId) {
        usernameMap.put(username, userId);
    }

    // Check availability in O(1)
    public boolean checkAvailability(String username) {

        // Track attempt frequency
        attemptCount.put(username,
                attemptCount.getOrDefault(username, 0) + 1);

        return !usernameMap.containsKey(username);
    }

    // Suggest alternatives if username is taken
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        if (!usernameMap.containsKey(username)) {
            suggestions.add(username);
            return suggestions;
        }

        // Append numbers
        for (int i = 1; i <= 5; i++) {
            String newName = username + i;
            if (!usernameMap.containsKey(newName)) {
                suggestions.add(newName);
            }
        }

        // Replace underscore with dot
        String modified = username.replace("_", ".");
        if (!usernameMap.containsKey(modified)) {
            suggestions.add(modified);
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String mostAttempted = null;
        int max = 0;

        for (String user : attemptCount.keySet()) {
            if (attemptCount.get(user) > max) {
                max = attemptCount.get(user);
                mostAttempted = user;
            }
        }

        return mostAttempted + " (" + max + " attempts)";
    }

    // Demo
    public static void main(String[] args) {

        UsernameChecker system = new UsernameChecker();

        // Existing users
        system.registerUser("john_doe", 1);
        system.registerUser("admin", 2);

        System.out.println(system.checkAvailability("john_doe")); // false
        System.out.println(system.checkAvailability("jane_smith")); // true

        System.out.println(system.suggestAlternatives("john_doe"));

        System.out.println(system.getMostAttempted());
    }
}
