package analyze;

import com.github.shyiko.mysql.binlog.BinaryLogFileReader;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventHeaderV4;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.QueryEventData;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.github.shyiko.mysql.binlog.event.deserialization.ChecksumType;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

public class AnalyzeBinLog {

	public static void main(String[] args){
		String fileName = "D:\\software\\mysql\\log_bin\\.000004";
//		byte[] buffer = new byte[2048];
//		int position = 0;
//		try(FileInputStream inputStream = new FileInputStream(fileName)){
//			while (inputStream.read(buffer, position, 2048-position) != -1){
//
//			}
//		}catch (FileNotFoundException exception){
//			exception.printStackTrace();
//		}catch (IOException exception){
//			exception.printStackTrace();
//		}
		EventDeserializer eventDeserializer = new EventDeserializer();
		eventDeserializer.setChecksumType(ChecksumType.CRC32);
		try(BinaryLogFileReader reader = new BinaryLogFileReader(new File(fileName), eventDeserializer)){
			Event event;
			while ((event = reader.readEvent()) != null){
//				if(event.getData() == null){
//					continue;
//				}
				EventHeaderV4 header = (EventHeaderV4) event.getHeader();
				if(header.getEventType() == EventType.QUERY){
					//ÓÐsql
					QueryEventData data = (QueryEventData) event.getData();
//					System.out.println(data.getSql());
				}else if(header.getEventType() == EventType.TABLE_MAP){
					//record Type
					TableMapEventData data = (TableMapEventData)event.getData();
//					TableMap.getInstance().addTable(header, data);
				}else if(EventType.isWrite(header.getEventType())){
					WriteRowsEventData data = (WriteRowsEventData) event.getData();
					StringBuilder printValue = new StringBuilder();
					printValue.append(data.getTableId()).append("|");
					for(Serializable[] value : data.getRows()){
						printValue.append(Arrays.toString(value));
					}
					System.out.println(printValue.toString());
				}else if(EventType.isDelete(header.getEventType())){
					DeleteRowsEventData data = (DeleteRowsEventData) event.getData();
					System.out.println(data.getTableId() + "|" + data.getRows());
				}else if(EventType.isUpdate(header.getEventType())){
					UpdateRowsEventData data = (UpdateRowsEventData)event.getData();
					System.out.println(data.getTableId() + "|" + data.getIncludedColumnsBeforeUpdate() + "|" + data.getIncludedColumns());
				}
//				EventType.isWrite(((EventHeaderV4)event.getHeader()).getEventType());
//				System.out.println(event);
			}
//			System.out.println(TableMap.getInstance().toString());
		}catch (Exception exception){
			exception.printStackTrace();
		}
	}

}
