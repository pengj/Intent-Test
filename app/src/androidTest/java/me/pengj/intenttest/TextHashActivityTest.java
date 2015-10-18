package me.pengj.intenttest;


import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.ComponentName;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TextHashActivityTest {


    private static final String TEST_INPUT = "test";

    private static final String TEST_SHA1 = "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3";

    @Rule
    public final ActivityTestRule<TextHashActivity> rule =
            new ActivityTestRule<>(TextHashActivity.class, false,
                    false);  // not launch the activity

    @Test
    public void testSha1Input() throws InterruptedException {

        rule.launchActivity(null);

        onView(withId(R.id.edittext_input)).perform(typeText(TEST_INPUT));
        closeSoftKeyboard();

        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.textview_sha1))
                .check(matches(withText(TEST_SHA1)));
    }

    @Test
    public void testCorrectIntent() throws InterruptedException {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(TextHashActivity.SUPPORT_TYPE);
        intent.putExtra(Intent.EXTRA_TEXT, TEST_INPUT);

        rule.launchActivity(intent);

        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.textview_sha1))
                .check(matches(withText(TEST_SHA1)));
    }

    @Test
    public void testWrongIntent() {

        Intent intent = new Intent();

        rule.launchActivity(intent);

        onView(withId(R.id.textview_sha1))
                .check(matches(withHint(R.string.hint_sha1_textview)));

    }

    @Test
    public void testFromOtherApp() throws InterruptedException {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType(TextHashActivity.SUPPORT_TYPE);
        intent.putExtra(Intent.EXTRA_TEXT, TEST_INPUT);

        // There maybe more than one app will handle this requirement
        String packageName = InstrumentationRegistry.getTargetContext().getPackageName();
        ComponentName componentName = new ComponentName(packageName,
                TextHashActivity.class.getName());
        intent.setComponent(componentName);

        Intents.init();
        InstrumentationRegistry.getContext().startActivity(intent);
        Matcher<Intent> expectedIntent = hasComponent(componentName);
        intended(expectedIntent);
        Intents.release();

        TimeUnit.SECONDS.sleep(1);
        onView(withId(R.id.textview_sha1))
                .check(matches(withText(TEST_SHA1)));

    }

}
