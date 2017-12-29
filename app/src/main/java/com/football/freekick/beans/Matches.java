package com.football.freekick.beans;

import java.util.List;

/**
 * Created by ly on 2017/11/19.
 */

public class Matches {

    /**
     * match : {"play_end":"2017-11-22T01:01:00.000Z","cancel_at_by_system":null,"status":"w","remark":null,
     * "deleted_at":null,"home_team_color":"4200FF","confirm_end":"2017-11-21T00:00:00.000Z",
     * "play_start":"2017-11-22T00:00:00.000Z","size":5,"id":10,"updated_at":"2017-11-19T06:25:13.000Z",
     * "created_at":"2017-11-19T06:25:13.000Z","home_team_id":25,"cancel_at_by_home":null,"pitch_id":4}
     */

    private MatchBean match;
    private List<String> errors;

    public MatchBean getMatch() {
        return match;
    }

    public void setMatch(MatchBean match) {
        this.match = match;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public static class MatchBean {
        /**
         * play_end : 2017-11-22T01:01:00.000Z
         * cancel_at_by_system : null
         * status : w
         * remark : null
         * deleted_at : null
         * home_team_color : 4200FF
         * confirm_end : 2017-11-21T00:00:00.000Z
         * play_start : 2017-11-22T00:00:00.000Z
         * size : 5
         * id : 10
         * updated_at : 2017-11-19T06:25:13.000Z
         * created_at : 2017-11-19T06:25:13.000Z
         * home_team_id : 25
         * cancel_at_by_home : null
         * pitch_id : 4
         */

        private String play_end;
        private String cancel_at_by_system;
        private String status;
        private String remark;
        private String deleted_at;
        private String home_team_color;
        private String confirm_end;
        private String play_start;
        private int size;
        private int id;
        private String updated_at;
        private String created_at;
        private int home_team_id;
        private String cancel_at_by_home;
        private String match_url;
        private int pitch_id;

        public String getMatch_url() {
            return match_url;
        }

        public void setMatch_url(String match_url) {
            this.match_url = match_url;
        }

        public String getPlay_end() {
            return play_end;
        }

        public void setPlay_end(String play_end) {
            this.play_end = play_end;
        }

        public String getCancel_at_by_system() {
            return cancel_at_by_system;
        }

        public void setCancel_at_by_system(String cancel_at_by_system) {
            this.cancel_at_by_system = cancel_at_by_system;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(String deleted_at) {
            this.deleted_at = deleted_at;
        }

        public String getHome_team_color() {
            return home_team_color;
        }

        public void setHome_team_color(String home_team_color) {
            this.home_team_color = home_team_color;
        }

        public String getConfirm_end() {
            return confirm_end;
        }

        public void setConfirm_end(String confirm_end) {
            this.confirm_end = confirm_end;
        }

        public String getPlay_start() {
            return play_start;
        }

        public void setPlay_start(String play_start) {
            this.play_start = play_start;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getHome_team_id() {
            return home_team_id;
        }

        public void setHome_team_id(int home_team_id) {
            this.home_team_id = home_team_id;
        }

        public String getCancel_at_by_home() {
            return cancel_at_by_home;
        }

        public void setCancel_at_by_home(String cancel_at_by_home) {
            this.cancel_at_by_home = cancel_at_by_home;
        }

        public int getPitch_id() {
            return pitch_id;
        }

        public void setPitch_id(int pitch_id) {
            this.pitch_id = pitch_id;
        }
    }
}
