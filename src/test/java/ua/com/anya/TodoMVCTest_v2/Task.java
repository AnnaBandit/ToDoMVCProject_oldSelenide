package ua.com.anya.TodoMVCTest_v2;

public class Task {
    public enum Status {
        COMPLETED, ACTIVE
    }
    String name;
    Status status;

    Task(String name, Status status){
        this.name = name;
        this.status = status;
    }
}
