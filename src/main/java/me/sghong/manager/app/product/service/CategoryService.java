package me.sghong.manager.app.product.service;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.product.dto.CategoryDto;
import me.sghong.manager.app.product.mapper.CategoryMapper;
import me.sghong.manager.app.product.request.*;
import me.sghong.manager.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getCategory1ListInProductCnt() {
        return categoryMapper.select_by_category1_list_for_productcnt();
    }

    public List<CategoryDto> getCategory2ListInProductCnt() {
        return categoryMapper.select_by_category2_list_for_productcnt();
    }

    public List<CategoryDto> getCategory1List() {
        return categoryMapper.select_by_category1_list();
    }

    public List<CategoryDto> getCategory2List(String CategoryCode1) {
        return categoryMapper.select_by_category2_list(CategoryCode1);
    }

    @Transactional
    public void insertCategory1(Category1AddRequest category1AddRequest) {
        CategoryDto categoryDto = CategoryDto.builder()
                .categorycode1(CommonUtil.MaketoZero(Integer.toString(categoryMapper.select_by_category1_for_max_code()), 2))
                .categoryname1(category1AddRequest.getCategoryName())
                .displaynum(categoryMapper.select_by_category1_for_max_displaynum())
                .displayflag(category1AddRequest.getDisplayFlag())
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();
        categoryMapper.insert_category1(categoryDto);
    }

    @Transactional
    public void insertCategory2(Category2AddRequest category2AddRequest) {
        CategoryDto categoryDto = CategoryDto.builder()
                .categorycode1(category2AddRequest.getCategoryCode1())
                .categorycode2(CommonUtil.MaketoZero(Integer.toString(categoryMapper.select_by_category2_for_max_code(category2AddRequest.getCategoryCode1())), 2))
                .categoryname2(category2AddRequest.getCategoryName())
                .displaynum(categoryMapper.select_by_category2_for_max_displaynum(category2AddRequest.getCategoryCode1()))
                .displayflag(category2AddRequest.getDisplayFlag())
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();
        categoryMapper.insert_category2(categoryDto);
    }

    public CategoryDto getCategory1Info(String CategoryCode1) {
        return categoryMapper.select_by_category1(CategoryCode1);
    }

    public CategoryDto getCategory2Info(String CategoryCode1, String CategoryCode2) {
        return categoryMapper.select_by_category2(CategoryCode1, CategoryCode2);
    }

    @Transactional
    public void updateCategory1(Category1ModifyRequest category1ModifyRequest) {
        CategoryDto categoryDto = CategoryDto.builder()
                .categorycode1(category1ModifyRequest.getCategoryCode1())
                .categoryname1(category1ModifyRequest.getCategoryName())
                .displayflag(category1ModifyRequest.getDisplayFlag())
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();
        categoryMapper.update_category1(categoryDto);
    }

    @Transactional
    public void updateCategory2(Category2ModifyRequest category2ModifyRequest) {
        CategoryDto categoryDto = CategoryDto.builder()
                .categorycode1(category2ModifyRequest.getCategoryCode1())
                .categorycode2(category2ModifyRequest.getCategoryCode2())
                .categoryname2(category2ModifyRequest.getCategoryName())
                .displayflag(category2ModifyRequest.getDisplayFlag())
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();
        categoryMapper.update_category2(categoryDto);
    }

    @Transactional
    public void updateCategory1DisplayNum(Category1DisplayNumModifyRequest category1DisplayNumModifyRequest) {
        CategoryDto nowDto = categoryMapper.select_by_category1(category1DisplayNumModifyRequest.getCategoryCode1());
        CategoryDto chgDto = switch (category1DisplayNumModifyRequest.getModType()) {
            case "up" -> categoryMapper.select_by_category1_for_displaynum_up(nowDto.getDisplaynum());
            case "down" -> categoryMapper.select_by_category1_for_displaynum_down(nowDto.getDisplaynum());
            default -> null;
        };
        if (chgDto != null) {
            int nowDisplayNum = nowDto.getDisplaynum();
            nowDto.setDisplaynum(chgDto.getDisplaynum());
            nowDto.setCreateid(CommonUtil.getSession("adminid"));
            nowDto.setCreateip(CommonUtil.getSession("ip"));
            chgDto.setDisplaynum(nowDisplayNum);
            chgDto.setCreateid(CommonUtil.getSession("adminid"));
            chgDto.setCreateip(CommonUtil.getSession("ip"));

            categoryMapper.update_category1_displaynum(nowDto);
            categoryMapper.update_category1_displaynum(chgDto);
        }
    }

    @Transactional
    public void updateCategory2DisplayNum(Category2DisplayNumModityRequest category2DisplayNumModityRequest) {
        CategoryDto nowDto = categoryMapper.select_by_category2(category2DisplayNumModityRequest.getCategoryCode1(), category2DisplayNumModityRequest.getCategoryCode2());
        CategoryDto chgDto = switch (category2DisplayNumModityRequest.getModType()) {
            case "up" -> categoryMapper.select_by_category2_for_displaynum_up(nowDto.getCategorycode1(), nowDto.getDisplaynum());
            case "down" -> categoryMapper.select_by_category2_for_displaynum_down(nowDto.getCategorycode1(), nowDto.getDisplaynum());
            default -> null;
        };
        if (chgDto != null) {
            int nowDisplayNum = nowDto.getDisplaynum();
            nowDto.setDisplaynum(chgDto.getDisplaynum());
            nowDto.setCreateip(CommonUtil.getSession("ip"));
            chgDto.setDisplaynum(nowDisplayNum);
            chgDto.setCreateid(CommonUtil.getSession("adminid"));
            chgDto.setCreateip(CommonUtil.getSession("ip"));

            categoryMapper.update_category2_displaynum(nowDto);
            categoryMapper.update_category2_displaynum(chgDto);
        }
    }
}
