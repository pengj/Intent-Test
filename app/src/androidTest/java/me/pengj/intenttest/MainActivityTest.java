package me.pengj.intenttest;


import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private Matcher<Intent> expectedIntent;

    @Rule
    public final ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setupIntents() {

        expectedIntent = allOf(
                toPackage(InstrumentationRegistry.getTargetContext().getPackageName()),
                isInternal());
    }

    @Test
    public void testLoginIntent() {

        Intents.init();
        onView(withId(R.id.button_login)).perform(click());
        intended(expectedIntent);
        Intents.release();
    }

    @Test
    public void testLoginPass() {

        Instrumentation.ActivityResult activityResult = new Instrumentation.ActivityResult(
                Activity.RESULT_OK, new Intent());

        Intents.init();
        intending(expectedIntent).respondWith(activityResult);
        onView(withId(R.id.button_login)).perform(click());
        intended(expectedIntent);
        Intents.release();

        onView(withId(R.id.button_login)).check(matches(withText(R.string.pass_login)));

    }

    @Test
    public void testLoginCancel() {

        Instrumentation.ActivityResult activityResult = new Instrumentation.ActivityResult(
                Activity.RESULT_CANCELED, new Intent());

        Intents.init();
        intending(expectedIntent).respondWith(activityResult);
        onView(withId(R.id.button_login)).perform(click());
        intended(expectedIntent);
        Intents.release();

        onView(withId(R.id.button_login)).check(matches(withText(R.string.cancel_login)));

    }

}
