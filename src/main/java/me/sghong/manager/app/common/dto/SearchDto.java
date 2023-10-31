package me.sghong.manager.app.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
    private int page;
    private int recordsize;
    private int pagesize;
    private String searchtype;
    private String keyword;
    private Pagination pagination;

    public SearchDto() {
        this.page = 1;
        this.recordsize = 10;
        this.pagesize = 10;
    }
}
