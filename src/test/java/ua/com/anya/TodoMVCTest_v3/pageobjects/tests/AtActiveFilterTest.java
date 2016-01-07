package ua.com.anya.TodoMVCTest_v3.pageobjects.tests;

import org.junit.Test;
import ua.com.anya.TodoMVCTest_v3.pageobjects.components.AtTodoMVCPageWithClearedDataAfterEachTest;
import ua.com.anya.TodoMVCTest_v3.pageobjects.pages.TodoMVCPage;

import static ua.com.anya.TodoMVCTest_v3.pageobjects.components.Task.Status.*;
import static ua.com.anya.TodoMVCTest_v3.pageobjects.components.Task.aTask;

public class AtActiveFilterTest extends AtTodoMVCPageWithClearedDataAfterEachTest{
    TodoMVCPage page = new TodoMVCPage();

    @Test
    public void testEditByPressingEnter(){
        page.givenAtActive("a");

        page.startEdit("a", "a-edited").pressEnter();
        page.assertVisibleTasks("a-edited");
        page.assertItemsLeft(1);
    }

    @Test
    public void testCompleteAll(){
        page.givenAtActive("a", "b");

        page.toggleAll();
        page.assertVisibleTasksListIsEmpty();
        page.openCompletedFilter();
        page.assertVisibleTasks("a", "b");
    }

    @Test
    public void testClearCompleted(){
        page.givenAtActive(aTask("a", COMPLETED),
                aTask("b", COMPLETED),
                aTask("c", ACTIVE));

        page.clearCompleted();
        page.assertExistingTasks("c");
        page.assertItemsLeft(1);
        page.openCompletedFilter();
        page.assertVisibleTasksListIsEmpty();
    }

    @Test
    public void testDelete(){
        page.givenAtActive("a", "b");

        page.delete("a");
        page.assertVisibleTasks("b");
        page.assertItemsLeft(1);
    }
}
