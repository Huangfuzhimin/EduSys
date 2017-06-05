package org.itheima.edu.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Poplar on 2016/12/12.
 */
public class ExamConfigBean {

    public List<Dir> dirs = new ArrayList();

    public void add(Dir dir){
        dirs.add(dir);
    }

    public static class Dir {
        public String name;
        public String type;
        public List<Item> items = new ArrayList();

        public Dir(String name, String type) {
            this.name = name;
            this.type = type;
        }


        public void add(Item item){
            items.add(item);
        }

        @Override
        public String toString() {
            return "Dir{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", items=" + items +
                    '}';
        }

        public static class Item {
            public String name;
            public String alias;

            public Item() {
            }

            public Item(String name, String alias) {
                this.name = name;
                this.alias = alias;
            }

            @Override
            public String toString() {
                return "Item{" +
                        "name='" + name + '\'' +
                        ", alias='" + alias + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "ExamConfigBean{" +
                "dirs=" + dirs +
                '}';
    }
}
