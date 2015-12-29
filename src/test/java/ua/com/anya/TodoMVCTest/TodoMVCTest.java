package ua.com.anya.TodoMVCTest;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.yandex.qatools.allure.annotations.*;


import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


public class TodoMVCTest extends AtTodoMVCPageWithClearedDataAfterEachTest {

    @Test
    public void testDeleteWhileEditing(){
        add("1", "2");
        startEdit("1", "").pressEnter();
        assertExistingTasks("2");
        assertItemsLeft(1);
    }

    @Test
    public void testCancelEditingByESC(){
        add("1");
        startEdit("1", "1-edited").sendKeys(Keys.ESCAPE);
        assertExistingTasks("1");
        assertItemsLeft(1);
    }

    @Test
    public void testEditAndSaveByClickingOutside(){
        add("1");
        startEdit("1", "1-edited");
        add("2");
        assertExistingTasks("1-edited", "2");
        assertItemsLeft(2);
    }

    @Test
    public void testActivateAllOnCompletedFilter(){
        // given on completed filter with all tasks completed
        add("1", "2");
        toggleAll();
        openCompletedFilter();
        assertVisibleTasks("1", "2");
        assertItemsLeft(0);

        // activate All
        toggleAll();
        assertVisibleTasksListIsEmpty();
        assertItemsLeft(2);
        openActiveFilter();
        assertVisibleTasks("1", "2");
    }

    @Test
    public void testTasksMainFlow(){
        add("1");
        toggle("1");

        openActiveFilter();
        assertVisibleTasksListIsEmpty();

        add("2");
        startEdit("2", "2-edited").pressEnter();
        assertVisibleTasks("2-edited");

        //complete All
        toggleAll();
        assertVisibleTasksListIsEmpty();
        openCompletedFilter();
        assertVisibleTasks("1", "2-edited");

        //activate
        toggle("2-edited");
        assertVisibleTasks("1");

        clearCompleted();
        assertVisibleTasksListIsEmpty();

        openAllFilter();
        delete("2-edited");
        assertExistingTasksListIsEmpty();
    }

    ElementsCollection tasksList = $$("#todo-list li");
    ElementsCollection visibleTasks = tasksList.filter(visible);

    @Step
    void add(String... tasksTexts){
        for(String taskText: tasksTexts){
            $("#new-todo").setValue(taskText).pressEnter();
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

}
