package edu.uw.rgrambo.calendarto_do;

/**
 * Created by holdenstegman on 3/8/16.
 */
public class TodoItem {
    public String title;
    public String todoFor;

    public TodoItem(String title, String todoFor) {
        this.title = title;
        this.todoFor = todoFor;
    }

    public String toString() {
        return this.title;
    }
}
