package me.pengj.intenttest;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {


    private static final String TEST_USER = "foo@example.com";

    private static final String TEST_PASS = "hello";

    private static final String TEST_WRONG_PASS = "hell01";

    @Rule
    public final ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class);


    @Test
    public void testLoginSuccess() throws InterruptedException {

        onView(withId(R.id.email)).perform(typeText(TEST_USER));
        onView(withId(R.id.password)).perform(typeText(TEST_PASS));
        closeSoftKeyboard();

        Intents.init();
        onView(withId(R.id.email_sign_in_button)).perform(click());

        TimeUnit.SECONDS.sleep(2);
        intending(hasExtra(MainActivity.LOGIN_NAME, TEST_USER));

        Intents.release();
    }


    @Test
    public void testLoginFailed() throws InterruptedException {
        onView(withId(R.id.email)).perform(typeText(TEST_USER));
        onView(withId(R.id.password)).perform(typeText(TEST_WRONG_PASS));
        closeSoftKeyboard();

        onView(withId(R.id.email_sign_in_button)).perform(click());

        TimeUnit.SECONDS.sleep(2);
        onView(withId(R.id.password))
                .check(matches(hasErrorText(R.string.error_incorrect_password)));
    }


    // from
    private static Matcher<? super View> hasErrorText(int expectedError) {
        return new ErrorTextMatcher(expectedError);
    }

    private static class ErrorTextMatcher extends TypeSafeMatcher<View> {

        private final String expectedError;

        private ErrorTextMatcher(int expectedError) {
            this.expectedError = InstrumentationRegistry.getTargetContext()
                    .getString(expectedError);
        }

        @Override
        public boolean matchesSafely(View view) {
            if (!(view instanceof EditText)) {
                return false;
            }
            EditText editText = (EditText) view;
            return expectedError.equals(editText.getError());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("with error: " + expectedError);
        }
    }
}
