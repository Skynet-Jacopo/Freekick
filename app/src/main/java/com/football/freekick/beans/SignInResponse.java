package com.football.freekick.beans;

/**
 * Created by LiuQun on 2017/11/7.
 */

public class SignInResponse {

    /**
     * data : {"id":8,"email":"test@yopmail.com","provider":"email","avatar":{"url":null},"mobile_no":"11111111111","username":"test","register_type":"mobile","uid":"test@yopmail.com","login_fail":0,"remark":null,"status":"a","deleted_at":null}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 8
         * email : test@yopmail.com
         * provider : email
         * avatar : {"url":null}
         * mobile_no : 11111111111
         * username : test
         * register_type : mobile
         * uid : test@yopmail.com
         * login_fail : 0
         * remark : null
         * status : a
         * deleted_at : null
         */

        private int id;
        private String     email;
        private String     provider;
        private AvatarBean avatar;
        private String     mobile_no;
        private String     username;
        private String     register_type;
        private String     uid;
        private int        login_fail;
        private Object     remark;
        private String     status;
        private Object     deleted_at;

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

        public String getMobile_no() {
            return mobile_no;
        }

        public void setMobile_no(String mobile_no) {
            this.mobile_no = mobile_no;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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
}
