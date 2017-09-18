package com.medicinedot.www.medicinedot.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android on 2017/8/9.
 */

public class HomeSupplierinfo implements Serializable{

    /**
     * errorcode : 200
     * msg : 查询成功
     * data : [{"uid":2,"utype":2,"name":"朱芸","region":"北京","content":"供应药品1000+种，包括药品1、药品2、药品3...","address":"北京市昌平区","sex":null,"headimg":""}]
     */

    private String errorcode;
    private String msg;
    private String count;
    private String is_member;
    private String endtime;

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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 2
         * utype : 2
         * name : 朱芸
         * region : 北京
         * content : 供应药品1000+种，包括药品1、药品2、药品3...
         * address : 北京市昌平区
         * sex : null
         * headimg :
         */

        private int uid;
        private int utype;
        private String name;
        private String region;
        private String content;
        private String address;
        private Object sex;
        private String headimg;
        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getUtype() {
            return utype;
        }

        public void setUtype(int utype) {
            this.utype = utype;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public Object getSex() {
            return sex;
        }

        public void setSex(Object sex) {
            this.sex = sex;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }
    }
}
