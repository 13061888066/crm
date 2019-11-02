function searchUsers() {
    $('#dg').datagrid("load",{
        userName:$('#userName').val(),
        trueName:$('#trueName').val(),
        phone:$('#phone').val(),
        email:$('#email').val()
    })
}
//打开添加对话框
function openAddUserDialog() {
    $('#fm').form('clear');
    $('#dlg').dialog('open').dialog('setTitle',"添加用户记录");
}
//打开修改对话框
function openModifyUserDialog() {
    var rows= $("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选中待更新记录!","info");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","只能选择一条记录执行更新!","info");
        return;
    }
    console.log(rows[0]);
    $('#fm').form('load',rows[0]);
    $('#dlg').dialog('open').dialog('setTitle',"修改用户记录");
}
//删除
function deleteUser() {
    var rows= $("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选中一条要删除的记录!","info");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","只能选择删除一条记录!","info");
        return;
    }
    $.messager.confirm("来自crm","确定删除选中的"+rows.length+"条记录?",function(r){
        if(r){
            $.ajax({
                type:"post",
                url:ctx+"/user/delete",
                data:"id=" + rows[0].id,
                dataType:"json",
                success:function(data){
                    $.messager.alert("来自crm",data.msg,"info");
                    if(data.code==200){
                        closeDialog();
                        searchUsers();
                    }
                }
            });
        }
    });

}

//弹框中的保存
function saveOrUpdateUser() {
    var id = $('#id').val();
    var url = ctx+"/user/update";
    if(isEmpty(id)){
        url = ctx+"/user/insert";
    }
    $("#fm").form("submit",{
        url:url,
        onSubmit:function () {
            return $("#fm").form("validate");
        },
        success: function (data) {
            data = JSON.parse(data);
            if(data.code==200){
                $.messager.alert("来自crm",data.msg,"info");
                searchUsers();
                closeDialog();
            }else{
                $.messager.alert("来自crm",data.msg,"info");
            }
        }
    });
}
function closeDialog() {
    $("#dlg").dialog("close");
}