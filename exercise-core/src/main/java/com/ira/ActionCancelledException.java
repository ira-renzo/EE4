package com.ira;

public class ActionCancelledException extends Exception {
    public ActionCancelledException() {
        super("Action Cancelled By User");
    }
}