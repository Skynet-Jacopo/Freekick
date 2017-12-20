package com.football.freekick.beans;

/**
 * Created by ly on 2017/12/20.
 */

public class RateResponse {

    /**
     * rating : {"id":24,"match_id":1,"team_id":10,"attack":5,"defense":4,"on_time":4,"technic":4,"personality":5,
     * "created_at":"2017-11-06T15:59:44.000Z","updated_at":"2017-11-06T15:59:44.000Z"}
     */

    private RatingBean rating;
    /**
     * errors : You have already provided rating for this match.
     */

    private String errors;

    public RatingBean getRating() {
        return rating;
    }

    public void setRating(RatingBean rating) {
        this.rating = rating;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public static class RatingBean {
        /**
         * id : 24
         * match_id : 1
         * team_id : 10
         * attack : 5
         * defense : 4
         * on_time : 4
         * technic : 4
         * personality : 5
         * created_at : 2017-11-06T15:59:44.000Z
         * updated_at : 2017-11-06T15:59:44.000Z
         */

        private int id;
        private int match_id;
        private int team_id;
        private int attack;
        private int defense;
        private int on_time;
        private int technic;
        private int personality;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMatch_id() {
            return match_id;
        }

        public void setMatch_id(int match_id) {
            this.match_id = match_id;
        }

        public int getTeam_id() {
            return team_id;
        }

        public void setTeam_id(int team_id) {
            this.team_id = team_id;
        }

        public int getAttack() {
            return attack;
        }

        public void setAttack(int attack) {
            this.attack = attack;
        }

        public int getDefense() {
            return defense;
        }

        public void setDefense(int defense) {
            this.defense = defense;
        }

        public int getOn_time() {
            return on_time;
        }

        public void setOn_time(int on_time) {
            this.on_time = on_time;
        }

        public int getTechnic() {
            return technic;
        }

        public void setTechnic(int technic) {
            this.technic = technic;
        }

        public int getPersonality() {
            return personality;
        }

        public void setPersonality(int personality) {
            this.personality = personality;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
