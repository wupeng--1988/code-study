package analyze;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestMysql {

	public static void main(String[] args){
		String url = "jdbc:mysql://localhost:3306/neko?useUnicode=true&characterEncoding=utf8&useSSL=false&rewriteBatchedStatements=true&connectTimeout=30000&socketTimeout=60000";
		String username = "root", password = "root";

		Connection connection = null;
		Statement statement = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, username, password);
			statement = connection.createStatement();
			connection.setAutoCommit(true);
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet resultSet = databaseMetaData.getColumns(connection.getCatalog(), "neko", "user", "%");
			while (resultSet.next()){
				String columnName = resultSet.getString("COLUMN_NAME");
				String columnType = resultSet.getString("TYPE_NAME");
				int datasize = resultSet.getInt("COLUMN_SIZE");
				int digit = resultSet.getInt("DECIMAL_DIGITS");
				int nullable = resultSet.getInt("NULLABLE");
				System.out.println(columnName + " " + columnType + " " +datasize + " " + digit + " " + nullable);
			}
			statement.close();
			connection.close();
		}catch (Exception ex){
			ex.printStackTrace();
		}finally {
			if(statement != null){
				try {
					statement.close();
				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
			}
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
			}
		}

	}

}
