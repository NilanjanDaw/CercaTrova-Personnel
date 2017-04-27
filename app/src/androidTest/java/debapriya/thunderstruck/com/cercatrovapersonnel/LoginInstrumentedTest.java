package debapriya.thunderstruck.com.cercatrovapersonnel;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginInstrumentedTest {

    @Rule
    public IntentsTestRule<LoginActivity> mActivityRule
            = new IntentsTestRule<>(LoginActivity.class);
    @Test
    public void testLoginUserName() throws Exception {

        onView(withId(R.id.email)).perform(clearText());
        onView(withId(R.id.email))
                .perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
        onView(withId(R.id.email)).check(matches(hasFocus()));
    }

    @Test
    public void invalidPassword() throws Exception {
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
    public void  invalidCredentials() {
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
    public void validCredentials() {
        onView(withId(R.id.email)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.email))
                .perform(typeText("P7942"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("abc123"), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
    }
}
