package canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import java.net.InetSocketAddress;
import java.util.List;

public class Connect {

	/**
	 * simple 连接， 没有使用 任何中间件
	 * canal.properties 配置 tcp使用这种模式， 但是这种模式会在关闭 服务器的时候丢失存储的信息1
	 * @param args
	 */
	public static void main(String[] args){
		CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("10.66.3.72", 11111), "example", "", "");
		int bachSize = 1000;
		try {
			connector.connect();
			connector.subscribe(".*\\..*");
			connector.rollback();
			while (true){
				Message message = connector.getWithoutAck(bachSize);
				long batchId = message.getId();
				int size = message.getEntries().size();
				if(batchId == -1 || size == 0){
					try {
						Thread.sleep(1000);
					}catch (InterruptedException ex){
						ex.printStackTrace();
					}
					continue;
				}else {
					printEntry(message.getEntries());
				}
				connector.ack(batchId);
			}
		}finally {
			connector.disconnect();
		}
	}

	private static void printEntry(List<CanalEntry.Entry> entryList){
		for(CanalEntry.Entry entry : entryList){
			if(entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND){
				continue;
			}
			CanalEntry.RowChange rowChange;
			try {
				rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
			}catch (Exception ex){
				throw new RuntimeException("Error ## parser of eromanga-event has an error, data:" + entry.toString(), ex);
			}

			CanalEntry.EventType eventType = rowChange.getEventType();
			for(CanalEntry.RowData rowData : rowChange.getRowDatasList()){
				if(eventType == CanalEntry.EventType.DELETE){
					System.out.println("delete--------------------");
					printColumn(rowData.getBeforeColumnsList());
					System.out.println("--------------------------");
				}else if(eventType == CanalEntry.EventType.INSERT){
					System.out.println("insert--------------------");
					printColumn(rowData.getAfterColumnsList());
					System.out.println("--------------------------");
				}else{
					System.out.println("update--------------------");
					System.out.println("-------------&gt; before");
					printColumn(rowData.getBeforeColumnsList());
					System.out.println("-------------&gt; after");
					printColumn(rowData.getAfterColumnsList());
					System.out.println("--------------------------");
				}
			}
		}
	}

	private static void printColumn(List<CanalEntry.Column> columnList){
		for(CanalEntry.Column column : columnList){
			System.out.println(column.getName() + ":" + column.getValue() + "   update=" + column.getUpdated());
		}
	}

}
