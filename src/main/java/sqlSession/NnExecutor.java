package sqlSession;

import entity.Monster;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NnExecutor implements Executor {

    private NnbatisConfiguration nnbatisConfiguration =
            new NnbatisConfiguration();

    @Override
    public <T> T query(String statement, Object parameter) {
        Connection connection = getConnection();

        ResultSet set = null;
        PreparedStatement pre = null;

        try {
            pre = connection.prepareStatement(statement);
            pre.setString(1,parameter.toString());
            set = pre.executeQuery();

            Monster monster = new Monster();

            while(set.next()){
                monster.setId(set.getInt("id"));
                monster.setName(set.getString("name"));
                monster.setEmail(set.getString("email"));
                monster.setAge(set.getInt("age"));
                monster.setGender(set.getInt("gender"));
                monster.setBirthday(set.getDate("birthday"));
                monster.setSalary(set.getDouble("salary"));
            }

            return (T) monster;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(set != null){
                    set.close();
                }
                if(pre != null){
                    pre.close();
                }
                if(connection!=null){
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private Connection getConnection(){
        Connection connection = nnbatisConfiguration.build("nnbatis.xml");
        return connection;
    }
}
