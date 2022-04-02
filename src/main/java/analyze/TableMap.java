package analyze;

import analyze.pojo.Table;
import com.github.shyiko.mysql.binlog.event.EventHeaderV4;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TableMap {

	public static TableMap getInstance(){
		return ClassHolder.intance;
	}

	private static class ClassHolder{
		private static TableMap intance = new TableMap();
	}

	private Map<Long, Table> tables = new ConcurrentHashMap<>(128);

	/**
	 * 记录table的对应事件
	 * @param header 头部
	 * @param data 数据部分
	 */
	public void addTable(EventHeaderV4 header, TableMapEventData data){
		if(data == null){
			return;
		}
		long tableId = data.getTableId();
		if(tables.containsKey(tableId)){
			return;
		}
		String database = data.getDatabase(), tableName = data.getTable();
		byte[] column = data.getColumnTypes();
		tables.put(tableId, new Table(tableId, database, tableName, column));
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("TableMap{");
		result.append("tables={");
		for(Map.Entry<Long, Table> entry : tables.entrySet()){
			result.append("[").append(entry.getKey()).append(",").append(entry.getValue()).append("],");
		}
		result.append("}");
		result.append("}");
		return result.toString();
	}
}
