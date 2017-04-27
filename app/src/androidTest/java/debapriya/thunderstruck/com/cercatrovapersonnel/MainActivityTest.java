package debapriya.thunderstruck.com.cercatrovapersonnel;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.LocalBroadcastManager;

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
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by nilanjan on 27-Apr-17.
 * Project client_personnel
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule
            = new IntentsTestRule<MainActivity>(MainActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            ArrayList<Double> arrayList = new ArrayList<>();
            arrayList.add(99.0);
            arrayList.add(102.45);
            Location location = new Location("Point", arrayList);

            EmergencyPersonnel personnel = new EmergencyPersonnel("P7942", "", "", "", "", "", 1, "", "", location);
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Intent result = new Intent(targetContext, MainActivity.class);
            result.putExtra("profile_data", personnel);
            return result;
        }
    };

    @Test
    public void activateButton() {
        onView((withId(R.id.unit_status))).check(matches(withText(mActivityRule.getActivity()
                .getString(R.string.unit_status_inactive))));
        onView((withId(R.id.unit_status_question))).check(matches(withText(mActivityRule.getActivity()
                .getString(R.string.unit_active_question))));
        onView(withId(R.id.button_yes)).perform(click());
        onView((withId(R.id.unit_status))).check(matches(withText(mActivityRule.getActivity().getString(R.string.unit_status_active))));
        onView((withId(R.id.unit_status_question))).check(matches(withText(mActivityRule.getActivity()
                .getString(R.string.unit_inactive_question))));
    }

    @Test
    public void deactivateButton() {
        onView((withId(R.id.unit_status))).check(matches(withText(mActivityRule.getActivity()
                .getString(R.string.unit_status_inactive))));
        onView((withId(R.id.unit_status_question))).check(matches(withText(mActivityRule.getActivity()
                .getString(R.string.unit_active_question))));
        onView(withId(R.id.button_yes)).perform(click());
        onView((withId(R.id.unit_status))).check(matches(withText(mActivityRule.getActivity().getString(R.string.unit_status_active))));
        onView((withId(R.id.unit_status_question))).check(matches(withText(mActivityRule.getActivity()
                .getString(R.string.unit_inactive_question))));
        onView((withId(R.id.unit_status))).check(matches(withText(mActivityRule.getActivity().getString(R.string.unit_status_active))));
        onView((withId(R.id.unit_status_question))).check(matches(withText(mActivityRule.getActivity()
                .getString(R.string.unit_inactive_question))));
        onView(withId(R.id.button_yes)).perform(click());
        onView((withId(R.id.unit_status))).check(matches(withText(mActivityRule.getActivity()
                .getString(R.string.unit_status_inactive))));
        onView((withId(R.id.unit_status_question))).check(matches(withText(mActivityRule.getActivity()
                .getString(R.string.unit_active_question))));
    }

    @Test
    public void BroadcastListenerTest() throws InterruptedException {
        ArrayList<Double> arrayList = new ArrayList<>();
        arrayList.add(99.0);
        arrayList.add(102.45);
        Location location = new Location("Point", arrayList);
        User user = new User("1234", "abc", "dce", "aa", "123", "add", 12, "M", "A", "abc123", location, "adasda");
        Context context = mActivityRule.getActivity();
        LocalBroadcastManager.getInstance(context).registerReceiver(mActivityRule.getActivity().getBroadcastReceiver(),
                new IntentFilter(mActivityRule.getActivity().getString(R.string.broadcast_intent_filter)));
        Intent intent = new Intent(mActivityRule.getActivity().getString(R.string.broadcast_intent_filter));
        intent.putExtra("user_profile_data", user);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        Thread.sleep(2000);
        intended(hasComponent(AlertActivity.class.getName()));
    }
}