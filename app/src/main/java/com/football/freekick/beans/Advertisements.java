package com.football.freekick.beans;

import java.util.List;

/**
 * Created by ly on 2017/11/19.
 */

public class Advertisements {

    private List<AdvertisementsBean> advertisements;

    public List<AdvertisementsBean> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(List<AdvertisementsBean> advertisements) {
        this.advertisements = advertisements;
    }

    public static class AdvertisementsBean {
        /**
         * id : 2
         * screen : hello 中文 2
         * updated_at : 2017-10-28T17:42:01.000Z
         * name : hello 中文
         * deleted_at : null
         * created_at : 2017-10-23T19:29:31.000Z
         * image : http://api.freekick.hk/uploads/advertisement/image/2/Caerte_van_Oostlant_4MB.jpg
         * default_image : http://api.freekick.hk/uploads/advertisement/default_image/2/images__2_.jpg
         */

        private int id;
        private String screen;
        private String updated_at;
        private String name;
        private Object deleted_at;
        private String created_at;
        private String image;
        private String default_image;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getScreen() {
            return screen;
        }

        public void setScreen(String screen) {
            this.screen = screen;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(Object deleted_at) {
            this.deleted_at = deleted_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDefault_image() {
            return default_image;
        }

        public void setDefault_image(String default_image) {
            this.default_image = default_image;
        }
    }
}
