package com.myplas.q.myinfo.beans;

import java.util.List;

/**
 * 编写： 黄双
 * 电话：15378412400
 * 邮箱：15378412400@163.com
 * 时间：2017/3/29 09:40
 */
public class ApplyInvoiceBean {


    /**
     * err : 0
     * data : {"detail":{"total_price":"86.00","rise":"WE战队有限公司","order_sn":"SO2017080715275918","unbilling_price":"86.0000","billing_price":"86.0000"},"list":[{"id":"131","f_name":"沈阳石化","model":"7042","number":2,"unit_price":"22.00","price":44,"total_num":4,"b_number":1},{"id":"132","f_name":"沈阳石化","model":"7042","number":2,"unit_price":"21.00","price":42,"total_num":4,"b_number":1}],"page":""}
     */

    private int err;
    private DataBean data;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * detail : {"total_price":"86.00","rise":"WE战队有限公司","order_sn":"SO2017080715275918","unbilling_price":"86.0000","billing_price":"86.0000"}
         * list : [{"id":"131","f_name":"沈阳石化","model":"7042","number":2,"unit_price":"22.00","price":44,"total_num":4,"b_number":1},{"id":"132","f_name":"沈阳石化","model":"7042","number":2,"unit_price":"21.00","price":42,"total_num":4,"b_number":1}]
         * page :
         */

        private DetailBean detail;
        private String page;
        private List<ListBean> list;

        public DetailBean getDetail() {
            return detail;
        }

        public void setDetail(DetailBean detail) {
            this.detail = detail;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class DetailBean {
            /**
             * total_price : 86.00
             * rise : WE战队有限公司
             * order_sn : SO2017080715275918
             * unbilling_price : 86.0000
             * billing_price : 86.0000
             */

            private String total_price;
            private String rise;
            private String order_sn;
            private String unbilling_price;
            private String billing_price;

            public String getTotal_price() {
                return total_price;
            }

            public void setTotal_price(String total_price) {
                this.total_price = total_price;
            }

            public String getRise() {
                return rise;
            }

            public void setRise(String rise) {
                this.rise = rise;
            }

            public String getOrder_sn() {
                return order_sn;
            }

            public void setOrder_sn(String order_sn) {
                this.order_sn = order_sn;
            }

            public String getUnbilling_price() {
                return unbilling_price;
            }

            public void setUnbilling_price(String unbilling_price) {
                this.unbilling_price = unbilling_price;
            }

            public String getBilling_price() {
                return billing_price;
            }

            public void setBilling_price(String billing_price) {
                this.billing_price = billing_price;
            }
        }

        public static class ListBean {
            /**
             * id : 131
             * f_name : 沈阳石化
             * model : 7042
             * number : 2
             * unit_price : 22.00
             * price : 44
             * total_num : 4
             * b_number : 1
             */

            private String id;
            private String f_name;
            private String model;
            private double number;
            private String unit_price;
            private String price;
            private double total_num;
            private double b_number;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getF_name() {
                return f_name;
            }

            public void setF_name(String f_name) {
                this.f_name = f_name;
            }

            public String getModel() {
                return model;
            }

            public void setModel(String model) {
                this.model = model;
            }

            public double getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public String getUnit_price() {
                return unit_price;
            }

            public void setUnit_price(String unit_price) {
                this.unit_price = unit_price;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public double getTotal_num() {
                return total_num;
            }

            public void setTotal_num(int total_num) {
                this.total_num = total_num;
            }

            public double getB_number() {
                return b_number;
            }

            public void setB_number(int b_number) {
                this.b_number = b_number;
            }
        }
    }
}
