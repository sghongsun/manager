function chk_FieldP() {
    var str = alltrim(document.menu1form.MenuName.value);
    if (str.length == 0) {
        alert("메뉴명을 입력해 주십시오.");
        document.menu1form.MenuName.focus();
        return false;
    }
}
function chk_FieldC() {
    var str = alltrim(document.menu2form.MenuName.value);
    if (str.length === 0) {
        alert("메뉴명을 입력해 주십시오.");
        document.menu2form.MenuName.focus();
        return false;
    }
    str = alltrim(document.menu2form.MenuURL.value);
    if (str.length === 0) {
        alert("메뉴URL을 입력해 주십시오.");
        document.menu2form.MenuURL.focus();
        return false;
    }
}
//(현재 선택된 부모메뉴코드, 삭제대상 메뉴부모코드, 삭제대상 메뉴코드)
function del_Menu(MenuPCode, MenuCode) {
    var conf = confirm("삭제 하시겠습니까?");
    if (conf === true) {
        document.modifyform.MenuPCode.value = MenuPCode;
        document.modifyform.MenuCode.value = MenuCode;
        document.modifyform.action="/manage/menu/delok";
        document.modifyform.submit();
    }
}

/* 메뉴 게시 순서 수정 */
function mod_ManageMenuDisNum(udType, menuPCode, menuCode) {
    document.modifyform.UDType.value = udType;
    document.modifyform.MenuPCode.value = menuPCode;
    document.modifyform.MenuCode.value = menuCode;
    document.modifyform.action="/manage/menu/dispnummodifyok";
    document.modifyform.submit();
}

function chk_Field() {
    var str = alltrim(document.form.MenuName.value);
    if (str.length === 0) {
        alert("메뉴명을 입력해 주십시오.");
        document.form.MenuName.focus();
        return false;
    }
    str = alltrim(document.form.MenuURL.value);
    if (str.length === 0) {
        alert("URL을 입력해 주십시오.");
        document.form.MenuURL.focus();
        return false;
    }
    document.form.submit();
}
function delete_Menu() {
    var conf = confirm("삭제 하시겠습니까?");
    if (conf === true) {
        document.fform.action="/manage/menu/delok";
        document.fform.submit();
    }
}
