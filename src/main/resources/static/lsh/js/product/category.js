/***********************************************************************************************/
/* 대분류
/***********************************************************************************************/

/* 대분류 입력 폼 */
function form_Category1() {
    $.ajax({
        type		 : "post",
        url			 : "/product/category/ajax/category1addform",
        async		 : true,
        dataType	 : "text",
        beforeSend: function(xhr){
            xhr.setRequestHeader($("meta[name='_csrf_header']").attr('content'), $("meta[name='_csrf']").attr('content'));
        },
        success		 : function (data) {
            var splitData	 = data.split("|||||");
            var result		 = splitData[0];
            var cont		 = splitData[1];

            if (result == "OK") {
                $("#Category1AddFrom").html(cont);
                return;
            }
            else if (result == "LOGIN") {
                PageReload();
                return;
            }
            else {
                alert(cont);
                return;
            }
        },
        error		 : function (data) {
            alert("처리 도중 오류가 발생하였습니다.");
        }
    });
}

/* 대분류 입력폼 체크 */
function ins_Category1() {
    var categoryName1 = alltrim($("#CategoryName1").val());
    if (categoryName1.length == 0) {
        alert("대분류명을 입력해 주십시오.");
        $("#CategoryName1").focus();
        return;
    }

    var conf = confirm("입력 하시겠습니까?");
    if (conf) {
        $.ajax({
            type		 : "post",
            url			 : "/ASP/Product/Category/Ajax/Category1AddOk.asp",
            async		 : true,
            data		 : $("#InsCategory1").serialize(),
            dataType	 : "text",
            success		 : function (data) {
                var splitData	 = data.split("|||||");
                var result		 = splitData[0];
                var cont		 = splitData[1];

                if (result == "OK") {
                    alert("입력 되었습니다.");
                    form_Category1();
                    list_Category1();
                    return;
                }
                else if (result == "LOGIN") {
                    PageReload();
                    return;
                }
                else {
                    alert(cont);
                    return;
                }
            },
            error		 : function (data) {
                alert("처리 도중 오류가 발생하였습니다.");
            }
        });
    }
}


/* 대분류 수정폼 체크 */
function mod_Category1(num) {
    var categoryCode1 = $("input[name='MCategoryCode1']").eq(num).val();
    var categoryName1 = alltrim($("input[name='MCategoryName1']").eq(num).val());
    if (categoryName1.length == 0) {
        alert("대분류명을 입력해 주십시오.");
        $("input[name='MCategoryName1']").eq(num).focus();
        return;
    }
    var displayFlag = $("select[name='MDisplayFlag']").eq(num).val();

    var conf = confirm("수정 하시겠습니까?");
    if (conf) {
        $("#C_CategoryCode1").val(categoryCode1);
        $("#C_CategoryName1").val(categoryName1);
        $("#C_DisplayFlag").val(displayFlag);

        $.ajax({
            type		 : "post",
            url			 : "/ASP/Product/Category/Ajax/Category1ModifyOk.asp",
            async		 : true,
            data		 : $("#ModCategory1").serialize(),
            dataType	 : "text",
            success		 : function (data) {
                var splitData	 = data.split("|||||");
                var result		 = splitData[0];
                var cont		 = splitData[1];

                if (result == "OK") {
                    alert("수정 되었습니다.");
                    list_Category1();
                    return;
                }
                else if (result == "LOGIN") {
                    PageReload();
                    return;
                }
                else {
                    alert(cont);
                    return;
                }
            },
            error		 : function (data) {
                alert("처리 도중 오류가 발생하였습니다.");
            }
        });
    }
}


/* 대분류 게시순서 변경 */
function mod_Category1DisplayNum(num, modType) {
    var categoryCode1 = $("input[name='MCategoryCode1']").eq(num).val();

    $.ajax({
        type		 : "post",
        url			 : "/ASP/Product/Category/Ajax/Category1DisplayNumModifyOk.asp",
        async		 : true,
        data		 : "CategoryCode1=" + categoryCode1 + "&ModType=" + modType,
        dataType	 : "text",
        success		 : function (data) {
            var splitData	 = data.split("|||||");
            var result		 = splitData[0];
            var cont		 = splitData[1];

            if (result == "OK") {
                list_Category1();
                return;
            }
            else if (result == "LOGIN") {
                PageReload();
                return;
            }
            else {
                alert(cont);
                return;
            }
        },
        error		 : function (data) {
            alert("처리 도중 오류가 발생하였습니다.");
        }
    });
}


/***********************************************************************************************/
/* 중분류
/***********************************************************************************************/

