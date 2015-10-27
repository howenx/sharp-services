package modules;

import com.google.inject.PrivateModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.guice.session.SqlSessionManagerProvider;
import service.ThemeService;
import service.ThemeServiceImpl;

/**
 * Guice private module insulate the multiple DataSources Env.And inject service interface.
 * Created by howen on 15/10/26.
 */
public class PrivateBindModule extends PrivateModule{
    @Override
    protected void configure() {

        /**
         * setting the default env and datasource to conf/mybatis-config.xml
         */
        install(new org.mybatis.guice.XMLMyBatisModule(){

            @Override
            protected void initialize() {
                setEnvironmentId("development");
            }
        });

        /**
         * bind SQLsession to isolate the multiple datasources.
         */
        bind(SqlSession.class).annotatedWith(Names.named("development")).toProvider(SqlSessionManagerProvider.class).in(Scopes.SINGLETON);
        expose(SqlSession.class).annotatedWith(Names.named("development"));

        /**
         * bind service for controller or other service inject.
         */
        bind(ThemeService.class).to(ThemeServiceImpl.class);
        expose(ThemeService.class);
    }
}
