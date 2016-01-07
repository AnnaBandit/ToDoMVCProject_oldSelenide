package ua.com.anya.TodoMVCTest_v3.pagemodules.tests;

import org.junit.Test;
import ua.com.anya.TodoMVCTest_v3.pagemodules.components.AtTodoMVCPageWithClearedDataAfterEachTest;
import ua.com.anya.TodoMVCTest_v3.pagemodules.pages.TodoMVCPage;

import static ua.com.anya.TodoMVCTest_v3.pagemodules.pages.TodoMVCPage.*;

public class LifecycleTest extends AtTodoMVCPageWithClearedDataAfterEachTest {

    @Test
    public void testTasksMainFlowThroughFilters(){
        TodoMVCPage page = new TodoMVCPage();

        givenAtAll();

        add("a");
        toggle("a");
        assertExistingTasks("a");

        openActiveFilter();
        assertVisibleTasksListIsEmpty();

        openCompletedFilter();
        assertVisibleTasks("a");

        //activate
        toggle("a");
        assertVisibleTasksListIsEmpty();

        openActiveFilter();
        assertVisibleTasks("a");

        toggle("a");
        assertVisibleTasksListIsEmpty();

        openAllFilter();
        assertExistingTasks("a");

        delete("a");
        assertExistingTasksListIsEmpty();
    }
}
