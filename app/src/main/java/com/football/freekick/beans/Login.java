package com.football.freekick.beans;

import java.util.List;

/**
 * Created by 90516 on 2017/11/20.
 */

public class Login {

    /**
     * user : {"login_fail":0,"uid":"huo@yopmail.com","status":"a","remark":null,"deleted_at":null,"provider":"email","avatar":{"url":null},"id":26,"teams":[{"id":17,"average_height":2,"establish_year":2015,"status":"a","remark":null,"team_name":"KKK","age_range_max":65,"image":{"url":null},"age_range_min":29,"district":null,"color2":"00FFF7","color1":"00FFF6","size":7},{"id":18,"average_height":2,"establish_year":2015,"status":"a","remark":null,"team_name":"KKKa","age_range_max":65,"image":{"url":null},"age_range_min":29,"district":null,"color2":"00FFF7","color1":"00FFF6","size":7},{"id":19,"average_height":2,"establish_year":2015,"status":"a","remark":null,"team_name":"Ka","age_range_max":65,"image":{"url":null},"age_range_min":29,"district":null,"color2":"00FFF7","color1":"00FFF6","size":7},{"id":20,"average_height":3,"establish_year":2015,"status":"a","remark":null,"team_name":"天崩地裂","age_range_max":69,"image":{"url":null},"age_range_min":24,"district":null,"color2":"0073FF","color1":"00FF58","size":8},{"id":21,"average_height":3,"establish_year":2015,"status":"a","remark":null,"team_name":"下雨了","age_range_max":80,"image":{"url":null},"age_range_min":36,"district":null,"color2":"3DFF00","color1":"4200FF","size":7},{"id":22,"average_height":2,"establish_year":2015,"status":"a","remark":null,"team_name":"Ksa","age_range_max":65,"image":{"url":null},"age_range_min":29,"district":null,"color2":"00FFF7","color1":"00FFF6","size":7},{"id":23,"average_height":2,"establish_year":2015,"status":"a","remark":null,"team_name":"木头","age_range_max":64,"image":{"url":null},"age_range_min":30,"district":null,"color2":"76FF00","color1":"FF7A00","size":7},{"id":24,"average_height":3,"establish_year":2017,"status":"a","remark":null,"team_name":"哦李白","age_range_max":85,"image":{"url":null},"age_range_min":12,"district":null,"color2":"5700FF","color1":"08FF00","size":7},{"id":25,"average_height":2,"establish_year":2014,"status":"a","remark":null,"team_name":"天地会","age_range_max":85,"image":{"url":null},"age_range_min":12,"district":null,"color2":"00FF08","color1":"4200FF","size":7}],"username":"木空","updated_at":"2017-11-20T22:25:07.000+08:00","sign_in_count":124,"email":"huo@yopmail.com","created_at":"2017-11-18T15:09:52.000+08:00","mobile_no":"988"}
     */

