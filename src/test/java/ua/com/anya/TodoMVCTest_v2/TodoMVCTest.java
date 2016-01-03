package ua.com.anya.TodoMVCTest_v2;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;

public class TodoMVCTest {

    @After
    public void clearData(){
        executeJavaScript("localStorage.clear()");
    }

    @Test
    public void testCreateOnCompletedFilter(){
        given(Filter.COMPLETED, aTask("1", Task.Status.ACTIVE));

        add("2");
        assertItemsLeft(2);
        openActiveFilter();
        assertExistingTasks("1", "2");
    }

    @Test
    public void testEditOnAllFilter(){
        given(Filter.ALL, aTask("1", Task.Status.ACTIVE));

        startEdit("1", "1-edited").pressEnter();
        assertExistingTasks("1-edited");
    }


    @Test
    public void testEditAndSaveByPressingEnterOnActiveFilter(){
        given(Filter.ACTIVE, aTask("1", Task.Status.ACTIVE));
        
        startEdit("1", "1-edited").pressEnter();
        assertVisibleTasks("1-edited");
    }

    @Test
    public void testEditOnCompletedFilter(){
        given(Filter.COMPLETED, aTask("1", Task.Status.COMPLETED));

        startEdit("1", "1-edited").pressEnter();
        assertVisibleTasks("1-edited");
    }

    @Test
    public void testCancelEditingByESC(){
        given(Filter.ALL, aTask("1", Task.Status.ACTIVE));

        startEdit("1", "1-edited").pressEscape();
        assertExistingTasks("1");
        assertItemsLeft(1);
    }

    @Test
    public void testEditAndSaveByClickingOutside(){
        given(Filter.ALL, aTask("1", Task.Status.ACTIVE));

        startEdit("1", "1-edited");
        add("2");
        assertExistingTasks("1-edited", "2");
        assertItemsLeft(2);
    }

    @Test
    public void testDeleteWhileEditing(){
        given(Filter.ALL, aTask("1", Task.Status.ACTIVE), aTask("2", Task.Status.ACTIVE));

        startEdit("1", "").pressEnter();
        assertExistingTasks("2");
        assertItemsLeft(1);
    }

    @Test
    public void testDeleteOnActiveFilter(){
        given(Filter.ACTIVE, aTask("1", Task.Status.ACTIVE));

        delete("1");
        assertExistingTasksListIsEmpty();
    }

    @Test
    public void testDeleteOnCompletedFilter(){
        given(Filter.COMPLETED, aTask("1", Task.Status.COMPLETED));

        delete("1");
        assertExistingTasksListIsEmpty();
    }

    @Test
    public void testCompleteAllOnAllFilter(){
        given(Filter.ALL, aTask("1", Task.Status.ACTIVE), aTask("2", Task.Status.ACTIVE));

        toggleAll();
        assertItemsLeft(0);
        openCompletedFilter();
        assertExistingTasks("1", "2");
    }

    @Test
    public void testCompleteAllOnActiveFilter(){
        given(Filter.ACTIVE, aTask("1", Task.Status.ACTIVE), aTask("2", Task.Status.ACTIVE));

        toggleAll();
        assertVisibleTasksListIsEmpty();
        openCompletedFilter();
        assertVisibleTasks("1", "2");
    }

    @Test
    public void testClearCompletedOnAllFilter(){
        given(Filter.ALL, aTask("1", Task.Status.COMPLETED), aTask("2", Task.Status.COMPLETED), aTask("3", Task.Status.ACTIVE));

        clearCompleted();
        assertExistingTasks("3");
        assertItemsLeft(1);
        openCompletedFilter();
        assertVisibleTasksListIsEmpty();
    }

    @Test
    public void testClearCompletedOnActiveFilter(){
        given(Filter.ACTIVE, aTask("1", Task.Status.COMPLETED), aTask("2", Task.Status.COMPLETED), aTask("3", Task.Status.ACTIVE));

        clearCompleted();
        assertItemsLeft(1);
        assertExistingTasks("3");
        openCompletedFilter();
        assertVisibleTasksListIsEmpty();
    }

    @Test
    public void testActivateOnAllFilter(){
        given(Filter.ALL, aTask("1", Task.Status.COMPLETED));

        toggle("1");
        assertItemsLeft(1);
        openActiveFilter();
        assertExistingTasks("1");
    }

