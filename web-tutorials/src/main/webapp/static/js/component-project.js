function ProjectStructure(id, data, tabsId) {
    this.id = id;
    this.data = data;
    this.tabsId = tabsId;
    this.map = new Map();

    this.createEditor = function (editorId) {
        var editor = ace.edit(editorId);
        editor.setTheme("ace/theme/chrome");
        editor.getSession().setMode("ace/mode/java");
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

    //加载初始化
    this.load();
}

ProjectStructure.prototype = {
    load: function () {
        var id = this.id;
        var data = this.data;
        var self = this;
        $('#' + id).tree({
            "data": data,
            "onClick": function (node) {
                self.addTab(node);
            }
        });
    },
    addTab: function (node) {
        var attrs = node.attributes;
        if (attrs == null || attrs.type != 1) {
            return;
        }
        var id = '#' + this.tabsId;
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

            //创建编辑区
            var editor = this.createEditor(title);
            editor.setReadOnly(attrs.readOnly == undefined ? true : attrs.readOnly);

            this.map.put(title, {editor: editor, source: node});
        }
    },
    resize: function () {
        for (var i = 0; i < this.map.size();i++) {
            var e = this.map.element(i);
            e.value.editor.resize(false);
        }
    }
}