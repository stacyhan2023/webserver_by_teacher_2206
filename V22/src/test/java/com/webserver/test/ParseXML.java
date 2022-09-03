package com.webserver.test;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 当前类用于保存所有与HTTP协议相关的规定内容以便重用
 */
public class ParseXML {
    /**
     * 资源后缀名与响应头Content-Type值的对应关系
     * key:资源后缀名
     * value:Content-Type对应的值
     */
    private static Map<String,String> mimeMapping = new HashMap<>();

    static{
        initMimeMapping();
    }

    private static void initMimeMapping(){
        try{
            PrintWriter pw = new PrintWriter(new File(ParseXML.class.getClassLoader().getResource("META-INF/mime.types").toURI()));
            SAXReader reader = new SAXReader();
            Document doc = reader.read("./web.xml");
            Element root = doc.getRootElement();
            List<Element> list = root.elements("mime-mapping");
            for(Element element : list){
                String key = element.elementText("extension");
                String value = element.elementText("mime-type");
                System.out.println(key+":"+value);
                mimeMapping.put(key,value);
                pw.println(value+" "+key+" "+key.toUpperCase(Locale.ROOT));
            }
            pw.flush();
            pw.close();
//            System.out.println(mimeMapping.size());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据给定的资源后缀名获取到对应的Content-Type的值
     * @param ext
     * @return
     */
    public static String getMimeType(String ext){
        return mimeMapping.get(ext);
    }

    public static void main(String[] args) {
        String type = getMimeType("png");
        System.out.println(type);
    }
}
