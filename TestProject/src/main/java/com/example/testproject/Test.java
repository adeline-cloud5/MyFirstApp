package com.example.testproject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

public class Test {
    public static void main(String[] args) throws IOException {
        String url = "https://www.fjnu.edu.cn/";
        URL httpUrl=new URL("http://www.usd-cny.com/bankofchina.htm");
        Connection connect = Jsoup.connect(url);//获取连接对象
        Document document = connect.get();//获取url页面的内容并解析成document对象
        Elements as = document.getElementsByTag("a");//获取所有的a标签
        for (int i = 0; i < as.size(); i++) {//遍历a标签
            Element element = as.get(i);
            String href = element.attr("href");//获取链接的url值
            String text = element.text();//获取链接的标题
            if(href.startsWith("http"))//筛选以http开头的链接
                System.out.println(href+" "+text);
        }
    }

}
