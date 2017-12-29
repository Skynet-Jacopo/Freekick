package com.football.freekick.beans;

import java.io.Serializable;
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

    public static class MatchesBean  implements Serializable {
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
        private String default_image;
        private String pitch_name;
        private String location;
        private HomeTeamBean home_team;
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

        public String getPitch_name() {
            return pitch_name;
        }

        public void setPitch_name(String pitch_name) {
            this.pitch_name = pitch_name;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
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

        public String getDefault_image() {
            return default_image;
        }

        public void setDefault_image(String default_image) {
            this.default_image = default_image;
        }

        public static class HomeTeamBean  implements Serializable {
            /**
             * id : 33
             * image : {"url":"/uploads/team/image/33/upload-image-9724761-1510149726."}
             */

            private int id;
            private String team_name;
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

            public String getTeam_name() {
                return team_name;
            }

            public void setTeam_name(String team_name) {
                this.team_name = team_name;
            }

            public static class ImageBean implements Serializable {
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

        public static class JoinMatchesBean implements Serializable {
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

            public static class TeamBean  implements Serializable {
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

                public static class ImageBeanX  implements Serializable {
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

                public static class DistrictBean implements Serializable {
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
