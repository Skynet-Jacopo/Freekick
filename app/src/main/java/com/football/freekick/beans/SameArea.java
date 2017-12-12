package com.football.freekick.beans;

import android.os.Parcel;
import android.os.Parcelable;

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

    public static class TeamBean implements Parcelable {
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
        private String remark;
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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
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

        public static class ImageBean implements Parcelable {
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

        public static class DistrictBean implements Parcelable {
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

        public static class UserBean implements Parcelable {
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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.username);
                dest.writeString(this.role);
            }

            public UserBean() {
            }

            protected UserBean(Parcel in) {
                this.id = in.readInt();
                this.username = in.readString();
                this.role = in.readString();
            }

            public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
                @Override
                public UserBean createFromParcel(Parcel source) {
                    return new UserBean(source);
                }

                @Override
                public UserBean[] newArray(int size) {
                    return new UserBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.team_name);
            dest.writeInt(this.size);
            dest.writeString(this.color1);
            dest.writeString(this.color2);
            dest.writeString(this.remark);
            dest.writeString(this.status);
            dest.writeParcelable(this.image, flags);
            dest.writeInt(this.establish_year);
            dest.writeInt(this.average_height);
            dest.writeInt(this.age_range_min);
            dest.writeInt(this.age_range_max);
            dest.writeParcelable(this.district, flags);
            dest.writeParcelable(this.user, flags);
            dest.writeStringList(this.style);
            dest.writeStringList(this.battle_preference);
            dest.writeByte(this.attention ? (byte) 1 : (byte) 0);
        }

        public TeamBean() {
        }

        protected TeamBean(Parcel in) {
            this.id = in.readInt();
            this.team_name = in.readString();
            this.size = in.readInt();
            this.color1 = in.readString();
            this.color2 = in.readString();
            this.remark = in.readString();
            this.status = in.readString();
            this.image = in.readParcelable(ImageBean.class.getClassLoader());
            this.establish_year = in.readInt();
            this.average_height = in.readInt();
            this.age_range_min = in.readInt();
            this.age_range_max = in.readInt();
            this.district = in.readParcelable(DistrictBean.class.getClassLoader());
            this.user = in.readParcelable(UserBean.class.getClassLoader());
            this.style = in.createStringArrayList();
            this.battle_preference = in.createStringArrayList();
            this.attention = in.readByte() != 0;
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
}
