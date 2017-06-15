function ChapterList(data) {
    var self = this;
    this.list = data.list;
    if (data.column) {
        this.column = data.column;
    } else {
        this.column = 3;
    }

    var table = $('<table></table>');
    var tbody = $('<tbody></tbody>');
    table.append(tbody);

    for (var i = 0; i < this.list.length; i += this.column) {
        var tr = $('<tr></tr>');
        tbody.append(tr);

        for (var j = 0; j < this.column; j++) {
            //获得数据
            var item = this.list[i + j];
            if (item === undefined) {
                break;
            }
            var td = $('<td></td>');

            if (item.done) {
                var a = $('<a class="waves-effect waves-light btn-flat"></a>');
                var iTag = $('<i class="material-icons left">done</i>');
                a.append(iTag);
                a.append(item.name);

                td.append(a);
            } else {
                var a = $('<a class="waves-effect waves-light btn"></a>');
                var iTag = $('<i class="material-icons left">code</i>');
                a.append(iTag);
                a.append(item.name);

                td.append(a);
            }
            tr.append(td);
        }
    }

    this.container = table;
}

ChapterList.prototype = {
    layout: function (root) {
        root.append(this.container);
    }
}