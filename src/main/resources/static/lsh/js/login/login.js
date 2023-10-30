$(function () {
    if (self.opener != null) {
        self.close();
        opener.PageReload();
    }

    if ($("input:checkbox[name='saveId']").is(':checked')) {
        $("input[name='loginPw']").focus();
    } else {
        $("input[name='loginId']").focus();
    }

    $('.login_enter').on('keypress', function (event) {
        if (event.keyCode === 13) {
            chk_Login();
        }
    });
    $('.btn_login').on('click', function () {
        chk_Login();
    });
});

function chk_Login() {
    var loginId = alltrim($('#loginId').val());
    if (loginId.length === 0) {
        alert("아이디를 입력해 주십시오.");
        $('#loginId').focus();
        return;
    }
    var loginPw = alltrim($('#loginPw').val());
    if (loginPw.length === 0) {
        alert("비밀번호를 입력해 주십시오.");
        $('#loginPw').focus();
        return;
    }
    $('#form').submit();
}