/* 중분류 입력 폼 */
function form_Category2(id) {
    $.ajax({
        type		 : "post",
        url			 : "/ASP/Product/Category/Ajax/Category2AddForm.asp",
        async		 : true,
        data		 : $("#" + id).serialize(),
        dataType	 : "text",
        success		 : function (data) {
            var splitData	 = data.split("|||||");
            var result		 = splitData[0];
            var cont		 = splitData[1];

            if (result == "OK") {
                $("#Category2AddFrom").html(cont);
                return;
            }
            else if (result == "LOGIN") {
                PageReload();
                return;
            }
            else {
                alert(cont);
                return;
            }
        },
        error		 : function (data) {
            alert("처리 도중 오류가 발생하였습니다.");
        }
    });
}


/* 중분류 리스트 */
function list_Category2(categoryCode1) {
    $.ajax({
        type		 : "post",
        url			 : "/ASP/Product/Category/Ajax/Category2List.asp",
        async		 : true,
        data		 : "CategoryCode1=" + categoryCode1,
        dataType	 : "text",
        success		 : function (data) {
            var splitData	 = data.split("|||||");
            var result		 = splitData[0];
            var cont		 = splitData[1];

            if (result == "OK") {
                $("#Category2List").html(cont);
                return;
            }
            else if (result == "LOGIN") {
                PageReload();
                return;
            }
            else {
                alert(cont);
                return;
            }
        },
        error		 : function (data) {
            alert("처리 도중 오류가 발생하였습니다.");
        }
    });
}


/* 중분류 입력폼 체크 */
function ins_Category2() {
    var categoryCode1 = alltrim($("#CategoryCode1").val());
    if (categoryCode1.length == 0) {
        alert("대분류를 선택해 주십시오.");
        $("#CategoryCode1").focus();
        return;
    }
    var categoryCode2 = alltrim($("#CategoryCode2").val());
    if (categoryCode2.length == 0) {
        alert("중분류코드가 없습니다. 새로고침하여 주십시오.");
        $("#CategoryCode2").focus();
        return;
    }
    var categoryName2 = alltrim($("#CategoryName2").val());
    if (categoryName2.length == 0) {
        alert("중분류명을 입력해 주십시오.");
        $("#CategoryName2").focus();
        return;
    }

    var conf = confirm("입력 하시겠습니까?");
    if (conf) {
        $.ajax({
            type		 : "post",
            url			 : "/ASP/Product/Category/Ajax/Category2AddOk.asp",
            async		 : true,
            data		 : $("#InsCategory2").serialize(),
            dataType	 : "text",
            success		 : function (data) {
                var splitData	 = data.split("|||||");
                var result		 = splitData[0];
                var cont		 = splitData[1];

                if (result == "OK") {
                    alert("입력 되었습니다.");
                    form_Category2(categoryCode1);
                    return;
                }
                else if (result == "LOGIN") {
                    PageReload();
                    return;
                }
                else {
                    alert(cont);
                    return;
                }
            },
            error		 : function (data) {
                alert("처리 도중 오류가 발생하였습니다.");
            }
        });
    }
}


/* 중분류 수정폼 체크 */
function mod_Category2(num) {
    var categoryCode1 = $("input[name='MCategoryCode1']").eq(num).val();
    var categoryCode2 = $("input[name='MCategoryCode2']").eq(num).val();
    var categoryName2 = alltrim($("input[name='MCategoryName2']").eq(num).val());
    if (categoryName2.length == 0) {
        alert("중분류명을 입력해 주십시오.");
        $("input[name='MCategoryName2']").eq(num).focus();
        return;
    }
    var displayFlag = $("select[name='MDisplayFlag']").eq(num).val();

    var conf = confirm("수정 하시겠습니까?");
    if (conf) {
        $("#C_CategoryCode1").val(categoryCode1);
        $("#C_CategoryCode2").val(categoryCode2);
        $("#C_CategoryName2").val(categoryName2);
        $("#C_DisplayFlag").val(displayFlag);

        $.ajax({
            type		 : "post",
            url			 : "/ASP/Product/Category/Ajax/Category2ModifyOk.asp",
            async		 : true,
            data		 : $("#ModCategory2").serialize(),
            dataType	 : "text",
            success		 : function (data) {
                var splitData	 = data.split("|||||");
                var result		 = splitData[0];
                var cont		 = splitData[1];

                if (result == "OK") {
                    alert("수정 되었습니다.");
                    list_Category2(categoryCode1);
                    return;
                }
                else if (result == "LOGIN") {
                    PageReload();
                    return;
                }
                else {
                    alert(cont);
                    return;
                }
            },
            error		 : function (data) {
                alert("처리 도중 오류가 발생하였습니다.");
            }
        });
    }
}


