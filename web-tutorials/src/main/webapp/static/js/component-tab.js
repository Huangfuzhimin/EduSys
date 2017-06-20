function Tabs(data) {
    var self = this;

    this.container = $('<div></div>');
    this.tabs = $('<ul class="tabs"></ul>');
    this.container.append(this.tabs);

    function addTab(title) {
        var tab = $('<li class="tab col s3"><a href="#' + title + '">' + title + '</a></li>');
        self.tabs.append(tab);

        var content = $('<div id="' + title + '" class="col s12"></div>');
        self.container.append(content);
    }

    for (var i = 0; i < data.titles.length; i++) {
        addTab(data.titles[i]);
    }
}
Tabs.prototype = {
    layout: function (root) {
        root.append(this.container);
    },
    get: function (title) {
        var root = $(this.container).children("#" + title);
        return root;
    }
};
