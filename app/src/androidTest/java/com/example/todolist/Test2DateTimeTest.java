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
public class Test2DateTimeTest {

    @Rule
    public ActivityTestRule<ToDoListActivity> rule  = new  ActivityTestRule<>(ToDoListActivity.class);

    @Test
    public void useAppContext() {

        Solo solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        // ============== Section One ================
        // Wait for activity: 'com.example.todolist.ui.ToDoListActivity'
        assertTrue("DateTimeTest failed:" + "Section One:" + "ToDoListActivity did not correctly load.", solo.waitForActivity(ToDoListActivity.class, 2000));

        // Click on action bar item to delete all items
        solo.clickOnActionBarItem(0x1);

        // Click on Add New To Do Item
        solo.clickOnView(solo.getView(com.example.todolist.R.id.footerView));

        // Wait for activity: 'com.example.todolist.ui.AddToDoActivity'
        assertTrue("DateTimeTest failed:" + "Section One:" + "AddToDoActivity did not correctly load.", solo.waitForActivity(AddToDoActivity.class));

        // Hide the soft keyboard
        solo.hideSoftKeyboard();

        // Enter the text: 'Simple Task'
        solo.clearEditText((android.widget.EditText) solo.getView(com.example.todolist.R.id.title));
        solo.enterText((android.widget.EditText) solo.getView(com.example.todolist.R.id.title), "Simple Task");

        // Hide the soft keyboard
        solo.hideSoftKeyboard();

        // Click on Done:
        solo.clickOnView(solo.getView(com.example.todolist.R.id.statusDone));
        // Click on Low
        solo.clickOnView(solo.getView(com.example.todolist.R.id.lowPriority));
        // Click on Choose Date
        solo.clickOnView(solo.getView(com.example.todolist.R.id.date_picker_button));

        // Wait for dialog
        solo.waitForDialogToOpen(10000);
        // Enter the text: 'Feb'
        solo.clearEditText((android.widget.EditText) solo.getView("numberpicker_input"));
        solo.enterText((android.widget.EditText) solo.getView("numberpicker_input"),"Feb");
        // Enter the text: '28'
        solo.clearEditText((android.widget.EditText) solo.getView("numberpicker_input", 1));
        solo.enterText((android.widget.EditText) solo.getView("numberpicker_input", 1),"28");
        // Enter the text: '2014'
        solo.clearEditText((android.widget.EditText) solo.getView("numberpicker_input", 2));
        solo.enterText((android.widget.EditText) solo.getView("numberpicker_input", 2),"2014");

        // Really set the date
        solo.setDatePicker(0, 2014, 1, 28);

        // Click on Done
        solo.clickOnView(solo.getView(android.R.id.button1));

        // Click on Choose Time
        solo.clickOnView(solo.getView(com.example.todolist.R.id.time_picker_button));
        // Wait for dialog
        solo.waitForDialogToOpen(10000);
        // Enter the text: '9'
        solo.clearEditText((android.widget.EditText) solo.getView("numberpicker_input"));
        solo.enterText((android.widget.EditText) solo.getView("numberpicker_input"),"9");
        // Enter the text: '19'
        solo.clearEditText((android.widget.EditText) solo.getView("numberpicker_input", 1));
        solo.enterText((android.widget.EditText) solo.getView("numberpicker_input", 1),"19");

        // Really set the time
        solo.setTimePicker(0, 9, 19);

        // Click on Done
        solo.clickOnView(solo.getView(android.R.id.button1));

        // Click on Submit
        solo.clickOnView(solo.getView(com.example.todolist.R.id.submitButton));

        // Wait for activity: 'com.example.todolist.ui.ToDoListActivity'
        assertTrue("DateTimeTest failed:" + "Section One:" + "ToDoListActivity did not load correctly", solo.waitForActivity(ToDoListActivity.class, 2000));

        // ============== Section Two =============
        // Makes sure the title was changed correctly
        assertTrue("DateTimeTest failed:" + "Section Two:" + "Did not modify title correctly",solo.searchText("Simple Task"));

        // Checks to see if the status was changed correctly
        assertTrue("DateTimeTest failed:" + "Section Two:" + "Did not change status correctly",solo.isCheckBoxChecked(0));

        // Checks to make sure the priority was correctly set
        assertTrue("DateTimeTest failed:" + "Section Two:" + "Did not correctly set priority",solo.searchText("LOW"));

        // Checks to make sure the Date was correctly set
        assertTrue("DateTimeTest failed:" + "Section Two:" + "Did not correctly set the date",solo.searchText("2014-02-28"));

        // Checks to make sure the Time was correctly set
        assertTrue("DateTimeTest failed:" + "Section Two:" + "Did not correctly set the time",solo.searchText("09:19:00"));

    }

}
