function opendia() {
    alert("去nmd");
}

function openTab(text, url, iconCls) {
    if ($("#tabs").tabs("exists", text)) {
        $("#tabs").tabs("select", text);
    } else {
        var content = "<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='" + url + "'></iframe>";
        $("#tabs").tabs("add", {
            title: text,
            iconCls: iconCls,
            closable: true,
            content: content
        });
    }
}

function openPasswordModifyDialog() {
    $("#dlg").dialog("open");
}

function closePasswordModifyDialog() {
    $("#dlg").dialog("close");
}

function logout() {
    $.messager.confirm("来自Crm", "你确定退出当前系统吗", function (res) {
        if (res) {
            setTimeout(function () {
                $.removeCookie("id");
                $.removeCookie("userName");
                $.removeCookie("trueName");
                window.location.href = "index";
            }, 500)
        }
    })
}

function modifyPassword() {
    $('#fm').form('submit', {
        url: ctx + "/user/updatePwd",
        onSubmit: function () {
            return $("#fm").form("validate");
        },
        success: function (data) {
            data = JSON.parse(data);
            if (data.code == 200) {
                $.messager.alert("来自crm", data.msg, "info");
                console.log(data);
                $.removeCookie("id");
                $.removeCookie("userName");
                $.removeCookie("trueName");
                window.location.href = "index";
            } else {
                $.messager.alert("来自Crm", data.msg, "info");
            }

        }
    });
}


