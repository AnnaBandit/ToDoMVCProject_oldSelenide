package ua.com.anya.TodoMVCTest_v3.pageobjects.tests;

import org.junit.Test;
import ua.com.anya.TodoMVCTest_v3.pageobjects.components.AtTodoMVCPageWithClearedDataAfterEachTest;
import ua.com.anya.TodoMVCTest_v3.pageobjects.pages.TodoMVCPage;

import static ua.com.anya.TodoMVCTest_v3.pageobjects.components.Task.Status.ACTIVE;
import static ua.com.anya.TodoMVCTest_v3.pageobjects.components.Task.Status.COMPLETED;
import static ua.com.anya.TodoMVCTest_v3.pageobjects.components.Task.aTask;

public class AtAllFilterTest extends AtTodoMVCPageWithClearedDataAfterEachTest{

    TodoMVCPage page = new TodoMVCPage();

    @Test
    public void testCreate(){
        page.givenAtAll();

        page.add("a", "b");
        page.assertExistingTasks("a", "b");
        page.assertItemsLeft(2);
    }

    @Test
    public void testEdit(){
        page.given("a", "b", "c");

        page.startEdit("b", "b-edited").pressEnter();
        page.assertExistingTasks("a", "b-edited", "c");
        page.assertItemsLeft(3);
    }

    @Test
    public void testCompleteAll(){
        page.given("a", "b");

        page.toggleAll();
        page.assertExistingTasks("a", "b");
        page.assertItemsLeft(0);
        page.openCompletedFilter();
        page.assertVisibleTasks("a", "b");
    }

    @Test
    public void testClearCompleted(){
        page.given(aTask("a", COMPLETED),
                aTask("b", COMPLETED),
                aTask("c", ACTIVE));

        page.clearCompleted();
        page.assertExistingTasks("c");
        page.assertItemsLeft(1);
    }

    @Test
    public void testActivateAll(){
        page.given(aTask("a", COMPLETED),
                aTask("b", COMPLETED));

        page.toggleAll();
        page.assertItemsLeft(2);
        page.openActiveFilter();
        page.assertVisibleTasks("a", "b");
    }

    @Test
    public void testActivate(){
        page.given(aTask("a", COMPLETED),
                aTask("b", COMPLETED));

        page.toggle("a");
        page.assertItemsLeft(1);
        page.openActiveFilter();
        page.assertVisibleTasks("a");
    }

    @Test
    public void testCancelEditingByESC(){
        page.given("a");

        page.startEdit("a", "a-edited").pressEscape();
        page.assertExistingTasks("a");
        page.assertItemsLeft(1);
    }

    @Test
    public void testEditAndSaveByClickingOutside(){
        page.given("a", "b");

        page.startEdit("b", "b-edited");
        page.add("c");
        page.assertExistingTasks("a", "b-edited", "c");
        page.assertItemsLeft(3);
    }

    @Test
    public void testDeleteWhileEditing(){
        page.given("a", "b");

        page.startEdit("a", "").pressEnter();
        page.assertExistingTasks("b");
        page.assertItemsLeft(1);
    }

    @Test
    public void testDelete(){
        page.givenAtAll();

        page.add("a", "b");
        page.assertExistingTasks("a", "b");

        page.delete("a");
        page.assertExistingTasks("b");
        page.assertItemsLeft(1);
    }

}
