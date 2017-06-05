package org.itheima.edu.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.itheima.edu.bean.ExamConfigBean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Poplar on 2016/12/12.
 */
public class XmlUtils {

    public static XStream newInstance(){
        XStream xStream = new XStream(new DomDriver());

        // 设置Bean对象的标签别名
        xStream.alias("edu", ExamConfigBean.class);
        xStream.alias("dir", ExamConfigBean.Dir.class);
        xStream.alias("item", ExamConfigBean.Dir.Item.class);

        // 配置隐式集合, 将dirs集合展开在edu子节点, 成为多个dir
        xStream.addImplicitCollection(ExamConfigBean.class, "dirs");
        // 配置隐式集合, 将items集合展开在dir子节点, 成为多个item
        xStream.addImplicitCollection(ExamConfigBean.Dir.class, "items");

        // 将name,type配置为Dir的属性
        xStream.useAttributeFor(ExamConfigBean.Dir.class, "name");
        xStream.useAttributeFor(ExamConfigBean.Dir.class, "type");
        // 将alias配置为Item的属性
        xStream.useAttributeFor(ExamConfigBean.Dir.Item.class, "alias");

        // 注册别名转换器
        xStream.registerConverter(new ItemAliasConverter());

        // 忽略未知的元素
        xStream.ignoreUnknownElements();
        return xStream;
    }


    public static class ItemAliasConverter implements Converter {

        public boolean canConvert(Class clazz) {
            return clazz.equals(ExamConfigBean.Dir.Item.class);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer,
                            MarshallingContext context) {
            ExamConfigBean.Dir.Item item = (ExamConfigBean.Dir.Item) value;
            // 将bean写成xml
            writer.addAttribute("alias", item.alias);
            writer.setValue(item.name);
        }

        public Object unmarshal(HierarchicalStreamReader reader,
                                UnmarshallingContext context) {
            // 从xml解析成bean
            ExamConfigBean.Dir.Item item = new ExamConfigBean.Dir.Item();

            item.alias = reader.getAttribute("alias");
            item.name = reader.getValue();

            // 如果需要向子节点遍历, 则执行此while
//            while(reader.hasMoreChildren()){
//                reader.moveDown();
//                //...
//                reader.moveUp();
//            }

            return item;
        }

    }

    public static String toXml(Object obj){
        return newInstance().toXML(obj);
    }

    public static <T> T parseData(String xml, Class<T> clazz){
        try {
            XStream xStream = newInstance();
            xStream.processAnnotations(clazz);
            T p = (T) xStream.fromXML(xml);
            System.out.println(p);
            return p;
        } catch (Exception e) {
//            e.printStackTrace();
            System.err.println("解析异常");
        }
        return null;
    }
    public static <T> T parseData(File file, Class<T> clazz){
        try {
            XStream xStream = newInstance();
            xStream.processAnnotations(clazz);
            T p = (T) xStream.fromXML(file);
            System.out.println(p);
            return p;
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("解析异常");
        }
        return null;
    }
    public static <T> T parseData(InputStream is, Class<T> clazz){
        XStream xStream = newInstance();
        xStream.processAnnotations(clazz);
        T p = (T) xStream.fromXML(is);
        try {
            if(is != null){
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(p);
        return p;
    }
}
