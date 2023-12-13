package me.sghong.manager.app.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.sghong.manager.ManagerApplication;
import me.sghong.manager.app.common.dto.Pagination;
import me.sghong.manager.app.common.dto.PagingDto;
import me.sghong.manager.app.product.dto.*;
import me.sghong.manager.app.product.mapper.BrandMapper;
import me.sghong.manager.app.product.mapper.CategoryMapper;
import me.sghong.manager.app.product.mapper.ProductMapper;
import me.sghong.manager.app.product.request.ProductAddRequest;
import me.sghong.manager.app.product.request.ProductStateModifyRequest;
import me.sghong.manager.exception.CustomException;
import me.sghong.manager.util.CommonUtil;
import me.sghong.manager.util.Constants;
import me.sghong.manager.util.FileUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.impl.FileUploadIOException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final BrandMapper brandMapper;

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
    public String insertProductExcel(MultipartFile fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "엑셀 파일을 등록하여 주세요.";
        }

        if (!FileUtil.validIFileMimetype(fileName, "xls")) {
            return "엑셀 파일을 등록하여 주세요.";
        }

        try {
            //Temp Table 초기화
            productMapper.delete_product_excel_temp_by_createid(CommonUtil.getSession("adminid"));

            Workbook workbook = null;

            if (StringUtils.getFilenameExtension(fileName.getOriginalFilename()).equals("xlsx")) {
                workbook = new XSSFWorkbook(fileName.getInputStream());
            } else if (StringUtils.getFilenameExtension(fileName.getOriginalFilename()).equals("xls")) {
                workbook = new HSSFWorkbook(fileName.getInputStream());
            }

            assert workbook != null;
            Sheet worksheet = workbook.getSheetAt(0);
            for (int i = 9; i < worksheet.getPhysicalNumberOfRows(); i++) {
                Row row = worksheet.getRow(i);
                ProductExcelAddDto data = new ProductExcelAddDto();

                data.setItemNo(row.getCell(0).getStringCellValue());                                                //ERP 상품코드
                data.setProductname(row.getCell(1).getStringCellValue());                                           //상품명
                data.setCategorycode1(row.getCell(2).getStringCellValue());                                         //대분류코드
                data.setCategorycode2(row.getCell(3).getStringCellValue());                                         //소분류코드
                data.setMarketingword(row.getCell(4).getStringCellValue());                                         //마케팅문구
                data.setStandard(row.getCell(5).getStringCellValue());                                              //규격
                data.setBrandcode(row.getCell(6).getStringCellValue());                                             //브랜드코드
                data.setOnlineflag(row.getCell(7).getStringCellValue());                                            //온라인전용
                data.setBizflag(row.getCell(8).getStringCellValue());                                               //사업자전용
                data.setFixdelvflag(row.getCell(9).getStringCellValue());

                try {
                    data.setCostprice(Integer.parseInt(String.valueOf(row.getCell(10).getStringCellValue())));      //사입가
                } catch (Exception ex) {
                    data.setCostprice(0);
                    data.setErrcode("A1");
                    data.setErrmsg("사입가 오류 입니다.");
                }

                try {
                    data.setAgencyprice(Integer.parseInt(String.valueOf(row.getCell(11).getStringCellValue())));    //대리점가
                } catch (Exception ex) {
                    data.setAgencyprice(0);
                    data.setErrcode("A1");
                    data.setErrmsg("대리점가 오류 입니다.");
                }

                try {
                    data.setTagprice(Integer.parseInt(String.valueOf(row.getCell(12).getStringCellValue())));       //정상가
                } catch (Exception ex) {
                    data.setTagprice(0);
                    data.setErrcode("A1");
                    data.setErrmsg("정상가 오류 입니다.");
                }

                try {
                    data.setSaleprice(Integer.parseInt(String.valueOf(row.getCell(13).getStringCellValue())));      //판매가
                } catch (Exception ex) {
                    data.setSaleprice(0);
                    data.setErrcode("A1");
                    data.setErrmsg("판매가 오류 입니다.");
                }

                try {
                    data.setEmpprice(Integer.parseInt(String.valueOf(row.getCell(14).getStringCellValue())));      //임직원가
                } catch (Exception ex) {
                    data.setEmpprice(0);
                    data.setErrcode("A1");
                    data.setErrmsg("임직원가 오류 입니다.");
                }

                try {
                    data.setBizprice(Integer.parseInt(String.valueOf(row.getCell(15).getStringCellValue())));      //사업자가
                } catch (Exception ex) {
                    data.setBizprice(0);
                    data.setErrcode("A1");
                    data.setErrmsg("사업자가 오류 입니다.");
                }

                try {
                    data.setQty(Integer.parseInt(String.valueOf(row.getCell(16).getStringCellValue())));           //묶음수량
                } catch (Exception ex) {
                    data.setQty(0);
                    data.setErrcode("A1");
                    data.setErrmsg("묶음수량 오류 입니다.");
                }

                try {
                    data.setMinsalecnt(Integer.parseInt(String.valueOf(row.getCell(17).getStringCellValue())));    //최소판매수량
                } catch (Exception ex) {
                    data.setMinsalecnt(0);
                    data.setErrcode("A1");
                    data.setErrmsg("최소판매수량 오류 입니다.");
                }

                try {
                    data.setMaxsalecnt(Integer.parseInt(String.valueOf(row.getCell(18).getStringCellValue())));    //최대판매수량
                } catch (Exception ex) {
                    data.setMaxsalecnt(0);
                    data.setErrcode("A1");
                    data.setErrmsg("최대판매수량 오류 입니다.");
                }

                data.setKeyword(row.getCell(19).getStringCellValue());                                             //검색키워드
                data.setDescription(row.getCell(20).getStringCellValue());                                         //상세설명

                if (data.getErrcode() == null) {
                    try {
                        data.setImage1(row.getCell(21).getStringCellValue());                                      //대표이미지

                        if (!data.getImage1().isEmpty() && !FileUtil.FileExists("/update/product_img_temp/" + data.getImage1())) {
                            data.setErrcode("I1");
                            data.setErrmsg("대표이미지 파일 없음 오류 입니다.");
                        }
                    } catch (Exception ex) {
                        data.setImage1("");
                    }
                }

                if (data.getErrcode() == null) {
                    try {
                        data.setImage2(row.getCell(22).getStringCellValue());                                       //보조이미지1

                        if (!data.getImage2().isEmpty() && !FileUtil.FileExists("/update/product_img_temp/" + data.getImage2())) {
                            data.setErrcode("I2");
                            data.setErrmsg("보조 이미지1 파일 없음 오류 입니다.");
                        }
                    } catch (Exception ex) {
                        data.setImage2("");
                    }
                }

                if (data.getErrcode() == null) {
                    try {
                        data.setImage3(row.getCell(23).getStringCellValue());                                       //보조이미지2

                        if (!data.getImage3().isEmpty() && !FileUtil.FileExists("/update/product_img_temp/" + data.getImage3())) {
                            data.setErrcode("I3");
                            data.setErrmsg("보조 이미지2 파일 없음 오류 입니다.");
                        }
                    } catch (Exception ex) {
                        data.setImage3("");
                    }
                }

                data.setProductgubn(row.getCell(24).getStringCellValue());                                         //관리형태 : 1.위탁상품 2.제조상품 3.사입상품 4.직영상품
                data.setProducttype(row.getCell(25).getStringCellValue());                                         //정보고시제품정보

                ArrayList<String> gosi = new ArrayList<>();
                for (int gosicnt=0; gosicnt<33; gosicnt++) {
                    try {
                        gosi.add(row.getCell(26 + gosicnt).getStringCellValue());                                   //고시항목
                    } catch (Exception ex) {
                        gosi.add("");
                    }
                }
                data.setGosi(gosi);

                try {
                    data.setMaker(row.getCell(27).getStringCellValue());                                            //제조사
                } catch (Exception ex) {
                    data.setMaker("");
                }

                try {
                    data.setOrigin(row.getCell(28).getStringCellValue());                                           //제조국(원산지)
                } catch (Exception ex) {
                    data.setOrigin("");
                }

                data.setReleasecentercode("001");                                                                          //출고지
                data.setEmpflag("N");                                                                                      //임직원전용
                data.setFreedelvflag("N");                                                                                 //무료배송여부
                data.setTaxtype("V");                                                                                      //과세/면세구분(V:과세, E:면세)
                data.setSalestate("N");                                                                                    //판매상태
                data.setOffflag("Y");                                                                                      //품절상태

                if (data.getBizflag().equals("N")) {
                    data.setBizprice(data.getSaleprice());
                }

                if (data.getQty() == 1) {
                    data.setProductclass("P");
                } else {
                    data.setProductclass("S");
                }

                data.setDescription(data.getDescription().replace("http://", "https://"));

                if (data.getErrcode() == null && data.getProducttype().length() != 3) {
                    data.setErrcode("B1");
                    data.setErrmsg("정보고시코드 형식 오류 입니다.");
                }

                if (data.getErrcode() == null && !data.getProductgubn().equals("1") && !data.getProductgubn().equals("2") && !data.getProductgubn().equals("3") && !data.getProductgubn().equals("4")) {
                    data.setErrcode("B2");
                    data.setErrmsg("관리형태 오류 입니다.");
                }

                if (data.getErrcode() == null && categoryMapper.select_by_category1(data.getCategorycode1()) == null) {
                    data.setErrcode("C1");
                    data.setErrmsg("대분류코드 없음 오류 입니다.");
                }

                if (data.getErrcode() == null && categoryMapper.select_by_category2(data.getCategorycode1(), data.getCategorycode2()) == null) {
                    data.setErrcode("C2");
                    data.setErrmsg("소분류코드 없음 오류 입니다.");
                }

                if (data.getErrcode() == null && brandMapper.select_by_brandcode(data.getBrandcode()) == null) {
                    data.setErrcode("C3");
                    data.setErrmsg("브랜드코드 없음 오류 입니다.");
                }

                //세트상품의 가격 비교
                int unitproductcode = 0;
                if (data.getErrcode() == null && data.getProductclass().equals("S")) {
                    ProductDto productDto = productMapper.select_product_by_itemNo(data.getItemNo());
                    if (productDto != null) {
                        data.setReleasecentercode(productDto.getReleasecentercode());
                        unitproductcode = productDto.getProductcode();

                        if (productDto.getTagprice() * data.getQty() == productDto.getTagprice()) {
                            data.setErrcode("E1");
                            data.setErrmsg("세트상품 정상가 단품과 동일 오류 입니다.");
                        } else if (data.getSaleprice() == productDto.getSaleprice()) {
                            data.setErrcode("E1");
                            data.setErrmsg("세트상품 판매가 단품과 동일 오류 입니다.");
                        } else if (data.getEmpprice() == productDto.getEmpprice()) {
                            data.setErrcode("E1");
                            data.setErrmsg("세트상품 임직원가 단품과 동일 오류 입니다.");
                        } else if (data.getBizprice() == productDto.getBizprice()) {
                            data.setErrcode("E1");
                            data.setErrmsg("세트상품 사업자가 단품과 동일 오류 입니다.");
                        }
                    } else {
                        data.setErrcode("E0");
                        data.setErrmsg("미등록된 단품상품 코드 오류 입니다.");
                    }
                }

                //단일상품 중 이미 등록된 상품코드(ITEM_NO)
                if (data.getErrcode() == null && data.getQty() == 1 && productMapper.select_product_by_itemNo(data.getItemNo()) != null) {
                    data.setErrcode("E2");
                    data.setErrmsg("이미 등록된 상품 코드 오류 입니다.");
                }

                //상품 고시 확인
                if (data.getErrcode() == null && productMapper.select_product_gosi_category1_by_code1(data.getProducttype()) == null) {
                    data.setErrcode("E3");
                    data.setErrmsg("없는 고시코드 정보 오류 입니다.");
                }

                if (data.getErrcode() == null || data.getErrcode().isEmpty()) {
                    data.setErrcode("00");
                    data.setErrmsg("");
                }
                data.setCreateid(CommonUtil.getSession("adminid"));
                data.setCreateip(CommonUtil.getSession("ip"));

                //Temp Data Insert
                productMapper.insert_product_excel_temp_by_createid(data);

                if (data.getErrcode().equals("00")) {
                    //상품정보
                    ProductDto productDto = ProductDto.builder()
                            .itemNo(data.getItemNo())
                            .categorycode1(data.getCategorycode1())
                            .categorycode2(data.getCategorycode2())
                            .productclass(data.getProductclass())
                            .marketingword(data.getMarketingword())
                            .productname(data.getProductname())
                            .standard(data.getStandard())
                            .onlineflag(data.getOnlineflag())
                            .empflag(data.getEmpflag())
                            .bizflag(data.getBizflag())
                            .costprice(data.getCostprice())
                            .agencyprice(data.getAgencyprice())
                            .tagprice(data.getTagprice())
                            .saleprice(data.getSaleprice())
                            .empprice(data.getEmpprice())
                            .bizprice(data.getBizprice())
                            .minsalecnt(data.getMinsalecnt())
                            .maxsalecnt(data.getMaxsalecnt())
                            .salestate(data.getSalestate())
                            .offflag("Y")
                            .brandcode(data.getBrandcode())
                            .keyword(data.getKeyword())
                            .description(data.getDescription())
                            .fixdelvflag(data.getFixdelvflag())
                            .freedelvflag(data.getFreedelvflag())
                            .taxtype(data.getTaxtype())
                            .producttype(data.getProducttype())
                            .productgubn(data.getProductgubn())
                            .maker(data.getMaker())
                            .origin(data.getOrigin())
                            .releasecentercode(data.getReleasecentercode())
                            .createid(CommonUtil.getSession("adminid"))
                            .createip(CommonUtil.getSession("ip"))
                            .build();
                    productMapper.insert_product(productDto);
                    int productCode = productDto.getProductcode();

                    if (data.getProductclass().equals("S")) {
                        productMapper.update_product_by_productcode_for_itemno_set(productCode, "S"+productCode);
                    }

                    //단품의 경우 unit 상품코드
                    if (unitproductcode == 0) {
                        unitproductcode = productCode;
                    }

                    //고시정보
                    insertProductGosi(productCode, data.getProducttype(), data.getGosi());

                    //유닛정보
                    ProductUnitDto productUnitDto = ProductUnitDto.builder()
                            .productcode(productCode)
                            .unitproductcode(unitproductcode)
                            .qty(data.getQty())
                            .unitsaleprice(data.getSaleprice())
                            .unitempprice(data.getEmpprice())
                            .unitbizprice(data.getBizprice())
                            .standardflag("Y")
                            .createid(CommonUtil.getSession("adminid"))
                            .createip(CommonUtil.getSession("ip"))
                            .build();
                    productMapper.insert_product_unit(productUnitDto);

                    //재고정보
                    if (data.getProductclass().equals("P")) {
                        ProductStockDto productStockDto = ProductStockDto.builder()
                                .productcode(productCode)
                                .itemNo(data.getItemNo())
                                .stockqty(0)
                                .saleqty(0)
                                .outsaleqty(0)
                                .restqty(0)
                                .createid(CommonUtil.getSession("adminid"))
                                .createip(CommonUtil.getSession("ip"))
                                .build();
                        productMapper.insert_product_stock(productStockDto);
                    }
                    insertProductHistory(productCode);
                    FileUtil.uploadFile(fileName, "excelupload/product_add");
                }
            }
            return "OK";
        } catch (Exception ex) {
            System.out.println(ex.toString());
            FileUtil.uploadFile(fileName, "excelupload/product_add");
            throw new CustomException("엑셀 파일 업로드 오류 입니다.", "history.back();");
        }
    }

    public PagingDto<ProductExcelAddErrorDto> getProductExcelAddErrorList(ProductExcelAddErrorSearchDto productExcelAddErrorSearchDto) {
        productExcelAddErrorSearchDto.setCreateid(CommonUtil.getSession("adminid"));
        int count = productMapper.select_product_excel_temp_by_createid_for_totalcount(productExcelAddErrorSearchDto);

        if (count < 1) {
            return new PagingDto<>(Collections.emptyList(), null);
        }

        Pagination pagination = new Pagination(count, productExcelAddErrorSearchDto);
        productExcelAddErrorSearchDto.setPagination(pagination);

        List<ProductExcelAddErrorDto> list = productMapper.select_product_excel_temp_by_createid_for_list(productExcelAddErrorSearchDto);
        return new PagingDto<>(list, pagination);
    }

    public List<ProductExcelAddErrorDto> getProductExcelAddErrorExcelDown() {
        return productMapper.select_product_excel_temp_by_createid_for_exceldown(CommonUtil.getSession("adminid"));
    }

    @Transactional
    public void insertProductHistory(int productCode) {
        productMapper.insert_product_history_by_productcode(productCode);
    }
}
