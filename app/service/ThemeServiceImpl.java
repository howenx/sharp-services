package service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.Application;
import domain.*;
import mapper.ThemeMapper;
import play.Logger;
import play.libs.Json;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * implements Service and transaction processing.
 * Created by howen on 15/10/26.
 */
public class ThemeServiceImpl implements ThemeService {

    @Inject
    private ThemeMapper themeMapper;

    @Override
    public List<ThemeDto> getThemes(int pageSize, int offset) {

        Map<String, Integer> params = new HashMap<String, Integer>();
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
     *
     * @param id 商品主键
     * @return map
     */
    @Override
    public Map<String, Object> getItemDetail(Long id, Long skuId) {
        Item item = new Item();
        item.setId(id);

        Map<String, Object> map = new HashMap<>();
        item = themeMapper.getItemById(item);

        //将Json字符串转成list
        List<String> itemDetailImgsList =new ArrayList<String>(json2List(item.getItemDetailImgs(),ArrayList.class,List.class,String.class));

        //使用Java8 Stream写法,增加图片地址前缀
        item.setItemDetailImgs(itemDetailImgsList.stream().map((s) -> Application.IMAGE_URL+s).collect(Collectors.toList()).toString());

        map.put("main", item);

        //遍历库存list 对其进行相应的处理
        List<Inventory> list = themeMapper.getInvList(item).stream().map(l -> {

            //拼接sku链接
            if (null != l.getInvUrl() && !"".equals(l.getInvUrl())) {
                l.setInvUrl(controllers.Application.DEPLOY_URL + l.getInvUrl());
            } else {
                l.setInvUrl(controllers.Application.DEPLOY_URL + "/comm/detail/" + id + "/" + l.getId());
            }
            //列表页跳转到详细页面
            Integer flag = -1;

            //判断是否是当前需要显示的sku
            if (!skuId.equals(flag.longValue()) && !l.getId().equals(skuId)) {
                l.setOrMasterInv(false);
            } else if (l.getId().equals(skuId)) {
                l.setOrMasterInv(true);
            }

            //将Json字符串转成list
            List<String> previewList =new ArrayList<String>(json2List(l.getItemPreviewImgs(),ArrayList.class,List.class,String.class));

            //使用Java8 Stream写法,增加图片地址前缀
            l.setItemPreviewImgs(previewList.stream().map((s) -> Application.IMAGE_URL+s).collect(Collectors.toList()).toString());

            return l;

        }).collect(Collectors.toList());

        map.put("stock", list);
        return map;
    }

    //将Json串转换成List
    final static ObjectMapper mapper = new ObjectMapper();

    public static <T> List<T> json2List(String json,Class<?> parametrized, Class<?> parametersFor,
                                           Class<?>... parameterClasses){
        List<T> lst = new ArrayList<>();
        try {
                lst = mapper.readValue(json, mapper.getTypeFactory().constructParametrizedType(parametrized,parametersFor, parameterClasses));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lst;
    }

}
