import java.awt.*;
import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

public class FileConversion
{

    public void createFile(Map<String, String> attributeMap, String dimension, String dbname , String path) throws IOException {
        String csvSplitter = ",";
        int number_of_attributes=attributeMap.size();

        for(int i=0;i<number_of_attributes;i++)
        {
            BufferedReader br = new BufferedReader(new FileReader(path));
            PrintWriter pw;
            String line;
            line=br.readLine();
            String[] header=line.split(csvSplitter);
            String  csv_name=header[i];
            pw = new PrintWriter(new File("src/main/resources/"+dbname+"/"+dimension+"/"+csv_name+".csv"));
            StringBuffer csvData = new StringBuffer("");
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(csvSplitter);
                csvData.append(cols[i]);
                csvData.append('\n');
            }
            pw.write(csvData.toString());
            pw.close();
            br.close();
        }

    }
}



