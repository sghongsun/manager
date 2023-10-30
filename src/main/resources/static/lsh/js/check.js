/* =================================================================
chkFileExt()
허용하는 파일 확장자 체크
--------------------------------------------------------------------
val			From Field Value
all_ext		Allow Extention Value
================================================================= */
function chkFileExt(val, all_ext) {
    var lng, s_chr, ext, all_exts
    if (val.length != 0) {
        s_chr = val.lastIndexOf(".")
        if (s_chr < 0) {
            return false;
        }
        else {
            ext = val.substring(s_chr + 1, val.length);
            ext = ext.toLowerCase();
            all_exts = all_ext.split("/");
            for (var i = 0; i < all_exts.length; i++) {
                if (all_exts[i] == ext) {
                    return true;
                    break;
                }
            }
            return false;
        }
    }
}


/* =================================================================
	denyFileExt()
	허용되지 않는 파일 확장자 체크
--------------------------------------------------------------------
	val			From Field Value
	all_ext		Deny Extention Value
================================================================= */
function denyFileExt(val, all_ext) {
    var lng, s_chr, ext, all_exts
    if (val.length != 0) {
        s_chr = val.lastIndexOf(".")
        if (s_chr < 0) {
            return false;
        }
        else {
            ext = val.substring(s_chr + 1, val.length);
            ext = ext.toLowerCase();
            all_exts = all_ext.split(",");
            for (var i = 0; i < all_exts.length; i++) {
                if (all_exts[i] == ext) {
                    return false;
                    break;
                }
            }
            return true;
        }
    }
}



function ltrim(str) {
    var i;
    var ch;
    var retStr = '';
    if (str.length == 0)
        return str;
    for (i = 0; i < str.length; i++) {
        ch = str.charAt(i);
        if (retStr.length == 0 && (ch == ' ' || ch == '\r' || ch == '\n'))
            continue;
        retStr += ch;
    }
    return retStr;
}

function rtrim(str) {
    var i;
    var ch;
    var retStr = '';
    if (str.length == 0)
        return str;
    for (i = str.length - 1; i >= 0; i--) {
        ch = str.charAt(i);
        if (ch != ' ' && ch != '\r' && ch != '\n') {
            break;
        }
    }
    retStr = str.substring(0, i + 1);
    return retStr;
}

function trim(str) {
    var retStr;
    retStr = ltrim(str);
    retStr = rtrim(retStr);
    return retStr;
}

function alltrim(str) {
    var i;
    var ch;
    var retStr = '';
    if (str.length == 0)
        return str;
    for (i = 0; i < str.length; i++) {
        ch = str.charAt(i);
        if (ch == ' ' || ch == '\r' || ch == '\n')
            continue;
        retStr += ch;
    }
    return retStr;
}

function beAllowStr(str, allowStr) {
    var i;
    var ch;
    for (i = 0; i < str.length; i++) {
        ch = str.charAt(i);
        if (allowStr.indexOf(ch) < 0) {
            return false;
        }
    }
    return true;
}


