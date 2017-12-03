package com.football.freekick.beans;

/**
 * Created by ly on 2017/12/3.
 */

public class CancelMatch {

    /**
     * match : {"cancel_at_by_home":"2017-09-28T10:27:44.000Z","id":1,"status":"c","home_team_id":1,
     * "home_team_color":"ffffff","size":5,"play_start":"2017-09-28T10:18:11.000Z",
     * "play_end":"2017-10-14T01:00:00.000Z","confirm_end":"2017-10-14T11:30:00.000Z","pitch_id":null,
     * "other_pitch":"optional","remark":null,"deleted_at":null,"created_at":"2017-09-28T09:50:30.000Z",
     * "updated_at":"2017-09-28T10:27:44.000Z","cancel_at_by_system":null}
     */

    private MatchBean match;
    /**
     * errors : Record not found
     */

    private String errors;

    public MatchBean getMatch() {
        return match;
    }

    public void setMatch(MatchBean match) {
        this.match = match;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public static class MatchBean {
        /**
         * cancel_at_by_home : 2017-09-28T10:27:44.000Z
         * id : 1
         * status : c
         * home_team_id : 1
         * home_team_color : ffffff
         * size : 5
         * play_start : 2017-09-28T10:18:11.000Z
         * play_end : 2017-10-14T01:00:00.000Z
         * confirm_end : 2017-10-14T11:30:00.000Z
         * pitch_id : null
         * other_pitch : optional
         * remark : null
         * deleted_at : null
         * created_at : 2017-09-28T09:50:30.000Z
         * updated_at : 2017-09-28T10:27:44.000Z
         * cancel_at_by_system : null
         */

        private String cancel_at_by_home;
        private int id;
        private String status;
        private int home_team_id;
        private String home_team_color;
        private int size;
        private String play_start;
        private String play_end;
        private String confirm_end;
        private Object pitch_id;
        private String other_pitch;
        private Object remark;
        private Object deleted_at;
        private String created_at;
        private String updated_at;
        private Object cancel_at_by_system;

        public String getCancel_at_by_home() {
            return cancel_at_by_home;
        }

        public void setCancel_at_by_home(String cancel_at_by_home) {
            this.cancel_at_by_home = cancel_at_by_home;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getHome_team_id() {
            return home_team_id;
        }

        public void setHome_team_id(int home_team_id) {
            this.home_team_id = home_team_id;
        }

        public String getHome_team_color() {
            return home_team_color;
        }

        public void setHome_team_color(String home_team_color) {
            this.home_team_color = home_team_color;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getPlay_start() {
            return play_start;
        }

        public void setPlay_start(String play_start) {
            this.play_start = play_start;
        }

        public String getPlay_end() {
            return play_end;
        }

        public void setPlay_end(String play_end) {
            this.play_end = play_end;
        }

        public String getConfirm_end() {
            return confirm_end;
        }

        public void setConfirm_end(String confirm_end) {
            this.confirm_end = confirm_end;
        }

        public Object getPitch_id() {
            return pitch_id;
        }

        public void setPitch_id(Object pitch_id) {
            this.pitch_id = pitch_id;
        }

        public String getOther_pitch() {
            return other_pitch;
        }

        public void setOther_pitch(String other_pitch) {
            this.other_pitch = other_pitch;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public Object getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(Object deleted_at) {
            this.deleted_at = deleted_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public Object getCancel_at_by_system() {
            return cancel_at_by_system;
        }

        public void setCancel_at_by_system(Object cancel_at_by_system) {
            this.cancel_at_by_system = cancel_at_by_system;
        }
    }
}
