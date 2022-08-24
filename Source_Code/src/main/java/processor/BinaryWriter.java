package processor;

import java.util.List;

import org.apache.commons.csv.CSVRecord;



@FunctionalInterface
public interface BinaryWriter {
  void addNode(int left,int right, List<CSVRecord> records,int num);
}
