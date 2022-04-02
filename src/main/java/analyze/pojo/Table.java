package analyze.pojo;

import java.util.Arrays;

public class Table {

	private long tableId;
	private String database;
	private String tableName;
	private byte[] column;

	public Table(long tableId, String database, String tableName, byte[] column) {
		this.tableId = tableId;
		this.database = database;
		this.tableName = tableName;
		this.column = column;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Table{");
		result.append("tableId=").append(tableId);
		result.append(", database='").append(database).append("'");
		result.append(", tableName='").append(tableName).append("'");
		result.append(", column=").append(Arrays.toString(column));
		result.append("}");
		return result.toString();
	}

}
