package me.sghong.manager.app.common.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PagingDto<T> {
    private List<T> list = new ArrayList<>();
    private Pagination pagination;

    public PagingDto(List<T> list, Pagination pagination) {
        this.list.addAll(list);
        this.pagination = pagination;
    }
}
