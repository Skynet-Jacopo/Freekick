package com.football.freekick.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 90516 on 2017/12/8.
 * 主隊確認與某個參與隊作賽
 */

public class ConfirmMatch {

    /**
     * join_match : {"status":"confirmed","match_id":1,"id":1,"join_team_id":2,"join_team_color":"ffc300","cancel_at_by_join":null,"deleted_at":null,"created_at":"2017-09-28T10:43:56.000Z","updated_at":"2017-09-28T11:05:29.000Z","\u201csize\u201d":"\u201c10\u201d"}
     */

    private JoinMatchBean join_match;

    private String error;
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    public JoinMatchBean getJoin_match() {
        return join_match;
    }

    public void setJoin_match(JoinMatchBean join_match) {
        this.join_match = join_match;
    }

    public static class JoinMatchBean {
        /**
         * status : confirmed
         * match_id : 1
         * id : 1
         * join_team_id : 2
         * join_team_color : ffc300
         * cancel_at_by_join : null
         * deleted_at : null
         * created_at : 2017-09-28T10:43:56.000Z
         * updated_at : 2017-09-28T11:05:29.000Z
         * “size” : “10”
         */

        private String status;
        private int    match_id;
        private int    id;
        private int    join_team_id;
        private String join_team_color;
        private Object cancel_at_by_join;
        private Object deleted_at;
        private String created_at;
        private String updated_at;
        @SerializedName("“size”")
        private String _$Size227; // FIXME check this code

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getMatch_id() {
            return match_id;
        }

        public void setMatch_id(int match_id) {
            this.match_id = match_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getJoin_team_id() {
            return join_team_id;
        }

        public void setJoin_team_id(int join_team_id) {
            this.join_team_id = join_team_id;
        }

        public String getJoin_team_color() {
            return join_team_color;
        }

        public void setJoin_team_color(String join_team_color) {
            this.join_team_color = join_team_color;
        }

        public Object getCancel_at_by_join() {
            return cancel_at_by_join;
        }

        public void setCancel_at_by_join(Object cancel_at_by_join) {
            this.cancel_at_by_join = cancel_at_by_join;
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

        public String get_$Size227() {
            return _$Size227;
        }

        public void set_$Size227(String _$Size227) {
            this._$Size227 = _$Size227;
        }
    }
}
