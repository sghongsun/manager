function setQueryStringParams(formid) {
    if (!location.search) {
        return false;
    }

    const form = document.getElementById(formid);

    new URLSearchParams(location.search).forEach((value, key) => {
        if(form[key]) {
            form[key].value = value;
        }
    })
}

function getSearch() {
    $("#page").val(1);
    document.searchForm.submit();
}

function moveList(url) {
    const queryString = (location.search) ? location.search : "";
    location.href=url+queryString;
}

function movePage(page) {
    $("#page").val(page);
    document.searchForm.submit();
}

function moveView(loc, id) {
    document.searchForm.action = loc+"/"+id;
    document.searchForm.submit();
}