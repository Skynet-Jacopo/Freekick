package com.football.freekick.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ly on 2017/11/26.
 */

public class Article {

    private List<ArticleBean> article;

    public List<ArticleBean> getArticle() {
        return article;
    }

    public void setArticle(List<ArticleBean> article) {
        this.article = article;
    }

    public static class ArticleBean implements Parcelable {
        /**
         * content : <p>hello article</p>

         * id : 2
         * category : news
         * updated_at : 2017-11-10T16:12:50.000+08:00
         * tag :
         * subject : test
         * deleted_at : null
         * created_at : 2017-11-10T16:12:50.000+08:00
         * image : http://api.freekick.hk/uploads/article/image/2/__1.png
         */

        private String content;
        private int id;
        private String category;
        private String updated_at;
        private String tag;
        private String subject;
        private String deleted_at;
        private String created_at;
        private String image;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(String deleted_at) {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.content);
            dest.writeInt(this.id);
            dest.writeString(this.category);
            dest.writeString(this.updated_at);
            dest.writeString(this.tag);
            dest.writeString(this.subject);
            dest.writeString(this.deleted_at);
            dest.writeString(this.created_at);
            dest.writeString(this.image);
        }

        public ArticleBean() {
        }

        protected ArticleBean(Parcel in) {
            this.content = in.readString();
            this.id = in.readInt();
            this.category = in.readString();
            this.updated_at = in.readString();
            this.tag = in.readString();
            this.subject = in.readString();
            this.deleted_at = in.readString();
            this.created_at = in.readString();
            this.image = in.readString();
        }

        public static final Parcelable.Creator<ArticleBean> CREATOR = new Parcelable.Creator<ArticleBean>() {
            @Override
            public ArticleBean createFromParcel(Parcel source) {
                return new ArticleBean(source);
            }

            @Override
            public ArticleBean[] newArray(int size) {
                return new ArticleBean[size];
            }
        };
    }
}