/* 중분류 게시순서 변경 */
function mod_Category2DisplayNum(num, modType) {
    var categoryCode1 = $("input[name='MCategoryCode1']").eq(num).val();
    var categoryCode2 = $("input[name='MCategoryCode2']").eq(num).val();

    $.ajax({
        type		 : "post",
        url			 : "/ASP/Product/Category/Ajax/Category2DisplayNumModifyOk.asp",
        async		 : true,
        data		 : "CategoryCode1=" + categoryCode1 + "&CategoryCode2=" + categoryCode2 + "&ModType=" + modType,
        dataType	 : "text",
        success		 : function (data) {
            var splitData	 = data.split("|||||");
            var result		 = splitData[0];
            var cont		 = splitData[1];

            if (result == "OK") {
                list_Category2(categoryCode1);
                return;
            }
            else if (result == "LOGIN") {
                PageReload();
                return;
            }
            else {
                alert(cont);
                return;
            }
        },
        error		 : function (data) {
            alert("처리 도중 오류가 발생하였습니다.");
        }
    });
}



/***********************************************************************************************/
/* 전체분류
/***********************************************************************************************/
/* 전체분류 수정 폼 */
function lay_CategoryModifyForm(depth, categoryCode1, categoryCode2) {
    $.ajax({
        type		 : "post",
        url			 : "/product/category/ajax/categorymodifyform",
        async		 : false,
        data		 : "Depth=" + depth + "&CategoryCode1=" + categoryCode1 + "&CategoryCode2=" + categoryCode2,
        dataType	 : "text",
        beforeSend: function(xhr){
            xhr.setRequestHeader($("meta[name='_csrf_header']").attr('content'), $("meta[name='_csrf']").attr('content'));
        },
        success		 : function (data) {
            var splitData	 = data.split("|||||");
            var result		 = splitData[0];
            var cont		 = splitData[1];


            if (result === "OK") {
                $("#popup").html(cont);
                $("#popup").css({ 'top': '50%', 'left': '50%', 'width': 640 });
                openPop('popup');
                $("#popup").draggable({ cancel: ".pContents" });
                return;
            }
            else if (result === "LOGIN") {
                PageReload();
                return;
            }
            else {
                alert(cont);
                return;
            }
        },
        error		 : function (data) {
            alert("처리 도중 오류가 발생하였습니다.");
        }
    });
}

/* 전체분류 수정폼 체크 */
function lay_CategoryModify() {
    var depth			 = $("input[name='Depth']",			 "form[name='ModCategory']").val();
    var categoryCode1	 = $("input[name='CategoryCode1']",	 "form[name='ModCategory']").val();
    var categoryCode2	 = $("input[name='CategoryCode2']",	 "form[name='ModCategory']").val();
    var categoryName	 = $("input[name='CategoryName']",	 "form[name='ModCategory']").val();
    //var displayFlag		 = $("select[name='DisplayFlag']",	 "form[name='ModCategory']").val();

    if (categoryCode1.length == 0) {
        alert("대분류 코드 정보가 없습니다.");
        return;
    }

    if (depth === "2") {
        if (categoryCode2.length == 0) {
            alert("소분류 코드 정보가 없습니다.");
            return;
        }
    }

    if (categoryName.length == 0) {
        alert("분류명을 입력하여 주십시오.");
        $("input[name='CategoryName']", "form[name='ModCategory']").focus();
        return;
    }

    var conf = confirm("수정 하시겠습니까?");
    if (conf) {
        $.ajax({
            type		 : "post",
            url			 : "/product/category/ajax/categorymodifyok",
            async		 : true,
            data		 : $("#ModCategory").serialize(),
            dataType	 : "text",
            success		 : function (data) {
                var splitData	 = data.split("|||||");
                var result		 = splitData[0];
                var cont		 = splitData[1];

                if (result == "OK") {
                    alert("수정 되었습니다.");
                    closePop('popup');
                    PageReload();
                    return;
                }
                else if (result == "LOGIN") {
                    PageReload();
                    return;
                }
                else {
                    alert(cont);
                    return;
                }
            },
            error		 : function (data) {
                alert("처리 도중 오류가 발생하였습니다.");
            }
        });
    }
}

