package ua.com.anya.TodoMVCTest;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.By;


import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TodoMVCTest {

    @Test
    public void testTasksMainFlow(){

        open("https://todomvc4tasj.herokuapp.com/");
        addTasks("1");
        startEdit("1", "1-edited").pressEnter();
        toggleTask("1-edited");
        tasksList.filterBy(cssClass("completed")).shouldHaveSize(1);

        openActiveFilter();
        addTasks("2");
        assertExistingTasks("", "2");
        toggleAllTasks();

        openCompletedFilter();
        assertExistingCompletedTasks("1-edited", "2");
        toggleTask("1-edited");
        clearCompletedTasks();
        tasksList.filter(visible).shouldHaveSize(0);

        openAllFilter();
        itemsLeft(1);
        deleteTask("1-edited");
        tasksList.shouldBe(empty);
    }

    ElementsCollection tasksList = $$("#todo-list li");
    SelenideElement editableField = tasksList.find(cssClass("editing")).find(".edit");
    SelenideElement newTask = $("#new-todo");

    private void addTasks(String... tasksTexts){
        for(String taskText: tasksTexts){
            newTask.setValue(taskText).pressEnter();
        }
    }

    private void assertExistingTasks(String... tasksTexts){
        tasksList.shouldHave(exactTexts(tasksTexts));
    }

    private void assertExistingCompletedTasks(String... tasksTexts){
        $$(".completed label").shouldHave(exactTexts(tasksTexts));
    }

    private SelenideElement startEdit(String taskText, String newTaskText){
        tasksList.find(exactText(taskText)).find("label").doubleClick();
        editableField.setValue(newTaskText);
        return editableField;
    }

    private void deleteTask(String taskText){
        tasksList.find(exactText(taskText)).hover().find(".destroy").click();
    }

    private void toggleTask(String taskText){
        tasksList.find(exactText(taskText)).find(".toggle").click();
    }

    private void toggleAllTasks(){
        $("#toggle-all").click();
    }

    private void clearCompletedTasks(){
        $("#clear-completed").click();
    }

    private void itemsLeft(Integer counter){
        $("#todo-count strong").shouldHave(exactText(String.valueOf(counter)));
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
}
