package com.football.freekick;

/**
 * Created by LiuQun on 2017/10/17.
 */

public class Register {

    /**
     * user : {"email":"testuser@yopmail.com","password":"123456789","password_confirmation":"123456789","username":"test_user","register_type":"mobile","mobile_no":"9111110000"}
     */

    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * email : testuser@yopmail.com
         * password : 123456789
         * password_confirmation : 123456789
         * username : test_user
         * register_type : mobile
         * mobile_no : 9111110000
         */

        private String email;
        private String password;
        private String password_confirmation;
        private String username;
        private String register_type;
        private String mobile_no;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPassword_confirmation() {
            return password_confirmation;
        }

        public void setPassword_confirmation(String password_confirmation) {
            this.password_confirmation = password_confirmation;
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

        public String getMobile_no() {
            return mobile_no;
        }

        public void setMobile_no(String mobile_no) {
            this.mobile_no = mobile_no;
        }
    }
}
