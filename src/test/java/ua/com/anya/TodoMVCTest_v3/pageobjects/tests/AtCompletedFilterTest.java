package ua.com.anya.TodoMVCTest_v3.pageobjects.tests;

import org.junit.Test;
import ua.com.anya.TodoMVCTest_v3.pageobjects.components.AtTodoMVCPageWithClearedDataAfterEachTest;
import ua.com.anya.TodoMVCTest_v3.pageobjects.pages.TodoMVCPage;

import static ua.com.anya.TodoMVCTest_v3.pageobjects.components.Task.Status.COMPLETED;
import static ua.com.anya.TodoMVCTest_v3.pageobjects.components.Task.aTask;

public class AtCompletedFilterTest extends AtTodoMVCPageWithClearedDataAfterEachTest{
    TodoMVCPage page = new TodoMVCPage();

    @Test
    public void testCreate(){
        page.givenAtCompleted(page.convertTaskTextsIntoActiveTasks("a"));

        page.add("b");
        page.assertVisibleTasksListIsEmpty();
        page.assertItemsLeft(2);
        page.openActiveFilter();
        page.assertVisibleTasks("a", "b");
    }

    @Test
    public void testEdit(){
        page.givenAtCompleted(aTask("a", COMPLETED));

        page.startEdit("a", "a-edited").pressEnter();
        page.assertVisibleTasks("a-edited");
        page.assertItemsLeft(0);
    }

    @Test
    public void testDelete(){
        page.givenAtCompleted(aTask("a", COMPLETED));

        page.delete("a");
        page.assertExistingTasksListIsEmpty();
    }

    @Test
    public void testActivateAll(){
        page.givenAtCompleted(aTask("a", COMPLETED),
                aTask("b", COMPLETED));

        page.toggleAll();
        page.assertVisibleTasksListIsEmpty();
        page.assertItemsLeft(2);
        page.openActiveFilter();
        page.assertVisibleTasks("a", "b");
    }
}
