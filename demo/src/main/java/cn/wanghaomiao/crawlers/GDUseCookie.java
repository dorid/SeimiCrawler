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
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by dorid on 2017/3/28.
 */
@Crawler(name = "gdUsecookie",useCookie = true)
public class GDUseCookie extends BaseSeimiCrawler {
    @Override
    public String[] startUrls() {
        return null;
    }

    public Request getGeetestValidate() {
        Request start = Request.build("http://120.77.36.82:8080/rest/geetest/cracker?targetUrl=http://gd.gsxt.gov.cn&userId=000000005a1b3747015a1b3c5b600000","start");
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

        Request start = Request.build("http://gd.gsxt.gov.cn/aiccips/verify/sec.html","minePage");
        Map<String ,String> formParams = new HashedMap();
        formParams.put("textfield", "长虹");
        formParams.put("geetest_challenge", geetestChallenge);
        formParams.put("geetest_validate", geetestValidate);
        formParams.put("geetest_seccode", geetestSeccode);

        Map<String ,String> headerMap = new HashedMap();
        headerMap.put("Accept", "application/json, text/javascript, */*; q=0.01");
        headerMap.put("Accept-Encoding", "gzip, deflate");
        headerMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headerMap.put("Host", "gd.gsxt.gov.cn");
        headerMap.put("Origin", "http://gd.gsxt.gov.cn");
        headerMap.put("Referer", "http://gd.gsxt.gov.cn");
        headerMap.put("User-Agent", "ozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        headerMap.put("X-Requested-With", "XMLHttpRequest");

        start.setParams(formParams);
        start.setHeader(headerMap);
        start.setHttpMethod(HttpMethod.POST);
        push(start);
    }

    public void minePage(Response response){
        String content = response.getContent();
        JSONObject jsonObject = JSON.parseObject(content);
        if ("success".equals(jsonObject.getString("status"))) {
            String textfield = jsonObject.getString("textfield");

            Request start = Request.build("http://gd.gsxt.gov.cn/aiccips/CheckEntContext/showCheck.html","companyList");
            Map<String ,String> formParams = new HashedMap();
            formParams.put("textfield", textfield);
            formParams.put("type", "nomal");

            start.setParams(formParams);
            start.setHttpMethod(HttpMethod.POST);
            push(start);
        }

    }

    public void companyList(Response response) {
        System.out.println(response.getContent());

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
        }

    }

}
