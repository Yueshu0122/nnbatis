package sqlSession;

public interface Executor {
    public <T> T query(String statement, Object parameter);
}

