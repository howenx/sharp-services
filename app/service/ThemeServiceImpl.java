package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.*;
import mapper.ThemeMapper;
import play.Logger;
import play.libs.Json;
import scala.util.parsing.json.JSONObject$;

import javax.inject.Inject;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * implements Service and transaction processing.
 * Created by howen on 15/10/26.
 */
public class ThemeServiceImpl implements ThemeService{

    @Inject
    private ThemeMapper themeMapper;

    @Override
    public List<ThemeDto> getThemes(int pageSize, int offset) {

        Map<String,Integer> params = new HashMap<String,Integer>();
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        return themeMapper.getThemeIosDto(params);
    }

    @Override
    public List<ThemeListDto> getThemeList(Long themeId) {

        return themeMapper.getThemeListDtoByThemeId(themeId);
    }

    @Override
    public List<Slider> getSlider() {
        return themeMapper.getSlider();
    }

    /**
     * 组装返回详细页面数据
     * @param id 商品主键
     * @return  map
     */
    @Override
    public Map<String,Object> getItemDetail(Long id) {
        Item item = new Item();
        item.setId(id);
//        Logger.error(themeMapper.getItemById(item).toString());

        Map<String,Object> map= new HashMap<>();
        map.put("main",themeMapper.getItemById(item));

        List<Inventory> list = themeMapper.getInvList(item);

        Map<String,Object> colorMap = new HashMap<>();

        List<Inventory> colorList = new ArrayList<>();

        //逻辑:检查除过第一个元素外,其余的每个颜色与上一个进行比较,如果不一样,则拷贝当前list到一个新list,然后放到map中

        for(int i=0;i<list.size();i++){

            colorList.add(list.get(i));

            if(i!=0 && !list.get(i).getItemColor().equals(list.get(i-1).getItemColor())){

                colorList.remove(list.get(i));

                List<Inventory> tempList = new ArrayList<>();
                tempList.addAll(colorList);

                colorMap.put(list.get(i-1).getItemColor(),tempList);

                colorList.clear();
                colorList.add(list.get(i));
            }
            if (i==(list.size()-1)){
                colorMap.put(list.get(i).getItemColor(),colorList);
            }
        }
        map.put("stock",colorMap);
        return map;
    }

}
