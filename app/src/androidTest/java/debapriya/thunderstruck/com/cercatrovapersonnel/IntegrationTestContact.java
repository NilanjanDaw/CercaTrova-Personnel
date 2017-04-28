package debapriya.thunderstruck.com.cercatrovapersonnel;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import debapriya.thunderstruck.com.cercatrovapersonnel.support.Location;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by nilanjan on 29-Apr-17.
 * Project client_personnel
 */
@RunWith(AndroidJUnit4.class)
public class IntegrationTestContact {

    @Rule
    public IntentsTestRule<LoginActivity> mActivityRule
            = new IntentsTestRule<>(LoginActivity.class);
    @Test
    public void ontactButton() throws Exception {
        onView(withId(R.id.email)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.email))
                .perform(typeText("P7942"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("abc123"), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());

        ArrayList<Double> arrayList = new ArrayList<>();
        arrayList.add(99.0);
        arrayList.add(102.45);
        Location location = new Location("Point", arrayList);
        User user = new User("1234", "abc", "dce", "aa", "123", "add", 12, "M", "A", "abc123", location, "adasda");
        Activity activity = getCurrentActivity();
        MainActivity mainActivity = (MainActivity)activity;
        LocalBroadcastManager.getInstance(activity).registerReceiver(mainActivity.getBroadcastReceiver(),
                new IntentFilter(mActivityRule.getActivity().getString(R.string.broadcast_intent_filter)));
        Intent intent = new Intent(mActivityRule.getActivity().getString(R.string.broadcast_intent_filter));
        intent.putExtra("user_profile_data", user);
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);

        Thread.sleep(2000);
        onView(withId(R.id.investigate)).perform(click());
        String phoneNumber = "123";
        onView(withId(R.id.contact)).perform(click());
        intended(allOf(
                hasAction(Intent.ACTION_DIAL),
                hasData(Uri.parse("tel:" + phoneNumber))
        ));
    }
    private Activity getCurrentActivity() {
        final Activity[] activity = new Activity[1];
        onView(isRoot()).check(new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                activity[0] = (Activity) view.getContext();
            }
        });
        return activity[0];
    }
}
