package debapriya.thunderstruck.com.cercatrovapersonnel;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import debapriya.thunderstruck.com.cercatrovapersonnel.support.Location;
import debapriya.thunderstruck.com.cercatrovapersonnel.support.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by nilanjan on 28-Apr-17.
 * Project client_personnel
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegrationTest {

    @Rule
    public IntentsTestRule<LoginActivity> mActivityRule
            = new IntentsTestRule<>(LoginActivity.class);

    @Test
    public void a_testLoginUserName() throws Exception {

        onView(withId(R.id.email)).perform(clearText());
        onView(withId(R.id.email))
                .perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.email)).check(matches(hasFocus()));
    }

    @Test
    public void b_invalidPassword() throws Exception {
        onView(withId(R.id.email)).perform(clearText());
        onView(withId(R.id.email))
                .perform(typeText("P7942"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.password)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.password)).check(matches(hasFocus()));

        onView(withId(R.id.email)).perform(clearText());
        onView(withId(R.id.email))
                .perform(typeText("P7942"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.password)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.password)).check(matches(hasFocus()));

        onView(withId(R.id.email)).perform(clearText());
        onView(withId(R.id.email))
                .perform(typeText("P7942"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.password)).perform(typeText("abc12"), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.password)).check(matches(hasFocus()));
    }

    @Test
    public void  c_invalidCredentials() {
        onView(withId(R.id.email)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.email))
                .perform(typeText("P0000"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("abc123"), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withText("Login Failed"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity()
                        .getWindow().getDecorView())))).check(matches(isDisplayed()));

        onView(withId(R.id.email)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.email))
                .perform(typeText("P7942"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("abc12345"), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withText("Login Failed"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity()
                        .getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void d_validCredentials() {
        onView(withId(R.id.email)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.email))
                .perform(typeText("P7942"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("abc123"), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void e_activateButton() {
        onView(withId(R.id.email)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.email))
                .perform(typeText("P7942"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("abc123"), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
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
    public void f_deactivateButton() {
        onView(withId(R.id.email)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.email))
                .perform(typeText("P7942"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("abc123"), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
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
    public void g_BroadcastListenerTest() throws InterruptedException {
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
        intended(hasComponent(AlertActivity.class.getName()));
    }

    @Test
    public void h_setupUI() throws Exception {
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
        onView(withId(R.id.name)).check(matches(withText(user.getFirstName() + " " + user.getLastName())));
        onView(withId(R.id.age)).check(matches(withText(Integer.toString(user.getAge()))));
        onView(withId(R.id.address)).check(matches(withText(user.getAddress())));
        onView(withId(R.id.gender)).check(matches(withText(user.getGender())));
        onView(withId(R.id.adhaar_number)).check(matches(withText(user.getAdhaarNumber())));
        onView(withId(R.id.blood_group)).check(matches(withText(user.getBloodGroup())));
    }

    @Test
    public void j_navigateButton() throws Exception {
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
        onView(withId(R.id.navigate)).perform(click());
        intended(hasComponent(MapsActivity.class.getName()));
    }

    @Test
    public void k_MapsActivityNavigation() throws Exception {
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
        onView(withId(R.id.navigate)).perform(click());
        onView(withId(R.id.fab_navigate)).perform(click());
        intended(hasAction(Intent.ACTION_VIEW));
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
