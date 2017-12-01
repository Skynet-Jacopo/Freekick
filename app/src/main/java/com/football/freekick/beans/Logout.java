package com.football.freekick.beans;

/**
 * Created by ly on 2017/12/1.
 */

public class Logout {

    /**
     * success : false
     * message : Invalid token.
     */

    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
