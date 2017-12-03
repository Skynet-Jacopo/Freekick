package com.football.freekick.beans;

/**
 * Created by ly on 2017/12/3.
 */

public class ConfirmInvite {

    /**
     * join_match : {"id":52,"match_id":11,"status":"confirmed","join_team_id":37,"join_team_color":"ffc300",
     * "cancel_at_by_join":null,"deleted_at":null,"created_at":"2017-11-17T11:59:28.000Z",
     * "updated_at":"2017-11-17T12:06:33.000Z","size":10}
     */

    private JoinMatchBean join_match;
    /**
     * error : You have already confirmed a team to join.
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
         * id : 52
         * match_id : 11
         * status : confirmed
         * join_team_id : 37
         * join_team_color : ffc300
         * cancel_at_by_join : null
         * deleted_at : null
         * created_at : 2017-11-17T11:59:28.000Z
         * updated_at : 2017-11-17T12:06:33.000Z
         * size : 10
         */

        private int id;
        private int match_id;
        private String status;
        private int join_team_id;
        private String join_team_color;
        private Object cancel_at_by_join;
        private Object deleted_at;
        private String created_at;
        private String updated_at;
        private int size;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMatch_id() {
            return match_id;
        }

        public void setMatch_id(int match_id) {
            this.match_id = match_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}
