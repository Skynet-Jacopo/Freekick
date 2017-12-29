package com.football.freekick.beans;

import java.util.List;

/**
 * Created by ly on 2017/12/5.
 */

public class MatchDetail {

    /**
     * match : {"id":107,"play_end":"2017-12-10T15:00:00.000+08:00","status":"m","home_team_color":"FF0D00",
     * "home_team":{"id":55,"image":{"url":"/uploads/team/image/55/image.jpeg"},"team_name":"雷队"},
     * "join_matches":[{"id":38,"team":{"district":{"region":"Hong Kong","district":"Central and Western","id":19},
     * "image":{"url":"/uploads/team/image/52/image.jpeg"},"size":7,"team_name":"岳队"},"join_team_color":null,
     * "status":"confirmed","join_team_id":52}],"play_start":"2017-12-10T12:00:00.000+08:00","pitch_id":1,"size":7}
     */

    private MatchBean match;

    public MatchBean getMatch() {
        return match;
    }

    public void setMatch(MatchBean match) {
        this.match = match;
    }

    public static class MatchBean {
        /**
         * id : 107
         * play_end : 2017-12-10T15:00:00.000+08:00
         * status : m
         * home_team_color : FF0D00
         * home_team : {"id":55,"image":{"url":"/uploads/team/image/55/image.jpeg"},"team_name":"雷队"}
         * join_matches : [{"id":38,"team":{"district":{"region":"Hong Kong","district":"Central and Western",
         * "id":19},"image":{"url":"/uploads/team/image/52/image.jpeg"},"size":7,"team_name":"岳队"},
         * "join_team_color":null,"status":"confirmed","join_team_id":52}]
         * play_start : 2017-12-10T12:00:00.000+08:00
         * pitch_id : 1
         * size : 7
         */

        private int id;
        private String play_end;
        private String status;
        private String home_team_color;
        private HomeTeamBean home_team;
        private String play_start;
        private int pitch_id;
        private int size;
        private String location;
        private String pitch_name;
        private List<JoinMatchesBean> join_matches;
        private double longitude;
        private double latitude;
        private String pitch_image;
        private String match_url;

        public String getMatch_url() {
            return match_url;
        }

        public void setMatch_url(String match_url) {
            this.match_url = match_url;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getPitch_image() {
            return pitch_image;
        }

        public void setPitch_image(String pitch_image) {
            this.pitch_image = pitch_image;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getPitch_name() {
            return pitch_name;
        }

        public void setPitch_name(String pitch_name) {
            this.pitch_name = pitch_name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPlay_end() {
            return play_end;
        }

        public void setPlay_end(String play_end) {
            this.play_end = play_end;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getHome_team_color() {
            return home_team_color;
        }

        public void setHome_team_color(String home_team_color) {
            this.home_team_color = home_team_color;
        }

        public HomeTeamBean getHome_team() {
            return home_team;
        }

        public void setHome_team(HomeTeamBean home_team) {
            this.home_team = home_team;
        }

        public String getPlay_start() {
            return play_start;
        }

        public void setPlay_start(String play_start) {
            this.play_start = play_start;
        }

        public int getPitch_id() {
            return pitch_id;
        }

        public void setPitch_id(int pitch_id) {
            this.pitch_id = pitch_id;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public List<JoinMatchesBean> getJoin_matches() {
            return join_matches;
        }

        public void setJoin_matches(List<JoinMatchesBean> join_matches) {
            this.join_matches = join_matches;
        }

        public static class HomeTeamBean {
            /**
             * id : 55
             * image : {"url":"/uploads/team/image/55/image.jpeg"}
             * team_name : 雷队
             */

            private int id;
            private ImageBean image;
            private String team_name;

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

            public static class ImageBean {
                /**
                 * url : /uploads/team/image/55/image.jpeg
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

        public static class JoinMatchesBean {
            /**
             * id : 38
             * team : {"district":{"region":"Hong Kong","district":"Central and Western","id":19},
             * "image":{"url":"/uploads/team/image/52/image.jpeg"},"size":7,"team_name":"岳队"}
             * join_team_color : null
             * status : confirmed
             * join_team_id : 52
             */

            private int id;
            private TeamBean team;
            private String join_team_color;
            private String status;
            private int join_team_id;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public TeamBean getTeam() {
                return team;
            }

            public void setTeam(TeamBean team) {
                this.team = team;
            }

            public String getJoin_team_color() {
                return join_team_color;
            }

            public void setJoin_team_color(String join_team_color) {
                this.join_team_color = join_team_color;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getJoin_team_id() {
                return join_team_id;
            }

            public void setJoin_team_id(int join_team_id) {
                this.join_team_id = join_team_id;
            }

            public static class TeamBean {
                /**
                 * district : {"region":"Hong Kong","district":"Central and Western","id":19}
                 * image : {"url":"/uploads/team/image/52/image.jpeg"}
                 * size : 7
                 * team_name : 岳队
                 */

                private DistrictBean district;
                private ImageBeanX image;
                private int size;
                private String team_name;

                public DistrictBean getDistrict() {
                    return district;
                }

                public void setDistrict(DistrictBean district) {
                    this.district = district;
                }

                public ImageBeanX getImage() {
                    return image;
                }

                public void setImage(ImageBeanX image) {
                    this.image = image;
                }

                public int getSize() {
                    return size;
                }

                public void setSize(int size) {
                    this.size = size;
                }

                public String getTeam_name() {
                    return team_name;
                }

                public void setTeam_name(String team_name) {
                    this.team_name = team_name;
                }

                public static class DistrictBean {
                    /**
                     * region : Hong Kong
                     * district : Central and Western
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

                public static class ImageBeanX {
                    /**
                     * url : /uploads/team/image/52/image.jpeg
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
    }
}
