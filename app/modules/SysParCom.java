package modules;

import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 查询参数表中的参数项
 * Created by hao on 16/2/28.
 */
@Singleton
public class SysParCom {

    //每页固定的取数
    public static  Integer PAGE_SIZE;

    //图片展示时每页固定的取数
    public static  Integer IMG_PAGE_SIZE;

    //图片服务器url
    public static String IMAGE_URL;

    //发布服务器url
    public static String DEPLOY_URL;

    @Inject
    public SysParCom( Configuration configuration) {

        PAGE_SIZE = configuration.getInt("theme.page.size");

        IMAGE_URL = configuration.getString("image.server.url");

        DEPLOY_URL = configuration.getString("deploy.server.url");

        IMG_PAGE_SIZE = configuration.getInt("img.page.size");

    }

}
