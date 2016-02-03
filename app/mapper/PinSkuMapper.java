package mapper;

import domain.PinSku;
import domain.PinTieredPrice;

import java.util.List;

/**
 *
 * Created by tiffany on 16/1/20.
 */
public interface PinSkuMapper {

    /**
     *通过ID获取拼购    Added by Tiffany Zhu 2016.01.22
     * @param pinId pinId
     * @return PinSku
     */
    PinSku getPinSkuById(Long pinId);

    List<PinTieredPrice> getTieredPriceByPinId(Long pinId);
}
