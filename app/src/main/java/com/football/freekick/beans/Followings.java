package com.football.freekick.beans;

import java.util.List;

/**
 * Created by ly on 2017/12/3.
 */

public class Followings {

    private List<TeamsBean> teams;

    public List<TeamsBean> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamsBean> teams) {
        this.teams = teams;
    }

    public static class TeamsBean {
        /**
         * id : 18
         * team_name : Lions
         * prefer_district_id : null
         * image : {"url":"/uploads/team/image/18/upload-image-9432642-1509545236."}
         */

        private int id;
        private String team_name;
        private Object prefer_district_id;
        private ImageBean image;

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

        public Object getPrefer_district_id() {
            return prefer_district_id;
        }

        public void setPrefer_district_id(Object prefer_district_id) {
            this.prefer_district_id = prefer_district_id;
        }

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public static class ImageBean {
            /**
             * url : /uploads/team/image/18/upload-image-9432642-1509545236.
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
