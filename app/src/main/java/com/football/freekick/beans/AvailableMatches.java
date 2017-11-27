package com.football.freekick.beans;

import java.util.List;

/**
 * Created by ly on 2017/11/25.
 */

public class AvailableMatches {

    private List<MatchesBean> matches;

    public List<MatchesBean> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchesBean> matches) {
        this.matches = matches;
    }

    public static class MatchesBean {
        /**
         * id : 7
         * play_start : 2017-11-20T12:00:00.000Z
         * play_end : 2017-11-22T01:00:00.000Z
         * pitch_id : 1
         * home_team_color : ffffff
         * status : w
         * size : "5"
         * home_team : {"id":33,"image":{"url":"/uploads/team/image/33/upload-image-9724761-1510149726."}}
         * join_matches : [{"join_team_id":48,"status":"confirmation_pending","join_team_color":"ffc300",
         * "team":{"team_name":"Lions9dd875","size":5,"image":{"url":null},"district":{"id":72,"district":"Yuen
         * Long","region":"New Territories"}}}]
         */

        private int id;
        private String play_start;
        private String play_end;
        private int pitch_id;
        private String home_team_color;
        private String status;
        private String size;
        private HomeTeamBean home_team;
        private List<JoinMatchesBean> join_matches;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPlay_start() {
            return play_start;
        }

        public void setPlay_start(String play_start) {
            this.play_start = play_start;
        }

        public String getPlay_end() {
            return play_end;
        }

        public void setPlay_end(String play_end) {
            this.play_end = play_end;
        }

        public int getPitch_id() {
            return pitch_id;
        }

        public void setPitch_id(int pitch_id) {
            this.pitch_id = pitch_id;
        }

        public String getHome_team_color() {
            return home_team_color;
        }

        public void setHome_team_color(String home_team_color) {
            this.home_team_color = home_team_color;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public HomeTeamBean getHome_team() {
            return home_team;
        }

        public void setHome_team(HomeTeamBean home_team) {
            this.home_team = home_team;
        }

        public List<JoinMatchesBean> getJoin_matches() {
            return join_matches;
        }

        public void setJoin_matches(List<JoinMatchesBean> join_matches) {
            this.join_matches = join_matches;
        }

        public static class HomeTeamBean {
            /**
             * id : 33
             * image : {"url":"/uploads/team/image/33/upload-image-9724761-1510149726."}
             */

            private int id;
            private ImageBean image;

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

            public static class ImageBean {
                /**
                 * url : /uploads/team/image/33/upload-image-9724761-1510149726.
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
             * join_team_id : 48
             * status : confirmation_pending
             * join_team_color : ffc300
             * team : {"team_name":"Lions9dd875","size":5,"image":{"url":null},"district":{"id":72,"district":"Yuen
             * Long","region":"New Territories"}}
             */

            private int join_team_id;
            private String status;
            private String join_team_color;
            private TeamBean team;

            public int getJoin_team_id() {
                return join_team_id;
            }

            public void setJoin_team_id(int join_team_id) {
                this.join_team_id = join_team_id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getJoin_team_color() {
                return join_team_color;
            }

            public void setJoin_team_color(String join_team_color) {
                this.join_team_color = join_team_color;
            }

            public TeamBean getTeam() {
                return team;
            }

            public void setTeam(TeamBean team) {
                this.team = team;
            }

            public static class TeamBean {
                /**
                 * team_name : Lions9dd875
                 * size : 5
                 * image : {"url":null}
                 * district : {"id":72,"district":"Yuen Long","region":"New Territories"}
                 */

                private String team_name;
                private int size;
                private ImageBeanX image;
                private DistrictBean district;

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

                public ImageBeanX getImage() {
                    return image;
                }

                public void setImage(ImageBeanX image) {
                    this.image = image;
                }

                public DistrictBean getDistrict() {
                    return district;
                }

                public void setDistrict(DistrictBean district) {
                    this.district = district;
                }

                public static class ImageBeanX {
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

                public static class DistrictBean {
                    /**
                     * id : 72
                     * district : Yuen Long
                     * region : New Territories
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
            }
        }
    }
}
