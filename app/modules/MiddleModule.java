package modules;

import com.google.inject.AbstractModule;
import middle.DetailMid;
import middle.ThemeListMid;
import play.Configuration;
import play.Environment;
import util.cache.MemcachedCache;
import util.cache.MemcachedClientWrapper;
import util.cache.MemcachedConfiguration;

/**
 * 中间层module
 * Created by howen on 16/2/28.
 */
public class MiddleModule extends AbstractModule {

    private final Environment environment;
    private final Configuration configuration;

    public MiddleModule(
            Environment environment,
            Configuration configuration) {
        this.environment = environment;
        this.configuration = configuration;
    }

    protected void configure() {
        bind(DetailMid.class);
        bind(ThemeListMid.class);
        bind(SysParCom.class).asEagerSingleton();
        bind(MemcachedConfiguration.class).asEagerSingleton();
    }
}