    @Test
    public void testActivateAllOnAllFilter(){
        given(Filter.ALL, aTask("1", Task.Status.COMPLETED), aTask("2", Task.Status.COMPLETED));

        toggleAll();
        assertItemsLeft(2);
        openActiveFilter();
        assertExistingTasks("1", "2");
    }

    @Test
    public void testActivateAllOnCompletedFilter(){
        given(Filter.COMPLETED, aTask("1", Task.Status.COMPLETED), aTask("2", Task.Status.COMPLETED));

        toggleAll();
        assertVisibleTasksListIsEmpty();
        assertItemsLeft(2);
        openActiveFilter();
        assertVisibleTasks("1", "2");
    }

    @Test
    public void testTasksMainFlow(){
        open("https://todomvc4tasj.herokuapp.com/");

        add("1");
        assertExistingTasks("1");

        toggle("1");
        openActiveFilter();
        assertVisibleTasksListIsEmpty();

        add("2");
        toggle("2");
        assertVisibleTasksListIsEmpty();

        openCompletedFilter();
        assertVisibleTasks("1", "2");

        //activate
        toggle("2");
        assertVisibleTasks("1");

        clearCompleted();
        assertVisibleTasksListIsEmpty();

        openAllFilter();
        assertExistingTasks("2");

        delete("2");
        assertExistingTasksListIsEmpty();
    }


    ElementsCollection tasksList = $$("#todo-list li");
    ElementsCollection visibleTasks = tasksList.filter(visible);
    public enum Filter {
        ALL, ACTIVE, COMPLETED
    }

    @Step
    void add(String... tasksTexts){
        for(String taskText: tasksTexts){
            $("#new-todo").shouldBe(enabled).setValue(taskText).pressEnter();
        }
    }

    @Step
    private void assertVisibleTasks(String... tasksTexts){
        visibleTasks.shouldHave(exactTexts(tasksTexts));
    }

    @Step
    private void assertVisibleTasksListIsEmpty(){
        visibleTasks.shouldBe(empty);
    }

    @Step
    private void assertExistingTasks(String... tasksTexts){
        tasksList.shouldHave(exactTexts(tasksTexts));
    }

    @Step
    private void assertExistingTasksListIsEmpty(){
        tasksList.shouldBe(empty);
    }

    @Step
    private SelenideElement startEdit(String taskText, String newTaskText){
        tasksList.find(exactText(taskText)).find("label").doubleClick();
        return tasksList.find(cssClass("editing")).find(".edit").setValue(newTaskText);
    }

    @Step
    private void delete(String taskText){
        tasksList.find(exactText(taskText)).hover().find(".destroy").click();
    }

    @Step
    private void toggle(String taskText){
        tasksList.find(exactText(taskText)).find(".toggle").click();
    }

    @Step
    private void toggleAll(){
        $("#toggle-all").click();
    }

    @Step
    private void clearCompleted(){
        $("#clear-completed").click();
        $("#clear-completed").shouldBe(hidden);
    }

    @Step
    private void assertItemsLeft(Integer counter){
        $("#todo-count>strong").shouldHave(exactText(String.valueOf(counter)));
    }

    @Step
    private void openAllFilter(){
        $(By.linkText("All")).click();
    }

    @Step
    private void openActiveFilter(){
        $(By.linkText("Active")).click();
    }

    @Step
    private void openCompletedFilter(){
        $(By.linkText("Completed")).click();
    }

    private Task aTask(String name, Task.Status status){
        return new Task(name, status);
    }

    private void given(Filter filter, Task... tasks){
        open("https://todomvc4tasj.herokuapp.com/");

        String js = "localStorage.setItem('todos-troopjs', '[";

        for (Task task: tasks) {

            if (task.status == Task.Status.ACTIVE){
                js += "{\"completed\":false, \"title\":\"" +  task.name + "\"},";
        }
            if (task.status == Task.Status.COMPLETED){
                js += "{\"completed\":true, \"title\":\"" +  task.name + "\"},";
        }

    }

        js = js.substring(0, js.length()-1) + "]');";
        executeJavaScript(js);
        refresh();

        if (filter == Filter.ACTIVE){
            open("https://todomvc4tasj.herokuapp.com/#/active");
        }
        if (filter == Filter.COMPLETED){
            open("https://todomvc4tasj.herokuapp.com/#/completed");
        }

    }

}
