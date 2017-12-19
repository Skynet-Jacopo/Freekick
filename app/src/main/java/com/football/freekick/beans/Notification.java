package com.football.freekick.beans;

import java.util.List;

/**
 * Created by ly on 2017/12/17.
 */

public class Notification {

    private List<NotificationBean> notification;

    public List<NotificationBean> getNotification() {
        return notification;
    }

    public void setNotification(List<NotificationBean> notification) {
        this.notification = notification;
    }

    public static class NotificationBean {
        /**
         * id : 2
         * body : 提提你：有場紙同夠一隊先好去約波呀！
         * updated_at : 2017-12-09T14:24:52.000+08:00
         * subject : null
         * match_id : 117
         * deleted_at : null
         * created_at : 2017-12-09T14:24:52.000+08:00
         * user_id : 27
         * is_read : N
         * team_id : 52
         */

        private int id;
        private String body;
        private String updated_at;
        private String subject;
        private int match_id;
        private String deleted_at;
        private String created_at;
        private int user_id;
        private String is_read;
        private String notification_type;
        private int team_id;

        public String getNotification_type() {
            return notification_type;
        }

        public void setNotification_type(String notification_type) {
            this.notification_type = notification_type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public int getMatch_id() {
            return match_id;
        }

        public void setMatch_id(int match_id) {
            this.match_id = match_id;
        }

        public String getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(String deleted_at) {
            this.deleted_at = deleted_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getIs_read() {
            return is_read;
        }

        public void setIs_read(String is_read) {
            this.is_read = is_read;
        }

        public int getTeam_id() {
            return team_id;
        }

        public void setTeam_id(int team_id) {
            this.team_id = team_id;
        }
    }
}
