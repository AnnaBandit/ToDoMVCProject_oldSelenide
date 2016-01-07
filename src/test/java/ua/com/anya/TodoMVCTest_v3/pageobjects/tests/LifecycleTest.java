package ua.com.anya.TodoMVCTest_v3.pageobjects.tests;

import org.junit.Test;
import ua.com.anya.TodoMVCTest_v3.pageobjects.components.AtTodoMVCPageWithClearedDataAfterEachTest;
import ua.com.anya.TodoMVCTest_v3.pageobjects.pages.TodoMVCPage;

public class LifecycleTest extends AtTodoMVCPageWithClearedDataAfterEachTest{

    @Test
    public void testTasksMainFlowThroughFilters(){
        TodoMVCPage page = new TodoMVCPage();

        page.givenAtAll();

        page.add("a");
        page.toggle("a");
        page.assertExistingTasks("a");

        page.openActiveFilter();
        page.assertVisibleTasksListIsEmpty();

        page.openCompletedFilter();
        page.assertVisibleTasks("a");

        //activate
        page.toggle("a");
        page.assertVisibleTasksListIsEmpty();

        page.openActiveFilter();
        page.assertVisibleTasks("a");

        page.toggle("a");
        page.assertVisibleTasksListIsEmpty();

        page.openAllFilter();
        page.assertExistingTasks("a");

        page.delete("a");
        page.assertExistingTasksListIsEmpty();
    }
}
