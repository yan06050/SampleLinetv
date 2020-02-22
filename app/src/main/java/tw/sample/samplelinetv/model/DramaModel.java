package tw.sample.samplelinetv.model;

import java.io.Serializable;
import java.util.ArrayList;

public class DramaModel {
    private ArrayList<DramaInfo> data;

    public ArrayList<DramaInfo> getDramaInfos() {
        return data;
    }

    public void setDramaInfos(ArrayList<DramaInfo> data) {
        this.data = data;
    }

    public class DramaInfo implements Serializable {
        private int drama_id;
        private String name;
        private int total_views;
        private String created_at;
        private String thumb;
        private double rating;

        public int getDrama_id() {
            return drama_id;
        }

        public void setDrama_id(int drama_id) {
            this.drama_id = drama_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTotal_views() {
            return total_views;
        }

        public void setTotal_views(int total_views) {
            this.total_views = total_views;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }
    }


}
