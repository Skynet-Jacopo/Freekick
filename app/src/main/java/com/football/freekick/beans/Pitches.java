package com.football.freekick.beans;

import java.util.List;

/**
 * Created by ly on 2017/11/19.
 */

public class Pitches {

    private List<PitchesBean> pitches;

    public List<PitchesBean> getPitches() {
        return pitches;
    }

    public void setPitches(List<PitchesBean> pitches) {
        this.pitches = pitches;
    }

    public static class PitchesBean {
        /**
         * id : 1
         * district : {"region":"Hong Kong","district":"Central and Western","id":19}
         * location : Test location 2
         * name : Test12
         * size : 19
         */

        private int id;
        private DistrictBean district;
        private String location;
        private String name;
        private int size;
        /**
         * image : {"url":"/uploads/pitch/image/11/HKE1.jpg"}
         * longitude : 23
         * latitude : 114
         */

        private ImageBean image;
        private double longitude;
        private double latitude;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public DistrictBean getDistrict() {
            return district;
        }

        public void setDistrict(DistrictBean district) {
            this.district = district;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public static class ImageBean {
            /**
             * url : /uploads/pitch/image/11/HKE1.jpg
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
