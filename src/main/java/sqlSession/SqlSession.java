package sqlSession;

import java.lang.reflect.Proxy;

public class SqlSession {
    private Executor executor = new NnExecutor();

    private NnbatisConfiguration nnbatisConfiguration =
            new NnbatisConfiguration();

    //There would be multiple methods of sql and all of them will call the query in the Executor.
    public <T> T selectOne(String statement, Object para){
        return executor.query(statement,para);
    }

    public <T> T getMapper(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},
                new MapperProxy(nnbatisConfiguration,this,clazz));
    }

}
