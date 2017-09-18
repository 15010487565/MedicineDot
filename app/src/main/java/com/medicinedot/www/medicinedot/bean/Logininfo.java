package com.medicinedot.www.medicinedot.bean;

import java.io.Serializable;

/**
 * Created by Android on 2017/8/9.
 */

public class Logininfo implements Serializable{

    /**
     * errorcode : 200
     * msg : 登录成功
     * data : {"uid":"19","utype":"1","name":"15727393984","phone":"15727393984","region":"-北京市-","content":"","address":"","is_member":"1","endtime":"2017-09-11 10:47:25","sex":"2","headimg":"","token":"acde8f400b0ac19479e70345ffa4086f748419ba"}
     */

    private String errorcode;
    private String msg;
    private DataBean data;

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 19
         * utype : 1
         * name : 15727393984
         * phone : 15727393984
         * region : -北京市-
         * content :
         * address :
         * is_member : 1
         * endtime : 2017-09-11 10:47:25
         * sex : 2
         * headimg :
         * token : acde8f400b0ac19479e70345ffa4086f748419ba
         */

        private String uid;
        private String utype;
        private String name;
        private String phone;
        private String region;
        private String content;
        private String address;
        private String is_member;
        private String endtime;
        private String sex;
        private String headimg;
        private String token;
        private String hxname;
        private String hxpwd;

        public String getHxname() {
            return hxname;
        }

        public void setHxname(String hxname) {
            this.hxname = hxname;
        }

        public String getHxpwd() {
            return hxpwd;
        }

        public void setHxpwd(String hxpwd) {
            this.hxpwd = hxpwd;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUtype() {
            return utype;
        }

        public void setUtype(String utype) {
            this.utype = utype;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIs_member() {
            return is_member;
        }

        public void setIs_member(String is_member) {
            this.is_member = is_member;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
