function CodePanel() {
    this.map = new Map();

    this.container = $('<div style="width: 100%;height: 100%"></div>');
    this.breadcrumb = $('<div style="background-color: #cccccc"></div>');
    this.container.append(this.breadcrumb);

    this.tabContainer = $('<div style="width: 100%;height: 85%"></div>');
    this.container.append(this.tabContainer);
    this.tabTitleContainer = $('<ul class="tabs"></ul>');
    this.tabContainer.append(this.tabTitleContainer);

    this.optionsContianer = $('<div class="right-align" style="background-color: #cccccc"></div>');
    this.container.append(this.optionsContianer);

    this.breadcrumb.append($('<span class="breadcrumb" style="background-color:transparent;margin-bottom: 0px;padding: 2px;">src</span>'));

    this.btnRun = $('<a class="btn-floating red" style="margin: 5px;"><i class="material-icons">send</i></a>');
    // this.btnReset = $('<a class="btn-floating red" style="margin: 5px;"><i class="material-icons">refresh</i></a>');
    this.optionsContianer.append(this.btnRun);
    // this.optionsContianer.append(this.btnReset);

    var self = this;

    //创建编辑区
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
    }

    //判断是否存在
    this.exists = function (title) {
        if (self.map.get(title)) {
            return true;
        }
        return false;
    }
    //判断是否打开
    this.isOpen = function (title) {
        var item = self.map.get(title);
        try {
            if (item.editor) {
                return true;
            }
        } catch (err) {
        }
        return false;
    }

    //选中某个tab
    this.select = function (title) {
        self.tabTitleContainer.tabs('select_tab', title);
        self.map.get(title).editor.focus();
        if (self.breadcrumb.children().size() > 1) {
            self.breadcrumb.children(':last-child').remove();
        }
        self.breadcrumb.append($('<span class="breadcrumb" style="background-color:transparent;margin-bottom: 0px;padding: 2px;">' + title + '</span>'));
    }

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
}

CodePanel.prototype = {
    openTab: function (title) {
        var self = this;
        if (!self.exists(title)) {
            return;
        }

        if (self.isOpen(title)) {
            //存在，就选中
            self.select(title);
        } else {
            //不存在，创建
            //创建tab
            var tabDiv = $('<li class="tab col"><a href="#' + title + '">' + title + '</a></li>');
            //创建tab对应的content
            var contentDiv = $('<div id="' + title + '" style="width: 100%;height: 90%;"></div>');

            //添加tab
            self.tabTitleContainer.append(tabDiv);

            //添加content
            self.tabContainer.append(contentDiv);

            //创建编辑器
            var tab = self.map.get(title);
            var editor = self.createEditor(title);
            editor.setReadOnly(tab.readOnly);
            editor.setValue(tab.content);
            //存储
            tab.editor = editor;

            //选中
            self.select(title);
        }
        return true;
    },
    runClick: function (click) {
        var self = this;
        self.btnRun.click(click);
    },
    getCodes: function () {
        var self = this;

        var keys = self.map.keys();
        var datas = [];
        for (var i = 0; i < keys.length; i++) {
            var item = self.map.get(keys[i]);
            var editor = item.editor;
            var content = editor == undefined ? item.content : editor.getValue();

            datas[i] = {
                name: item.title,
                content: content
            }
        }
        return datas;
    },
    init: function (datas) {
        var self = this;

        for (var i = 0; i < datas.length; i++) {
            var data = datas[i];
            var readOnly = data.attributes.readonly == undefined ? false : data.attributes.readonly;
            var item = {
                title: data.text,
                content: data.content,
                readOnly: readOnly
            }

            self.map.put(item.title, item);
        }
    },
    layout: function (root) {
        root.append(this.container);
    }
}
