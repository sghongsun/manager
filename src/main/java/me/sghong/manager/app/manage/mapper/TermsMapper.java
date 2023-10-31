package me.sghong.manager.app.manage.mapper;

import me.sghong.manager.app.manage.dto.TermsDto;
import me.sghong.manager.app.manage.dto.TermsSearchDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TermsMapper {
    List<TermsDto> select_by_list(TermsSearchDto termsSearchDto);
    int select_by_list_for_totalCount(TermsSearchDto termsSearchDto);
    void insert_terms(TermsDto termsDto);
    TermsDto select_by_idx(int idx);
    void update_terms(TermsDto termsDto);
    void delete_terms(TermsDto termsDto);

}
