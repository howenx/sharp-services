package service;

import domain.PinSku;
import domain.PinTieredPrice;
import mapper.PinSkuMapper;

import javax.inject.Inject;
import java.util.List;

/**
 *
 * Created by howen on 16/1/25.
 */
public class PromotionServiceImpl implements PromotionService {

    @Inject
    private PinSkuMapper pinSkuMapper;

    @Override
    public PinSku getPinSkuById(Long pinId) {
        return pinSkuMapper.getPinSkuById(pinId);
    }

    @Override
    public List<PinTieredPrice> getTieredPriceByPinId(Long pinId) {
        return pinSkuMapper.getTieredPriceByPinId(pinId);
    }
}
