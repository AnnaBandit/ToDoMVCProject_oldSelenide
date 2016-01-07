package ua.com.anya.TodoMVCTest_v3.pagemodules.tests;

import org.junit.Test;
import ua.com.anya.TodoMVCTest_v3.pagemodules.components.AtTodoMVCPageWithClearedDataAfterEachTest;
import ua.com.anya.TodoMVCTest_v3.pagemodules.pages.TodoMVCPage;

import static ua.com.anya.TodoMVCTest_v3.pagemodules.pages.TodoMVCPage.*;
import static ua.com.anya.TodoMVCTest_v3.pagemodules.components.Task.Status.ACTIVE;
import static ua.com.anya.TodoMVCTest_v3.pagemodules.components.Task.Status.COMPLETED;
import static ua.com.anya.TodoMVCTest_v3.pagemodules.components.Task.aTask;

public class AtAllFilterTest extends AtTodoMVCPageWithClearedDataAfterEachTest {

    TodoMVCPage page = new TodoMVCPage();

    @Test
    public void testCreate(){
        givenAtAll();

        add("a", "b");
        assertExistingTasks("a", "b");
        assertItemsLeft(2);
    }

    @Test
    public void testEdit(){
        given("a", "b", "c");

        startEdit("b", "b-edited").pressEnter();
        assertExistingTasks("a", "b-edited", "c");
        assertItemsLeft(3);
    }

    @Test
    public void testCompleteAll(){
        given("a", "b");

        toggleAll();
        assertExistingTasks("a", "b");
        assertItemsLeft(0);
        openCompletedFilter();
        assertVisibleTasks("a", "b");
    }

    @Test
    public void testClearCompleted(){
        given(aTask("a", COMPLETED),
                aTask("b", COMPLETED),
                aTask("c", ACTIVE));

        clearCompleted();
        assertExistingTasks("c");
        assertItemsLeft(1);
    }

    @Test
    public void testActivateAll(){
        given(aTask("a", COMPLETED),
                aTask("b", COMPLETED));

        toggleAll();
        assertItemsLeft(2);
        openActiveFilter();
        assertVisibleTasks("a", "b");
    }

    @Test
    public void testActivate(){
        given(aTask("a", COMPLETED),
                aTask("b", COMPLETED));

        toggle("a");
        assertItemsLeft(1);
        openActiveFilter();
        assertVisibleTasks("a");
    }

    @Test
    public void testCancelEditingByESC(){
        given("a");

        startEdit("a", "a-edited").pressEscape();
        assertExistingTasks("a");
        assertItemsLeft(1);
    }

    @Test
    public void testEditAndSaveByClickingOutside(){
        given("a", "b");

        startEdit("b", "b-edited");
        add("c");
        assertExistingTasks("a", "b-edited", "c");
        assertItemsLeft(3);
    }

    @Test
    public void testDeleteWhileEditing(){
        given("a", "b");

        startEdit("a", "").pressEnter();
        assertExistingTasks("b");
        assertItemsLeft(1);
    }

    @Test
    public void testDelete(){
        givenAtAll();

        add("a", "b");
        assertExistingTasks("a", "b");

        delete("a");
        assertExistingTasks("b");
        assertItemsLeft(1);
    }

}
