package com.hap.baking;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by luis on 1/20/18.
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BakingAppTest {
    private static final String TAG = BakingAppTest.class.getName();

    @Rule
    public ActivityTestRule<BakingActivity> bakingTestRule = new ActivityTestRule<>(BakingActivity.class);
    @Rule
    public ActivityTestRule<RecipeActivity> recipeTestRule = new ActivityTestRule<>(RecipeActivity.class);
    @Rule
    public ActivityTestRule<DetailActivity> detailTestRule = new ActivityTestRule<>(DetailActivity.class);

    @Test
    public void onAppTest() {
        // select one recipe
        onView(withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // select the ingredients
        onView(withId(R.id.rv_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // back to the ingredients recipe
        onView(withContentDescription("Navigate up"))
                .perform(click());

        // click one of the steps
        onView(withId(R.id.rv_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // check if the step description is visible, then we're done
        onView(Matchers.allOf(withId(R.id.step_detail), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}