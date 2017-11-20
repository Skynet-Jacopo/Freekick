package com.football.freekick.event;

/**
 * Created by ly on 2017/11/20.
 */

public class MainEvent {
    public MainEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;
}
