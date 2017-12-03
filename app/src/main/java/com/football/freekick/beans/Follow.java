package com.football.freekick.beans;

/**
 * Created by ly on 2017/12/1.
 */

public class Follow {

    /**
     * success : Now, you are following this team.
     */

    private String success;
    /**
     * errors : You are already follower of this team.
     */

    private String errors;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
