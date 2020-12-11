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
public class Test3CancelTest {

    @Rule
    public ActivityTestRule<ToDoListActivity> rule  = new  ActivityTestRule<>(ToDoListActivity.class);

    @Test
    public void useAppContext() {

        Solo solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        // Wait for activity: 'com.example.todolist.ui.ToDoListActivity'
        assertTrue("CancelTest failed:" + "Section One:" + "ToDoListActivity did not load correctly.",
                solo.waitForActivity(
                        ToDoListActivity.class, 2000));

        // Click on action bar item
        solo.clickOnActionBarItem(0x1);

        // Click on Add New To Do Item
        solo.clickOnView(solo.getView(com.example.todolist.R.id.footerView));

        // Wait for activity: 'com.example.todolist.ui.AddToDoActivity'
        assertTrue("CancelTest failed:" +
                        "Section One:" +
                        "AddToDoActivity did not load correctly.",
                solo.waitForActivity(AddToDoActivity.class));

        // Hide the soft keyboard
        solo.hideSoftKeyboard();
        // Enter the text: 'Simple Task 1'
        solo.clearEditText((android.widget.EditText) solo
                .getView(com.example.todolist.R.id.title));
        solo.enterText((android.widget.EditText) solo
                .getView(com.example.todolist.R.id.title), "Simple Task 1");
        // Hide the soft keyboard
        solo.hideSoftKeyboard();
        // Click on Done:
        solo.clickOnView(solo.getView(com.example.todolist.R.id.statusDone));
        // Click on High
        solo.clickOnView(solo
                .getView(com.example.todolist.R.id.highPriority));

        // Click on Cancel
        solo.clickOnView(solo
                .getView(com.example.todolist.R.id.cancelButton));

        // Click on Add New To Do Item
        solo.clickOnView(solo.getView(com.example.todolist.R.id.footerView));
        // Hide the soft keyboard
        solo.hideSoftKeyboard();
        // Enter the text: 'Simple Task 2'
        solo.clearEditText((android.widget.EditText) solo
                .getView(com.example.todolist.R.id.title));
        solo.enterText((android.widget.EditText) solo
                .getView(com.example.todolist.R.id.title), "Simple Task 2");

        // Hide the soft keyboard
        solo.hideSoftKeyboard();
        // Click on Done:
        solo.clickOnView(solo.getView(com.example.todolist.R.id.statusDone));
        // Click on Low
        solo.clickOnView(solo.getView(com.example.todolist.R.id.lowPriority));

        // Click on Submit
        solo.clickOnView(solo
                .getView(com.example.todolist.R.id.submitButton));

        // ================ Section Two ===================
        // Wait for activity: 'com.example.todolist.ui.ToDoListActivity'
        assertTrue("CancelTest failed:" +
                        "Section Two:" +
                        "ToDoListActivity did not load correctly.",
                solo.waitForActivity(ToDoListActivity.class));

        assertFalse("CancelTest failed:" +
                        "Section Two:" +
                        "Did not correctly cancel the creation of a ToDo Task.",
                solo.searchText("Simple Task 1"));

        assertTrue("CancelTest failed:" +
                        "Section Two:" +
                        "Did not correctly set title of ToDo Task following cancel.",
                solo.searchText("Simple Task 2"));
        assertTrue("CancelTest failed:" +
                        "Section Two:" +
                        "Did not correctly set priority of ToDo Task following cancel.",
                solo.searchText("LOW"));


    }


}
