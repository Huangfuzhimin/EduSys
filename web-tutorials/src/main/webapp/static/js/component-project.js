function ProjectStructure(id, data, tabsId,language) {
    // ########### 属性区 ##################
    this.id = id;//层级树容器id
    this.data = data;
    this.tabsId = tabsId;//代码区容器id
    this.language = language;//编程语言
    this.map = new Map();
    var self = this;

    // ############ 初始化逻辑 #############
    // 将原始数据保存
    saveSource();

    //加载初始化
    load();

    // ######### 私有方法区 ###############
    //map集合
    function Map() {
        this.elements = new Array();
        //获取MAP元素个数
        this.size = function () {
            return this.elements.length;
        };
        //判断MAP是否为空
        this.isEmpty = function () {
            return (this.elements.length < 1);
        };
        //删除MAP所有元素
        this.clear = function () {
            this.elements = new Array();
        };
        //向MAP中增加元素（key, value)
        this.put = function (_key, _value) {
            this.elements.push({
                key: _key,
                value: _value
            });
        };
        //删除指定KEY的元素，成功返回True，失败返回False
        this.remove = function (_key) {
            var bln = false;
            try {
                for (i = 0; i < this.elements.length; i++) {
                    if (this.elements[i].key == _key) {
                        this.elements.splice(i, 1);
                        return true;
                    }
                }
            } catch (e) {
                bln = false;
            }
            return bln;
        };
        //获取指定KEY的元素值VALUE，失败返回NULL
        this.get = function (_key) {
            try {
                for (i = 0; i < this.elements.length; i++) {
                    if (this.elements[i].key == _key) {
                        return this.elements[i].value;
                    }
                }
            } catch (e) {
                return null;
            }
        };
        //获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
        this.element = function (_index) {
            if (_index < 0 || _index >= this.elements.length) {
                return null;
            }
            return this.elements[_index];
        };
        //判断MAP中是否含有指定KEY的元素
        this.containsKey = function (_key) {
            var bln = false;
            try {
                for (i = 0; i < this.elements.length; i++) {
                    if (this.elements[i].key == _key) {
                        bln = true;
                    }
                }
            } catch (e) {
                bln = false;
            }
            return bln;
        };
        //判断MAP中是否含有指定VALUE的元素
        this.containsValue = function (_value) {
            var bln = false;
            try {
                for (i = 0; i < this.elements.length; i++) {
                    if (this.elements[i].value == _value) {
                        bln = true;
                    }
                }
            } catch (e) {
                bln = false;
            }
            return bln;
        };
        //获取MAP中所有VALUE的数组（ARRAY）
        this.values = function () {
            var arr = new Array();
            for (i = 0; i < this.elements.length; i++) {
                arr.push(this.elements[i].value);
            }
            return arr;
        };
        //获取MAP中所有KEY的数组（ARRAY）
        this.keys = function () {
            var arr = new Array();
            for (i = 0; i < this.elements.length; i++) {
                arr.push(this.elements[i].key);
            }
            return arr;
        };
    }

    //创建编辑区
    function createEditor(editorId) {
        var editor = ace.edit(editorId);
        editor.setTheme("ace/theme/chrome");
        editor.getSession().setMode("ace/mode/" + self.language);
        editor.setFontSize(18);

        //去掉保存快捷键
        editor.commands.addCommand({
            name: "save",
            bindKey: {win: "Ctrl-S", mac: "Command-S"},
            exec: function (args) {
                console.log("save");
            }
        });
        //设置编辑器的配置
        editor.setOptions({
            enableBasicAutocompletion: true,
            enableSnippets: true,
            enableLiveAutocompletion: true
        });
        return editor;
    };

    // 保存原始数据到map中
    function saveSource() {
        var data = self.data;
        var src = data.src;

        for (var i = 0; i < src.length; i++) {
            var item = src[i];
            var name = item.name;
            self.map.put(name, {
                source: item
            });
        }
    }

    //通过数据获得项目树
    function generateTree() {
        var data = self.data;
        var title = data.title;
        var src = data.src;

        var tree = [{
            text: title,
            iconCls: 'icon-j-folder',
            state: 'open',
            children: [{
                text: 'src',
                iconCls: 'icon-j-src',
            }, {
                text: 'jre1.8环境',
                iconCls: 'icon-j-lib'
            }, {
                text: 'libs依赖',
                iconCls: 'icon-j-lib'
            }]
        }];

        tree[0].children[0].children = [];
        for (var i = 0; i < src.length; i++) {
            var item = src[i];
            tree[0].children[0].children[i] = {
                text: item.name,
                iconCls: 'icon-j-file',
                attributes: {
                    type: 1,
                    readOnly: item.readonly,
                    content: item.content
                }
            };
        }
        return tree;
    }

    //初始化加载的方法
    function load() {
        var id = self.id;
        var tree = generateTree();

        $('#' + id).tree({
            "data": tree,
            "onClick": function (node) {
                addTab(node);
            }
        });

        $('#' + self.tabsId).tabs({
            onAdd: function (title, index) {
                onTabAdd(title, index);
            },
            onClose: function (title, index) {
                onTabClose(title, index);
            }
        });
    }

    // 添加tab的方法
    function addTab(node) {
        var attrs = node.attributes;
        if (attrs == null || attrs.type != 1) {
            return;
        }
        var id = '#' + self.tabsId;
        var title = node.text;
        var exists = $(id).tabs('exists', title);
        if (exists) {
            //存在
            $(id).tabs('select', title);
        } else {
            //不存在
            $(id).tabs('add', {
                title: title,
                content: '<div id="' + title + '" style="width: 100%;height: 90%;"></div>',
                closable: true
            });
        }
    }

    function onTabAdd(title, index) {
        var data = self.map.get(title);
        var readOnly = data.source.readonly == undefined ? false : data.source.readonly;
        var content = "";
        if (data.write == undefined) {
            content = data.source.content == undefined ? "" : data.source.content;
        } else {
            content = data.write;
        }
        //创建编辑区
        var editor = createEditor(title);
        editor.setReadOnly(readOnly);
        editor.setValue(content);
        editor.focus();
        //保存到map中
        data.editor = editor;
    }

    function onTabClose(title, index) {
        var data = self.map.get(title);
        var editor = data.editor;
        //保存用户编写的代码
        data.write = editor.getValue();
        data.editor = null;
    }
}

ProjectStructure.prototype = {
    resize: function () {
        for (var i = 0; i < this.map.size(); i++) {
            var e = this.map.element(i);
            e.value.editor.resize(false);
        }
    },
    getCodes: function () {
        var self = this;

        var keys = self.map.keys();
        var datas = [];
        for (var i = 0; i < keys.length; i++) {
            var item = self.map.get(keys[i]);
            var editor = item.editor;
            var content = null;
            if (editor == undefined) {
                var write = item.write;
                if (write == undefined) {
                    content = item.source.content;
                } else {
                    content = write;
                }
            } else {
                content = editor.getValue();
            }

            datas[i] = {
                name: item.source.name,
                content: content
            }
        }
        return datas;
    }
}