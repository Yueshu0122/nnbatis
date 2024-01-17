import entity.Monster;
import mapper.MonsterMapper;
import org.junit.Test;
import sqlSession.SqlSession;

public class MapperTest {
    @Test
    public void getMapper(){
        SqlSession sqlSession = new SqlSession();
        MonsterMapper mapper = sqlSession.getMapper(MonsterMapper.class);
        System.out.println(mapper.getClass());
        Monster monster = mapper.getMonsterById(1);
        System.out.println(monster);
    }
}
