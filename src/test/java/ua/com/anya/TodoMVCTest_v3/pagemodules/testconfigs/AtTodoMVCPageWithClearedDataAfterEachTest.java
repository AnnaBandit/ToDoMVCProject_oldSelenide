package ua.com.anya.TodoMVCTest_v3.pagemodules.testconfigs;

import org.junit.After;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class AtTodoMVCPageWithClearedDataAfterEachTest {
    @After
    public void clearData(){
        executeJavaScript("localStorage.clear()");
    }
}
