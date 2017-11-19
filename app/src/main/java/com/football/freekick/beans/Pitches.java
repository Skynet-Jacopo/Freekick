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
    }
}
