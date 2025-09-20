package com.maxinhai.platform.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlUtils {

    /**
     * 连接到指定URL并获取文档对象
     * @param url 网页地址
     * @return
     * @throws IOException
     */
    public static Document getDocument(String url) throws IOException {
        // 连接到指定URL并获取文档对象，设置超时时间
        Document document = Jsoup.connect(url).header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                .timeout(5000)
                .get();
        return document;
    }

    /**
     * 连接到指定URL并获取文档标题
     * @param url
     * @return
     * @throws IOException
     */
    public static String getTitle(String url) throws IOException {
        Document document = getDocument(url);
        return document.title();
    }

    public static Elements getElements(Document document, String tag) {
        Elements elements = document.select(tag);
        return elements;
    }

    public static List<String> getUrls(Document document) {
        List<String> urls = new ArrayList<>();
        Elements elements = document.select("relative inline-flex items-center px-4 py-2 -ml-px text-sm font-medium text-nord4 leading-5 rounded-lg hover:bg-nord1 focus:z-10 focus:outline-none active:bg-nord1 transition ease-in-out duration-150");
        for (Element element : elements) {
            String href = element.attr("href");
            urls.add(href);
        }
        return urls;
    }

    public static void main(String[] args) throws IOException {
        Document document = getDocument("https://missav.live/dm34/cn/madou?page=1");
        List<String> urls = getUrls(document);
        for (String url : urls) {
            System.out.println(url);
        }
    }

}
