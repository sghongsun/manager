package me.sghong.manager.app.manage.service;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.manage.dto.ShopInfoDto;
import me.sghong.manager.app.manage.mapper.ShopInfoMapper;
import me.sghong.manager.app.manage.request.ShopInfoRequest;
import me.sghong.manager.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShopInfoService {
    private final ShopInfoMapper shopInfoMapper;

    public ShopInfoDto getShopInfo() {
        return shopInfoMapper.select_by_shopinfo();
    }

    @Transactional
    public void updateShopInfo(ShopInfoRequest shopInfoRequest) {
        ShopInfoDto shopInfoDto = ShopInfoDto.builder()
                .standardprice(shopInfoRequest.getStandardprice())
                .deliveryprice(shopInfoRequest.getDeliveryprice())
                .returndeliveryprice(shopInfoRequest.getReturndeliveryprice())
                .changedeliveryprice(shopInfoRequest.getChangedeliveryprice())
                .foreignstandardprice(shopInfoRequest.getForeignstandardprice())
                .foreigndeliveryprice(shopInfoRequest.getForeigndeliveryprice())
                .foreignreturndeliveryprice(shopInfoRequest.getForeignreturndeliveryprice())
                .foreignchangedeliveryprice(shopInfoRequest.getForeignchangedeliveryprice())
                .rzipcode(shopInfoRequest.getRzipcode())
                .raddr1(shopInfoRequest.getRaddr1())
                .raddr2(shopInfoRequest.getRaddr2())
                .foreignrzipcode(shopInfoRequest.getForeignrzipcode())
                .foreignraddr1(shopInfoRequest.getForeignraddr1())
                .foreignraddr2(shopInfoRequest.getForeignraddr2())
                .txtreviewpoint(shopInfoRequest.getTxtreviewpoint())
                .imgreviewpoint(shopInfoRequest.getImgreviewpoint())
                .createid(CommonUtil.getSession("adminid"))
                .createip(CommonUtil.getSession("ip"))
                .build();
        shopInfoMapper.update_shopinfo(shopInfoDto);
    }
}
