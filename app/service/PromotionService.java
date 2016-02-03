package service;

import com.fasterxml.jackson.databind.JsonNode;
import domain.PinSku;
import domain.PinTieredPrice;
import domain.Slider;
import domain.Theme;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * For homepage theme list display function.
 * Created by howen on 15/10/26.
 */
public interface PromotionService {

    PinSku getPinSkuById(Long pinId);

    List<PinTieredPrice> getTieredPriceByPinId(Long pinId);
}