function check_RegExp(val, patten) {
    if (patten.test(val)) { return true; }
    else { return false; }
}
function check_HP_Val(val) {
    var regEx = /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;
    return check_RegExp(val, regEx);
}
function check_Tel_Val(val) {
    var regEx = /^(0(2|3[1-3]|4[1-4]|5[1-5]|6[1-4]|70))-?(\d{3,4})-?(\d{4})$/;
    return check_RegExp(val, regEx);
}
function check_Email_Val(val) {
    var regEx = /^[_0-9a-zA-Z-]+(.[_0-9a-zA-Z-]+)*@[0-9a-zA-Z-]+(.[0-9a-zA-Z-]+)*$/;
    return check_RegExp(val, regEx);
}
function check_Num_Val(val) {
    var regEx = /^[0-9]*$/;
    return check_RegExp(val, regEx);
}
function check_Float_Val(val) {
    var regEx = /^\d+(?:[.]\d+)?$/;
    return check_RegExp(val, regEx);
}
function check_Alpha_Val(val, blankFlag) {
    var regEx;
    if (blankFlag === "Y") {
        regEx = /^[a-zA-Z ]*$/;
    } else {
        regEx = /^[a-zA-Z]*$/;
    }
    return check_RegExp(val, regEx);
}
function check_AlphaNum_Val(val, blankFlag) {
    var regEx;
    if (blankFlag === "Y") {
        regEx = /^[0-9a-zA-Z ]*$/;
    } else {
        regEx = /^[0-9a-zA-Z]*$/;
    }
    return check_RegExp(val, regEx);
}
function check_HanAlphaNum_Val(val, blankFlag) {
    var regEx;
    if (blankFlag === "Y") {
        regEx = /^[가-힣0-9a-zA-Z ]*$/;
    } else {
        regEx = /^[가-힣0-9a-zA-Z]*$/;
    }
    return check_RegExp(val, regEx);
}
function check_Date_Val(dateStr) {
    const regex = /^\d{4}-\d{2}-\d{2}$/;

    if (dateStr.match(regex) === null) {
        return false;
    }

    const date = new Date(dateStr);

    const timestamp = date.getTime();

    if (typeof timestamp !== 'number' || Number.isNaN(timestamp)) {
        return false;
    }

    return date.toISOString().startsWith(dateStr);
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

function strLengthByte(str) {
    var totLength = 0;
    for (var i = 0; i < str.length; i++) {
        var currentByte = str.charCodeAt(i);
        if (currentByte > 128) {
            totLength += 2;
        }
        else {
            totLength++;
        }
    }
    return totLength;
}


// byte로 자르기
function chr_byte(chr) {
    if (escape(chr).length > 4) return 2;
    else return 1;
}
function cutByte(str, limit) {
    var tmpStr = str;
    var byte_count = 0;
    var len = str.length;
    var dot = "";

    for (i = 0; i < len; i++) {
        byte_count += chr_byte(str.charAt(i));
        if (byte_count == limit - 1) {
            if (chr_byte(str.charAt(i + 1)) == 2) {
                tmpStr = str.substring(0, i + 1);
                dot = "...";
            }
            else {
                if (i + 2 != len) dot = "...";
                tmpStr = str.substring(0, i + 2);
            }
            break;
        }
        else if (byte_count == limit) {
            if (i + 1 != len) dot = "...";
            tmpStr = str.substring(0, i + 1);
            break;
        }
    }
    //return (tmpStr+dot);
    return (tmpStr);
}


function checkDate(v_year, v_month, v_day) {
    var result = true;

    try {
        var dateRegex = /^(?=\d)(?:(?:31(?!.(?:0?[2469]|11))|(?:30|29)(?!.0?2)|29(?=.0?2.(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))(?:\x20|$))|(?:2[0-8]|1\d|0?[1-9]))([-.\/])(?:1[012]|0?[1-9])\1(?:1[6-9]|[2-9]\d)?\d\d(?:(?=\x20\d)\x20|$))?(((0?[1-9]|1[012])(:[0-5]\d){0,2}(\x20[AP]M))|([01]\d|2[0-3])(:[0-5]\d){1,2})?$/;
        result = dateRegex.test(v_day + '-' + v_month + '-' + v_year);
    }
    catch (err) {
        result = false;
    }
    return result;
}

/*숫자만 입력*/
function num_Only(obj, val) {
    val = val.replace(/[^0-9]/g, '').replace(/(\..*)\./g, '$1');
    obj.val(val);
}


function add_Comma(val) {
    var num = parseFloat(val);
    if (isNaN(num)) {
        return "";
    }
    else {
        return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    }
}
function del_Comma(val) {
    if (val == '')
        return val;

    return parseFloat(val.replace(/[^\d\.-]/g, ''));
}


/* 일자 선택 */
function chg_Date(dv, td) {
    var year	 = $("#" + dv + "Year").val();
    var month	 = $("#" + dv + "Month").val();
    var oDay	 = $("#" + dv + "Day");

    var days	 = get_Days(year, month);

    oDay.empty();

    var val = "";
    for (var i = 1; i <= days; i++) {
        if (i < 10) { val = "0" + i; }
        else { val = i; }
        oDay.append(new Option(val, val));
    }

    if (td != "") {
        oDay.val(td);
    }
}
function get_Days(y, m) {
    var lastDate	 = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
    var today		 = new Date();
    var year		 = parseInt(y);
    var month		 = parseInt(m);

    if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
        lastDate[1] = 29;
    }
    else {
        lastDate[1] = 28;
    }

    return lastDate[m - 1];
}