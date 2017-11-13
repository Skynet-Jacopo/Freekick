package com.football.freekick.beans;

import java.util.List;

/**
 * Created by LiuQun on 2017/11/12.
 */

public class Area {

    private List<RegionsBean> regions;

    public List<RegionsBean> getRegions() {
        return regions;
    }

    public void setRegions(List<RegionsBean> regions) {
        this.regions = regions;
    }

    public static class RegionsBean {
        /**
         * region : Hong Kong
         * districts : [{"district":"Central and Western","district_id":"19"},{"district":"Eastern","district_id":"20"},{"district":"Southern","district_id":"21"},{"district":"Wan Chai","district_id":"22"}]
         */

        private String region;
        private List<DistrictsBean> districts;

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public List<DistrictsBean> getDistricts() {
            return districts;
        }

        public void setDistricts(List<DistrictsBean> districts) {
            this.districts = districts;
        }

        public static class DistrictsBean {
            /**
             * district : Central and Western
             * district_id : 19
             */

            private String district;
            private String district_id;

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getDistrict_id() {
                return district_id;
            }

            public void setDistrict_id(String district_id) {
                this.district_id = district_id;
            }
        }
    }
}
