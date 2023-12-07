package me.sghong.manager.app.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.common.dto.Pagination;
import me.sghong.manager.app.common.dto.PagingDto;
import me.sghong.manager.app.product.dto.*;
import me.sghong.manager.app.product.mapper.ProductMapper;
import me.sghong.manager.app.product.request.ProductAddRequest;
import me.sghong.manager.app.product.request.ProductStateModifyRequest;
import me.sghong.manager.exception.CustomException;
import me.sghong.manager.util.CommonUtil;
import me.sghong.manager.util.Constants;
import me.sghong.manager.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductMapper productMapper;

    public PagingDto<ProductListDto> getProductList(ProductSearchDto productSearchDto) {
        if (productSearchDto.getKeyword() != null && !productSearchDto.getKeyword().isEmpty()) {
            productSearchDto.setKeywords(productSearchDto.getKeyword().split("\r\n"));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> map = objectMapper.convertValue(productSearchDto, HashMap.class);

        int count = productMapper.select_product_by_list_for_totalcount(map);

        if (count < 1) {
            return new PagingDto<>(Collections.emptyList(), null);
        }

        Pagination pagination = new Pagination(count, productSearchDto);
        productSearchDto.setPagination(pagination);

        map.put("limitstart", pagination.getLimitStart());

        List<ProductListDto> list = productMapper.select_product_by_list(map);
        return new PagingDto<>(list, pagination);
    }

    public List<ReleasecenterDto> getReleaseCenterList() {
        return productMapper.select_releasecenter_by_list_for_use();
    }

    public List<ProductGosiCategoryDto> getProductGosiCategory1List() {
        return productMapper.select_product_gosi_category1_by_list_for_use();
    }

    public ProductDto getProductInfo(int productCode) {
        return productMapper.select_product_by_productcode(productCode);
    }

    @Transactional
    public String updateProductState(ProductStateModifyRequest productStateModifyRequest) {
        String[] productCodes = productStateModifyRequest.getProductCode().split(",");
        String[] offFlags = productStateModifyRequest.getOffFlag().split(",");
        String[] saleStates = productStateModifyRequest.getSaleState().split(",");

        if (productCodes.length != offFlags.length || productCodes.length != saleStates.length) {
            return "자료가 정확하지 않습니다.";
        }

        for (int i=0; i<productCodes.length; i++) {
            ProductDto productDto = ProductDto.builder()
                    .productcode(Integer.parseInt(productCodes[i]))
                    .offflag(offFlags[i])
                    .salestate(saleStates[i])
                    .createid(CommonUtil.getSession("adminid"))
                    .createip(CommonUtil.getSession("ip"))
                    .build();
            productMapper.update_product_by_productcode_for_state(productDto);
            insertProductHistory(Integer.parseInt(productCodes[i]));
        }

        return "OK";
    }

    public List<ProductGosiDto> getProductGosiList(String productCode, String productType) {
        if (productCode == null || productCode.isEmpty()) {
            return productMapper.select_product_gosi_category2_by_code1(productType);
        } else {
            return productMapper.select_products_gosi_by_productcode(Integer.parseInt(productCode));
        }
    }

    @Transactional
    public String insertProduct(ProductAddRequest productAddRequest) {
        if (productAddRequest.getProductclass().equals("P") &&
                productMapper.select_product_by_itemNo(productAddRequest.getItemNo()) != null) {
            return "이미 등록된 ERP코드 입니다.";
        }
        if (productAddRequest.getImage1() == null || productAddRequest.getImage1().isEmpty()) {
            return "대표 이미지 파일을 선택하여 주세요.";
        }
        if (!FileUtil.validIFileMimetype(productAddRequest.getImage1(), "image")) {
            return "이미지 파일만 등록 가능합니다.";
        }
        if (productAddRequest.getImage2() != null && !productAddRequest.getImage2().isEmpty()) {
            if (!FileUtil.validIFileMimetype(productAddRequest.getImage2(), "image")) {
                return "이미지 파일만 등록 가능합니다.";
            }
        }
        if (productAddRequest.getImage3() != null && !productAddRequest.getImage3().isEmpty()) {
            if (!FileUtil.validIFileMimetype(productAddRequest.getImage3(), "image")) {
                return "이미지 파일만 등록 가능합니다.";
            }
        }

        String OnlineFlag = productAddRequest.getOnlineflag();
        if (OnlineFlag == null || OnlineFlag.isEmpty()) {
            OnlineFlag = "N";
        }
        String EmpFlag = productAddRequest.getEmpflag();
        if (EmpFlag == null || EmpFlag.isEmpty()) {
            EmpFlag = "N";
        }
        String BizFlag = productAddRequest.getBizflag();
        if (BizFlag == null || BizFlag.isEmpty()) {
            BizFlag = "N";
        }

        //상품정보
        ProductDto productDto = ProductDto.builder()
                .itemNo(productAddRequest.getItemNo())
                .categorycode1(productAddRequest.getCategorycode1())
                .categorycode2(productAddRequest.getCategorycode2())
                .productclass(productAddRequest.getProductclass())
                .marketingword(productAddRequest.getMarketingword())
                .productname(productAddRequest.getProductname())
                .standard(productAddRequest.getStandard())
                .onlineflag(OnlineFlag)
                .empflag(EmpFlag)
                .bizflag(BizFlag)
                .costprice(productAddRequest.getCostprice())
                .agencyprice(productAddRequest.getAgencyprice())
                .tagprice(productAddRequest.getTagprice())
                .saleprice(productAddRequest.getSaleprice())
                .empprice(productAddRequest.getEmpprice())
                .bizprice(productAddRequest.getBizprice())
                .minsalecnt(productAddRequest.getMinsalecnt())
                .maxsalecnt(productAddRequest.getMaxsalecnt())
                .salestate(productAddRequest.getSalestate())
                .offflag("Y")
                .brandcode(productAddRequest.getBrandcode())
                .keyword(productAddRequest.getKeyword())
                .description(productAddRequest.getDescription())
                .fixdelvflag(productAddRequest.getFixdelvflag())
                .freedelvflag(productAddRequest.getFreedelvflag())
                .taxtype(productAddRequest.getTaxtype())
                .producttype(productAddRequest.getProducttype())
                .productgubn(productAddRequest.getProductgubn())
                .maker(productAddRequest.getMaker())
                .origin(productAddRequest.getOrigin())
                .releasecentercode(productAddRequest.getReleasecentercode())
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();
        productMapper.insert_product(productDto);
        int productCode = productDto.getProductcode();

        //고시정보
        insertProductGosi(productCode, productAddRequest.getProducttype(), productAddRequest.getGosi());

        switch (productAddRequest.getProductclass()) {
            case "P" -> {
                //유닛정보
                ProductUnitDto productUnitDto = ProductUnitDto.builder()
                        .productcode(productCode)
                        .unitproductcode(productCode)
                        .qty(1)
                        .unitsaleprice(productAddRequest.getSaleprice())
                        .unitempprice(productAddRequest.getEmpprice())
                        .unitbizprice(productAddRequest.getBizprice())
                        .standardflag("Y")
                        .createid(CommonUtil.getSession("adminid"))
                        .createip(CommonUtil.getSession("ip"))
                        .build();
                productMapper.insert_product_unit(productUnitDto);

                //재고정보
                ProductStockDto productStockDto = ProductStockDto.builder()
                        .productcode(productCode)
                        .itemNo(productAddRequest.getItemNo())
                        .stockqty(0)
                        .saleqty(0)
                        .outsaleqty(0)
                        .restqty(0)
                        .createid(CommonUtil.getSession("adminid"))
                        .createip(CommonUtil.getSession("ip"))
                        .build();
                productMapper.insert_product_stock(productStockDto);
            }
            case "S" -> {
                if (productAddRequest.getSetproductcodes() == null || productAddRequest.getSetproductcodes().isEmpty()) {
                    throw new CustomException("세트상품의 세부 상품코드가 없습니다.", "history.back();");
                }

                productMapper.update_product_by_productcode_for_itemno_set(productCode, "S"+productCode);

                int sumSetUnitQtys = 0;
                String[] SetProductCodes = productAddRequest.getSetproductcodes().split(",");
                String[] SetUnitSalePrices = productAddRequest.getSetunitsaleprices().split(",");
                String[] SetUnitEmpPrices = productAddRequest.getSetunitempprices().split(",");
                String[] SetUnitBizPrices = productAddRequest.getSetunitbizprices().split(",");
                String[] SetUnitQtys = productAddRequest.getSetunitqtys().split(",");

                if (SetProductCodes.length != SetUnitSalePrices.length) {
                    throw new CustomException("세트상품 수와 판매가의 수가 일치 하지 않습니다.", "history.back();");
                }
                if (SetProductCodes.length != SetUnitQtys.length) {
                    throw new CustomException("세트상품 수와 상품수량의 수가 일치 하지 않습니다.", "history.back();");
                }
                for (String setUnitQty : SetUnitQtys) {
                    sumSetUnitQtys += Integer.parseInt(setUnitQty);
                }
                if (sumSetUnitQtys < 2) {
                    throw new CustomException("세트상품의 상품수량이 2개 이상이어야 합니다.", "history.back();");
                }

                for (int i=0; i<SetProductCodes.length; i++) {
                    if (productMapper.select_product_by_productcode(Integer.parseInt(SetProductCodes[i])) == null) {
                        throw new CustomException("[" + SetProductCodes[i] + "] 상품코드는 없는 상품입니다.", "history.back();");
                    }

                    String StandardFlag = "N";
                    if (i == 0) StandardFlag = "Y";

                    //유닛정보
                    ProductUnitDto productUnitDto = ProductUnitDto.builder()
                            .productcode(productCode)
                            .unitproductcode(Integer.parseInt(SetProductCodes[i]))
                            .qty(Integer.parseInt(SetUnitQtys[i]))
                            .unitsaleprice(Integer.parseInt(SetUnitSalePrices[i]))
                            .unitempprice(Integer.parseInt(SetUnitEmpPrices[i]))
                            .unitbizprice(Integer.parseInt(SetUnitBizPrices[i]))
                            .standardflag(StandardFlag)
                            .createid(CommonUtil.getSession("adminid"))
                            .createip(CommonUtil.getSession("ip"))
                            .build();
                    productMapper.insert_product_unit(productUnitDto);
                }
            }
            case "G" -> {
                if (productAddRequest.getGroupproductcodes() == null || productAddRequest.getGroupproductcodes().isEmpty()) {
                    throw new CustomException("묶음상품의 세부 상품코드가 없습니다.", "history.back();");
                }

                if (productAddRequest.getGroupproductcodes().split(",").length < 1) {
                    throw new CustomException("묶음상품은 상품이 2개 이상이어야 합니다.", "history.back();");
                }

                productMapper.update_product_by_productcode_for_itemno_set(productCode, "G"+productCode);

                String[] Groupproductcodes = productAddRequest.getGroupproductcodes().split(",");
                String[] Groupstandardflags = productAddRequest.getGroupstandardflags().split(",");

                for (int i=0; i<Groupproductcodes.length; i++) {
                    if (productMapper.select_product_by_productcode(Integer.parseInt(Groupproductcodes[i])) == null) {
                        throw new CustomException("[" + Groupproductcodes[i] + "] 상품코드는 없는 상품입니다.", "history.back();");
                    }

                    //유닛정보
                    ProductUnitDto productUnitDto = ProductUnitDto.builder()
                            .productcode(productCode)
                            .unitproductcode(Integer.parseInt(Groupproductcodes[i]))
                            .qty(1)
                            .unitsaleprice(productAddRequest.getSaleprice())
                            .unitempprice(productAddRequest.getEmpprice())
                            .unitbizprice(productAddRequest.getBizprice())
                            .standardflag(Groupstandardflags[i])
                            .createid(CommonUtil.getSession("adminid"))
                            .createip(CommonUtil.getSession("ip"))
                            .build();
                    productMapper.insert_product_unit(productUnitDto);
                }
            }
        }

        //이미지
        if (productAddRequest.getImage1() != null && !productAddRequest.getImage1().isEmpty()) {
            String fileName = Objects.requireNonNull(FileUtil.uploadFile(productAddRequest.getImage1(), "product_img")).getSaveFileName();
            for (Integer imageSize : Constants.productimagesize) {
                String imageFileName = FileUtil.createProductImage(fileName, imageSize, imageSize);
                ProductImageDto productImageDto = ProductImageDto.builder()
                        .productcode(productCode)
                        .imageno(1)
                        .sizeclass(CommonUtil.MaketoZero(Integer.toString(imageSize), 4))
                        .imageurl(imageFileName)
                        .createid(CommonUtil.getSession("adminid"))
                        .createip(CommonUtil.getSession("ip"))
                        .build();
                productMapper.insert_product_image(productImageDto);
            }
            FileUtil.deleteFile(fileName);
        }

        if (productAddRequest.getImage2() != null && !productAddRequest.getImage2().isEmpty()) {
            String fileName = Objects.requireNonNull(FileUtil.uploadFile(productAddRequest.getImage2(), "product_img")).getSaveFileName();
            for (Integer imageSize : Constants.productsubimagesize) {
                String imageFileName = FileUtil.createProductImage(fileName, imageSize, imageSize);
                ProductImageDto productImageDto = ProductImageDto.builder()
                        .productcode(productCode)
                        .imageno(2)
                        .sizeclass(CommonUtil.MaketoZero(Integer.toString(imageSize), 4))
                        .imageurl(imageFileName)
                        .createid(CommonUtil.getSession("adminid"))
                        .createip(CommonUtil.getSession("ip"))
                        .build();
                productMapper.insert_product_image(productImageDto);
            }
            FileUtil.deleteFile(fileName);
        }

        if (productAddRequest.getImage3() != null && !productAddRequest.getImage3().isEmpty()) {
            String fileName = Objects.requireNonNull(FileUtil.uploadFile(productAddRequest.getImage3(), "product_img")).getSaveFileName();
            for (Integer imageSize : Constants.productsubimagesize) {
                String imageFileName = FileUtil.createProductImage(fileName, imageSize, imageSize);
                ProductImageDto productImageDto = ProductImageDto.builder()
                        .productcode(productCode)
                        .imageno(3)
                        .sizeclass(CommonUtil.MaketoZero(Integer.toString(imageSize), 4))
                        .imageurl(imageFileName)
                        .createid(CommonUtil.getSession("adminid"))
                        .createip(CommonUtil.getSession("ip"))
                        .build();
                productMapper.insert_product_image(productImageDto);
            }
            FileUtil.deleteFile(fileName);
        }

        insertProductHistory(productCode);
        return "OK";
    }

    @Transactional
    public void insertProductGosi(int productCode, String productType, ArrayList<String> gosi) {
        int gosiCnt = productMapper.select_product_gosi_category2_by_code1_for_count(productType);
        for(int i=1; i<=gosiCnt; i++) {
            ProductGosiDto productGosiDto = ProductGosiDto.builder()
                    .productcode(productCode)
                    .code1(productType)
                    .code2(CommonUtil.MaketoZero(Integer.toString(i), 3))
                    .contents(gosi.get(i-1))
                    .createid(CommonUtil.getSession("adminid"))
                    .createip(CommonUtil.getSession("ip"))
                    .build();
            productMapper.insert_product_gosi(productGosiDto);
        }
    }

    public PagingDto<ProductListDto> getSetProductSearchList(SetProductSearchDto setProductSearchDto) {
        if (setProductSearchDto.getKeyword() != null && !setProductSearchDto.getKeyword().isEmpty()) {
            setProductSearchDto.setKeywords(setProductSearchDto.getKeyword().split("\r\n"));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> map = objectMapper.convertValue(setProductSearchDto, HashMap.class);

        if (setProductSearchDto.getProductcodes() != null && !setProductSearchDto.getProductcodes().isEmpty()) {
            map.put("notinproductcodes", setProductSearchDto.getProductcodes().split(","));
        }

        int count = productMapper.select_product_by_list_for_totalcount(map);

        if (count < 1) {
            return new PagingDto<>(Collections.emptyList(), null);
        }

        Pagination pagination = new Pagination(count, setProductSearchDto);
        setProductSearchDto.setPagination(pagination);

        map.put("limitstart", pagination.getLimitStart());

        List<ProductListDto> list = productMapper.select_product_by_list(map);
        return new PagingDto<>(list, pagination);
    }

    public List<ProductListDto> getSetProductAddList(SetProductSearchDto setProductSearchDto) {
        if (setProductSearchDto.getProductcodes() == null || setProductSearchDto.getProductcodes().isEmpty()) {
            return null;
        }

        String[] productcode = setProductSearchDto.getProductcodes().split(",");
        String[] saleprice = setProductSearchDto.getUnitsaleprices().split(",");
        String[] empprice = setProductSearchDto.getUnitempprices().split(",");
        String[] bizprice = setProductSearchDto.getUnitbizprices().split(",");
        String[] qty = setProductSearchDto.getUnitqtys().split(",");

        List<ProductListDto> productList = productMapper.select_product_by_productcodesin(productcode);

        for (int i=0; i<productcode.length; i++) {
            for (ProductListDto productListDto : productList) {
                if (productcode[i].equals(Integer.toString(productListDto.getProductcode()))) {
                    productListDto.setSaleprice(Integer.parseInt(saleprice[i]));
                    productListDto.setEmpprice(Integer.parseInt(empprice[i]));
                    productListDto.setBizprice(Integer.parseInt(bizprice[i]));
                    productListDto.setQty(Integer.parseInt(qty[i]));
                }
            }
        }

        return productList;
    }

    public String SetProductApplyCheck(SetProductSearchDto setProductSearchDto) {
        if (setProductSearchDto.getProductcodes() == null || setProductSearchDto.getProductcodes().isEmpty()) {
            return "선택된 상품이 없습니다.";
        }

        List<ProductListDto> productList = productMapper.select_product_by_productcodesin(setProductSearchDto.getProductcodes().split(","));
        for (int i=0; i<productList.size(); i++) {
            if (i > 0) {
                if (!productList.get(i).getTaxtype().equals(productList.get(i-1).getTaxtype())) {
                    return "과세상품과 면세상품을 함께 세트상품으로 만들 수 없습니다.";
                }

                if (!productList.get(i).getReleasecentercode().equals(productList.get(i-1).getReleasecentercode())) {
                    return "출고지가 다른 상품을 함께 세트상품으로 만들 수 없습니다.";
                }
            }
        }

        return "OK";
    }

    public String SetProductDupCheck(String ProductCodes, String Qtys) {
        productMapper.delete_product_setproduct_temp_by_createid(CommonUtil.getSession("adminid"));
        String[] productCode = ProductCodes.split(",");
        String[] qty = Qtys.split(",");

        for (int i=0; i<productCode.length; i++) {
            productMapper.insert_product_setproduct_temp_by_createid(
                    Integer.parseInt(productCode[i]),
                    Integer.parseInt(qty[i]),
                    CommonUtil.getSession("adminid"),
                    CommonUtil.getSession("ip"));
        }

        int count = productMapper.select_product_setproduct_temp_by_createid_for_count(CommonUtil.getSession("adminid"));

        if (productMapper.select_product_setproduct_by_createid_for_dupcheck(CommonUtil.getSession("adminid"), count) != null) {
            return "DUP";
        }

        return "OK";
    }

    public PagingDto<ProductListDto> getGroupProductSearchList(GroupProductSearchDto groupProductSearchDto) {
        if (groupProductSearchDto.getKeyword() != null && !groupProductSearchDto.getKeyword().isEmpty()) {
            groupProductSearchDto.setKeywords(groupProductSearchDto.getKeyword().split("\r\n"));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> map = objectMapper.convertValue(groupProductSearchDto, HashMap.class);

        if (groupProductSearchDto.getProductcodes() != null && !groupProductSearchDto.getProductcodes().isEmpty()) {
            map.put("notinproductcodes", groupProductSearchDto.getProductcodes().split(","));
        }

        int count = productMapper.select_product_by_list_for_totalcount(map);

        if (count < 1) {
            return new PagingDto<>(Collections.emptyList(), null);
        }

        Pagination pagination = new Pagination(count, groupProductSearchDto);
        groupProductSearchDto.setPagination(pagination);
        map.put("limitstart", pagination.getLimitStart());

        List<ProductListDto> list = productMapper.select_product_by_list(map);
        return new PagingDto<>(list, pagination);
    }

    public List<ProductListDto> getGroupProductAddList(GroupProductSearchDto groupProductSearchDto) {
        if (groupProductSearchDto.getProductcodes() == null || groupProductSearchDto.getProductcodes().isEmpty()) {
            return null;
        }

        String[] productcode = groupProductSearchDto.getProductcodes().split(",");
        String[] standardflag = groupProductSearchDto.getStandardflags().split(",");

        List<ProductListDto> productList = productMapper.select_product_by_productcodesin(productcode);

        for (int i=0; i<standardflag.length; i++) {
            for (ProductListDto productListDto : productList) {
                if (productcode[i].equals(Integer.toString(productListDto.getProductcode()))) {
                    productListDto.setStandardflag(standardflag[i]);
                }
            }
        }

        return productList;
    }

    public String GroupProductApplyCheck(GroupProductSearchDto groupProductSearchDto) {
        if (groupProductSearchDto.getProductcodes() == null || groupProductSearchDto.getProductcodes().isEmpty()) {
            return "선택된 상품이 없습니다.";
        }
        return "OK";
    }

    @Transactional
    public void insertProductHistory(int productCode) {
        productMapper.insert_product_history_by_productcode(productCode);
    }
}
