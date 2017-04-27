package debapriya.thunderstruck.com.cercatrovapersonnel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by nilanjan on 28-Apr-17.
 * Project client_personnel
 */
@RunWith(AndroidJUnit4.class)
public class SOSDetailsActivityTest {
    @Rule
    public IntentsTestRule<SOSDetailsActivity> mActivityRule
            = new IntentsTestRule<SOSDetailsActivity>(SOSDetailsActivity.class) {
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
    public void setupUI() throws Exception {
        ArrayList<Double> arrayList = new ArrayList<>();
        arrayList.add(99.0);
        arrayList.add(102.45);
        Location location = new Location("Point", arrayList);
        User user = new User("1234", "abc", "dce", "aa", "123", "add", 12, "M", "A", "abc123", location, "adasda");
        onView(withId(R.id.name)).check(matches(withText(user.getFirstName() + " " + user.getLastName())));
        onView(withId(R.id.age)).check(matches(withText(Integer.toString(user.getAge()))));
        onView(withId(R.id.address)).check(matches(withText(user.getAddress())));
        onView(withId(R.id.gender)).check(matches(withText(user.getGender())));
        onView(withId(R.id.adhaar_number)).check(matches(withText(user.getAdhaarNumber())));
        onView(withId(R.id.blood_group)).check(matches(withText(user.getBloodGroup())));
    }

    @Test
    public void contactButton() {
        String phoneNumber = "123";
        onView(withId(R.id.contact)).perform(click());
        intended(allOf(
                hasAction(Intent.ACTION_DIAL),
                hasData(Uri.parse("tel:" + phoneNumber))
                ));
    }

    @Test
    public void navigateButton() {

        onView(withId(R.id.navigate)).perform(click());
        intended(hasComponent(MapsActivity.class.getName()));
    }

}