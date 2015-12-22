package ua.com.anya.TodoMVCSmokeTest;

import com.codeborne.selenide.ElementsCollection;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class TodoMVCSmokeTest {

    @Test
    public void testSmoke () {

        open("https://todomvc4tasj.herokuapp.com/");
        addTasks("A","B","C","D");
        editTask("A","A1");
        toggleTask("A1");
        toggleTask("A1");
        deleteTask("A1");
        toggleAllTasks();
        deleteTask("B");
        clearCompletedTasks();
        taskList.shouldBe(empty);

        //open Active tab
        $("#filters a[href='#/active']").click();
        addTasks("E","F","G","H");
        editTask("E", "E1");
        toggleTask("E1");
        assertExistingTasks("F","G","H");
        deleteTask("F");
        toggleAllTasks();
        taskList.shouldBe(empty);

        //open Completed tab
        $("#filters a[href='#/completed']").click();
        assertExistingTasks("E1","G","H");
        toggleTask("E1");
        toggleAllTasks();
        $("#filters a[href='#/active']").click();
        toggleTask("E1");
        toggleTask("G");
        toggleTask("H");
        $("#filters a[href='#/completed']").click();
        deleteTask("E1");
        clearCompletedTasks();
        taskList.shouldBe(empty);
    }

    ElementsCollection taskList = $$("#todo-list li");

    private void addTasks(String... tasksTexts){
        for(String taskText: tasksTexts){
            $("#new-todo").setValue(taskText).pressEnter();
        }
    }

    private void editTask(String taskTextOld, String taskTextNew) {
        taskList.find(exactText(taskTextOld)).doubleClick().setValue(taskTextNew).pressEnter();
    };

    private void deleteTask(String taskText){
        taskList.find(exactText(taskText)).hover().find(".destroy").click();
    }

    private void toggleTask(String taskText){
        taskList.find(exactText(taskText)).find(".toggle").click();
    }

    private void toggleAllTasks(){
        $("#toggle-all").click();
    }

    private void clearCompletedTasks(){
        $("#clear-completed").click();
    }

    private void assertExistingTasks(String... taskText){
        $$("#todo-list li").shouldHave(exactTexts(taskText));
    }

}
