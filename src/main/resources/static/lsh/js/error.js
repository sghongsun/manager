function check_MenuError() {
	var vType = $("#T_VType").val();
	if (vType === "DL" || vType === "DA") {
		var id = $(".fieldError").eq(0).data("id");
		var html = $(".fieldError").eq(0).html();
		if (html !== undefined) {
			var msgs = html.split("<br>");
			var map = new Map();
			for(let item of msgs) {
				var kv = item.split("/");
				map.set(kv[0], kv[1]);
			}

			var mapArr = Array.from(map);
			mapArr.sort();
			var errMsg = mapArr[0][1];
			if (vType === "DL") {
				$(".fieldError").eq(0).html(errMsg);
				$(".fieldError").eq(0).removeClass("dp_n");
				$("#" + id).focus();
			}
			else {
				alert(errMsg);
				$("#" + id).focus();
			}
		}
	}
}