package com.football.freekick.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ly on 2017/11/19.
 */

public class Recommended {

    private List<TeamsBean> teams;
    /**
     * errors : Record not found
     */

    private String errors;

    public List<TeamsBean> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamsBean> teams) {
        this.teams = teams;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public static class TeamsBean {
        /**
         * id : 19
         * team_name : Star
         * image : {"url":"/uploads/team/image/19/upload-image-8843737-1509546403."}
         */

        private int id;
        private String team_name;
        private ImageBean image;
        private boolean attention;
        /**
         * district : {"region":"Hong Kong","district":"Central and Western","id":19}
         */

        private DistrictBean district;

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

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public DistrictBean getDistrict() {
            return district;
        }

        public void setDistrict(DistrictBean district) {
            this.district = district;
        }

        public static class ImageBean {
            /**
             * url : /uploads/team/image/19/upload-image-8843737-1509546403.
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
             * region : Hong Kong
             * district : Central and Western
             * id : 19
             */

            private String region;
            private String district;
            @SerializedName("id")
            private int idX;

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

            public int getIdX() {
                return idX;
            }

            public void setIdX(int idX) {
                this.idX = idX;
            }
        }
    }
}
