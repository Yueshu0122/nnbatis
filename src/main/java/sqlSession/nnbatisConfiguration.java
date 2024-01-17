package sqlSession;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class nnbatisConfiguration {
    private static ClassLoader loader =
            ClassLoader.getSystemClassLoader();

    public Connection build(String resource){
        try {
            InputStream stream =
                    loader.getResourceAsStream(resource);
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            Element root = document.getRootElement();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Connection evalDataSource(Element node){
        if(!"database".equals(node.getName())){
            throw new RuntimeException("root should be <database>");
        }

        String driverClassName = null;
        String url = null;
        String username = null;
        String password = null;

        for(Object item : node.elements("property")){
            Element i = (Element) item;
            String name = i.attributeValue("name");
            String value = i.attributeValue("value");

            if(name == null || value == null){
                throw new RuntimeException("no name or no value");
            }

            switch (name){
                case "url":
                    url = value;
                    break;
                case "username":
                    username = value;
                    break;
                case "driverClassName" :
                    driverClassName = value;
                    break;
                case "password":
                    password = value;
                    break;
                default:
                    throw new RuntimeException("no such property");
            }

        }
        Connection connection = null;
        try {
            Class.forName(driverClassName);
            connection = DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
