package me.sghong.manager.app.manage.mapper;

import me.sghong.manager.app.manage.dto.ShopInfoDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopInfoMapper {
    ShopInfoDto select_by_shopinfo();
    void update_shopinfo(ShopInfoDto shopInfoDto);
}
