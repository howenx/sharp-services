package service;

import domain.*;
import mapper.ThemeMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import play.Logger;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;

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


    private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    /**
     * 字节数据转十六进制字符串
     *
     * @param data
     *            输入数据
     * @return 十六进制内容
     */
    public static String byteArrayToString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            // 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            // 取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i < data.length - 1) {
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }

    private static final String dCase = "abcdefghijklmnopqrstuvwxyz";
    private static final String uCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String sChar = "_/-+&^=";
    private static final String intChar = "0123456789";
    private static Random r = new Random();
    private static String pass = "";

    @Override
    public Map<String,Object> getItemDetail(Long id,Long skuId) {
        Item item = new Item();
        item.setId(id);
//        Logger.error(themeMapper.getItemById(item).toString());

        Map<String,Object> map= new HashMap<>();
        map.put("main",themeMapper.getItemById(item));

        List<Inventory> list = themeMapper.getInvList(item);

        Map<String,Object> colorMap = new HashMap<>();

        List<Inventory> colorList = new ArrayList<>();

        //test
        SecureRandom random = null;
        random = new SecureRandom();
        byte[] sharedSecret = new byte[32];
        random.nextBytes(sharedSecret);

        String data = new String(sharedSecret, StandardCharsets.UTF_8);
        System.out.println("原始的:"+data);

        System.out.println("UUID:"+UUID.randomUUID().toString().replaceAll("-", ""));

        Random ranGen = new SecureRandom();
        byte[] aesKey = new byte[32];
        ranGen.nextBytes(aesKey);
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < aesKey.length; i++) {
            String hex = Integer.toHexString(0xff & aesKey[i]);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }




        System.out.println("hex:"+hexString);

        System.out.println("16进制的:"+byteArrayToString(sharedSecret));

        data = new String(Base64.encodeBase64(sharedSecret));
        System.out.println("Base64:"+data);


        while (pass.length () != 32){
            int rPick = r.nextInt(4);
            if (rPick == 0){
                int spot = r.nextInt(25);
                pass += dCase.charAt(spot);
            } else if (rPick == 1) {
                int spot = r.nextInt (25);
                pass += uCase.charAt(spot);
            } else if (rPick == 2) {
                int spot = r.nextInt (7);
                pass += sChar.charAt(spot);
            } else if (rPick == 3){
                int spot = r.nextInt (9);
                pass += intChar.charAt (spot);
            }
        }
        System.out.println ("Generated Pass: " + pass);

        System.out.println("common:"+ RandomStringUtils.randomAscii(32));

        //逻辑:检查除过第一个元素外,其余的每个颜色与上一个进行比较,如果不一样,则拷贝当前list到一个新list,然后放到map中

        for(int i=0;i<list.size();i++){

            //拼接sku链接
            if(null!=list.get(i).getInvUrl() && !"".equals(list.get(i).getInvUrl())){
                list.get(i).setInvUrl(controllers.Application.DEPLOY_URL+ list.get(i).getInvUrl());
            }
            else {
                list.get(i).setInvUrl(controllers.Application.DEPLOY_URL+ "/comm/detail/"+id+"/"+list.get(i).getId());
            }

            //判断是否是当前需要显示的sku
            if(!skuId.equals(Long.valueOf(-1)) && !list.get(i).getId().equals(skuId)){
                list.get(i).setOrMasterInv(false);
            }else if (list.get(i).getId().equals( skuId)){
                list.get(i).setOrMasterInv(true);
            }

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
