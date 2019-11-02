function searchCustomer(){
    $("#dg").datagrid("load", {
        khno: $("#s_khno").val(),
        name: $("#s_name").val(),
    })
}

function openCustomerAddDialog() {
    $("#fm").form("clear");
    $("#dlg").dialog("open").dialog("setTitle","添加客户信息");
}

function saveOrUpdateCustomer() {

    var id = $('#id').val();
    var url = ctx+'/customer/update';
    if(isEmpty(id)){
        url = ctx+'/customer/insert';
    }
    $("#fm").form("submit",{
        url: url,
        onSubmit: function (params) {
            return $("#fm").form("validate");
        },
        success: function (data) {
            data = JSON.parse(data);
            if (data.code == 200) {
                $.messager.alert("来自Crm", data.msg, "info");
                closeCustomerDialog();
                searchCustomer();
            } else {
                $.messager.alert("来自Crm", data.msg, "error");
            }
        }
    })


}


function openCustomerModifyDialog() {
    var rows=$("#dg").datagrid("getSelections");
    if (rows.length==0){
        $.messager.alert("来自Crm","请选中一条记录","warning");
        return;
    }
    if (rows.length>1){
        $.messager.alert("来自Crm","只能选中一条记录","warning");
        return;
    }
    $("#fm").form("load",rows[0]);
    $("#dlg").dialog("open").dialog("setTitle","修改客户信息");
}

function deleteCustomer(){
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
        url: ctx + "/customer/delete",
        data: params,
        dataType: 'json',
        success: function (data) {
            if (data.code == 200) {
                $.messager.alert("来自Crm", data.msg, "info");
                searchCustomer();
            } else {
                $.messager.alert("来自Crm", data.msg, "info");
            }
        }
    })
}



function closeCustomerDialog() {
    $("#dlg").dialog("close");
}

function openCustomerOtherInfo(title,type) {
    var rows = $("#dg").datagrid("getSelections");
    if (rows.length < 1) {
        $.messager.alert("来自Crm", "请至少选择一条记录", "info");
        return;
    }
    if (rows.length > 1) {
        $.messager.alert("来自Crm", "只能选择一条记录", "info");
        return;
    }
    window.parent.openTab(title,ctx+"/customer/openCustomerOtherInfo/"+type+"/"+rows[0].id)
}