/* 전체분류 게시순서 변경 */
function mod_CategoryDisplayNum(depth, modType, categoryCode1, categoryCode2) {
    $.ajax({
        type		 : "post",
        url			 : "/product/category/ajax/categorydisplaymummodifyok",
        async		 : true,
        data		 : "Depth=" + depth + "&ModType=" + modType + "&CategoryCode1=" + categoryCode1 + "&CategoryCode2=" + categoryCode2,
        dataType	 : "text",
        beforeSend: function(xhr){
            xhr.setRequestHeader($("meta[name='_csrf_header']").attr('content'), $("meta[name='_csrf']").attr('content'));
        },
        success		 : function (data) {
            var splitData	 = data.split("|||||");
            var result		 = splitData[0];
            var cont		 = splitData[1];

            if (result == "OK") {
                PageReload();
                return;
            }
            else if (result == "LOGIN") {
                PageReload();
                return;
            }
            else {
                alert(cont);
                return;
            }
        },
        error		 : function (data) {
            alert("처리 도중 오류가 발생하였습니다.");
        }
    });
}


/* 전체분류 입력 폼 */
function lay_CategoryAddForm(num) {
    var depth			 = "";
    var categoryCode1	 = "";
    var categoryCode2	 = "";
    var categoryCode3	 = "";
    var categoryName	 = "";
    var displayFlag		 = "";

    if (num == "0") {
        depth = $("#AddDepth").val();
        $("input[name='Depth']",		 "form[name='FormCategory']").val(depth);
        $("input[name='CategoryCode1']", "form[name='FormCategory']").val("");
        $("input[name='CategoryName']",	 "form[name='FormCategory']").val("");
        $("input[name='DisplayFlag']",	 "form[name='FormCategory']").val("");
    }
    else if (num == "1") {
        depth			 = $("input[name='Depth']",			 "form[id='InsCategory']").val();
        categoryCode1	 = $("select[name='CategoryCode1']", "form[id='InsCategory']").val();
        categoryName	 = $("input[name='CategoryName']",	 "form[id='InsCategory']").val();
        displayFlag		 = $("select[name='DisplayFlag']",	 "form[id='InsCategory']").val();
        $("input[name='Depth']",		 "form[name='FormCategory']").val(depth);
        $("input[name='CategoryCode1']", "form[name='FormCategory']").val(categoryCode1);
        $("input[name='CategoryName']",	 "form[name='FormCategory']").val(categoryName);
        $("input[name='DisplayFlag']",	 "form[name='FormCategory']").val(displayFlag);
    }

    $.ajax({
        type		 : "post",
        url			 : "/product/category/ajax/categoryaddform",
        async		 : false,
        data		 : $("#FormCategory").serialize(),
        dataType	 : "text",
        success		 : function (data) {
            var splitData	 = data.split("|||||");
            var result		 = splitData[0];
            var cont		 = splitData[1];


            if (result === "OK") {
                $("#popup").html(cont);
                $("#popup").css({ 'top': '50%', 'left': '50%', 'width': 640 });
                openPop('popup');
                $("#popup").draggable({ cancel: ".pContents" });
                return;
            }
            else if (result === "LOGIN") {
                PageReload();
                return;
            }
            else {
                alert(cont);
                return;
            }
        },
        error		 : function (data) {
            alert("처리 도중 오류가 발생하였습니다.");
        }
    });
}


/* 전체분류 입력폼 체크 */
function lay_CategoryAdd() {
    var depth	 = alltrim($("#Depth", "form[name='InsCategory']").val());
    var cateName = "";

    if (depth === "2") {
        var categoryCode1 = alltrim($("#CategoryCode1", "form[name='InsCategory']").val());
        if (categoryCode1.length == 0) {
            alert("대분류를 선택해 주십시오.");
            $("#CategoryCode1", "form[name='InsCategory']").focus();
            return;
        }
    }

    var categoryName = alltrim($("#CategoryName", "form[name='InsCategory']").val());
    if (categoryName.length == 0) {
        alert("분류명을 입력해 주십시오.");
        $("#CategoryName", "form[name='InsCategory']").focus();
        return;
    }

    var conf = confirm("입력 하시겠습니까?");
    if (conf) {
        $.ajax({
            type		 : "post",
            url			 : "/product/category/ajax/categoryaddok",
            async		 : true,
            data		 : $("#InsCategory").serialize(),
            dataType	 : "text",
            success		 : function (data) {
                var splitData	 = data.split("|||||");
                var result		 = splitData[0];
                var cont		 = splitData[1];

                if (result == "OK") {
                    alert("입력 되었습니다.");
                    closePop('popup');
                    PageReload();
                    return;
                }
                else if (result == "LOGIN") {
                    PageReload();
                    return;
                }
                else {
                    alert(cont);
                    return;
                }
            },
            error		 : function (data) {
                alert(data.responseText)//alert("처리 도중 오류가 발생하였습니다.");
            }
        });
    }
}