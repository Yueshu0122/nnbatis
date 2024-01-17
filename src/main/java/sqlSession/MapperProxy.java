package sqlSession;

import config.Function;
import config.MapperBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class MapperProxy implements InvocationHandler {

    private SqlSession sqlSession;
    private String mapperFile;
    private NnbatisConfiguration nnbatisConfiguration;

    public MapperProxy(NnbatisConfiguration nnbatisConfiguration,SqlSession sqlSession,Class clazz) {
        this.sqlSession = sqlSession;
        this.nnbatisConfiguration = nnbatisConfiguration;
        this.mapperFile = clazz.getSimpleName() + ".xml";
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MapperBean mapperBean =
                nnbatisConfiguration.readMapper(this.mapperFile);

        if(!method.getDeclaringClass().getName().equals(mapperBean.getInterfaceName())){
            return null;
        }

        List<Function> functions = mapperBean.getFunctions();
        if(null != functions && 0 != functions.size()){
            for(Function function : functions){
                if(method.getName().equals(function.getFuncName())){
                    if("select".equalsIgnoreCase(function.getSqlType())){
                        return sqlSession.selectOne(function.getSql(),String.valueOf(args[0]));
                    }
                }
            }
        }

        return null;
    }
}
