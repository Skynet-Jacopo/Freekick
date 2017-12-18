package com.football.freekick.beans;

import java.util.List;

/**
 * Created by LiuQun on 2017/11/7.
 */

public class RegisterResponse {

    /**
     * status : success
     * data : {"id":1,"email":"testuser@yopmail.com","provider":"email","avatar":{"url":null},"username":"test_user","mobile_no":"9111110000","register_type":"mobile","uid":"testuser@yopmail.com","login_fail":0,"remark":null,"status":"a","deleted_at":null,"created_at":"2017-09-28T07:14:05.000Z","updated_at":"2017-09-28T07:14:06.000Z"}
     * errors : {"password":["is too short (minimum is 6 characters)"],"mobile_no":["can't be blank"],"register_type":["can't be blank"],"full_messages":["Password is too short (minimum is 6 characters)","Mobile no can't be blank","Register type can't be blank"]}
     */

    private String status;
    private DataBean   data;
    private ErrorsBean errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(ErrorsBean errors) {
        this.errors = errors;
    }

    public static class DataBean {
        /**
         * id : 1
         * email : testuser@yopmail.com
         * provider : email
         * avatar : {"url":null}
         * username : test_user
         * mobile_no : 9111110000
         * register_type : mobile
         * uid : testuser@yopmail.com
         * login_fail : 0
         * remark : null
         * status : a
         * deleted_at : null
         * created_at : 2017-09-28T07:14:05.000Z
         * updated_at : 2017-09-28T07:14:06.000Z
         */

        private int id;
        private String     email;
        private String     provider;
        private AvatarBean avatar;
        private String     username;
        private String     mobile_no;
        private String     register_type;
        private String     uid;
        private int        login_fail;
        private Object     remark;
        private String     status;
        private Object     deleted_at;
        private String     created_at;
        private String     updated_at;
        private String     android_device_token;
        private boolean     push_notification;

        public String getAndroid_device_token() {
            return android_device_token;
        }

        public void setAndroid_device_token(String android_device_token) {
            this.android_device_token = android_device_token;
        }

        public boolean getPush_notification() {
            return push_notification;
        }

        public void setPush_notification(boolean push_notification) {
            this.push_notification = push_notification;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public AvatarBean getAvatar() {
            return avatar;
        }

        public void setAvatar(AvatarBean avatar) {
            this.avatar = avatar;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMobile_no() {
            return mobile_no;
        }

        public void setMobile_no(String mobile_no) {
            this.mobile_no = mobile_no;
        }

        public String getRegister_type() {
            return register_type;
        }

        public void setRegister_type(String register_type) {
            this.register_type = register_type;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public int getLogin_fail() {
            return login_fail;
        }

        public void setLogin_fail(int login_fail) {
            this.login_fail = login_fail;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public static class AvatarBean {
            /**
             * url : null
             */

            private Object url;

            public Object getUrl() {
                return url;
            }

            public void setUrl(Object url) {
                this.url = url;
            }
        }
    }

    public static class ErrorsBean {
        private List<String> password;
        private List<String> mobile_no;
        private List<String> register_type;
        private List<String> full_messages;

        public List<String> getPassword() {
            return password;
        }

        public void setPassword(List<String> password) {
            this.password = password;
        }

        public List<String> getMobile_no() {
            return mobile_no;
        }

        public void setMobile_no(List<String> mobile_no) {
            this.mobile_no = mobile_no;
        }

        public List<String> getRegister_type() {
            return register_type;
        }

        public void setRegister_type(List<String> register_type) {
            this.register_type = register_type;
        }

        public List<String> getFull_messages() {
            return full_messages;
        }

        public void setFull_messages(List<String> full_messages) {
            this.full_messages = full_messages;
        }
    }
}
