window.onpageshow = function (event) {
    formInit();//closePop('loading');
}

$(function () {
	var tMenuCode1 = $("#T_MenuCode1").val();
	var tMenuCode2 = $("#T_MenuCode2").val();

	getGnb(tMenuCode1, tMenuCode2);

    $(".common_enter").on("keypress", function (event) {
        if (event.keyCode == 13) {
            var formId = $(this).data("formid");
            var action = $(this).data("action");
            $("#" + formId).attr("action", action).attr("target", "_self").submit();
        }
    });
    $(".common_submit").on("click", function () {
        var formId = $(this).data("formid");
        var action = $(this).data("action");
        $("#" + formId).attr("action", action).attr("target", "_self").submit();
    });
    $(".common_change").on("change", function () {
        var formId = $(this).data("formid");
        $("#" + formId).submit();
    });
    $(".common_exceldown").on("click", function (event) {
        var formId = $(this).data("formid");
        var action = $(this).data("action");
        $("#" + formId).attr("action", action).attr("target", "_self").submit();
    });
    $(".common_htmldown").on("click", function (event) {
        var formId = $(this).data("formid");
        var action = $(this).data("action");
        $("#" + formId).attr("action", action).attr("target", "act_frame1").submit();
    });
    $(".only_num").on("input", function () {
        num_Only($(this), $(this).val());
    });
    $(".pickdate").on("click", function () {
        var dateId = $(this).data("dateid");
        dateSelect(dateId);
    });
    $(".setdate").on("click", function () {
        var period  = $(this).data("period");
        var sDate   = $(this).data("sdate");
        var eDate   = $(this).data("edate");
        var useDate = $(this).data("usedate");
        var formId  = $(this).data("formid");
        var action  = window.location.pathname;

        common_setDate(period, sDate, eDate);
        if (useDate != "" && useDate != undefined) {
            $("#" + useDate).attr("checked", false);
        }
        $("#" + formId).attr("action", action).submit();
    });
    $("#popup").on("click", ".close_layer", function () {
        var layerId = $(this).data("layerid");
        closePop(layerId);
    });
    $(".history_popup").on("click", function () {
        var url = $(this).data("url");
        hPopup(url);
    });
    $(".close_popup").on("click", function () {
        self.close();
    });
    $(".search_post").on("click", function () {
        var zipCode = $(this).data("zipcode");
        var addr    = $(this).data("addr");
        execDaumPostcode(zipCode, addr)
    });
});


function PageReload() {
    location.href = document.URL;
}

