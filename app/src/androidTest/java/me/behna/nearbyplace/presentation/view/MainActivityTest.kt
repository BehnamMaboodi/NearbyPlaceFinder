package me.behna.nearbyplace.presentation.view


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import me.behna.nearbyplace.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val textInputEditText = onView(
            allOf(
                withId(R.id.edt_search),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("new york"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.edt_search), withText("new york"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(pressImeActionButton())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.edt_search),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(click())

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.edt_search),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText4.perform(click())

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.edt_search),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText5.perform(replaceText("new york"), closeSoftKeyboard())

        val textInputEditText6 = onView(
            allOf(
                withId(R.id.edt_search), withText("new york"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText6.perform(pressImeActionButton())

        val button = onView(
            allOf(
                withText("new york"),
                withParent(withParent(withId(R.id.scroll_view_search_results))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val editText = onView(
            allOf(
                withId(R.id.edt_search), withText("new york"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        editText.check(matches(withText("new york")))

        val textView = onView(
            allOf(
                withText("Results for"),
                withParent(withParent(withId(R.id.scroll_view_search_results))),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
