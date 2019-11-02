function searchRoles() {
    $("#dg").datagrid("load", {
        roleName: $("#roleName").val()
    });
}

function openAddRoleDialog() {
    $('#fm').form('clear');
    $("#dlg").dialog("open");
}

function closeDialog() {
    $("#dlg").dialog("close");
}

function openModifyRoleDialog() {
    //获取数据表格中选项，判定能只且只能选中一条信息
    var rows = $("#dg").datagrid("getSelections");
    if (rows.length == 0) {
        $.messager.alert("来自crm", "请选中待修改记录!", "info");
        return;
    }
    if (rows.length > 1) {
        $.messager.alert("来自crm", "不能同时修改多条记录!", "info");
        return;
    }
    $('#fm').form('load', rows[0]);
    $("#dlg").dialog("open");
}

function deleteRole() {
    //获取数据表格中选项，判定能只且只能选中一条信息
    var rows = $("#dg").datagrid("getSelections");
    if (rows.length == 0) {
        $.messager.alert("来自crm", "请选中待删除记录!", "info");
        return;
    }
    if (rows.length > 1) {
        $.messager.alert("来自crm", "不能同时删除多条记录!", "info");
        return;
    }
    $.messager.confirm("来自crm", "确定删除选中的" + rows.length + "条记录?", function (r) {
        if (r) {
            $.ajax({
                type: "post",
                url: ctx + "/role/delete",
                data: "id=" + rows[0].id,
                dataType: "json",
                success: function (data) {
                    $.messager.alert("来自crm", data.msg, "info");
                    if (data.code == 200) {
                        closeDialog();
                        searchRoles();
                    }
                }
            });
        }
    });
}


function saveOrUpdateRole() {
    var id = $("#id").val();
    var url = ctx + "/role/insert";
    if (!isEmpty(id)) {
        url = ctx + "/role/update";
    }
    $("#fm").form("submit", {
        url: url,
        onSubmit: function () {
            return $("#fm").form("validate");
        },
        success: function (data) {
            data = JSON.parse(data);
            if (data.code == 200) {
                $.messager.alert("来自crm系统", data.msg, "info")
                $("#fm").form("clear");
                closeDialog();
                searchRoles();
            } else {
                $.messager.alert("来自crm系统", data.msg, "info")
            }
        }
    });
}

var ztreeObj

function openRelatePermissionDlg() {
    //从数据表格中加载数据，（需选中一条角色记录）
    var rows = $("#dg").datagrid("getSelections");
    if (rows.length == 0) {
        $.messager.alert("来自crm", "请选择角色进行授权!", "info");
        return;
    }
    if (rows.length > 1) {
        $.messager.alert("来自crm", "只能选择一条角色进行授权!", "info");
        return;
    }
    $("#rid").val(rows[0].id),
        loadParams();
    $("#dlg02").dialog("open");
}

function zTreeOnCheck(event, treeId, treeNode) {
    //获取已经选中的数节点
    var znodes = ztreeObj.getCheckedNodes(true);
    //动态拼接moduelIds
    var moduleIds = "moduleIds=";
    for (var i = 0; i < znodes.length; i++) {
        if (i < znodes.length - 1) {
            moduleIds = moduleIds + znodes[i].id + "&moduleIds=";
        } else {
            moduleIds = moduleIds + znodes[i].id;
        }
    }
    console.log(moduleIds);
    $('#moduleIds').val(moduleIds);
};

function loadParams() {
    $.ajax({
        type: "post",
        url: ctx + "/module/queryAllsModuleDtos",
        data: "rid=" + $("#rid").val(),
        dataType: "json",
        success: function (data) {
            var setting = {
                check: {
                    enable: true
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                callback: {
                    onCheck: zTreeOnCheck
                }
            };
            var zNodes = data;
            ztreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        }
    })
}

function addPermission() {
    $.ajax({
        type: "post",
        url: ctx + "/role/addPermission",
        data: "rid=" + $("#rid").val() + "&" + $("#moduleIds").val(),
        dataType: "json",
        success: function (data) {
            console.log(data);
            if (data.code == 200) {
                $.messager.alert("来自crm", data.msg, "info");
                $("#moduleIds").val("");
                $("#rid").val("");
                closeDialog02();
            } else {
                $.messager.alert("来自crm", data.msg, "info");
            }
        }
    })

}

function closeDialog02() {
    $("#dlg02").dialog("close");
}