/* GNB */
function getGnb(menuCode1, menuCode2) {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");

	$.ajax({
		url		: "/gnbMenu",
		type	: "POST",
		dataType	: "text",
		cache   : false,
		beforeSend : function(xhr){
			/* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
			xhr.setRequestHeader(header, token);
		},
		success  : function(result, status) {
			console.log("result=" + result + " / status=" + status);
			var obj = JSON.parse(result);
		},
		error : function(jqXHR, status, error) {
			alert("jqXHR=" + jqXHR + " / status=" + status + " / error=" + error);
		}
	});
}

/* 대분류 변경 */
function chg_SCode1(pageCode, id) {
    var categoryCode1 = $("#" + id + "1").val();
    $("#" + id + "2 option:not(:first)").remove();
    $("#" + id + "3 option:not(:first)").remove();

    if (categoryCode1 != "") {
        $.ajax({
            type		 : "post",
            url			 : "/Common/Category2List",
            async		 : false,
            data		 : "PageCode=" + pageCode + "&CategoryCode1=" + categoryCode1,
            dataType	 : "html",
            headers		 : { '__RequestVerificationToken' : getCsrfToken() },
            success		 : function (data) {
                var splitData	 = data.split("|||||");
                var result		 = splitData[0];
                var cont		 = splitData[1];

                if (result == "OK") {
                    $("#" + id + "2").append(cont);
                    return;
                }
                else if (result == "LOGIN") {
                    if (cont != "") { alert(cont); }
                    PageReload();
                    return;
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
}

/* 중분류 변경 */
function chg_SCode2(pageCode, id) {
    var categoryCode1 = $("#" + id + "1").val();
    var categoryCode2 = $("#" + id + "2").val();
    $("#" + id + "3 option:not(:first)").remove();

    if (categoryCode1 != "" && categoryCode2 != "") {
        $.ajax({
            type		 : "post",
            url			 : "/Common/Category3List",
            async		 : false,
            data		 : "PageCode=" + pageCode + "&CategoryCode1=" + categoryCode1 + "&CategoryCode2=" + categoryCode2,
            dataType	 : "html",
            headers		 : { '__RequestVerificationToken' : getCsrfToken() },
            success		 : function (data) {
                var splitData	 = data.split("|||||");
                var result		 = splitData[0];
                var cont		 = splitData[1];

                if (result == "OK") {
                    $("#" + id + "3").append(cont);
                    return;
                }
                else if (result == "LOGIN") {
                    if (cont != "") { alert(cont); }
                    PageReload();
                    return;
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
}

var wrapper_top;
function openPop(id) {
    $('#dimmed').show();

    var pop = $("#" + id);
    var pop_height = pop.height();


    var popMargLeft = ($("#" + id).width() + 2) / 2;
    $("#" + id).css({ 'margin-left': -popMargLeft });

    var top = ($('#dimmed').height() - $("#" + id).height()) / 2;
    $("#" + id).fadeIn().css({ 'top': top });

    $("#" + id + " .popHeader").css("cursor", "move");
    $("#" + id).draggable({ 'cancel': '.none_move_area' });

    lock();
}
function closePop(id) {
    $('#dimmed').hide();
    $("#" + id).hide();
    release();
};
function lock() {
    $("body").css("overflow", "hidden");
}
function release() {
    $("body").css("overflow", "auto");
};

function dateSelect(id) {
    $("#" + id).focus();
}
function chk_SameChr(val, len) {
    var b = "";
    var c = "";
    var j = 0;
    for (var i = 0; i < val.length; i++) {
        var c = val.charAt(i).toLowerCase();
        if (b == "") { b = c; }
        if (b == c) { j = j + 1; }
        else { j = 1; }
        if (j >= len) { break; }
        b = c;
    }
    if (j >= len) {
        return false;
    }
    else {
        return true;
    }
}

// 기간별 일자 셋팅후 재검색
function common_setDate(term, sDateID, eDateID) {
    var sDate, eDate, year, month, day, weekday;

    // 시작일자
    sDate = new Date();

    if (term == "-1d") {			// 어제
        sDate.setDate(sDate.getDate() - 1);
    } else if (term == "-2d") {		// 2일전
        sDate.setDate(sDate.getDate() - 2);
    } else if (term == "-3d") {		// 3일전
        sDate.setDate(sDate.getDate() - 3);
    } else if (term == "0m") {		// 이번달
        sDate.setDate(1);
    } else if (term == "-1m") {		// 지난달
        sDate.setMonth(sDate.getMonth() - 1);
        sDate.setDate(1);
    } else if (term == "3d") {		// 3일
        sDate.setDate(sDate.getDate() - 3);
    } else if (term == "7d") {		// 7일
        sDate.setDate(sDate.getDate() - 7);
    } else if (term == "30d") {		// 30일
        sDate.setDate(sDate.getDate() - 30);
    } else if (term == "1m") {		// 1개월
        sDate.setMonth(sDate.getMonth() - 1);
    } else if (term == "3m") {		// 3개월
        sDate.setMonth(sDate.getMonth() - 3);
    } else if (term == "1y") {		// 1년
        sDate.setFullYear(sDate.getFullYear() - 1);
    } else if (term == "-1y") {		// 전년
        sDate = new Date($("#" + sDateID).val());
        sDate.setFullYear(sDate.getFullYear() - 1);
    } else if (term == "0w") {			// 이번주
        weekday = sDate.getDay();		// 일(0), 월(1), 화(2), 수(3), 목(4), 금(5), 토(6)
        if (weekday == 0) weekday = 7;
        sDate.setDate(sDate.getDate() - (weekday - 1));
    } else if (term == "-1w") {			// 지난주
        weekday = sDate.getDay();		// 일(0), 월(1), 화(2), 수(3), 목(4), 금(5), 토(6)
        if (weekday == 0) weekday = 7;
        sDate.setDate(sDate.getDate() - (weekday - 1) - 7);
    } else if (term == "-2w") {			// 2주전
        weekday = sDate.getDay();		// 일(0), 월(1), 화(2), 수(3), 목(4), 금(5), 토(6)
        if (weekday == 0) weekday = 7;
        sDate.setDate(sDate.getDate() - (weekday - 1) - 14);
    } else if (term == "1h") {			// 상반기
        sDate.setFullYear(sDate.getFullYear());
        sDate.setMonth(0);
        sDate.setDate(1);
    } else if (term == "2h") {			// 하반기
        sDate.setFullYear(sDate.getFullYear());
        sDate.setMonth(6);
        sDate.setDate(1);
    } else if (term == "0d") {		// 오늘
        sDate.setDate(sDate.getDate());
    }

    year = sDate.getFullYear();
    month = sDate.getMonth() + 1;
    day = sDate.getDate();
    if (month < 10) month = "0" + month;
    if (day < 10) day = "0" + day;
    sDate = year + "-" + month + "-" + day;

    // 종료일자
    eDate = new Date();

    if (term == "-1d") {			// 어제
        eDate.setDate(eDate.getDate() - 1);
    } else if (term == "-2d") {		// 2일전
        eDate.setDate(eDate.getDate() - 2);
    } else if (term == "-3d") {		// 3일전
        eDate.setDate(eDate.getDate() - 3);
    } else if (term == "0m") {		// 이번달
        eDate.setDate(1);
        eDate.setMonth(eDate.getMonth() + 1);
        eDate.setDate(eDate.getDate() - 1);
    } else if (term == "-1m") {		// 지난달
        eDate.setDate(1);
        eDate.setDate(eDate.getDate() - 1);
    } else if (term == "-1y") {		// 전년
        eDate = new Date($("#" + eDateID).val());
        eDate.setFullYear(eDate.getFullYear() - 1);
    } else if (term == "0w") {			// 이번주
        eDate = new Date(year, month - 1, day);
        eDate.setDate(eDate.getDate() + 6);
    } else if (term == "-1w") {			// 지난주
        eDate = new Date(year, month - 1, day);
        eDate.setDate(eDate.getDate() + 6);
    } else if (term == "-2w") {			// 2주전
        eDate = new Date(year, month - 1, day);
        eDate.setDate(eDate.getDate() + 6);
    } else if (term == "1h") {			// 상반기
        eDate.setFullYear(eDate.getFullYear());
        eDate.setMonth(5);
        eDate.setDate(30);
    } else if (term == "2h") {			// 하반기
        eDate.setFullYear(eDate.getFullYear());
        eDate.setMonth(11);
        eDate.setDate(31);
    } else if (term == "0d") {		// 오늘
        eDate.setDate(eDate.getDate());
    }

    year = eDate.getFullYear();
    month = eDate.getMonth() + 1;
    day = eDate.getDate();
    if (month < 10) month = "0" + month;
    if (day < 10) day = "0" + day;
    eDate = year + "-" + month + "-" + day;

    $("#" + sDateID).val(sDate);
    $("#" + eDateID).val(eDate);
}

/* 사이드 메뉴 보이기 */
function show_SideMenu() {
    if ($(".lnbW").css("left") == "-200px") {
        $(".lnbW").animate({ "left": "0px" }, "fast");
        $(".container .contents").animate({ "margin-left": "200px" }, "fast");
    } else {
        $(".lnbW").animate({ "left": "-200px" }, "fast");
        $(".container .contents").animate({ "margin-left": "0px" }, "fast");
    }
}

/* 브랜드검색 */
function getSearchBrandList(oBrandCode, formName) {
    var aData = "";
    if (formName != "") {
        aData = $("form[name='" + formName + "']").serialize();
    }
    else {
        aData = "OBrandCode=" + oBrandCode + "&SBrandCode=" + $("#" + oBrandCode).val();
    }

    $.ajax({
        type: "post",
        url: "/Common/Ajax/SearchBrandList.asp",
        async: true,
        data: aData,
        dataType: "text",
        success: function (data) {
            var splitData = data.split("|||||");
            var result = splitData[0];
            var cont = splitData[1];fdate

            if (result == "OK") {
                $("#PopupLayer").html(cont);
                openPop('PopupLayer');
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
        error: function (data) {
            alert("처리 도중 오류가 발생하였습니다.");
        }
    });
}

function getSearchBrandPop(formName) {
    getSearchBrandList('', formName);
}

/* 특정 브랜드구분 선택 */
function selectBrandType(bType, sel) {
    $("input:checkbox[name='BrandCode']", "form[name='bForm1']").each(function () {
        if (bType == "" || $(this).data("btype") == bType) {
            if (sel == "Y") {
                $(this).prop("checked", true);
            } else {
                $(this).prop("checked", false);
            }
        }
    });
}

/* 선택 브랜드 적용 */
function setSearchBrandList(oBrandCode) {
    var brandCode = "";

    $("input:checkbox[name='BrandCode']:checked").each(function () {
        if (brandCode == "") {
            brandCode = $(this).val();
        } else {
            brandCode += "," + $(this).val();
        }
    });

    $("#" + oBrandCode).val(brandCode);
    $("#" + oBrandCode + "_Text").html(brandCode);

    closePop('PopupLayer');
}
function admin_Certification(authFor) {
    window.open('/Nice/NiceID_Main?AuthFor=' + authFor, 'cert', 'width=450, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no').focus();
}
function getCsrfToken() {
    var token = $("input[name='__RequestVerificationToken']", "#frmCsrfToken").val();
    return token;
}
function refreshToken(formid) {
    $.ajax({
        type: "post",
        url: '/Shared/RefreshToken',
        async: false,
        dataType: "html",
        success: function (html) {
            var token = $('<div />').html(html).find('input[type="hidden"]').val();
            $("input[name='__RequestVerificationToken']", "#" + formid).val(token);
        },
        error: function (data) {
        }
    });
}

/* =================================================================
 execDaumPostcode(zipCode, address)
 다음 주소 검색 팝업창
 zipCode : 우편번호 input id
 address : 주소 input id
 ================================================================= */
function execDaumPostcode(zipCode, address) {
    new daum.Postcode({
        oncomplete: function (data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
            //alert(data.addressType + "-" + data.userSelectedType + "\n\n1 : " + data.roadAddress + "\n2 : " + data.autoRoadAddress + "\n\n3 : " + data.jibunAddress + "\n4 : " + data.autoJibunAddress);

            // data.addressType : 주소검색방법 R=도로명검색, J=지번검색
            // data.userSelectedType : 주소선택구분 R=도로명주소선택, J=지번주소선택
            var roadAddress = data.roadAddress;
            var jibunAddress = data.jibunAddress;
            if (data.addressType == "R") {
                if (jibunAddress == "" && data.userSelectedType == "R") {
                    jibunAddress = data.autoJibunAddress;
                }
            } else {
                if (roadAddress == "" && data.userSelectedType == "J") {
                    roadAddress = data.autoRoadAddress;
                }
            }

            // 도로명 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var fullRoadAddr = roadAddress; // 도로명 주소 변수
            var extraRoadAddr = ""; // 도로명 조합형 주소 변수

            // 법정동명이 있을 경우 추가한다. (법정리는 제외)
            // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
                extraRoadAddr += data.bname;
            }
            // 건물명이 있고, 공동주택일 경우 추가한다.
            if (data.buildingName !== "" && data.apartment === "Y") {
                extraRoadAddr += (extraRoadAddr !== "" ? ", " + data.buildingName : data.buildingName);
            }
            // 도로명, 지번 조합형 주소가 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
            if (extraRoadAddr !== "") {
                extraRoadAddr = " (" + extraRoadAddr + ")";
            }
            // 도로명, 지번 주소의 유무에 따라 해당 조합형 주소를 추가한다.
            if (fullRoadAddr !== "") {
                fullRoadAddr += extraRoadAddr;
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById(zipCode).value = data.zonecode; //5자리 새우편번호 사용
            document.getElementById(address).value = fullRoadAddr;
        }
    }).open();
}


function formSubmit(cls) {
    openPop('loading');
    btnDisabled(cls, true);
}
function formInit(cls) {
    closePop('loading');
    btnDisabled(cls, false);
}
function btnDisabled(cls, flag) {
    if (cls === undefined) {
        $('.btn-c').attr('disabled', flag);
    }
    else {
        $('.' + cls).attr('disabled', flag);
    }
}