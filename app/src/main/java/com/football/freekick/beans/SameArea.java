package com.football.freekick.beans;

import java.util.List;

/**
 * Created by ly on 2017/12/1.
 * 同區球隊
 */

public class SameArea {

    private List<TeamsBean> teams;

    public List<TeamsBean> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamsBean> teams) {
        this.teams = teams;
    }

    public static class TeamsBean {
        /**
         * district : {"region":"港島區","district":"中西區","id":19}
         * id : 14
         * image : {"url":null}
         * team_name : Wonder Team
         */
        private boolean isAttention;
        private DistrictBean district;
        private int id;
        private ImageBean image;
        private String team_name;

        public DistrictBean getDistrict() {
            return district;
        }

        public void setDistrict(DistrictBean district) {
            this.district = district;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public String getTeam_name() {
            return team_name;
        }

        public void setTeam_name(String team_name) {
            this.team_name = team_name;
        }

        public boolean isAttention() {
            return isAttention;
        }

        public void setAttention(boolean attention) {
            isAttention = attention;
        }

        public static class DistrictBean {
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

        public static class ImageBean {
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
    }
}
