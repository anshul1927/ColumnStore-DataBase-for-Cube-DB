package OLAP;
import dnl.utils.text.table.TextTable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RollUp {

    public void rollUpFunction(Map<String, Map<String, String>> dimensionAttributeMap, Map<String, String> dimensionID, Map<String, String> factV, Set<Set<String>> powerSet, String dbname) throws IOException {

        System.out.println("These are the available dimension for DB "+dbname+"");
        List<String> headerList = new ArrayList<>();

        for(Map.Entry<String, String> s : dimensionID.entrySet()){
            System.out.println(s.getKey());
        }


        System.out.println("Enter number of dimensions for Roll Up operations");
        Scanner sc = new Scanner(System.in);

        int numberOfDim= sc.nextInt();
        int numberOfDimForFurtherUse=numberOfDim;

        LinkedHashMap<String, String> dimAttr = new LinkedHashMap<>();
        LinkedHashMap<String,Integer> columnN = new LinkedHashMap<>();

        String dimName = "";
        String attrName = "";
        int intt=0;
        while (numberOfDim != 0) {
            System.out.println("Enter dimension name");
            dimName = sc.next();

            System.out.println("These are the Attribute's for Dimension "+dimName);

            for(Map.Entry<String , String > s: dimensionAttributeMap.get(dimName).entrySet()){
                System.out.println(s.getKey());
            }
            System.out.println("Select the attribute name from above list");
            attrName = sc.next();
            dimAttr.putIfAbsent(dimName, attrName);
            columnN.putIfAbsent(dimensionID.get(dimName),intt);//column name and index
            System.out.println(dimAttr);
            System.out.println(columnN);
            intt++;
            numberOfDim--;
        }
//            System.out.println("dimAttr :- "+dimAttr);
//            System.out.println(columnN);
        String alreadyCreatedLattice = "";
        List<List<String>> records = new ArrayList<>();/////for non factid columns
        HashMap<String,Integer> recordsMapping = new HashMap<>();
        int i=0;
        for (Map.Entry<String, String> dim : dimAttr.entrySet()) {
            List<String> record = new ArrayList<>();
//                System.out.println(dim.getValue());
            Path pathToCheck = Paths.get("src/main/resources/"+dbname+"/Fact" + "/" + dim.getValue() + ".csv");
            boolean check = Files.exists(pathToCheck);
            if (check) {
                alreadyCreatedLattice = alreadyCreatedLattice + dim.getValue() + ", ";
            } else {
                String x = dimensionID.get(dim.getKey());
                alreadyCreatedLattice = alreadyCreatedLattice + x + ", ";
                pathToCheck = Paths.get("src/main/resources/"+dbname+"/" + dim.getKey() + "/" + dim.getValue() + ".csv");
                record = readFile(pathToCheck);
                records.add(record);
                recordsMapping.put(dim.getValue(),i);
                i++;
            }

//                System.out.println(alreadyCreatedLattice);
        }
//            System.out.println("alreadyCreatedLattice :- "+ alreadyCreatedLattice);
//            System.out.println("records :- "+records);
//            System.out.println("recordsMapping :- "+recordsMapping);
        List<List<String>> finalOp = new ArrayList<>();

        if (alreadyCreatedLattice.length() > 0) {
            alreadyCreatedLattice = alreadyCreatedLattice.substring(0, alreadyCreatedLattice.length() - 2);
            String fpath = "src/main/resources/"+dbname+"/lattice/lattice[" + alreadyCreatedLattice + "]";
            for (Map.Entry<String, String> dim : dimAttr.entrySet()) {
                String y = dimensionID.get(dim.getKey());
                List<String> columnValue = listFinalOP(y, fpath);
                finalOp.add(columnValue);
            }
//                System.out.println("finalOp dimAttr :- "+finalOp);
            for (Map.Entry<String, String> fact : factV.entrySet()) {
                String y = fact.getKey();
                String z= fact.getValue();
                String m=y+"_"+z;
                List<String> columnValue = listFinalOP(m, fpath);
                finalOp.add(columnValue);
            }
//                System.out.println("finalOp fact :- "+finalOp);
        }
//            System.out.println("finalOp :- "+finalOp);



        for(int j=0;j<numberOfDimForFurtherUse;j++)
        {
            //System.out.println("Inside for");
            String columnName="";
            String dimenName="";
            String requiredAttr="";
            int index=0;
            for(Map.Entry<String, Integer> entry: columnN.entrySet()) {
                if(entry.getValue() == j) {
                    columnName= entry.getKey();
                    break;
                }
            }
            for(Map.Entry<String, String> entry1: dimensionID.entrySet()) {
                if(entry1.getValue().equalsIgnoreCase(columnName)) {
                    dimenName= entry1.getKey();
                    break;
                }
            }
            for (Map.Entry<String, String> dim1 : dimAttr.entrySet()) {
                if(dim1.getKey().equalsIgnoreCase(dimenName)) {
                    requiredAttr= dim1.getValue();
                    break;
                }
            }

            headerList.add(requiredAttr);
//                System.out.println("columnName is "+ columnName);
//                System.out.println("required attr is "+ requiredAttr);
//                System.out.println("dimName is :- "+dimenName);

            if(!columnName.equalsIgnoreCase(requiredAttr))
            {
//                    System.out.println("Inside else\n");
                for (Map.Entry<String, Integer> rec : recordsMapping.entrySet()) {
                    if(rec.getKey().equalsIgnoreCase(requiredAttr)) {
                        index= rec.getValue();
                        break;
                    }
                }
//                    System.out.println("index is "+index);
                for (int p = 0; p < finalOp.get(j).size(); p++) {
                    int x = Integer.parseInt(finalOp.get(j).get(p));
                    x = x - 1;
//                        System.out.println("index is :- "+index);
//                        System.out.println("x is :-"+x);
//                        System.out.println("records.get(index).get(x) :-"+records.get(index).get(x));
//                        System.out.println("finalOp(get(j)"+finalOp.get(j));
                    finalOp.get(j).set(p, records.get(index).get(x));
                }
            }
        }

        int r = finalOp.size();
        int c = finalOp.get(r-1).size();

        final int N = finalOp.stream().mapToInt(List::size).max().orElse(-1);

        for(Map.Entry<String, String> s: factV.entrySet()){
            headerList.add(s.getKey());
        }

        List<Iterator<String>> iterList = finalOp.stream().map(List::iterator).collect(Collectors.toList());
        List<List<String>> finalresult = IntStream.range(0, N)
                .mapToObj(n -> iterList.stream()
                        .filter(Iterator::hasNext)
                        .map(Iterator::next)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());


        int r1 = finalresult.size();
        int c1 = finalresult.get(r1-1).size();
        String[][] outputresult = new String[r1][c1];

        int a=0;
        for (List<String> stt : finalresult) {
            if(a>=r1){
                break;
            }
            int b = 0;
            for(String sttt: stt){
                if(b>=c1){
                    break;
                }
                outputresult[a][b] = sttt;
                b++;
            }
            a++;
        }

        TextTable tt = new TextTable(Arrays.copyOf(headerList.toArray(),headerList.toArray().length, String[].class), outputresult);


        tt.printTable();
//            System.out.println(headerList);
//            for (List<String> stt : finalresult) {
//                System.out.println(stt);
//            }


        System.out.println("*************************");
    }




    private static List<String> listFinalOP(String y, String fpath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fpath+ "/"+y+".csv"));
        String line = "";
        ArrayList<String> columnValue = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            String[] arr = line.split(",");
            columnValue.add(arr[0]);
        }
        br.close();
        return columnValue;
    }
    private static List<String> readFile(Path pathToCheck) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(String.valueOf(pathToCheck)));
        List<String> records = new ArrayList<>();
        String line = "";
        while ((line = br.readLine()) != null) {
            String[] lineSplit = line.split(",");
            records.addAll(Arrays.asList(lineSplit));
        }
        br.close();
        return records;
    }
}

