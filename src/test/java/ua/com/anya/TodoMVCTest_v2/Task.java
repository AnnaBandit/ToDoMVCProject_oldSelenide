package ua.com.anya.TodoMVCTest_v2;

public class Task {
    public enum Status {
        COMPLETED, ACTIVE
    }
    String text;
    Status status;

    Task(String text, Status status){
        this.text = text;
        this.status = status;
    }

    public static Task aTask(String text, Status status){
        return new Task(text, status);
    }
}
