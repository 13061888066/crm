function formatterGrade(val){
	if(val==0){
		return "根级";
	}
	if(val==1){
		return "第一级";
	}
	if(val==2){
		return "第二级";
	}
}

function searchModules(){
	$("#dg").datagrid("load",{
		moduleName:$("#moduleName").val(),
		optValue:$("#optValue").val(),
		parentModuleName:$("#parentModuleName").val()
	});
}

$(function(){
	$("#dlg").dialog({
		onClose:function(){
			initFormData();
		}
	});
	
	$("#parentMenu").hide();

	//如果下拉款选择的不是根级，就显示上一级
	$("#grade").combobox({
		onChange:function(grade){
			if(grade==1||grade==2){
				$("#parentMenu").show();
			}
			if(grade==0){
				$("#parentMenu").hide();
			}
			loadParentModules(grade-1);
		}
	});
});

function loadParentModules(grade){
	//加载上一级模块时，先清楚下拉框再请求数据
	$("#parentId").combobox("clear");
	$("#parentId").combobox({
		url:ctx+"/module/queryModulesByGrade?grade="+grade,
		valueField:'id',
		textField:'moduleName'
	});
}

function initFormData(){
	$('#fm').form('clear');
}

function openAddModuleDialog(){
	$("#dlg").dialog("open").dialog("setTitle","添加模块");
	$("#dlg").dialog("open");

}

function openModifyModuleDialog() {
	var rows=$("#dg").datagrid("getSelections");
	if(rows.length==0){
		$.messager.alert("来自crm","请选择一条记录进行更新!");
		return ;
	}
	if(rows.length>1){
		$.messager.alert("来自crm","只能选择一条记录进行更新!");
		return ;
	}
	$('#fm').form("load",rows[0]);
	var grade = rows[0].grade;
	console.log(grade);
	if(grade!=0){
		loadParentModules(grade-1);
		$("#parentMenu").show();
		$("#parentId").combobox("setValue",rows[0].parentId);
	} else{
		$("#grade").combobox("setValue",grade);
	}
	$("#dlg").dialog("open").dialog("setTitle","修改模块");
}

function closeDialog(){
    $("#dlg").dialog("close")
}


function saveOrUpdateModule(){
	var id=$("#id").val();
	var url=ctx+"/module/insert";
	if(!isEmpty(id)){
		url=ctx+"/module/update";
	}
	$("#fm").form("submit",  {
		url:url,
		onSubmit:function () {
			return $("#fm").form("validate");
		},
		success:function (data) {
			data = JSON.parse(data);
			if(data.code==200){
				$.messager.alert("来自crm系统",data.msg,"info")
				$("#fm").form("clear");
				closeDialog();
				searchModules();
			}else{
				$.messager.alert("来自crm系统",data.msg,"info")
			}
		}
	});
}

function deleteModule(){
	var rows=$("#dg").datagrid("getSelections");
	if(rows.length==0){
		$.messager.alert("来自crm","请选中待删除记录!","info");
		return;
	}
	if(rows.length>1){
		$.messager.alert("来自crm","一次只能删除一条记录!","info");
		return;
	}
	var ids="ids=";
	for(var i=0;i<rows.length;i++){
		if(i < rows.length-1){
			ids=ids+rows[i].id+"&ids=";
		}else{
			ids=ids+rows[i].id;
		}
	}
	$.messager.confirm("来自crm","确定删除选中的"+rows.length+"条记录?",function(r){
		if(r){
			console.log(rows[0]);
			console.log(rows[0].id);
			$.ajax({
				type:"post",
				url:ctx+"/module/delete",
				data:ids,
				dataType:"json",
				success:function(data){
					$.messager.alert("来自crm",data.msg,"info");
					if(data.code==200){
						closeDialog();
						searchModules();
					}
				}
			});
		}
	});
}

