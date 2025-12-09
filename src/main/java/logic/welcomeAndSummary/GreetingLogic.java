
package welcomelogic;

import java.time.LocalTime;
import java.time.ZoneId;

public class GreetingLogic {

    public static String getGreeting(String username) {
        // Get time in GMT+8
        LocalTime timeNow = LocalTime.now(ZoneId.of("GMT+8"));

        String greeting;

        if (timeNow.isBefore(LocalTime.NOON)) {
            greeting = "Good Morning";
        } else if (timeNow.isBefore(LocalTime.of(17, 0))) {
            greeting = "Good Afternoon";
        } else {
            greeting = "Good Evening";
        }

        return greeting + ", " + username + "!";
    }
}


