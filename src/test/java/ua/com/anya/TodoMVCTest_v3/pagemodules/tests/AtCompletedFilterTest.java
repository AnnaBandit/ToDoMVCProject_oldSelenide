package ua.com.anya.TodoMVCTest_v3.pagemodules.tests;

import org.junit.Test;
import ua.com.anya.TodoMVCTest_v3.pagemodules.components.AtTodoMVCPageWithClearedDataAfterEachTest;
import ua.com.anya.TodoMVCTest_v3.pagemodules.pages.TodoMVCPage;

import static ua.com.anya.TodoMVCTest_v3.pagemodules.pages.TodoMVCPage.*;
import static ua.com.anya.TodoMVCTest_v3.pagemodules.components.Task.Status.COMPLETED;
import static ua.com.anya.TodoMVCTest_v3.pagemodules.components.Task.aTask;

public class AtCompletedFilterTest extends AtTodoMVCPageWithClearedDataAfterEachTest {
    TodoMVCPage page = new TodoMVCPage();

    @Test
    public void testCreate(){
        givenAtCompleted(convertTaskTextsIntoActiveTasks("a"));

        add("b");
        assertVisibleTasksListIsEmpty();
        assertItemsLeft(2);
        openActiveFilter();
        assertVisibleTasks("a", "b");
    }

    @Test
    public void testEdit(){
        givenAtCompleted(aTask("a", COMPLETED));

        startEdit("a", "a-edited").pressEnter();
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
