package com.football.freekick.beans;

/**
 * Created by ly on 2017/12/29.
 */

public class ForgetPassword {

    /**
     * message : An email has been sent to 'wei@yopmail.com' containing instructions for resetting your password.
     * success : true
     */

    private String message;
    private boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