    private UserBean user;
    private List<String> errors;
    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * login_fail : 0
         * uid : huo@yopmail.com
         * status : a
         * remark : null
         * deleted_at : null
         * provider : email
         * avatar : {"url":null}
         * id : 26
         * teams : [{"id":17,"average_height":2,"establish_year":2015,"status":"a","remark":null,"team_name":"KKK","age_range_max":65,"image":{"url":null},"age_range_min":29,"district":null,"color2":"00FFF7","color1":"00FFF6","size":7},{"id":18,"average_height":2,"establish_year":2015,"status":"a","remark":null,"team_name":"KKKa","age_range_max":65,"image":{"url":null},"age_range_min":29,"district":null,"color2":"00FFF7","color1":"00FFF6","size":7},{"id":19,"average_height":2,"establish_year":2015,"status":"a","remark":null,"team_name":"Ka","age_range_max":65,"image":{"url":null},"age_range_min":29,"district":null,"color2":"00FFF7","color1":"00FFF6","size":7},{"id":20,"average_height":3,"establish_year":2015,"status":"a","remark":null,"team_name":"天崩地裂","age_range_max":69,"image":{"url":null},"age_range_min":24,"district":null,"color2":"0073FF","color1":"00FF58","size":8},{"id":21,"average_height":3,"establish_year":2015,"status":"a","remark":null,"team_name":"下雨了","age_range_max":80,"image":{"url":null},"age_range_min":36,"district":null,"color2":"3DFF00","color1":"4200FF","size":7},{"id":22,"average_height":2,"establish_year":2015,"status":"a","remark":null,"team_name":"Ksa","age_range_max":65,"image":{"url":null},"age_range_min":29,"district":null,"color2":"00FFF7","color1":"00FFF6","size":7},{"id":23,"average_height":2,"establish_year":2015,"status":"a","remark":null,"team_name":"木头","age_range_max":64,"image":{"url":null},"age_range_min":30,"district":null,"color2":"76FF00","color1":"FF7A00","size":7},{"id":24,"average_height":3,"establish_year":2017,"status":"a","remark":null,"team_name":"哦李白","age_range_max":85,"image":{"url":null},"age_range_min":12,"district":null,"color2":"5700FF","color1":"08FF00","size":7},{"id":25,"average_height":2,"establish_year":2014,"status":"a","remark":null,"team_name":"天地会","age_range_max":85,"image":{"url":null},"age_range_min":12,"district":null,"color2":"00FF08","color1":"4200FF","size":7}]
         * username : 木空
         * updated_at : 2017-11-20T22:25:07.000+08:00
         * sign_in_count : 124
         * email : huo@yopmail.com
         * created_at : 2017-11-18T15:09:52.000+08:00
         * mobile_no : 988
         */

        private int login_fail;
        private String          uid;
        private String          status;
        private String          remark;
        private String          deleted_at;
        private String          provider;
        private AvatarBean      avatar;
        private int             id;
        private String          username;
        private String          updated_at;
        private int             sign_in_count;
        private String          email;
        private String          created_at;
        private String          mobile_no;
        private List<TeamsBean> teams;

        public int getLogin_fail() {
            return login_fail;
        }

        public void setLogin_fail(int login_fail) {
            this.login_fail = login_fail;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getSign_in_count() {
            return sign_in_count;
        }

        public void setSign_in_count(int sign_in_count) {
            this.sign_in_count = sign_in_count;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getMobile_no() {
            return mobile_no;
        }

        public void setMobile_no(String mobile_no) {
            this.mobile_no = mobile_no;
        }

        public List<TeamsBean> getTeams() {
            return teams;
        }

        public void setTeams(List<TeamsBean> teams) {
            this.teams = teams;
        }

        public static class AvatarBean {
            /**
             * url : null
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class TeamsBean {
            /**
             * id : 17
             * average_height : 2
             * establish_year : 2015
             * status : a
             * remark : null
             * team_name : KKK
             * age_range_max : 65
             * image : {"url":null}
             * age_range_min : 29
             * district : null
             * color2 : 00FFF7
             * color1 : 00FFF6
             * size : 7
             */

            private int id;
            private int       average_height;
            private int       establish_year;
            private String    status;
            private String    remark;
            private String    team_name;
            private int       age_range_max;
            private ImageBean image;
            private int       age_range_min;
            private String    district;
            private String    color2;
            private String    color1;
            private int       size;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getAverage_height() {
                return average_height;
            }

            public void setAverage_height(int average_height) {
                this.average_height = average_height;
            }

            public int getEstablish_year() {
                return establish_year;
            }

            public void setEstablish_year(int establish_year) {
                this.establish_year = establish_year;
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

            public String getTeam_name() {
                return team_name;
            }

            public void setTeam_name(String team_name) {
                this.team_name = team_name;
            }

            public int getAge_range_max() {
                return age_range_max;
            }

            public void setAge_range_max(int age_range_max) {
                this.age_range_max = age_range_max;
            }

            public ImageBean getImage() {
                return image;
            }

            public void setImage(ImageBean image) {
                this.image = image;
            }

            public int getAge_range_min() {
                return age_range_min;
            }

            public void setAge_range_min(int age_range_min) {
                this.age_range_min = age_range_min;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getColor2() {
                return color2;
            }

            public void setColor2(String color2) {
                this.color2 = color2;
            }

            public String getColor1() {
                return color1;
            }

            public void setColor1(String color1) {
                this.color1 = color1;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public static class ImageBean {
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
}
