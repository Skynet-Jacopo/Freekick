package com.football.freekick.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ly on 2017/12/2.
 */

public class TeamDetail {

    /**
     * team : {"average_height":3,"establish_year":2017,"status":"a","remark":null,"team_name":"空中监狱",
     * "image":{"url":"/uploads/team/image/51/image.jpeg"},"size":7,"id":51,"style":["short_pass"],
     * "age_range_max":40,"battle_preference":["become_strong"],"age_range_min":25,"district":{"region":"港島區",
     * "district":"中西區","id":19},"color2":"32FF00","user":{"id":27,"role":"captain","username":"来来来"},"color1":"FF5400"}
     */

    private TeamBean team;

    public TeamBean getTeam() {
        return team;
    }

    public void setTeam(TeamBean team) {
        this.team = team;
    }

    public static class TeamBean implements Serializable{
        /**
         * average_height : 3
         * establish_year : 2017
         * status : a
         * remark : null
         * team_name : 空中监狱
         * image : {"url":"/uploads/team/image/51/image.jpeg"}
         * size : 7
         * id : 51
         * style : ["short_pass"]
         * age_range_max : 40
         * battle_preference : ["become_strong"]
         * age_range_min : 25
         * district : {"region":"港島區","district":"中西區","id":19}
         * color2 : 32FF00
         * user : {"id":27,"role":"captain","username":"来来来"}
         * color1 : FF5400
         */

        private int average_height;
        private int establish_year;
        private String status;
        private Object remark;
        private String team_name;
        private ImageBean image;
        private int size;
        private int id;
        private int age_range_max;
        private int age_range_min;
        private DistrictBean district;
        private String color2;
        private UserBean user;
        private String color1;
        private List<String> style;
        private List<String> battle_preference;

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

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public String getTeam_name() {
            return team_name;
        }

        public void setTeam_name(String team_name) {
            this.team_name = team_name;
        }

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAge_range_max() {
            return age_range_max;
        }

        public void setAge_range_max(int age_range_max) {
            this.age_range_max = age_range_max;
        }

        public int getAge_range_min() {
            return age_range_min;
        }

        public void setAge_range_min(int age_range_min) {
            this.age_range_min = age_range_min;
        }

        public DistrictBean getDistrict() {
            return district;
        }

        public void setDistrict(DistrictBean district) {
            this.district = district;
        }

        public String getColor2() {
            return color2;
        }

        public void setColor2(String color2) {
            this.color2 = color2;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getColor1() {
            return color1;
        }

        public void setColor1(String color1) {
            this.color1 = color1;
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

        public static class ImageBean implements Serializable {
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

        public static class DistrictBean implements Serializable {
            /**
             * region : 港島區
             * district : 中西區
             * id : 19
             */

            private String region;
            private String district;
            private int id;

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }

        public static class UserBean  implements Serializable{
            /**
             * id : 27
             * role : captain
             * username : 来来来
             */

            private int id;
            private String role;
            private String username;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }
    }
}
