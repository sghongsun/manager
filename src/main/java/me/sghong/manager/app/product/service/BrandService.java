package me.sghong.manager.app.product.service;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.common.dto.Pagination;
import me.sghong.manager.app.common.dto.PagingDto;
import me.sghong.manager.app.product.dto.BrandDto;
import me.sghong.manager.app.product.dto.BrandSearchDto;
import me.sghong.manager.app.product.mapper.BrandMapper;
import me.sghong.manager.app.product.request.BrandAddRequest;
import me.sghong.manager.app.product.request.BrandDisplayNumModifyRequest;
import me.sghong.manager.app.product.request.BrandModifyRequest;
import me.sghong.manager.app.product.request.BrandUseFlagModifyRequest;
import me.sghong.manager.util.CommonUtil;
import me.sghong.manager.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class BrandService {
    private final BrandMapper brandMapper;

    public PagingDto<BrandDto> getList(BrandSearchDto brandSearchDto) {
        int count = brandMapper.select_by_list_for_totalcount(brandSearchDto);

        if (count < 1) {
            return new PagingDto<>(Collections.emptyList(), null);
        }

        Pagination pagination = new Pagination(count, brandSearchDto);
        brandSearchDto.setPagination(pagination);

        List<BrandDto> brandDtoList = brandMapper.select_by_list_for_search(brandSearchDto);
        return new PagingDto<>(brandDtoList, pagination);
    }

    @Transactional
    public String insertBrand(BrandAddRequest brandAddRequest) {
        String logoImg = "";
        if (brandAddRequest.getLogoImg() != null && !brandAddRequest.getLogoImg().isEmpty()) {
            if (!FileUtil.validIFileMimetype(brandAddRequest.getLogoImg(), "image")) {
                return "이미지 파일만 등록 가능합니다.";
            }
            logoImg = Objects.requireNonNull(FileUtil.uploadFile(brandAddRequest.getLogoImg(), "brand")).getSaveFileName();
        }

        String visualImg = "";
        if (brandAddRequest.getVisualImg() != null && !brandAddRequest.getVisualImg().isEmpty()) {
            if (!FileUtil.validIFileMimetype(brandAddRequest.getVisualImg(), "image")) {
                return "이미지 파일만 등록 가능합니다.";
            }
            visualImg = Objects.requireNonNull(FileUtil.uploadFile(brandAddRequest.getVisualImg(), "brand")).getSaveFileName();
        }

        String m_logoImg = "";
        if (brandAddRequest.getMlogoImg() != null && !brandAddRequest.getMlogoImg().isEmpty()) {
            if (!FileUtil.validIFileMimetype(brandAddRequest.getMlogoImg(), "image")) {
                return "이미지 파일만 등록 가능합니다.";
            }
            m_logoImg = Objects.requireNonNull(FileUtil.uploadFile(brandAddRequest.getMlogoImg(), "brand")).getSaveFileName();
        }

        String m_visualImg = "";
        if (brandAddRequest.getMvisualImg() != null && !brandAddRequest.getMvisualImg().isEmpty()) {
            if (!FileUtil.validIFileMimetype(brandAddRequest.getMvisualImg(), "image")) {
                return "이미지 파일만 등록 가능합니다.";
            }
            m_visualImg = Objects.requireNonNull(FileUtil.uploadFile(brandAddRequest.getMvisualImg(), "brand")).getSaveFileName();
        }

        if (brandMapper.select_by_brandcode_for_exists(brandAddRequest.getBrandCode()) > 0) {
            return "이미 등록된 브랜드 코드 입니다.";
        }

        int displayNum = brandMapper.select_by_displaynum_max();

        BrandDto brandDto = BrandDto.builder()
                .brandcode(brandAddRequest.getBrandCode())
                .brandname(brandAddRequest.getBrandName())
                .logoimg(logoImg)
                .visualimg(visualImg)
                .mlogoimg(m_logoImg)
                .mvisualimg(m_visualImg)
                .brandstory(brandAddRequest.getBrandStory())
                .displaynum(displayNum)
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();
        brandMapper.insert_brand(brandDto);

        return "OK";
    }

    public BrandDto getInfo(String BrandCode) {
        return brandMapper.select_by_brandcode(BrandCode);
    }

    @Transactional
    public String updateBrand(BrandModifyRequest brandModifyRequest) {
        BrandDto nowDto = brandMapper.select_by_brandcode(brandModifyRequest.getBrandCode());

        String logoImg = nowDto.getLogoimg();
        if (brandModifyRequest.getLogoImg() != null && !brandModifyRequest.getLogoImg().isEmpty()) {
            if (!FileUtil.validIFileMimetype(brandModifyRequest.getLogoImg(), "image")) {
                return "이미지 파일만 등록 가능합니다.";
            }

            if (logoImg != null && !logoImg.isEmpty()) {
                FileUtil.deleteFile(logoImg);
            }

            logoImg = Objects.requireNonNull(FileUtil.uploadFile(brandModifyRequest.getLogoImg(), "brand")).getSaveFileName();
        }

        String visualImg = nowDto.getVisualimg();
        if (brandModifyRequest.getVisualImg() != null && !brandModifyRequest.getVisualImg().isEmpty()) {
            if (!FileUtil.validIFileMimetype(brandModifyRequest.getVisualImg(), "image")) {
                return "이미지 파일만 등록 가능합니다.";
            }

            if (visualImg != null && !visualImg.isEmpty()) {
                FileUtil.deleteFile(visualImg);
            }

            visualImg = Objects.requireNonNull(FileUtil.uploadFile(brandModifyRequest.getVisualImg(), "brand")).getSaveFileName();
        }

        String m_logoImg = nowDto.getMlogoimg();
        if (brandModifyRequest.getMlogoImg() != null && !brandModifyRequest.getMlogoImg().isEmpty()) {
            if (!FileUtil.validIFileMimetype(brandModifyRequest.getMlogoImg(), "image")) {
                return "이미지 파일만 등록 가능합니다.";
            }

            if (m_logoImg != null && !m_logoImg.isEmpty()) {
                FileUtil.deleteFile(m_logoImg);
            }

            m_logoImg = Objects.requireNonNull(FileUtil.uploadFile(brandModifyRequest.getMlogoImg(), "brand")).getSaveFileName();
        }

        String m_visualImg = nowDto.getMvisualimg();
        if (brandModifyRequest.getMvisualImg() != null && !brandModifyRequest.getMvisualImg().isEmpty()) {
            if (!FileUtil.validIFileMimetype(brandModifyRequest.getMvisualImg(), "image")) {
                return "이미지 파일만 등록 가능합니다.";
            }

            if (m_visualImg != null && !m_visualImg.isEmpty()) {
                FileUtil.deleteFile(m_visualImg);
            }

            m_visualImg = Objects.requireNonNull(FileUtil.uploadFile(brandModifyRequest.getMvisualImg(), "brand")).getSaveFileName();
        }

        BrandDto brandDto = BrandDto.builder()
                .brandcode(brandModifyRequest.getBrandCode())
                .brandname(brandModifyRequest.getBrandName())
                .logoimg(logoImg)
                .visualimg(visualImg)
                .mlogoimg(m_logoImg)
                .mvisualimg(m_visualImg)
                .useflag(brandModifyRequest.getUseFlag())
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();
        brandMapper.update_brand(brandDto);

        return "OK";
    }

    @Transactional
    public String updateBrandUseFlag(BrandUseFlagModifyRequest brandUseFlagModifyRequest) {
        String[] brandCode = brandUseFlagModifyRequest.getBrandCode().split(",");
        String[] useFlag = brandUseFlagModifyRequest.getUseFlag().split(",");

        if (brandCode.length != useFlag.length) {
            return "자료가 정확하지 않습니다.";
        }

        for (int i=0; i<brandCode.length; i++) {
            BrandDto brandDto = BrandDto.builder()
                    .brandcode(brandCode[i])
                    .useflag(useFlag[i])
                    .createid(CommonUtil.getSession("adminid"))
                    .createip(CommonUtil.getSession("ip"))
                    .build();
            brandMapper.update_brand_useflag(brandDto);
        }

        return "OK";
    }

    public List<BrandDto> getListAll() {
        return brandMapper.select_by_list_for_all();
    }

    @Transactional
    public void updateBrandDisplayNum(BrandDisplayNumModifyRequest brandDisplayNumModifyRequest) {
        BrandDto nowDto = brandMapper.select_by_brandcode(brandDisplayNumModifyRequest.getBrandCode());
        nowDto.setCreateid(CommonUtil.getSession("adminid"));
        nowDto.setCreateip(CommonUtil.getSession("ip"));

        if (brandDisplayNumModifyRequest.getModType().equals("first") || brandDisplayNumModifyRequest.getModType().equals("last")) {
            switch (brandDisplayNumModifyRequest.getModType()) {
                case "first" -> brandMapper.update_brand_for_displaynum_first(nowDto);
                case "last" -> brandMapper.update_brand_for_displaynum_last(nowDto);
            };
        } else {
            BrandDto chgDto = switch (brandDisplayNumModifyRequest.getModType()) {
                case "up" -> brandMapper.select_brand_for_displaynum_up(nowDto.getDisplaynum());
                case "down" -> brandMapper.select_brand_for_displaynum_dn(nowDto.getDisplaynum());
                default -> null;
            };

            if (chgDto != null) {
                int nowDisplayNum = nowDto.getDisplaynum();
                nowDto.setDisplaynum(chgDto.getDisplaynum());
                chgDto.setDisplaynum(nowDisplayNum);
                chgDto.setCreateid(nowDto.getCreateid());
                chgDto.setCreateip(nowDto.getCreateip());
                brandMapper.update_brand_displaynum(nowDto);
                brandMapper.update_brand_displaynum(chgDto);
            }
        }
    }
}
