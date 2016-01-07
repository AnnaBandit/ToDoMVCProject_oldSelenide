package ua.com.anya.TodoMVCTest_v2;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;

import java.util.ArrayList;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static ua.com.anya.TodoMVCTest_v2.Task.Status.ACTIVE;
import static ua.com.anya.TodoMVCTest_v2.Task.Status.COMPLETED;
import static ua.com.anya.TodoMVCTest_v2.Task.aTask;

public class TodoMVCTest {

    @After
    public void clearData(){
        executeJavaScript("localStorage.clear()");
    }

    //at All filter
    @Test
    public void testCreateOnAllFilter(){
        ensureOpenedTodoMVC();

        add("a", "b");
        assertExistingTasks("a", "b");
        assertItemsLeft(2);
    }

    @Test
    public void testEditOnAllFilter(){
        givenAtAll("a", "b", "c");

        startEdit("b", "b-edited").pressEnter();
        assertExistingTasks("a", "b-edited", "c");
        assertItemsLeft(3);
    }

    @Test
    public void testCompleteAllOnAllFilter(){
        givenAtAll("a", "b");

        toggleAll();
        assertExistingTasks("a", "b");
        assertItemsLeft(0);
        openCompletedFilter();
        assertVisibleTasks("a", "b");
    }

    @Test
    public void testClearCompletedOnAllFilter(){
        given(aTask("a", COMPLETED),
              aTask("b", COMPLETED),
              aTask("c", ACTIVE));

        clearCompleted();
        assertExistingTasks("c");
        assertItemsLeft(1);
    }

    @Test
    public void testActivateAllOnAllFilter(){
        given(aTask("a", COMPLETED),
              aTask("b", COMPLETED));

        toggleAll();
        assertItemsLeft(2);
        openActiveFilter();
        assertVisibleTasks("a", "b");
    }

    @Test
    public void testActivateOnAllFilter(){
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
    public void testDeleteOnAllFilter(){
        ensureOpenedTodoMVC();

        add("a", "b");
        assertExistingTasks("a", "b");

        delete("a");
        assertExistingTasks("b");
        assertItemsLeft(1);
    }

    //at Active filter
    @Test
    public void testEditByPressingEnterOnActiveFilter(){
        givenAtActive("a");

        startEdit("a", "a-edited").pressEnter();
        assertVisibleTasks("a-edited");
        assertItemsLeft(1);
    }

    @Test
    public void testCompleteAllOnActiveFilter(){
        givenAtActive("a", "b");

        toggleAll();
        assertVisibleTasksListIsEmpty();
        openCompletedFilter();
        assertVisibleTasks("a", "b");
    }

    @Test
    public void testClearCompletedOnActiveFilter(){
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
    public void testDeleteOnActiveFilter(){
        givenAtActive("a", "b");

        delete("a");
        assertVisibleTasks("b");
        assertItemsLeft(1);
    }


    //at Completed filter
    @Test
    public void testCreateOnCompletedFilter(){
        givenAtCompleted(convertTaskTextsIntoActiveTasks("a"));

        add("b");
        assertVisibleTasksListIsEmpty();
        assertItemsLeft(2);
        openActiveFilter();
        assertVisibleTasks("a", "b");
    }

    @Test
    public void testEditOnCompletedFilter(){
        givenAtCompleted(aTask("a", COMPLETED));

        startEdit("a", "a-edited").pressEnter();
        assertVisibleTasks("a-edited");
        assertItemsLeft(0);
    }

    @Test
    public void testDeleteOnCompletedFilter(){
        givenAtCompleted(aTask("a", COMPLETED));

        delete("a");
        assertExistingTasksListIsEmpty();
    }

    @Test
    public void testActivateAllOnCompletedFilter(){
        givenAtCompleted(aTask("a", COMPLETED),
                         aTask("b", COMPLETED));

        toggleAll();
        assertVisibleTasksListIsEmpty();
        assertItemsLeft(2);
        openActiveFilter();
        assertVisibleTasks("a", "b");
    }

    @Test
    public void testTasksMainFlowThroughFilters(){
        ensureOpenedTodoMVC();

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


    ElementsCollection tasksList = $$("#todo-list li");
    ElementsCollection visibleTasks = tasksList.filter(visible);

    void add(String... tasksTexts){
        for(String taskText: tasksTexts){
            $("#new-todo").shouldBe(enabled).setValue(taskText).pressEnter();
        }
    }

    private void assertVisibleTasks(String... tasksTexts){
        visibleTasks.shouldHave(exactTexts(tasksTexts));
    }

    private void assertVisibleTasksListIsEmpty(){
        visibleTasks.shouldBe(empty);
    }

    private void assertExistingTasks(String... tasksTexts){
        tasksList.shouldHave(exactTexts(tasksTexts));
    }

    private void assertExistingTasksListIsEmpty(){
        tasksList.shouldBe(empty);
    }

    private SelenideElement startEdit(String taskText, String newTaskText){
        tasksList.find(exactText(taskText)).find("label").doubleClick();
        return tasksList.find(cssClass("editing")).find(".edit").setValue(newTaskText);
    }

    private void delete(String taskText){
        tasksList.find(exactText(taskText)).hover().find(".destroy").click();
    }

    private void toggle(String taskText){
        tasksList.find(exactText(taskText)).find(".toggle").click();
    }

    private void toggleAll(){
        $("#toggle-all").click();
    }

    private void clearCompleted(){
        $("#clear-completed").click();
        $("#clear-completed").shouldBe(hidden);
    }

    private void assertItemsLeft(Integer counter){
        $("#todo-count>strong").shouldHave(exactText(String.valueOf(counter)));
    }

    private void openAllFilter(){
        $(By.linkText("All")).click();
    }

    private void openActiveFilter(){
        $(By.linkText("Active")).click();
    }

    private void openCompletedFilter(){
        $(By.linkText("Completed")).click();
    }

    private void ensureOpenedTodoMVC(){
        if (url()!=("https://todomvc4tasj.herokuapp.com/")) {
            open("https://todomvc4tasj.herokuapp.com/");
        }
    }

    private void given(Task... tasks){
        ensureOpenedTodoMVC();

        String js = "localStorage.setItem('todos-troopjs', '[";
        for (Task task: tasks) {
            boolean isCompleted = task.status == COMPLETED;
            js += "{\"completed\":" + isCompleted + ", \"title\":\"" +  task.text + "\"},";
        }
        js = js.substring(0, js.length()-1) + "]');";

        executeJavaScript(js);
        refresh();
    }

    private void givenAtAll(String... tasksTexts){
        given(convertTaskTextsIntoActiveTasks(tasksTexts));
    }

    private void givenAtActive(Task... tasks){
        given(tasks);
        openActiveFilter();
    }

    private void givenAtActive(String... tasksTexts){
        givenAtActive(convertTaskTextsIntoActiveTasks(tasksTexts));
    }

    private void givenAtCompleted(Task... tasks){
        given(tasks);
        openCompletedFilter();
    }

    private Task[] convertTaskTextsIntoActiveTasks(String...tasksTexts){
        ArrayList<Task> tasks = new ArrayList<Task>();
        for (String taskText: tasksTexts) {
            tasks.add(new Task(taskText, ACTIVE));
        }
        return tasks.toArray(new Task[tasks.size()]);
    }
}
