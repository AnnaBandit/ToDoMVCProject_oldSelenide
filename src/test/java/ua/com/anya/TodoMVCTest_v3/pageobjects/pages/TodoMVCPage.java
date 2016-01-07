package ua.com.anya.TodoMVCTest_v3.pageobjects.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ua.com.anya.TodoMVCTest_v3.pageobjects.components.Task;

import java.util.ArrayList;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static ua.com.anya.TodoMVCTest_v3.pageobjects.components.Task.Status.*;

public class TodoMVCPage {
    private ElementsCollection tasksList = $$("#todo-list li");
    private ElementsCollection visibleTasks = tasksList.filter(visible);

    public void add(String... tasksTexts){
        for(String taskText: tasksTexts){
            $("#new-todo").shouldBe(enabled).setValue(taskText).pressEnter();
        }
    }

    public void assertVisibleTasks(String... tasksTexts){
        visibleTasks.shouldHave(exactTexts(tasksTexts));
    }

    public void assertVisibleTasksListIsEmpty(){
        visibleTasks.shouldBe(empty);
    }

    public void assertExistingTasks(String... tasksTexts){
        tasksList.shouldHave(exactTexts(tasksTexts));
    }

    public void assertExistingTasksListIsEmpty(){
        tasksList.shouldBe(empty);
    }

    public SelenideElement startEdit(String taskText, String newTaskText){
        tasksList.find(exactText(taskText)).find("label").doubleClick();
        return tasksList.find(cssClass("editing")).find(".edit").setValue(newTaskText);
    }

    public void delete(String taskText){
        tasksList.find(exactText(taskText)).hover().find(".destroy").click();
    }

    public void toggle(String taskText){
        tasksList.find(exactText(taskText)).find(".toggle").click();
    }

    public void toggleAll(){
        $("#toggle-all").click();
    }

    public void clearCompleted(){
        $("#clear-completed").click();
        $("#clear-completed").shouldBe(hidden);
    }

    public void assertItemsLeft(Integer counter){
        $("#todo-count>strong").shouldHave(exactText(String.valueOf(counter)));
    }

    public void openAllFilter(){
        $(By.linkText("All")).click();
    }

    public void openActiveFilter(){
        $(By.linkText("Active")).click();
    }

    public void openCompletedFilter(){
        $(By.linkText("Completed")).click();
    }

    public void given(Task... tasks){
        givenAtAll();
        String js = "localStorage.setItem('todos-troopjs', '[";
        for (Task task: tasks) {
            boolean isCompleted = task.getStatus() == COMPLETED;
            js += "{\"completed\":" + isCompleted + ", \"title\":\"" +  task.getText() + "\"},";
        }
        js = js.substring(0, js.length()-1) + "]');";

        executeJavaScript(js);
        refresh();
    }

    public void given(String... tasksTexts){
        given(convertTaskTextsIntoActiveTasks(tasksTexts));
    }

    public void givenAtAll(){
        if (url()!=("https://todomvc4tasj.herokuapp.com/")) {
            open("https://todomvc4tasj.herokuapp.com/");
        }
    }

    public void givenAtActive(Task... tasks){
        given(tasks);
        openActiveFilter();
    }

    public void givenAtActive(String... tasksTexts){
        givenAtActive(convertTaskTextsIntoActiveTasks(tasksTexts));
    }

    public void givenAtCompleted(Task... tasks){
        given(tasks);
        openCompletedFilter();
    }

    public Task[] convertTaskTextsIntoActiveTasks(String...tasksTexts){
        ArrayList<Task> tasks = new ArrayList<Task>();
        for (String taskText: tasksTexts) {
            tasks.add(new Task(taskText, ACTIVE));
        }
        return tasks.toArray(new Task[tasks.size()]);
    }
}
