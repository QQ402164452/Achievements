package bean;

import java.util.List;

/**
 * Created by Jason on 2016/11/26.
 */

public class CityBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * p : 北京
         * c : ["北京市"]
         */

        private String p;
        private List<String> c;

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public List<String> getC() {
            return c;
        }

        public void setC(List<String> c) {
            this.c = c;
        }
    }
}
