package ua.com.anya.TodoMVCTest_v3.pagemodules;

import org.junit.Test;
import ua.com.anya.TodoMVCTest_v3.pagemodules.testconfigs.AtTodoMVCPageWithClearedDataAfterEachTest;

import static ua.com.anya.TodoMVCTest_v3.pagemodules.pages.Task.Status.ACTIVE;
import static ua.com.anya.TodoMVCTest_v3.pagemodules.pages.Task.Status.COMPLETED;
import static ua.com.anya.TodoMVCTest_v3.pagemodules.pages.Task.aTask;
import static ua.com.anya.TodoMVCTest_v3.pagemodules.pages.TodoMVC.*;

public class AtAllFilterTest extends AtTodoMVCPageWithClearedDataAfterEachTest {

    @Test
    public void testCreate(){
        ensureOpenedTodoMVC();

        add("a", "b");
        assertExistingTasks("a", "b");
        assertItemsLeft(2);
    }

    @Test
    public void testEdit(){
        givenAtAll("a", "b", "c");

        startEdit("b", "b-edited").pressEnter();
        assertExistingTasks("a", "b-edited", "c");
        assertItemsLeft(3);
    }

    @Test
    public void testCompleteAll(){
        givenAtAll("a", "b");

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
        givenAtAll("a");

        startEdit("a", "a-edited").pressEscape();
        assertExistingTasks("a");
        assertItemsLeft(1);
    }

    @Test
    public void testEditAndSaveByClickingOutside(){
        givenAtAll("a", "b");

        startEdit("b", "b-edited");
        add("c");
        assertExistingTasks("a", "b-edited", "c");
        assertItemsLeft(3);
    }

    @Test
    public void testDeleteWhileEditing(){
        givenAtAll("a", "b");

        startEdit("a", "").pressEnter();
        assertExistingTasks("b");
        assertItemsLeft(1);
    }

    @Test
    public void testDelete(){
        ensureOpenedTodoMVC();

        add("a", "b");
        assertExistingTasks("a", "b");

        delete("a");
        assertExistingTasks("b");
        assertItemsLeft(1);
    }

}
