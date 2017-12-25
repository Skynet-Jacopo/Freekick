package com.football.freekick.beans;

import java.util.List;

/**
 * Created by ly on 2017/12/1.
 */

public class Settings {

    private List<SettingBean> setting;
    private List<String> errors;

    public List<SettingBean> getSetting() {
        return setting;
    }

    public void setSetting(List<SettingBean> setting) {
        this.setting = setting;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public static class SettingBean {
        /**
         * deleted_at : null
         * id : 2
         * s_key : about_us
         * created_at : 2017-10-28T07:04:32.000+08:00
         * s_value : <p>second about us</p>

         * updated_at : 2017-10-28T07:36:22.000+08:00
         */

        private Object deleted_at;
        private int id;
        private String s_key;
        private String created_at;
        private String s_value;
        private String updated_at;

        public Object getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(Object deleted_at) {
            this.deleted_at = deleted_at;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getS_key() {
            return s_key;
        }

        public void setS_key(String s_key) {
            this.s_key = s_key;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getS_value() {
            return s_value;
        }

        public void setS_value(String s_value) {
            this.s_value = s_value;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
