package ua.com.anya.TodoMVCTest_v2;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import ua.com.anya.TodoMVCTest_v2.Task.Status;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static ua.com.anya.TodoMVCTest_v2.Task.Status.ACTIVE;
import static ua.com.anya.TodoMVCTest_v2.Task.Status.COMPLETED;

public class TodoMVCTest {

    @Before
    public void openApp(){
        open("https://todomvc4tasj.herokuapp.com/");
    }

    @After
    public void clearData(){
        executeJavaScript("localStorage.clear()");
    }

    //test edit, complete all, clear completed, activate, activate all, cancel edit, edit with saving by clicking outside, delete while editing on All filter
    @Test
    public void testEditOnAllFilter(){
        givenAtAll(aTask("a", ACTIVE),
                   aTask("b", ACTIVE),
                   aTask("c", ACTIVE));

        startEdit("b", "b-edited").pressEnter();
        assertItemsLeft(3);
        assertExistingTasks("a", "b-edited", "c");
    }

    @Test
    public void testCompleteAllOnAllFilter(){
        givenAtAll("a", "b");

        toggleAll();
        assertItemsLeft(0);
        openCompletedFilter();
        assertExistingTasks("a", "b");
    }

    @Test
    public void testClearCompletedOnAllFilter(){
        givenAtAll(aTask("a", COMPLETED),
                   aTask("b", COMPLETED),
                   aTask("c", ACTIVE));

        clearCompleted();
        assertExistingTasks("c");
        assertItemsLeft(1);
        openCompletedFilter();
        assertVisibleTasksListIsEmpty();
    }

    @Test
    public void testActivateAllOnAllFilter(){
        givenAtAll(aTask("a", COMPLETED),
                   aTask("b", COMPLETED));

        toggleAll();
        assertItemsLeft(2);
        openActiveFilter();
        assertExistingTasks("a", "b");
    }

    @Test
    public void testActivateOnAllFilter(){
        givenAtAll(aTask("a", COMPLETED));

        toggle("a");
        assertItemsLeft(1);
        openActiveFilter();
        assertExistingTasks("a");
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
        givenAtAll("a", "b", "c");

        startEdit("c", "c-edited");
        add("d");
        assertExistingTasks("a", "b", "c-edited", "d");
        assertItemsLeft(4);
    }

    @Test
    public void testDeleteWhileEditing(){
        givenAtAll("a", "b");

        startEdit("a", "").pressEnter();
        assertExistingTasks("b");
        assertItemsLeft(1);
    }

    //test delete, complete all, clear completed, edit by pressing enter on Active filter
    @Test
    public void testDeleteOnActiveFilter(){
        givenAtActive("a");

        delete("a");
        assertExistingTasksListIsEmpty();
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
        assertItemsLeft(1);
        assertExistingTasks("c");
        openCompletedFilter();
        assertVisibleTasksListIsEmpty();
    }

    @Test
    public void testEditAndSaveByPressingEnterOnActiveFilter(){
        givenAtActive("a");

        startEdit("a", "a-edited").pressEnter();
        assertVisibleTasks("a-edited");
        assertItemsLeft(1);
    }

    //test create, edit, delete, activate all on Completed filter
    @Test
    public void testCreateOnCompletedFilter(){
        givenAtCompleted("a");

        add("b");
        assertVisibleTasksListIsEmpty();
        assertItemsLeft(2);
        openActiveFilter();
        assertExistingTasks("a", "b");
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
    public void testTasksMainFlow(){
        givenAtAll();

        add("a");
        assertExistingTasks("a");

        toggle("a");
        openActiveFilter();
        assertVisibleTasksListIsEmpty();

        add("b");
        toggle("b");
        assertVisibleTasksListIsEmpty();

        openCompletedFilter();
        assertVisibleTasks("a", "b");

        //activate
        toggle("b");
        assertVisibleTasks("a");

        clearCompleted();
        assertVisibleTasksListIsEmpty();

        openAllFilter();
        assertExistingTasks("b");

        delete("b");
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

    private Task aTask(String name, Status status){
        return new Task(name, status);
    }

    private void givenAtAll(Task... tasks){
        addTasks(tasks);
    }

    private void givenAtAll(String... tasksTexts){
        addTasks(tasksTexts);
    }

    private void givenAtAll(){
    }

    private void givenAtActive(Task... tasks){
        addTasks(tasks);
        $(By.linkText("Active")).click();
    }

    private void givenAtActive(String... tasksTexts){
        addTasks(tasksTexts);
        $(By.linkText("Active")).click();
    }

    private void givenAtActive(){
        $(By.linkText("Active")).click();
    }

    private void givenAtCompleted(Task... tasks){
        addTasks(tasks);
        $(By.linkText("Completed")).click();
    }

    private void givenAtCompleted(String... tasksTexts){
        addTasks(tasksTexts);
        $(By.linkText("Completed")).click();
    }

    private String addTaskToJS(Task task){
        boolean isCompleted = task.status == COMPLETED;
        return "{\"completed\":" + isCompleted + ", \"title\":\"" +  task.name + "\"},";
    }

    private String addTaskToJS(String text){
        return "{\"completed\":false, \"title\":\"" +  text + "\"},";
    }

    private void addTasks(Task... tasks){
        String js = "localStorage.setItem('todos-troopjs', '[";
        for (Task task: tasks) {
            js += addTaskToJS(task);
        }
        js = js.substring(0, js.length()-1) + "]');"; //removing extra "," (last character in js)

        executeJavaScript(js);
        refresh();
    }

    private void addTasks(String... texts){
        String js = "localStorage.setItem('todos-troopjs', '[";
        for (String text: texts) {
            js += addTaskToJS(text);
        }
        js = js.substring(0, js.length()-1) + "]');"; //removing extra "," (last character in js)

        executeJavaScript(js);
        refresh();
    }
}
