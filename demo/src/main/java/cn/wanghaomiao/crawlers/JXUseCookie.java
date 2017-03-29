package cn.wanghaomiao.crawlers;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.http.HttpMethod;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;
import cn.wanghaomiao.xpath.model.JXNode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dorid on 2017/3/28.
 */
@Crawler(name = "jxUsecookie",useCookie = true)
public class JXUseCookie extends BaseSeimiCrawler {
    @Override
    public String[] startUrls() {
        return null;
    }

    public Request getGeetestValidate() {
        Request start = Request.build("http://120.77.36.82:8080/rest/geetest/cracker?targetUrl=http://jx.gsxt.gov.cn&userId=000000005a1b3747015a1b3c5b600000","start");
        Map<String,String> params = new HashMap<>();
        start.setHttpMethod(HttpMethod.POST);
        start.setParams(params);

        return start;
    }

    @Override
    public List<Request> startRequests() {
        List<Request> requests = new LinkedList<>();
        requests.add(getGeetestValidate());
        return requests;
    }

    @Override
    public void start(Response response) {
        JSONObject jsonObject = JSON.parseObject(response.getContent());
        JSONObject data = jsonObject.getJSONObject("data");
        String geetestChallenge = data.getString("geetest_challenge");
        String geetestValidate = data.getString("geetest_validate");
        String geetestSeccode = data.getString("geetest_seccode");

        Request start = Request.build("http://jx.gsxt.gov.cn/vfygeettest/querygeetest","minePage");
        Map<String ,String> formParams = new HashedMap();
        formParams.put("geetest_challenge", geetestChallenge);
        formParams.put("geetest_validate", geetestValidate);
        formParams.put("geetest_seccode", geetestSeccode);

        formParams.put("searchkey", "MzYwNjgxMjEwMDM3NTMy");
        formParams.put("searchtype", "qyxy");
        formParams.put("entname", "360681210037532");


        Map<String ,String> headerMap = new HashedMap();
        headerMap.put("Cookie", "UM_distinctid=15ac7bf39390-0fcba3aadb76ae-6a11157a-100200-15ac7bf393a3ca; Hm_lvt_cdb4bc83287f8c1282df45ed61c4eac9=1489412408,1490273705,1490714962,1490792262; Hm_lpvt_cdb4bc83287f8c1282df45ed61c4eac9=1490792410; JSESSIONID=1CD03BD86FEFA077FC8A1C9BF7A5238C; test=21286086; CNZZDATA1261016996=2143322116-1490788323-%7C1490793728");
        headerMap.put("Referer", "http://jx.gsxt.gov.cn/index.jsp");
        headerMap.put("Accept", "application/json, text/javascript, */*; q=0.01");
        headerMap.put("Accept-Encoding", "gzip, deflate");
        headerMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headerMap.put("Host", "jx.gsxt.gov.cn");
        headerMap.put("Origin", "http://jx.gsxt.gov.cn");
        headerMap.put("User-Agent", "ozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        headerMap.put("X-Requested-With", "XMLHttpRequest");

        start.setParams(formParams);
        start.setHeader(headerMap);
        start.setHttpMethod(HttpMethod.POST);
        push(start);
    }

    public void minePage(Response response){
        String content = response.getContent();
        System.out.println("content = " + content);
        JSONObject jsonObject = JSON.parseObject(content);
        String status = jsonObject.getString("status");
        if ("success".equals(status)) {
            String url = jsonObject.getString("url");
            url = "http://jx.gsxt.gov.cn/" + url;
            System.out.println("url = " + url);

            Request start = Request.build(url,"companyList");


            Map<String ,String> headerMap = new HashedMap();
            headerMap.put("Cookie", "UM_distinctid=15ac7bf39390-0fcba3aadb76ae-6a11157a-100200-15ac7bf393a3ca; Hm_lvt_cdb4bc83287f8c1282df45ed61c4eac9=1489412408,1490273705,1490714962,1490792262; Hm_lpvt_cdb4bc83287f8c1282df45ed61c4eac9=1490792410; JSESSIONID=1CD03BD86FEFA077FC8A1C9BF7A5238C; test=21286086; CNZZDATA1261016996=2143322116-1490788323-%7C1490793728");
            headerMap.put("Referer", "http://jx.gsxt.gov.cn/index.jsp");
            headerMap.put("Accept", "application/json, text/javascript, */*; q=0.01");
            headerMap.put("Accept-Encoding", "gzip, deflate");
            headerMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            headerMap.put("Host", "jx.gsxt.gov.cn");
            headerMap.put("Origin", "http://jx.gsxt.gov.cn");
            headerMap.put("User-Agent", "ozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
            headerMap.put("X-Requested-With", "XMLHttpRequest");
            start.setHeader(headerMap);

            push(start);
        }
    }

    public void companyList(Response response) {
//        System.out.println(response.getContent());
        Pattern pattern = Pattern.compile("\\{.*\\}");
        Matcher matcher = pattern.matcher(response.getContent());
        while (matcher.find()) {
            String group = matcher.group();
            JSONObject jsonObject = JSONObject.parseObject(group);
            Integer total = jsonObject.getInteger("total");
            if (total != null && total != 0) {
                String data = jsonObject.getString("data");
                JSONArray array = JSONObject.parseArray(data);
                JSONObject object = (JSONObject) array.get(0);
                String pripid = object.getString("PRIPID");
                String decode = JSScriptEngine.decode(pripid);
                String url = "http://jx.gsxt.gov.cn/pages/nzgsfr/informationinfo.jsp?pripid=" + decode;
                System.out.println("url = " + url);


                Request start = Request.build(url,"companyDetail");

                Map<String ,String> headerMap = new HashedMap();
                headerMap.put("Cookie", "UM_distinctid=15ac7bf39390-0fcba3aadb76ae-6a11157a-100200-15ac7bf393a3ca; Hm_lvt_cdb4bc83287f8c1282df45ed61c4eac9=1489412408,1490273705,1490714962,1490792262; Hm_lpvt_cdb4bc83287f8c1282df45ed61c4eac9=1490792410; JSESSIONID=1CD03BD86FEFA077FC8A1C9BF7A5238C; test=21286086; CNZZDATA1261016996=2143322116-1490788323-%7C1490793728");
                headerMap.put("Referer", "http://jx.gsxt.gov.cn/index.jsp");
                headerMap.put("Accept", "application/json, text/javascript, */*; q=0.01");
                headerMap.put("Accept-Encoding", "gzip, deflate");
                headerMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                headerMap.put("Host", "jx.gsxt.gov.cn");
                headerMap.put("Origin", "http://jx.gsxt.gov.cn");
                headerMap.put("User-Agent", "ozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
                headerMap.put("X-Requested-With", "XMLHttpRequest");
                start.setHeader(headerMap);

                push(start);
                break;
            }
//            System.out.println("group = " + group);
        }
/*
        JXDocument doc = response.document();
        try {
            List<JXNode> nodeList = doc.selN("//div[@style='margin-left: 160px;padding-left: 10px;']");
            for (JXNode node : nodeList) {
                List<JXNode> sel = node.sel("//span[@class='rsfont']/text()");
                List<JXNode> code = node.sel("//span[@class='dataTextStyle']/text()");
                System.out.println( sel + " ," + code);
            }
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }*/

    }

    public void companyDetail(Response response) {
        JXDocument doc = response.document();
        try {
            List<Object> list = doc.sel("//th[@id='REGNO']");
            System.out.println(list);
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }
    }

}
