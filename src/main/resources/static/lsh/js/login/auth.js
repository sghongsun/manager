$(function () {
    $('.btn_auth').on('click', function () {
        var authFor = $(this).data('authfor');
        admin_Certification(authFor);/*com*/
    });
});

function move_Login() {
    $('#form').submit();
}