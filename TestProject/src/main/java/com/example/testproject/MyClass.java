package com.example.testproject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyClass {
    public static void main(String[] args) {
        System.out.println("Test");
        String url = "http://www.usd-cny.com/bankofchina.htm";
        Map<String,Float> map=new HashMap<String,Float>();
        List<String> data = new ArrayList<String>();

        try {
            Document doc = Jsoup.connect(url).get();
            System.out.println(doc.title());
            Element table = doc.getElementsByTag("table").first();
            Elements trs = table.getElementsByTag("tr");

            for(Element tr: trs){
                Elements tds = tr.getElementsByTag("td");
                if(tds.size()>0){
                    String td1 = tds.get(0).text();
                    String td2 = tds.get(5).text();
                    map.put(td1,Float.parseFloat(td2)/100);
                    data.add(td1+"==>"+td2);
                }
            }
            System.out.println(data);
            Float a = map.get("欧元");
            Float b = map.get("日元");
            Float c = map.get("美元");
            //System.out.println(a+"");
            //System.out.println(b+"");
            //System.out.println(c+"");

        }catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}