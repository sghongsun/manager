package me.sghong.manager.app.manage.service;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.common.dto.Pagination;
import me.sghong.manager.app.common.dto.PagingDto;
import me.sghong.manager.app.manage.dto.TermsDto;
import me.sghong.manager.app.manage.dto.TermsSearchDto;
import me.sghong.manager.app.manage.mapper.TermsMapper;
import me.sghong.manager.app.manage.request.TermsAddRequest;
import me.sghong.manager.app.manage.request.TermsDeleteRequest;
import me.sghong.manager.app.manage.request.TermsModifyRequest;
import me.sghong.manager.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TermsService {
    private final TermsMapper termsMapper;

    public PagingDto<TermsDto> getList(TermsSearchDto termsSearchDto) {
        int count = termsMapper.select_by_list_for_totalCount(termsSearchDto);

        if (count < 1) {
            return new PagingDto<>(Collections.emptyList(), null);
        }

        Pagination pagination = new Pagination(count, termsSearchDto);
        termsSearchDto.setPagination(pagination);

        List<TermsDto> termsDtoList = termsMapper.select_by_list(termsSearchDto);
        return new PagingDto<>(termsDtoList, pagination);
    }

    @Transactional
    public void insertTerms(TermsAddRequest termsAddRequest) {
        TermsDto termsDto = TermsDto.builder()
                .title(termsAddRequest.getTitle())
                .place(termsAddRequest.getPlace())
                .contents(termsAddRequest.getContents())
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();
        termsMapper.insert_terms(termsDto);
    }

    public TermsDto getTermsInfo(int idx) {
        return termsMapper.select_by_idx(idx);
    }

    @Transactional
    public void updateTerms(TermsModifyRequest termsModifyRequest) {
        TermsDto termsDto = TermsDto.builder()
                .idx(termsModifyRequest.getIdx())
                .title(termsModifyRequest.getTitle())
                .place(termsModifyRequest.getPlace())
                .contents(termsModifyRequest.getContents())
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();
        termsMapper.update_terms(termsDto);
    }

    @Transactional
    public void deleteTerms(TermsDeleteRequest termsDeleteRequest) {
        TermsDto termsDto = TermsDto.builder()
                .idx(termsDeleteRequest.getIdx())
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();
        termsMapper.delete_terms(termsDto);
    }
}
