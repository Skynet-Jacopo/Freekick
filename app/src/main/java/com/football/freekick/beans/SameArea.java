package com.football.freekick.beans;

import java.util.List;

/**
 * Created by ly on 2017/12/1.
 * 同區球隊
 */

public class SameArea {

    private List<TeamBean> team;

    public List<TeamBean> getTeam() {
        return team;
    }

    public void setTeam(List<TeamBean> team) {
        this.team = team;
    }

    public static class TeamBean {
        /**
         * id : 51
         * team_name : 空中监狱
         * size : 7
         * color1 : FF5400
         * color2 : 32FF00
         * remark : null
         * status : a
         * image : {"url":"/uploads/team/image/51/image.jpeg"}
         * establish_year : 2017
         * average_height : 3
         * age_range_min : 25
         * age_range_max : 40
         * district : {"id":19,"district":"Central and Western","region":"Hong Kong"}
         * user : {"id":27,"username":"来来2","role":"captain"}
         * style : ["short_pass"]
         * battle_preference : ["become_strong"]
         */

        private int id;
        private String team_name;
        private int size;
        private String color1;
        private String color2;
        private Object remark;
        private String status;
        private ImageBean image;
        private int establish_year;
        private int average_height;
        private int age_range_min;
        private int age_range_max;
        private DistrictBean district;
        private UserBean user;
        private List<String> style;
        private List<String> battle_preference;
        private boolean attention;

        public boolean isAttention() {
            return attention;
        }

        public void setAttention(boolean attention) {
            this.attention = attention;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTeam_name() {
            return team_name;
        }

        public void setTeam_name(String team_name) {
            this.team_name = team_name;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getColor1() {
            return color1;
        }

        public void setColor1(String color1) {
            this.color1 = color1;
        }

        public String getColor2() {
            return color2;
        }

        public void setColor2(String color2) {
            this.color2 = color2;
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

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public int getEstablish_year() {
            return establish_year;
        }

        public void setEstablish_year(int establish_year) {
            this.establish_year = establish_year;
        }

        public int getAverage_height() {
            return average_height;
        }

        public void setAverage_height(int average_height) {
            this.average_height = average_height;
        }

        public int getAge_range_min() {
            return age_range_min;
        }

        public void setAge_range_min(int age_range_min) {
            this.age_range_min = age_range_min;
        }

        public int getAge_range_max() {
            return age_range_max;
        }

        public void setAge_range_max(int age_range_max) {
            this.age_range_max = age_range_max;
        }

        public DistrictBean getDistrict() {
            return district;
        }

        public void setDistrict(DistrictBean district) {
            this.district = district;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<String> getStyle() {
            return style;
        }

        public void setStyle(List<String> style) {
            this.style = style;
        }

        public List<String> getBattle_preference() {
            return battle_preference;
        }

        public void setBattle_preference(List<String> battle_preference) {
            this.battle_preference = battle_preference;
        }

        public static class ImageBean {
            /**
             * url : /uploads/team/image/51/image.jpeg
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class DistrictBean {
            /**
             * id : 19
             * district : Central and Western
             * region : Hong Kong
             */

            private int id;
            private String district;
            private String region;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }
        }

        public static class UserBean {
            /**
             * id : 27
             * username : 来来2
             * role : captain
             */

            private int id;
            private String username;
            private String role;

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

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }
        }
    }
}
