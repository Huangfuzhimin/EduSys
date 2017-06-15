function CardList(options) {
    if (options.column) {
        this.column = options.column;
    } else {
        this.column = 3;
    }
    // 数量
    this.size = 0;
    var self = this;

    function newCard(options) {
        var columnCount = 12 / self.column;

        // 布局操作
        var root = $('<div class="col s12 m' + columnCount + '"></div>');
        var card = $('<div class="card blue-grey darken-2"></div>');
        root.append(card);

        //设置card悬浮
        $(card).hover(function () {
            $(this).removeClass("z-depth-2");
            $(this).addClass("z-depth-5");
        }, function () {
            $(this).removeClass("z-depth-5");
            $(this).addClass("z-depth-2");
        });

        var cardContent = $('<div class="card-content white-text"></div>');
        var title = $('<span class="card-title">' + options.title + '</span>');
        var content = $('<p>' + options.content + '</p>');
        cardContent.append(title);
        cardContent.append(content);
        card.append(cardContent);

        var cardAction = $('<div class="card-action"></div>');
        var progressDesc = $('<div class="right" style="color: white">' + options.progressDesc + '</div>');
        var progress = $('<div class="progress"><div class="determinate" style="width: ' + options.progress + '%"></div></div>');
        cardAction.append(progressDesc);
        cardAction.append(progress);

        var rating = (options.rating > 10 || options.rating < 0) ? 0 : options.rating;
        var ratingDiv = $('<div class="center"></div>');
        var ratingLabel = $('<span style="color: white;">评分：</span>');
        ratingDiv.append(ratingLabel);

        var count = 0;
        var len = Math.floor(rating / 2);
        for (var i = 0; i < len; i++) {
            var item = $('<i class="mdi mdi-24px mdi-light mdi-star mdi-spin"></i>');
            ratingDiv.append(item);
            count++;
        }
        if (count < 5) {
            if (rating % 2 != 0) {
                var item = $('<i class="mdi mdi-24px mdi-light mdi-star-half"></i>');
                ratingDiv.append(item);
                count++;
            }
            if (count < 5) {
                for (var i = 0; i < 5 - count; i++) {
                    var item = $('<i class="mdi mdi-24px mdi-dark mdi-star"></i>');
                    ratingDiv.append(item);
                    count++;
                }
            }
        }

        cardAction.append(ratingDiv);
        card.append(cardAction);
        return root;
    };
    function addCard(card) {
        if (self.size % self.column == 0) {
            self.row = $('<div class="row"></div>');
            self.container.append(self.row);
        }
        self.row.append(card);
        self.size++;
    };

    this.container = $('<div class="container"></div>');
    this.data = options.data;

    for (var i = 0; i < this.data.length; i++) {
        addCard(newCard(this.data[i]));
    }
}

CardList.prototype = {
    layout: function (root) {
        root.append(this.container);
    }
}
