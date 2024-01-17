package sqlSession;

public class SqlSession {
    private Executor executor = new NnExecutor();

    private NnbatisConfiguration nnbatisConfiguration =
            new NnbatisConfiguration();

    //There would be multiple methods of sql and all of them will call the query in the Executor.
    public <T> T selectOne(String statement, Object para){
        return executor.query(statement,para);
    }

}
