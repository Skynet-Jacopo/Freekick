package com.football.freekick.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ly on 2017/12/3.
 */

public class WithDraw {

    /**
     * join_match : {"status":"withdraw","id":1,"cancel_at_by_join":"2017-09-28T11:13:01.000Z","match_id":1,
     * "join_team_id":2,"join_team_color":"ffc300","deleted_at":null,"created_at":"2017-09-28T10:43:56.000Z",
     * "updated_at":"2017-09-28T11:13:01.000Z","\u201csize\u201d":5}
     */

    private JoinMatchBean join_match;
    /**
     * error : you are not authorized to perform this operation
     */

    private String error;

    public JoinMatchBean getJoin_match() {
        return join_match;
    }

    public void setJoin_match(JoinMatchBean join_match) {
        this.join_match = join_match;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static class JoinMatchBean {
        /**
         * status : withdraw
         * id : 1
         * cancel_at_by_join : 2017-09-28T11:13:01.000Z
         * match_id : 1
         * join_team_id : 2
         * join_team_color : ffc300
         * deleted_at : null
         * created_at : 2017-09-28T10:43:56.000Z
         * updated_at : 2017-09-28T11:13:01.000Z
         * “size” : 5
         */

        private String status;
        private int id;
        private String cancel_at_by_join;
        private int match_id;
        private int join_team_id;
        private String join_team_color;
        private Object deleted_at;
        private String created_at;
        private String updated_at;
        @SerializedName("“size”")
        private int _$Size324; // FIXME check this code

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCancel_at_by_join() {
            return cancel_at_by_join;
        }

        public void setCancel_at_by_join(String cancel_at_by_join) {
            this.cancel_at_by_join = cancel_at_by_join;
        }

        public int getMatch_id() {
            return match_id;
        }

        public void setMatch_id(int match_id) {
            this.match_id = match_id;
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

        public int get_$Size324() {
            return _$Size324;
        }

        public void set_$Size324(int _$Size324) {
            this._$Size324 = _$Size324;
        }
    }
}
