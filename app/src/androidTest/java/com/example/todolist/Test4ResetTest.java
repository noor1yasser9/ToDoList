package com.example.todolist;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.todolist.ui.AddToDoActivity;
import com.example.todolist.ui.ToDoListActivity;
import com.robotium.solo.Solo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class Test4ResetTest {

    @Rule
    public ActivityTestRule<ToDoListActivity> rule  = new  ActivityTestRule<>(ToDoListActivity.class);

    @Test
    public void useAppContext() {

        Solo solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        // ============= Section One ==============
        // Wait for activity: 'com.example.todolist.ui.ToDoListActivity'
        assertTrue(
                "ResetTest failed:" + "Section One:"
                        + "ToDoListActivity did not correctly load.",
                solo.waitForActivity(
                        ToDoListActivity.class, 2000));

        // Click on action bar item
        solo.clickOnActionBarItem(0x1);

        // Click on Add New To Do Item
        solo.clickOnView(solo.getView(com.example.todolist.R.id.footerView));

        // Wait for activity: 'com.example.todolist.ui.AddToDoActivity'
        assertTrue(
                "ResetTest failed:" + "Section One:"
                        + "AddToDoActivity did not correctly load.",
                solo.waitForActivity(AddToDoActivity.class));

        // Hide the soft keyboard
        solo.hideSoftKeyboard();

        // Enter the text: 'Simple Task'
        solo.clearEditText((android.widget.EditText) solo
                .getView(com.example.todolist.R.id.title));
        solo.enterText((android.widget.EditText) solo
                .getView(com.example.todolist.R.id.title), "Simple Task");

        // Hide the soft keyboard
        solo.hideSoftKeyboard();
        // Click on Done:
        solo.clickOnView(solo.getView(com.example.todolist.R.id.statusDone));

        // Click on High
        solo.clickOnView(solo
                .getView(com.example.todolist.R.id.highPriority));

        // Click on Reset
        solo.clickOnView(solo.getView(com.example.todolist.R.id.resetButton));

        // Click on Submit
        solo.clickOnView(solo
                .getView(com.example.todolist.R.id.submitButton));

        // ============= Section Two =================
        // Checks that reset button reset the text
        assertFalse("ResetTest failed:" + "Section Two:"
                        + "Title of ToDo Task was not correctly reset.",
                solo.searchText("Simple Task"));

        // Makes sure that the check box is not checked
        assertFalse("ResetTest failed:" + "SectionTwo:"
                        + "Done status of ToDo Task was not correctly reset",
                solo.isCheckBoxChecked(0));

        // Makes sure that the priority was reset to Medium
        assertTrue("ResetTest failed:" + "Section Two:"
                        + "Priority of ToDo Task was not correctly reset",
                solo.searchText("MED"));

        // Clicks on the Done box
        solo.clickOnCheckBox(0);

        // Makes sure that was able to correctly change completion status from
        // ToDoListActivity
        assertTrue(
                "ResetTest failed:"
                        + "Section Two:"
                        + "Was not able to modify Done status of ToDo Task from ToDoListActivity",
                solo.isCheckBoxChecked(0));
    }


}
