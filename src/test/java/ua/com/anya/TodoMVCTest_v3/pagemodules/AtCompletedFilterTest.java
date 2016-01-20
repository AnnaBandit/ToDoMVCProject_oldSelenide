package ua.com.anya.TodoMVCTest_v3.pagemodules;

import org.junit.Test;
import org.openqa.selenium.Keys;
import ua.com.anya.TodoMVCTest_v3.pagemodules.testconfigs.AtTodoMVCPageWithClearedDataAfterEachTest;

import static ua.com.anya.TodoMVCTest_v3.pagemodules.pages.Task.Status.COMPLETED;
import static ua.com.anya.TodoMVCTest_v3.pagemodules.pages.Task.aTask;
import static ua.com.anya.TodoMVCTest_v3.pagemodules.pages.TodoMVC.*;

public class AtCompletedFilterTest extends AtTodoMVCPageWithClearedDataAfterEachTest {

    @Test
    public void testCreate(){
        givenAtCompleted("a");

        add("b");
        assertVisibleTasksListIsEmpty();
        assertItemsLeft(2);
        openActiveFilter();
        assertVisibleTasks("a", "b");
    }

    @Test
    public void testEdit(){
        givenAtCompleted(aTask("a", COMPLETED));

        startEdit("a", "a-edited").sendKeys(Keys.ENTER);
        assertVisibleTasks("a-edited");
        assertItemsLeft(0);
    }

    @Test
    public void testDelete(){
        givenAtCompleted(aTask("a", COMPLETED));

        delete("a");
        assertExistingTasksListIsEmpty();
    }

    @Test
    public void testActivateAll(){
        givenAtCompleted(aTask("a", COMPLETED),
                aTask("b", COMPLETED));

        toggleAll();
        assertVisibleTasksListIsEmpty();
        assertItemsLeft(2);
        openActiveFilter();
        assertVisibleTasks("a", "b");
    }
}
