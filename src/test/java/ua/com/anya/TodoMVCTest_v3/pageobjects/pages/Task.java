package ua.com.anya.TodoMVCTest_v3.pageobjects.pages;

public class Task {
    public enum Status {
        COMPLETED, ACTIVE
    }

    String text;
    Status status;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Task(String text, Status status){
        this.text = text;
        this.status = status;
    }

    public static Task aTask(String text, Status status){
        return new Task(text, status);
    }
}
