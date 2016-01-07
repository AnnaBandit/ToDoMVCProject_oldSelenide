package ua.com.anya.TodoMVCTest_v3.pagemodules.tests;

import org.junit.Test;
import ua.com.anya.TodoMVCTest_v3.pagemodules.components.AtTodoMVCPageWithClearedDataAfterEachTest;
import ua.com.anya.TodoMVCTest_v3.pagemodules.pages.TodoMVCPage;

import static ua.com.anya.TodoMVCTest_v3.pagemodules.pages.TodoMVCPage.*;
import static ua.com.anya.TodoMVCTest_v3.pagemodules.components.Task.Status.ACTIVE;
import static ua.com.anya.TodoMVCTest_v3.pagemodules.components.Task.Status.COMPLETED;
import static ua.com.anya.TodoMVCTest_v3.pagemodules.components.Task.aTask;

public class AtActiveFilterTest extends AtTodoMVCPageWithClearedDataAfterEachTest {
    TodoMVCPage page = new TodoMVCPage();

    @Test
    public void testEditByPressingEnter(){
        givenAtActive("a");

        startEdit("a", "a-edited").pressEnter();
        assertVisibleTasks("a-edited");
        assertItemsLeft(1);
    }

    @Test
    public void testCompleteAll(){
        givenAtActive("a", "b");

        toggleAll();
        assertVisibleTasksListIsEmpty();
        openCompletedFilter();
        assertVisibleTasks("a", "b");
    }

    @Test
    public void testClearCompleted(){
        givenAtActive(aTask("a", COMPLETED),
                aTask("b", COMPLETED),
                aTask("c", ACTIVE));

        clearCompleted();
        assertExistingTasks("c");
        assertItemsLeft(1);
        openCompletedFilter();
        assertVisibleTasksListIsEmpty();
    }

    @Test
    public void testDelete(){
        givenAtActive("a", "b");

        delete("a");
        assertVisibleTasks("b");
        assertItemsLeft(1);
    }
}
