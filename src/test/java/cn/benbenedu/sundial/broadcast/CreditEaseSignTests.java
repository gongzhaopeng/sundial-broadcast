package cn.benbenedu.sundial.broadcast;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.util.*;

public class CreditEaseSignTests {

    private static final String SIGN_KEY = "7be47baf24d901787b6148d23661f7745e2ae156d00b2cab111913dd4ffe9ce1";

    @Test
    public void notificationSign() {

        final var timestamp = 1605514340430L;
        final var params = Map.<String, Object>of(
                "productId", "QSNHXSZ",
                "assessTime", "1605514323926",
                "assessCode", "Vjtkj1wn1l33",
                "assessResultUrl", "https://reports.benbenedu.cn/personalStatusYixin_v1/report_5fb23100ec198920eaa32888_20201116081211.pdf");
        final var token = sign(params, timestamp);
        System.out.println("notificationSign.token: " + token);
    }

    @Test
    public void acquiringSign() {

        final var timestamp = 1608612665075L;
        final var params = new HashMap<String, Object>();
        params.put("productId", "QSNHXSZ");
        params.put("assessCode", "9X6fMuALXdNX");
        params.put("orderCode", null);
        final var token = sign(params, timestamp);
        System.out.println("acquiringSign.token: " + token);
    }

    public String sign(final Map<String, Object> params,
                       final long timestamp) {

        SortedMap<String, Object> sortedMap = new TreeMap<>(params);
        Set<Map.Entry<String, Object>> items = sortedMap.entrySet();
        StringBuilder sb = new StringBuilder();
        sb.append(SIGN_KEY).append(",").append(timestamp);
        for (Map.Entry<String, Object> item : items) {
            //跳过为null的参数
            if (null != item.getValue()) {
                sb.append(",").append(item.getValue());
            }
        }
        String ret = DigestUtils.sha256Hex(sb.toString()).substring(40);
        return ret;
    }
}
