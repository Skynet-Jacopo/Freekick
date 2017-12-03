package com.football.freekick.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ly on 2017/12/1.
 */

public class MatchesComing {

    private List<MatchesBean> matches;

    public List<MatchesBean> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchesBean> matches) {
        this.matches = matches;
    }

    public static class MatchesBean implements Parcelable {
        public boolean isInvited() {
            return invited;
        }

        public void setInvited(boolean invited) {
            this.invited = invited;
        }

        /**
         * id : 5
         * play_start : 2017-11-18T12:00:00.000Z
         * play_end : 2017-11-20T01:00:00.000Z
         * pitch_id : 1
         * home_team_color : ffffff
         * status : w
         * home_team : {"id":37,"size":5,"image":{"url":"/uploads/team/image/37/upload-image-863384-1510218172."}}
         * join_matches : [{"join_team_id":48,"status":"confirmation_pending","join_team_color":"ffc300",
         * "team":{"team_name":"Lions9dd875","size":5,"image":{"url":null},"district":{"id":72,"district":"Yuen
         * Long","region":"New Territories"}}}]
         */

        private boolean invited;//標識join_match中有沒有invited
        private int id;
        private String play_start;
        private String play_end;
        private int pitch_id;
        private String home_team_color;
        private String status;
        private HomeTeamBean home_team;
        private List<JoinMatchesBean> join_matches;

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

        private String pitch_name;
        private String location;
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

        public static class HomeTeamBean implements Parcelable {
            /**
             * id : 37
             * size : 5
             * image : {"url":"/uploads/team/image/37/upload-image-863384-1510218172."}
             */

            private int id;
            private int size;
            private String team_name;
            private ImageBean image;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
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

            public static class ImageBean implements Parcelable {
                /**
                 * url : /uploads/team/image/37/upload-image-863384-1510218172.
                 */

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.url);
                }

                public ImageBean() {
                }

                protected ImageBean(Parcel in) {
                    this.url = in.readString();
                }

                public static final Parcelable.Creator<ImageBean> CREATOR = new Parcelable.Creator<ImageBean>() {
                    @Override
                    public ImageBean createFromParcel(Parcel source) {
                        return new ImageBean(source);
                    }

                    @Override
                    public ImageBean[] newArray(int size) {
                        return new ImageBean[size];
                    }
                };
            }

            public HomeTeamBean() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeInt(this.size);
                dest.writeString(this.team_name);
                dest.writeParcelable(this.image, flags);
            }

            protected HomeTeamBean(Parcel in) {
                this.id = in.readInt();
                this.size = in.readInt();
                this.team_name = in.readString();
                this.image = in.readParcelable(ImageBean.class.getClassLoader());
            }

            public static final Creator<HomeTeamBean> CREATOR = new Creator<HomeTeamBean>() {
                @Override
                public HomeTeamBean createFromParcel(Parcel source) {
                    return new HomeTeamBean(source);
                }

                @Override
                public HomeTeamBean[] newArray(int size) {
                    return new HomeTeamBean[size];
                }
            };
        }

        public static class JoinMatchesBean implements Parcelable {
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

            public static class TeamBean implements Parcelable {
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

                public static class ImageBeanX implements Parcelable {
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

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(this.url);
                    }

                    public ImageBeanX() {
                    }

                    protected ImageBeanX(Parcel in) {
                        this.url = in.readString();
                    }

                    public static final Parcelable.Creator<ImageBeanX> CREATOR = new Parcelable.Creator<ImageBeanX>() {
                        @Override
                        public ImageBeanX createFromParcel(Parcel source) {
                            return new ImageBeanX(source);
                        }

                        @Override
                        public ImageBeanX[] newArray(int size) {
                            return new ImageBeanX[size];
                        }
                    };
                }

                public static class DistrictBean implements Parcelable {
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

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeInt(this.id);
                        dest.writeString(this.district);
                        dest.writeString(this.region);
                    }

                    public DistrictBean() {
                    }

                    protected DistrictBean(Parcel in) {
                        this.id = in.readInt();
                        this.district = in.readString();
                        this.region = in.readString();
                    }

                    public static final Parcelable.Creator<DistrictBean> CREATOR = new Parcelable.Creator<DistrictBean>() {
                        @Override
                        public DistrictBean createFromParcel(Parcel source) {
                            return new DistrictBean(source);
                        }

                        @Override
                        public DistrictBean[] newArray(int size) {
                            return new DistrictBean[size];
                        }
                    };
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.team_name);
                    dest.writeInt(this.size);
                    dest.writeParcelable(this.image, flags);
                    dest.writeParcelable(this.district, flags);
                }

                public TeamBean() {
                }

                protected TeamBean(Parcel in) {
                    this.team_name = in.readString();
                    this.size = in.readInt();
                    this.image = in.readParcelable(ImageBeanX.class.getClassLoader());
                    this.district = in.readParcelable(DistrictBean.class.getClassLoader());
                }

                public static final Parcelable.Creator<TeamBean> CREATOR = new Parcelable.Creator<TeamBean>() {
                    @Override
                    public TeamBean createFromParcel(Parcel source) {
                        return new TeamBean(source);
                    }

                    @Override
                    public TeamBean[] newArray(int size) {
                        return new TeamBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.join_team_id);
                dest.writeString(this.status);
                dest.writeString(this.join_team_color);
                dest.writeParcelable(this.team, flags);
            }

            public JoinMatchesBean() {
            }

            protected JoinMatchesBean(Parcel in) {
                this.join_team_id = in.readInt();
                this.status = in.readString();
                this.join_team_color = in.readString();
                this.team = in.readParcelable(TeamBean.class.getClassLoader());
            }

            public static final Parcelable.Creator<JoinMatchesBean> CREATOR = new Parcelable.Creator<JoinMatchesBean>() {
                @Override
                public JoinMatchesBean createFromParcel(Parcel source) {
                    return new JoinMatchesBean(source);
                }

                @Override
                public JoinMatchesBean[] newArray(int size) {
                    return new JoinMatchesBean[size];
                }
            };
        }

        public MatchesBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.invited ? (byte) 1 : (byte) 0);
            dest.writeInt(this.id);
            dest.writeString(this.play_start);
            dest.writeString(this.play_end);
            dest.writeInt(this.pitch_id);
            dest.writeString(this.home_team_color);
            dest.writeString(this.status);
            dest.writeParcelable(this.home_team, flags);
            dest.writeTypedList(this.join_matches);
            dest.writeString(this.pitch_name);
            dest.writeString(this.location);
        }

        protected MatchesBean(Parcel in) {
            this.invited = in.readByte() != 0;
            this.id = in.readInt();
            this.play_start = in.readString();
            this.play_end = in.readString();
            this.pitch_id = in.readInt();
            this.home_team_color = in.readString();
            this.status = in.readString();
            this.home_team = in.readParcelable(HomeTeamBean.class.getClassLoader());
            this.join_matches = in.createTypedArrayList(JoinMatchesBean.CREATOR);
            this.pitch_name = in.readString();
            this.location = in.readString();
        }

        public static final Creator<MatchesBean> CREATOR = new Creator<MatchesBean>() {
            @Override
            public MatchesBean createFromParcel(Parcel source) {
                return new MatchesBean(source);
            }

            @Override
            public MatchesBean[] newArray(int size) {
                return new MatchesBean[size];
            }
        };
    }
}
