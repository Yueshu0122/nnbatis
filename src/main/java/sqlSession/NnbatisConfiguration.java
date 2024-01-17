package sqlSession;

import config.Function;
import config.MapperBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NnbatisConfiguration {
    private static ClassLoader loader =
            ClassLoader.getSystemClassLoader();

    public Connection build(String resource){
        try {
            InputStream stream =
                    loader.getResourceAsStream(resource);
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            Element root = document.getRootElement();
            return evalDataSource(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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

    public MapperBean readMapper(String path){
        MapperBean mapperBean = new MapperBean();

        try {
            InputStream stream = loader.getResourceAsStream(path);

            SAXReader reader = new SAXReader();

            Document document = reader.read(stream);

            Element root = document.getRootElement();

            String namespace = root.attributeValue("namespace").trim();

            mapperBean.setInterfaceName(namespace);

            Iterator iterator = root.elementIterator();

            List<Function> list = new ArrayList<>();

            while(iterator.hasNext()){
                Element e = (Element) iterator.next();
                Function function = new Function();
                String sqlType = e.getName().trim();
                String funcName = e.attributeValue("id").trim();
                String resultType = e.attributeValue("resultType").trim();
                String sql = e.getText().trim();

                function.setFuncName(funcName);
                function.setSql(sql);
                function.setSqlType(sqlType);

                Object newInstance = Class.forName(resultType).newInstance();
                function.setResultType(newInstance);

                list.add(function);


            }

            mapperBean.setFunctions(list);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return mapperBean;
    }
}
