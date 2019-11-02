function formatterState(val) {
    if (val == 0) {
        return "未分配";
    } else if (val == 1) {
        return "已分配";
    } else {
        return "未定义";
    }
}

function searchSaleChances() {
    $("#dg").datagrid("load", {
        createMan: $("#createMan").val(),
        customerName: $("#customerName").val(),
        createDate: $("#createDate").datebox("getValue"),
        state: $("#state").combobox("getValue")
    })
}

function openAddAccountDialog() {
    $("#fm").form("clear");
    $("#dlg").dialog("open");
}

function saveAccount() {
    //根据id决定url
    var id = $("#id").val();
    var url;
    if (isEmpty(id)) {//添加
        url = ctx + "/sale_chance/insert";
    } else {
        url = ctx + "/sale_chance/update";
    }
    var v = $("#cgjl").val();
    if (!/^[0-9]+$/.test(v)) {
        $.messager.alert("来自Crm", "成功几率只能包含数字", "error");
        return;
    }
    $("#fm").form("submit", {
        url: url,
        onSubmit: function (params) {
            params.createMan = $.cookie("trueName");
            return $("#fm").form("validate")
        },
        success: function (data) {
            data = JSON.parse(data);
            if (data.code == 200) {
                $.messager.alert("来自Crm", data.msg, "info");
                closeDialog();
                searchSaleChances();
            } else {
                $.messager.alert("来自Crm", data.msg, "error");
            }
        }
    })
}

function openModifyAccountDialog() {
    $("#fm").form("clear");
    var rows = $("#dg").datagrid("getSelections");
    if (rows.length != 1) {
        $.messager.alert("来自Crm", "请选择一条记录", "info");
        return;
    }
    console.log(rows);
    $("#fm").form("load", rows[0]);
    $("#dlg").dialog("open").dialog("setTitle", "修改营销机会记录");
}

function deleteAccount() {
    var rows = $("#dg").datagrid("getSelections");
    if (rows.length < 1) {
        $.messager.alert("来自Crm", "请至少选择一条记录", "info");
        return;
    }
    var params = "id=";
    for (var i = 0; i < rows.length; i++) {
        if (i < rows.length - 1) {
            params = params + rows[i].id + "&id=";
        } else {
            params = params + rows[i].id;
        }
    }
    $.ajax({
        url: ctx + "/sale_chance/delete",
        data: params,
        dataType: 'json',
        success: function (data) {
            if (data.code == 200) {
                $.messager.alert("来自Crm", data.msg, "info");
                searchSaleChances();
            } else {
                $.messager.alert("来自Crm", data.msg, "info");
            }
        }
    })
}

function closeDialog() {
    $("#dlg").dialog("close");
}