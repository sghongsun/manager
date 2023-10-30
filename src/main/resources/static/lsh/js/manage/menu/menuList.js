window.onpageshow = function (event) {
	check_MenuError();
}

$(function () {
	$(".enter_menu1").on("keypress", function (event) {
		if (event.keyCode == 13) {
			add_Menu1();
		}
	});
	$(".ins_menu1").on("click", function () {
		add_Menu1();
	});

	$(".enter_menu2").on("keypress", function (event) {
		if (event.keyCode == 13) {
			add_Menu2();
		}
	});
	$(".ins_menu2").on("click", function () {
		add_Menu2();
	});

	$("body").on("click", ".menu_dispnum", function () {
		var displyType = $(this).data("disptype");
		var menuPCode = $(this).data("menupcode");
		var menuCode = $(this).data("menucode");

		mod_ManageMenuDisNum(displyType, menuPCode, menuCode);
	});

	$("body").on("click", ".del_menu", function () {
		var sMenuPCode = $(this).data("smenupcode");
		var menuPCode = $(this).data("menupcode");
		var menuCode = $(this).data("menucode");
		del_Menu(sMenuPCode, menuPCode, menuCode);
	});


});


/* 관라자 대메뉴 입력 폼 체크 */
function add_Menu1() {
	// var menuName1 = alltrim($("input[name='menuName1']", "form[name='form1']").val());
	// if (menuName1.length === 0) {
	// 	alert("메뉴명을 입력해 주십시오.");
	// 	$("input[name='menuName1']", "form[name='form1']").focus();
	// 	return;
	// }

	formSubmit();
	$("#form1").submit();
}

/* 관라자 소메뉴 입력 폼 체크 */
function add_Menu2() {
	// var menuName2 = alltrim($("input[name='menuName2']", "form[name='form2']").val());
	// if (menuName2.length == 0) {
	// 	alert("메뉴명을 입력해 주십시오.");
	// 	$("input[name='menuName2']", "form[name='form2']").focus();
	// 	return;
	// }
	//
	// var menuURL2 = alltrim($("input[name='menuURL2']", "form[name='form2']").val());
	// if (menuURL2.length == 0) {
	// 	alert("메뉴URL을 입력해 주십시오.");
	// 	$("input[name='menuURL2']", "form[name='form2']").focus();
	// 	return;
	// }
	formSubmit();
	$("#form2").submit();
}


/*(현재 선택된 부모메뉴코드, 삭제대상 메뉴부모코드, 삭제대상 메뉴코드) */
function del_Menu(sMenuPCode, menuPCode, menuCode) {
	var conf = confirm("삭제 하시겠습니까?");
	if (conf == true) {
		$("input[name='SMenuPCode']",	 "form[name='frmDelMenu']").val(sMenuPCode);
		$("input[name='MenuPCode']",	 "form[name='frmDelMenu']").val(menuPCode);
		$("input[name='MenuCode']",		 "form[name='frmDelMenu']").val(menuCode);

		formSubmit();
		$("#frmDelMenu").submit();
	}
}


/* 메뉴 게시 순서 수정 */
function mod_ManageMenuDisNum(udType, menuPCode, menuCode) {
	var curMenuCode1 = $("#T_MenuCode1").val();

	$.ajax({
		type		 : "post",
		url			 : "MenuDispNumModifyOk",
		async		 : false,
		data		 : "UDType=" + udType + "&MenuPCode=" + menuPCode + "&MenuCode=" + menuCode,
		dataType	 : "text",
		headers		 : { '__RequestVerificationToken' : getCsrfToken() },
		success		 : function (data) {
						var splitData	 = data.split("|||||");
						var result;
						var cont;

						result			 = splitData[0];
						cont			 = splitData[1];

						if (result == "OK") {
							if (menuPCode == "0000") {
								$("#ListMenu1").html(cont);
								get_GNB_Menu();
							}
							else {
								$("#ListMenu2").html(cont);
								get_GNB_Menu();
								if (menuPCode == curMenuCode1) {
									get_Left_Menu(menuPCode);
								}
							}

							return;
						}
						else if (result == "LOGIN") {
							PageReload();
						}
						else {
							alert(cont);
							return;
						}
		},
		error		 : function (data) {
						alert("처리중 오류가 발생하였습니다.");
		}
	});
}

/* GNB 메뉴 */
function get_GNB_Menu() {
	var curMenuCode1 = $("#T_MenuCode1").val();

	$.ajax({
		type		 : "post",
		url			 : "/Menu/GNBMenu",
		async		 : false,
		data		 : "CurMenuCode1=" + curMenuCode1,
		dataType	 : "text",
		headers		 : { '__RequestVerificationToken' : getCsrfToken() },
		success		 : function (data) {
						var splitData	 = data.split("|||||");
						var result;
						var cont;

						result			 = splitData[0];
						cont			 = splitData[1];

						if (result == "OK") {
							$(".header .gnb").html(cont);
							return;
						}
						else if (result == "LOGIN") {
							PageReload();
						}
						else {
							alert(cont);
							return;
						}
		},
		error		 : function (data) {
						alert("처리중 오류가 발생하였습니다.");
		}
	});
}

/* Left 메뉴 */
function get_Left_Menu(menuPCode) {
	var curMenuCode2 = $("#T_MenuCode2").val();

	$.ajax({
		type		 : "post",
		url			 : "/Menu/LeftMenu",
		async		 : false,
		data		 : "MenuPCode=" + menuPCode + "&CurMenuCode2=" + curMenuCode2,
		dataType	 : "text",
		headers		 : { '__RequestVerificationToken' : getCsrfToken() },
		success		 : function (data) {
						var splitData	 = data.split("|||||");
						var result;
						var cont;

						result			 = splitData[0];
						cont			 = splitData[1];

						if (result == "OK") {
							$(".lnbW .lnb").html(cont);
							return;
						}
						else if (result == "LOGIN") {
							PageReload();
						}
						else {
							alert(cont);
							return;
						}
		},
		error		 : function (data) {
						alert("처리중 오류가 발생하였습니다.");
		}
	});
}