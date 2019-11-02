function userLogin() {
    var userName = $("#userName").val();
    var userPwd = $("#userPwd").val();
    if (isEmpty(userName)) {
        alert("用户名不能为空");
        return;
    }
    if (isEmpty(userPwd)) {
        alert("密码不能为空");
        return;
    }
    $.ajax({
        url: ctx + "/user/login",
        data: {
            userName: userName,
            userPwd: userPwd,
        },
        dataType: "json",
        success: function (res) {
            console.log(res);
            if (res.code == 200) {
                console.log(res.obj.id);
                console.log(res.obj.userName);
                console.log(res.obj.trueName);
                $.cookie("id", res.obj.id);
                $.cookie("userName", res.obj.userName);
                $.cookie("trueName", res.obj.trueName);
                window.location.href = "main";
            } else {
                alert(res.msg);
            }

        },

    })
}