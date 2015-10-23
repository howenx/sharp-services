package modules;

import com.google.inject.PrivateModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.guice.session.SqlSessionManagerProvider;
import service.PostgresqlService;
import service.PostgresqlServiceMapperImpl;

/**
 *
 * Created by howen on 15/10/23.
 */
public class PostgresqlXmlModule extends PrivateModule{

    @Override
    protected void configure() {
        install(new org.mybatis.guice.XMLMyBatisModule(){

            @Override
            protected void initialize() {
                setEnvironmentId("development");
            }
        });

        bind(SqlSession.class).annotatedWith(Names.named("DbB")).toProvider(SqlSessionManagerProvider.class).in(Scopes.SINGLETON);
        expose(SqlSession.class).annotatedWith(Names.named("DbB"));

        bind(PostgresqlService.class).to(PostgresqlServiceMapperImpl.class);
        expose(PostgresqlService.class);
    }
}
