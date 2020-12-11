package com.example.todolist;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

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
public class Test1AddTask {

    @Rule
    public ActivityTestRule<ToDoListActivity> rule  = new  ActivityTestRule<>(ToDoListActivity.class);

    @Test
    public void useAppContext() {

        Solo solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        // ============= Section One ===============
        // Wait for activity: 'com.example.todolist.ui.ToDoListActivity'
        assertTrue("SubmitTest failed:" + "Section One:" + "ToDoListActivity did not load correctly.", solo.waitForActivity(ToDoListActivity.class, 2000));

        // Click on action bar item to delete all items
        solo.clickOnActionBarItem(0x1);

        // Click on Add New To Do Item
        solo.clickOnView(solo.getView(R.id.footerView));

        // Hide the soft keyboard
        solo.hideSoftKeyboard();

        // Enter the text: 'Simple Task'
        solo.clearEditText((android.widget.EditText) solo.getView(R.id.title));
        solo.enterText((android.widget.EditText) solo.getView(R.id.title), "Simple Task");

        // Hide the soft keyboard
        solo.hideSoftKeyboard();
        // Click on Done:
        solo.clickOnView(solo.getView(R.id.statusDone));
        // Click on Low
        solo.clickOnView(solo.getView(R.id.lowPriority));
        // Click on Submit
        solo.clickOnView(solo.getView(R.id.submitButton));

        // ================= Section Two ================
        // Wait for activity: 'com.example.todolist.ui.ToDoListActivity'
        assertTrue("SubmitTest failed:" + "Section Two:" + "ToDoListActivity did not load correctly after pressing submit.", solo.waitForActivity(ToDoListActivity.class));
        assertTrue("SubmitTest failed:" + "Section Two:" + "Title was not correctly entered in the ToDoManager", solo.searchText("Simple Task"));
        assertTrue("SubmitTest failed:" + "Section Two:" + "Priority was not correctly entered in the ToDoManager", solo.searchText("LOW"));
        assertTrue("SubmitTest failed:" + "Section Two:" + "Did not correctly set completion status.", solo.isCheckBoxChecked(0));

    }
}
