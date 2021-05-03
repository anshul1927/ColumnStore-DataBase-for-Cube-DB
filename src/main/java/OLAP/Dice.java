package OLAP;


import dnl.utils.text.table.TextTable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Dice {
        static Scanner sc = new Scanner(System.in);
        public static void getDice(Map<String, Map<String, String>> dimensionAttributeMap, Map<String, String> dimensionID, Map<String, String> factV, Set<Set<String>> powerSet, String dbname) throws IOException {

            File directorypath = new File("src/main/resources/" + dbname + "/Fact");

            File[] filelist = directorypath.listFiles();

            ArrayList<String> factfilename = new ArrayList<>();

            for (Map.Entry<String, String> s : dimensionID.entrySet()) {
                factfilename.add(s.getValue());
            }

            for (Map.Entry<String, String> s : factV.entrySet()) {
                factfilename.add(s.getKey());
            }


//            System.out.println("These are the available dimension for DB "+dbname+"");
            List<String> headerList = new ArrayList<>();

            for (Map.Entry<String, String> s : dimensionID.entrySet()) {
                System.out.println(s.getKey());
            }


            System.out.println("These are the available Dimension for DB " + dbname + " please choose two or more for slice operation :- ");


            System.out.println();
            System.out.print("how many Dimension will you choose :- ");
            int totalDim = sc.nextInt();

            String[] dimparname = new String[totalDim];
            Map<String, String> attDim = new LinkedHashMap<>();

            List<String> attrName = new ArrayList<>();
            for (int i = 0; i < totalDim; i++) {
                System.out.println("Enter "+(i+1)+" Dimension name");
                dimparname[i] = sc.next();

                System.out.println("These are the Attribute's for Dimension " + dimparname[i]);

                for (Map.Entry<String, String> s : dimensionAttributeMap.get(dimparname[i]).entrySet()) {
                    System.out.println(s.getKey());
                }

                System.out.println("Select the attribute name from above list Choose 1");
                attrName.add(sc.next());

                attDim.put(dimparname[i], attrName.get(i));
            }

            System.out.println(attDim);


            System.out.println("Want to see complete data yes/no");
            String yesno = sc.next();

            if (yesno.equals("yes")) {

                int t1 = 0;
                for (Map.Entry<String, String> s : attDim.entrySet()) {
                    System.out.println(s.getValue());
                    System.out.println(s.getKey());

                    int linenumber = 0;
                    int r1 = 0;
                    String line = "";
                    if (s.getValue().equals(dimensionID.get(s.getKey()))) {
                        File selectedFile4 = new File("src/main/resources/" + dbname + "/Fact/" + s.getValue() + ".csv");
                        BufferedReader br4 = new BufferedReader(new FileReader(selectedFile4));
                        System.out.println();
                        System.out.println(s.getValue());
                        while ((line = br4.readLine()) != null) {
                            System.out.println(line);
                        }
                        System.out.println();
                    } else {
                        File selectedFile4 = new File("src/main/resources/" + dbname + "/" + s.getKey() + "/" + s.getValue() + ".csv");
                        BufferedReader br4 = new BufferedReader(new FileReader(selectedFile4));
                        System.out.println();
                        System.out.println(s.getValue());
                        while ((line = br4.readLine()) != null) {
                            System.out.println(line);
                        }
                    }
                }
                System.out.println("Enter the condition on which you want to filter slice based of above data");
            } else
                System.out.println("Enter the condition on which you want to filter slice");
            System.out.println("--> If values are in STRING format than use only == as a condition otherwise for Integer values >=, >, <=, <, =, !=");


            Map<String, List<Integer>> conditionindex = new LinkedHashMap<>();

            int t11 = 0;
            Map<String, List<String>> records = new LinkedHashMap<>();
            for (Map.Entry<String, String> s : attDim.entrySet()) {
                System.out.println("Enter Condition for " + s.getValue());
                String condition3 = sc.next();

                double limitdouble1 = 0;
                String limitString1 = "";
                String operand1;
                if (condition3.startsWith("==")) {
                    limitString1 = condition3.substring(2);
                    operand1 = condition3.substring(0, 2);
                } else {
                    if (condition3.charAt(1) == '=') {
                        limitdouble1 = Double.parseDouble(condition3.substring(2));
                        operand1 = condition3.substring(0, 2);
                    } else {
                        limitdouble1 = Double.parseDouble(condition3.substring(1));
                        operand1 = condition3.substring(0, 1);
                    }
                }
                List<Integer> condintioni = new ArrayList<>();
                if (s.getValue().equals(dimensionID.get(s.getKey()))) {
                    File selectedFile4 = new File("src/main/resources/" + dbname + "/Fact/" + s.getValue() + ".csv");
                    BufferedReader br4 = new BufferedReader(new FileReader(selectedFile4));
                    String line1 = "";
                    int linenumber1 = 0;

                    while ((line1 = br4.readLine()) != null) {
                        //System.out.println(line1);
                        Slice.findindex(limitdouble1, limitString1, operand1, condintioni, line1, linenumber1);
                        linenumber1++;
                    }

                    conditionindex.put(s.getValue(),condintioni);
                } else {
                    File selectedFile4 = new File("src/main/resources/" + dbname + "/" + s.getKey() + "/" + s.getValue() + ".csv");
                    BufferedReader br4 = new BufferedReader(new FileReader(selectedFile4));

                    String line1 = "";
                    int linenumber1 = 0;
                    List<String> record = new ArrayList<>();
                    while ((line1 = br4.readLine()) != null) {
                        //System.out.println(line1);
                        Slice.findindex(limitdouble1, limitString1, operand1, condintioni, line1, linenumber1);
                        record.add(line1);
                        linenumber1++;
                    }
                    conditionindex.put(s.getValue(),condintioni);
                    records.put(s.getValue(), record);
                }
            }



            int k = 0;


            List<Integer> finalconditionindex = new ArrayList<>();
            int t1 = 0;
            for(Map.Entry<String, List<Integer>> s: conditionindex.entrySet()){
                List<Integer> conditionindexcopy = new ArrayList<>();
                if(s.getKey().equals(dimensionID.get(dimparname[k]))){

//                    List<Integer> conditionindexcopy = new ArrayList<>();
                    if(t1==0){
                        finalconditionindex = conditionindex.get(s.getKey());
                    }else{
                        File selectedFile4 = new File("src/main/resources/"+dbname+"/Fact/" + s.getKey() + ".csv");
                        BufferedReader br4 = new BufferedReader(new FileReader(selectedFile4));
                        String line1 = "";
                        int linenumber1 = 0;
                        int outline = 0;
                        int r1 = 0;
                        int n2 = s.getValue().size();
                        int n3 = finalconditionindex.size();
                        int r2 = 0;
                        while ((line1 = br4.readLine())!=null){
                            if(r1<n2 && r1<n3 && finalconditionindex.get(r1)==linenumber1 ){
                                if(s.getValue().contains(finalconditionindex.get(r1))){
                                    conditionindexcopy.add(linenumber1);
                                }
                                r1++;
                            }
                            linenumber1++;
                        }
                        finalconditionindex.clear();
                        finalconditionindex = conditionindexcopy;
                    }
                }else{
                    if(t1==0)
                    {
                        String dname = "";
                        for(Map.Entry<String, String> s1: attDim.entrySet()){
                            if(s1.getValue().equals(s.getKey())){
                                dname = s1.getKey();
                                break;
                            }
                        }
                        String dnamepriKey = "";
                        for(Map.Entry<String, String> s1:dimensionID.entrySet()){
                            if(s1.getKey().equals(dname)){
                                dnamepriKey = s1.getValue();
                                break;
                            }
                        }


                        File selectedFile4 = new File("src/main/resources/"+dbname+"/Fact/" + dnamepriKey + ".csv");
                        BufferedReader br4 = new BufferedReader(new FileReader(selectedFile4));
                        String line1 = "";
                        int linenumber1 = 0;
                        int outline = 0;
                        int r1 = 0;
                        int n2 = s.getValue().size();
                        int r2 = 0;

                        while ((line1=br4.readLine())!=null){
                            if(s.getValue().contains(Integer.parseInt(line1)-1)){
                                    conditionindexcopy.add(linenumber1);
                                    System.out.println(linenumber1);
                            }
                        linenumber1++;
                    }

                        finalconditionindex.clear();
                        finalconditionindex = conditionindexcopy;
                    }
                    else{
                        String dname = "";
                        for(Map.Entry<String, String> s1: attDim.entrySet()){
                            if(s1.getValue().equals(s.getKey())){
                                dname = s1.getKey();
                                break;
                            }
                        }
                        String dnamepriKey = "";
                        for(Map.Entry<String, String> s1:dimensionID.entrySet()){
                            if(s1.getKey().equals(dname)){
                                dnamepriKey = s1.getValue();
                                break;
                            }
                        }


                        File selectedFile4 = new File("src/main/resources/"+dbname+"/Fact/" + dnamepriKey + ".csv");
                        BufferedReader br4 = new BufferedReader(new FileReader(selectedFile4));
                        String line1 = "";
                        int linenumber1 = 0;
                        int outline = 0;
                        int r1 = 0;
                        int n2 = s.getValue().size();
                        int r2 = 0;

                        while ((line1=br4.readLine())!=null){
                            if(s.getValue().contains(Integer.parseInt(line1)-1)){
                                if(finalconditionindex.contains(linenumber1)){
                                    conditionindexcopy.add(linenumber1);
                                }
                            }
                            linenumber1++;
                        }
                        finalconditionindex.clear();
                        finalconditionindex = conditionindexcopy;
                    }
                }
                k++;
                t1++;
            }



            System.out.println("***************************************");



            ArrayList<List<String>> columnvalues1 = new ArrayList<>();

            t1 = 0;

            for(String file: factfilename){

                String dimfinal = "";
                for(Map.Entry<String, String> s: dimensionID.entrySet()){
                    if(s.getValue().equals(file)){
                        dimfinal=s.getKey();
                        break;
                    }
                }



                if(attDim.containsKey(dimfinal) && !attDim.containsValue(file)){
                    File selectedFile = new File("src/main/resources/"+dbname+"/Fact/" + file + ".csv");
                    BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                    String line = "";
                    int linenumber = 0;
                    int outline = 0;
                    int n2 = finalconditionindex.size();
                    int r1 = 0;
                    List<String> record = new ArrayList<>();
                    record = records.get(attDim.get(dimfinal));

                    while ((line = br.readLine()) != null) {
                        if (r1 < n2 && (finalconditionindex.get(r1) == linenumber)) {
                            if (t1 == 0) columnvalues1.add(outline, Collections.singletonList(record.get(Integer.parseInt(line)-1)));
                            else {
                                List<String> s4 = new ArrayList<>(columnvalues1.get(outline));
                                s4.add(record.get(Integer.parseInt(line)-1));
                                columnvalues1.set(outline, s4);
                            }
                            outline++;
                            if (r1 == n2 - 1) break;
                            r1++;
                        }linenumber++;
                    }
                }else{
                    File selectedFile = new File("src/main/resources/"+dbname+"/Fact/" + file + ".csv");
                    BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                    String line = "";
                    int linenumber = 0;
                    int outline = 0;
                    int n2 = finalconditionindex.size();
                    int r1 = 0;
                    while ((line = br.readLine()) != null) {
                        if (r1 < n2 && (finalconditionindex.get(r1) == linenumber)) {
                            if (t1 == 0) columnvalues1.add(outline, Collections.singletonList(line));
                            else {
                                List<String> s4 = new ArrayList<>(columnvalues1.get(outline));
                                s4.add(line);
                                columnvalues1.set(outline, s4);
                            }
                            outline++;
                            if (r1 == n2 - 1) break;
                            r1++;
                        }linenumber++;
                    }
                }
            t1++;
            }


            int i = 0;
            for(Map.Entry<String , String> s: attDim.entrySet()){

                int index = factfilename.indexOf(dimensionID.get(dimparname[i]));
                factfilename.set(index, s.getValue());
                i++;
            }

            int r = columnvalues1.size();
            int c = columnvalues1.get(r-1).size();
            String[][] outputresult = new String[r][c];


            int a=0;
            for (List<String> stt : columnvalues1) {
                if(a>=r){
                    break;
                }
                int b = 0;
                for(String sttt: stt){
                    if(b>=c){
                        break;
                    }
                    outputresult[a][b] = sttt;
                    b++;
                }
                a++;
            }
            TextTable tt = new TextTable(Arrays.copyOf(factfilename.toArray(),factfilename.toArray().length, String[].class), outputresult);
            tt.printTable();
        }
    }