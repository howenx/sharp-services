package modules;

import com.google.inject.PrivateModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.guice.session.SqlSessionManagerProvider;
import service.H2Service;
import service.H2ServiceMapperImpl;

/**
 *
 * Created by howen on 15/10/23.
 */
public class H2XmlModule extends PrivateModule{
    @Override
    protected void configure() {
        install(new org.mybatis.guice.XMLMyBatisModule(){

            @Override
            protected void initialize() {
                setEnvironmentId("default");
            }
        });

        bind(SqlSession.class).annotatedWith(Names.named("DbA")).toProvider(SqlSessionManagerProvider.class).in(Scopes.SINGLETON);
        expose(SqlSession.class).annotatedWith(Names.named("DbA"));

        bind(H2Service.class).annotatedWith(Names.named("ServiceH2")).to(H2ServiceMapperImpl.class);
        expose(H2Service.class).annotatedWith(Names.named("ServiceH2"));

    }
}
