package es.iridiobis.temporizador.presentation.ui.splash

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.IntentMatchers.hasAction
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.UiDevice
import android.view.View
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.testtools.CustomMatchers.chooser
import es.iridiobis.temporizador.testtools.CustomMatchers.withHolderNamed
import es.iridiobis.temporizador.testtools.PermissionHandler.allowPermissions
import es.iridiobis.temporizador.testtools.PermissionHandler.allowPermissionsIfNeeded
import es.iridiobis.temporizador.testtools.PermissionHandler.revokePermissions
import io.realm.Realm
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anyOf
import org.junit.*
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class AddTaskTest {

    companion object {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

        @BeforeClass
        fun grantPermissions() {
            allowPermissions(permissions)
        }

        @AfterClass
        fun revokePermissions() {
            revokePermissions(permissions)
        }
    }

    @get:Rule
    val testRule = IntentsTestRule(SplashActivity::class.java, false, false)

    lateinit private var device: UiDevice

    @Before
    fun setUp() {
        val realm = Realm.getDefaultInstance()
        realm.close()
        Realm.deleteRealm(realm.configuration)
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        testRule.launchActivity(null)
    }

    @Test
    fun addTaskTest() {
        //Configure the image to be picked
        val intent = Intent(Intent.ACTION_GET_CONTENT, Uri.parse("content://es.iridiobis.temporizador.provider/kitten.jpeg"))
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, intent)
        intending(chooser(anyOf(hasAction(Intent.ACTION_GET_CONTENT), hasAction(Intent.ACTION_PICK)))).respondWith(result)

        //Press the fab to add a new task
        onView(allOf<View>(withId(R.id.fab), isDisplayed())).perform(click())

        //On the first screen, background selection, click on the button to select a background
        onView(allOf<View>(withId(R.id.bs_action_select), withContentDescription("Select a background"), isDisplayed()))
                .perform(click())

        //In case the device has not
        //allowPermissionsIfNeeded(device)

        //This will lead to the crop image screen, press crop to continue
        onView(allOf<View>(withId(R.id.crop_image_menu_crop), isDisplayed()))
                .perform(click())

        //Back to the background selection, press done
        onView(allOf<View>(withId(R.id.bs_action_done), isDisplayed()))
                .perform(click())

        //Select the crop background option to get the image from the background
        onView(allOf<View>(withId(R.id.is_action_crop), isDisplayed()))
                .perform(click())

        //Press crop to select the image
        onView(allOf<View>(withId(R.id.crop_image_menu_crop), isDisplayed()))
                .perform(click())

        //Press done to go the the thumbnail selection
        onView(allOf<View>(withId(R.id.is_action_done), isDisplayed()))
                .perform(click())

        //Press crop background to select the thumbnail from the background
        onView(allOf<View>(withId(R.id.ts_action_crop), isDisplayed()))
                .perform(click())

        //Press crop to select the thumnail
        onView(allOf<View>(withId(R.id.crop_image_menu_crop), withText("Crop"), withContentDescription("Crop"), isDisplayed()))
                .perform(click())

        //Press done to continue with the process
        onView(allOf<View>(withId(R.id.ts_action_done), withContentDescription("Done"), isDisplayed()))
                .perform(click())

        //Enter the task name
        onView(allOf<View>(withId(R.id.nt_info_name), isDisplayed()))
                .perform(replaceText("Test"), closeSoftKeyboard())

        //Click in the duration to edit it
        onView(allOf<View>(withId(R.id.nt_info_duration), withText("0:00:00"), isDisplayed()))
                .perform(click())

        //Enter 1:00:00 as duration and press ok to set the duration
        onView(allOf<View>(withId(R.id.numPad1), isDisplayed()))
                .perform(click())
        onView(allOf<View>(withId(R.id.numPad00), isDisplayed()))
                .perform(click())
        onView(allOf<View>(withId(R.id.numPad00), isDisplayed()))
                .perform(click())
        onView(allOf<View>(withId(android.R.id.button1), withText("OK")))
                .perform(scrollTo(), click())

        //Check the time is properly set
        onView(withId(R.id.nt_info_duration)).check(matches(withText("1:00:00")))

        //Press done to finish the creation
        onView(allOf<View>(withId(R.id.action_done), withContentDescription("Done"), isDisplayed()))
                .perform(click())

        //Check the task has been added
        onView(withId(R.id.main_tasks))
                .perform(RecyclerViewActions.scrollToHolder(withHolderNamed("Test")))
    }

}
