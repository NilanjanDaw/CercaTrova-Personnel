package debapriya.thunderstruck.com.cercatrovapersonnel;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import debapriya.thunderstruck.com.cercatrovapersonnel.support.EmergencyPersonnel;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.Location;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by nilanjan on 28-Apr-17.
 * Project client_personnel
 */
@RunWith(AndroidJUnit4.class)
public class AlertActivityTest {
    @Rule
    public IntentsTestRule<AlertActivity> mActivityRule
            = new IntentsTestRule<AlertActivity>(AlertActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            ArrayList<Double> arrayList = new ArrayList<>();
            arrayList.add(99.0);
            arrayList.add(102.45);
            Location location = new Location("Point", arrayList);
            EmergencyPersonnel personnel = new EmergencyPersonnel("P7942", "", "", "", "", "", 1, "", "", location);
            User user = new User("1234", "abc", "dce", "aa", "123", "add", 12, "M", "A", "abc123", location, "adasda");
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Intent result = new Intent(targetContext, MainActivity.class);
            result.putExtra("profile_data", personnel);
            result.putExtra("user_data", user);
            return result;
        }
    };

    @Test
    public void investigate() {
        onView(withId(R.id.investigate)).perform(click());
        intended(hasComponent(SOSDetailsActivity.class.getName()));
    }
}