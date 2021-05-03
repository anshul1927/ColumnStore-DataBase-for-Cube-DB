import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LatticeCreation {

    public Set<Set<String>> createLattice(String factPath, ArrayList<String> s, Map<String, String> factVariables, Integer columnNumber , String dbname) {

        long N = (long) Math.pow(2, s.size());
        Set<Set<String>> result = new HashSet<>();

        for (int i = 0; i < N; i++) {
            Set<String> set = new HashSet<>();

            // check every bit of `i`
            for (int j = 0; j < s.size(); j++) {
                // if j'th bit of `i` is set, add `S[j]` to the current set
                if ((i & (1 << j)) != 0) {
                    set.add(s.get(j));
                }
            }
            if(!set.isEmpty())
                result.add(set);
        }
        System.out.println(result);
        for(Set<String> stt : result)
            System.out.println(stt);

        System.out.println("Read Data Line by Line With Header \n");
        for (Map.Entry<String,String> entry : factVariables.entrySet())
        {
            readDataLineByLine(factPath , result , columnNumber++ , entry.getValue(), dbname);
        }

        System.out.println("_______________________________________________");
        return result;

    }

    public static void readDataLineByLine(String file, Set<Set<String>> powerSet , Integer factColumnNumber , String aggFunc , String dbname) {
        try
        {

            ArrayList<Integer> index=new ArrayList<>();
            int choice = 5;
            if(aggFunc.equalsIgnoreCase("SUM"))
                choice = 0;
            else if(aggFunc.equalsIgnoreCase("AVG"))
                choice = 1;
            else if(aggFunc.equalsIgnoreCase("COUNT"))
                choice = 2;
            else if(aggFunc.equalsIgnoreCase("MAX"))
                choice = 3;
            else if(aggFunc.equalsIgnoreCase("MIN"))
                choice = 4;
            else
            {
                System.out.println("Wrong input of aggregate function");
                System.exit(0);
            }



            for(Set<String> s : powerSet)
            {
                File csvFile = new File(file);
                BufferedReader br = new BufferedReader(new FileReader(csvFile));
                String line ="";
                index.clear();
                line = br.readLine();
                String[] header= line.split(",");
                for(int i=0;i< header.length;i++)
                {
                    if((s.contains(header[i])))
                    {
                        index.add(i);
                    }
                }
                String factheader=header[factColumnNumber];
                System.out.println(s);
                HashMap<ArrayList<String>, Double> column = new HashMap();
                while ((line = br.readLine()) != null)
                {
                    String[] arr = line.split(",");
                    //for the first line it'll print
                    ArrayList<String> columnValue = new ArrayList<>();
                    for(Integer j:index)
                    {
                        columnValue.add(arr[j]);
                    }
                    Double fact_new = Double.parseDouble(arr[factColumnNumber]);
                    if(column.containsKey(columnValue))
                    {
                        Double fact_old=column.get(columnValue);
                        Double temp =0.0;
                        switch(choice)
                        {
                            case 0: temp=fact_new+fact_old;
                                break;
                            case 1:temp=(fact_new+fact_old)/2;
                                break;
                            case 2:temp=fact_old+1;
                                break;
                            case 3:if(fact_new>=fact_old)
                                temp=fact_new;
                            else
                                temp=fact_old;
                                break;
                            case 4:if(fact_new>=fact_old)
                                temp=fact_old;
                            else
                                temp=fact_new;
                                break;
                            default: break;
                        }
                        column.put(columnValue,temp);
                    }
                    else
                        column.put(columnValue,fact_new);

                }
                br.close();
                System.out.println(column);
                FolderCreateLattice(s , dbname);
                FileCreateHashMapToCsv(column,s, factheader,dbname,aggFunc);
            }

            ///////apex cuboid creation


            File csvFile_apex = new File(file);
            BufferedReader br_apex= new BufferedReader(new FileReader(csvFile_apex));
            String line ="";
            line = br_apex.readLine();
            String[] header= line.split(",");
            String factheader_apex=header[factColumnNumber];
            PrintWriter pw_apex = new PrintWriter(new File("src/main/resources/"+dbname+"/lattice/lattice[]/"+factheader_apex+"_"+aggFunc+".csv"));
            StringBuffer csvData_apex = new StringBuffer("");
            csvData_apex.append(factheader_apex);
            csvData_apex.append("\n");
            Double temp=0.0;
            while ((line = br_apex.readLine()) != null) {
                String[] arr = line.split(",");
                Double fact_new_apex = Double.parseDouble(arr[factColumnNumber]);
                switch(choice)
                {
                    case 0: temp=fact_new_apex+temp;
                        break;
                    case 1:temp=(fact_new_apex+temp)/2;
                        break;
                    case 2:temp=fact_new_apex+1;
                        break;
                    case 3:if(fact_new_apex>=temp)
                        temp=fact_new_apex;
                        break;
                    case 4:if(fact_new_apex>=temp){}
                    else
                        temp=fact_new_apex;
                        break;
                    default: break;
                }
            }
            csvData_apex.append(temp);
            csvData_apex.append("\n");
            pw_apex.write(csvData_apex.toString());
            pw_apex.close();
            br_apex.close();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void FolderCreateLattice(Set<String> s, String dbname) {
        String header ="";
        for(String h : s)
            header=header+h+",";
        String m="lattice"+s;
        File folder=new File("src/main/resources/"+dbname+"/lattice");
        boolean ans=folder.mkdir();
        File file = new File("src/main/resources/"+dbname+"/lattice/"+m);
        boolean bool = file.mkdir();
        File file1 = new File("src/main/resources/"+dbname+"/lattice/"+"lattice[]");
        boolean bool1 = file1.mkdir();
    }

    private static void FileCreateHashMapToCsv(HashMap<ArrayList<String>, Double> column, Set<String> s, String factheader, String dbname, String aggFunc) {
        try {
            StringBuilder header = new StringBuilder();
            for(String h : s)
                header.append(h).append(",");
            System.out.println(s);
            PrintWriter pw = new PrintWriter(new File("src/main/resources/"+dbname+"/lattice/lattice"+s+"/"+"lattice"+s+"_"+factheader+".csv"));
            StringBuffer csvData = new StringBuffer("");
            csvData.append(header);
            String headerForFactVariable=factheader+"_"+aggFunc;
            csvData.append(headerForFactVariable);
            csvData.append("\n");
            for (Map.Entry<ArrayList<String>, Double> entry : column.entrySet())
            {
                String x="";
                for(String v: entry.getKey())
                    x=x+v+",";
                csvData.append(x);
                csvData.append(entry.getValue());
                csvData.append("\n");
            }
            pw.write(csvData.toString());
            pw.close();
            convertToColumnstore("src/main/resources/"+dbname+"/lattice/lattice"+s+"/"+"lattice"+s+"_"+factheader+".csv",s,dbname);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void convertToColumnstore(String path, Set<String> s, String dbname) throws IOException {
        File csvFile = new File(path);
        BufferedReader br1 = new BufferedReader(new FileReader(csvFile));
        String line1="";
        line1=br1.readLine();
        String csvSplitter = ",";
        String[] header1=line1.split(csvSplitter);
        int size=header1.length;
        br1.close();
        for(int i=0;i<size;i++)
        {
            BufferedReader br = new BufferedReader(new FileReader(path));
            PrintWriter pw;
            String line;
            line=br.readLine();
            String[] header=line.split(csvSplitter);
            String  csv_name=header[i];
            Path pathToCheck = Paths.get("src/main/resources/"+dbname+"/lattice/lattice"+s+"/"+csv_name+".csv");
            boolean check= Files.exists(pathToCheck);
            if(check)
            {
                continue;
            }
            else {
                pw = new PrintWriter(new File("src/main/resources/"+dbname+"/lattice/lattice" + s + "/" + csv_name + ".csv"));
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
            br.close();
        }

    }

    public ArrayList<String> findFactIDColumns(String factPath, Integer noOfColumns) throws IOException {

        File csvFile = new File(factPath);
        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        String line="";
        ArrayList<String> factColumns= new ArrayList<>();
        line = br.readLine();
        String[] header= line.split(",");
        for(int i=noOfColumns-1;i>=0;i--)
        {
           factColumns.add(header[i]);
        }
        br.close();
        return factColumns;


    }
}
