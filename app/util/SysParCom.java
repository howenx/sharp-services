package util;

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

    public static String REDIS_URL;
    public static String REDIS_PASSWORD;
    public static Integer REDIS_PORT;
    public static String REDIS_CHANNEL;

    @Inject
    public SysParCom( Configuration configuration) {

        PAGE_SIZE = configuration.getInt("theme.page.size");

        IMAGE_URL = configuration.getString("image.server.url");

        DEPLOY_URL = configuration.getString("deploy.server.url");

        IMG_PAGE_SIZE = configuration.getInt("img.page.size");

        REDIS_URL = configuration.getString("redis.host");
        REDIS_PASSWORD = configuration.getString("redis.password");
        REDIS_PORT = configuration.getInt("redis.port");
        REDIS_CHANNEL = configuration.getString("redis.channel");
    }

}
