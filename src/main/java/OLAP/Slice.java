package OLAP;


import dnl.utils.text.table.TextTable;

import java.io.*;
import java.util.*;

public class Slice {

    static Scanner sc = new Scanner(System.in);

    public static void getSlice(Map<String, Map<String, String>> dimensionAttributeMap, Map<String, String> dimensionID, Map<String, String> factV, Set<Set<String>> powerSet, String dbname) throws IOException {

//
//        System.out.println(dimensionAttributeMap);
//            System.out.println(dimensionID);
//            System.out.println(factV);
//            System.out.println(powerSet);
//            System.out.println(dbname);

        File directorypath = new File("src/main/resources/"+dbname+"/Fact");

        File[] filelist = directorypath.listFiles();



        ArrayList<String> factfilename = new ArrayList<>();

        for(Map.Entry<String,String> s: dimensionID.entrySet()){
            factfilename.add(s.getValue());
        }

        for(Map.Entry<String,String> s: factV.entrySet()){
            factfilename.add(s.getKey());
        }


        System.out.println("These are the available dimension for DB "+dbname+"");
        List<String> headerList = new ArrayList<>();

        for(Map.Entry<String, String> s : dimensionID.entrySet()){
            System.out.println(s.getKey());
        }


        System.out.println("These are the available Dimension please choose one for slice operation :- ");
        System.out.println();
        String dimparname = sc.next();


        System.out.println("These are the Attribute's for Dimension "+dimparname);

        for(Map.Entry<String , String > s: dimensionAttributeMap.get(dimparname).entrySet()){
            System.out.println(s.getKey());
        }
        System.out.println("Select the attribute name from above list Choose 1");
        String attrName = sc.next();

        System.out.println("Want to see complete data yes/no");

        String yesno = sc.next();


        if (yesno.equals("yes")) {
            if(attrName.equals(dimensionID.get(dimparname))){
                File selectedFile4 = new File("src/main/resources/"+dbname+"/Fact/" + attrName + ".csv");
                BufferedReader br4 = new BufferedReader(new FileReader(selectedFile4));
                String line = "";
                System.out.println();
                System.out.println(dimparname);
                while ((line = br4.readLine())!=null){
                    System.out.println(line);
                }
                System.out.println("Enter the condition on which you want to filter slice based of above data");
            }else {
                File selectedFile4 = new File("src/main/resources/"+dbname+"/"+dimparname+"/" + attrName + ".csv");
                BufferedReader br4 = new BufferedReader(new FileReader(selectedFile4));
                String line = "";
                System.out.println();
                System.out.println(dimparname);
                while ((line = br4.readLine())!=null){
                    System.out.println(line);
                }
                System.out.println("Enter the condition on which you want to filter slice based of above data");
            }

        }else
            System.out.println("Enter the condition on which you want to filter slice");


        System.out.println("--> If values are in STRING format than use only == as a condition otherwise for Integer values >=, >, <=, <, =, !=");
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


        List<Integer> conditionindex = new ArrayList<>();

        List<String> records = new ArrayList<>();

        if(attrName.equals(dimensionID.get(dimparname))){
            File selectedFile4 = new File("src/main/resources/"+dbname+"/Fact/" + attrName + ".csv");
            BufferedReader br4 = new BufferedReader(new FileReader(selectedFile4));

            String line1 = "";
            int linenumber1 = 0;

            while ((line1 = br4.readLine()) != null) {
                //System.out.println(line1);
                findindex(limitdouble1, limitString1, operand1, conditionindex, line1, linenumber1);
                linenumber1++;
            }
        }else{
            File selectedFile4 = new File("src/main/resources/"+dbname+"/"+dimparname+"/" + attrName + ".csv");
            BufferedReader br4 = new BufferedReader(new FileReader(selectedFile4));

            String line1 = "";
            int linenumber1 = 0;

            while ((line1 = br4.readLine()) != null) {
                //System.out.println(line1);
                findindex(limitdouble1, limitString1, operand1, conditionindex, line1, linenumber1);
                records.add(line1);
                linenumber1++;
            }
        }

        if(attrName.equals(dimensionID.get(dimparname))){
            output(factfilename,dbname, conditionindex);
        }else{
            outputattr(factfilename,dbname,conditionindex,attrName, dimparname, dimensionID.get(dimparname), records);
        }

    }

    private static void outputattr(ArrayList<String> factfilename, String dbname, List<Integer> conditionindex, String attrName, String dimName, String primaryIDofDim, List<String> records) throws IOException {

        List<Integer> mainConditionIndex = new ArrayList<>();
        File f = new File("src/main/resources/"+dbname+"/Fact/"+primaryIDofDim+".csv");
        BufferedReader br1 = new BufferedReader(new FileReader(f));


        String line1 = "";
        int linenumber1 = 0;
        while ((line1 = br1.readLine())!=null){

            if(conditionindex.contains(Integer.parseInt(line1)-1)){
//                System.out.println(line1+"  "+linenumber1);
                mainConditionIndex.add(linenumber1);
            }
            linenumber1++;
        }



        int t1 = 0;
        ArrayList<List<String>> columnvalues1 = new ArrayList<>();


//        System.out.println("*******************************");
        for (String file : factfilename) {

            if(file.equals(primaryIDofDim)){
                File selectedFile = new File("src/main/resources/"+dbname+"/Fact/" + file + ".csv");
                BufferedReader br = new BufferedReader(new FileReader(selectedFile));

                String line = "";
                int linenumber = 0;
                int outline = 0;
                int n2 = mainConditionIndex.size();
                int r1 = 0;
                while ((line = br.readLine()) != null) {
//                System.out.println("inside while loop");

                    if (r1 < n2 && (mainConditionIndex.get(r1) == linenumber)) {
//                    System.out.println("inside if condition " + linenumber);
//                    System.out.println("outline " + outline);
//                    System.out.println("line " + line);
//                    System.out.println(columnvalues1.size());

                        if (t1 == 0) {
                            columnvalues1.add(outline, Collections.singletonList(records.get(Integer.parseInt(line)-1)));
                        }
                        else {
//                        System.out.println("columnValues1.get(linenumber) " + columnvalues1.get(outline));
                            List<String> s4 = new ArrayList<>(columnvalues1.get(outline));
//                        System.out.println("S4 " + s4);
//                        System.out.println(s4);
//                        System.out.println(s4.add(line));
                            s4.add(records.get(Integer.parseInt(line)-1));
                            columnvalues1.set(outline, s4);
                        }
//                    System.out.println("linenumber :-" + outline);
                        outline++;
                        if (r1 == n2 - 1) {
                            break;
                        }
                        r1++;

                    }
                    linenumber++;
                }
            }
            else{
                File selectedFile = new File("src/main/resources/"+dbname+"/Fact/" + file + ".csv");
                BufferedReader br = new BufferedReader(new FileReader(selectedFile));

                String line = "";
                int linenumber = 0;
                int outline = 0;
                int n2 = mainConditionIndex.size();
                int r1 = 0;
                while ((line = br.readLine()) != null) {
//                System.out.println("inside while loop");

                    if (r1 < n2 && (mainConditionIndex.get(r1) == linenumber)) {
//                    System.out.println("inside if condition " + linenumber);
//                    System.out.println("outline " + outline);
//                    System.out.println("line " + line);
//                    System.out.println(columnvalues1.size());
                        if (t1 == 0) {
                            columnvalues1.add(outline, Collections.singletonList(line));
                        }
                        else {
//                        System.out.println("columnValues1.get(linenumber) " + columnvalues1.get(outline));
                            List<String> s4 = new ArrayList<>(columnvalues1.get(outline));
//                        System.out.println("S4 " + s4);
//                        System.out.println(s4);
//                        System.out.println(s4.add(line));
                            s4.add(line);
                            columnvalues1.set(outline, s4);
                        }
//                    System.out.println("linenumber :-" + outline);
                        outline++;
                        if (r1 == n2 - 1) {
                            break;
                        }
                        r1++;

                    }
                    linenumber++;
                }
            }

            t1 = 1;
//            System.out.println(file);
        }

        System.out.println("-----------------------------------------------------------");

        if(!attrName.equals(primaryIDofDim)){
            int index = factfilename.indexOf(primaryIDofDim);
            factfilename.set(index,attrName);
        }

//        for (String s: factfilename)
//            System.out.print(s+" ");
//

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


//        System.out.println();
//        for (List<String> s4 : columnvalues1) {
//            System.out.println((s4));
//        }

    }

    static void output(ArrayList<String> filename,String dbname, List<Integer> conditionindex) throws IOException {
//        System.out.println(conditionindex);


        int t1 = 0;
        ArrayList<List<String>> columnvalues1 = new ArrayList<>();
//        System.out.println("*******************************");
        for (String file : filename) {
//            System.out.println("Filename is :------------" + file);
            File selectedFile = new File("src/main/resources/"+dbname+"/Fact/" + file + ".csv");
            BufferedReader br = new BufferedReader(new FileReader(selectedFile));

            String line = "";
            int linenumber = 0;
            int outline = 0;
            int n2 = conditionindex.size();
            int r1 = 0;
            while ((line = br.readLine()) != null) {
//                System.out.println("inside while loop");

                if (r1 < n2 && (conditionindex.get(r1) == linenumber) ) {
//                    System.out.println("inside if condition " + linenumber);
//                    System.out.println("outline " + outline);
//                    System.out.println("line " + line);
//                    System.out.println(columnvalues1.size());
                    if (t1 == 0) {
                        columnvalues1.add(outline, Collections.singletonList(line));
                    }
                    else {
//                        System.out.println("columnValues1.get(linenumber) " + columnvalues1.get(outline));
                        List<String> s4 = new ArrayList<>(columnvalues1.get(outline));
//                        System.out.println("S4 " + s4);
//                        System.out.println(s4);
//                        System.out.println(s4.add(line));
                        s4.add(line);
                        columnvalues1.set(outline, s4);
                    }
//                    System.out.println("linenumber :-" + outline);
                    outline++;
                    if (r1 == n2 - 1) {
                        break;
                    }
                    r1++;

                }
                linenumber++;
            }
            t1 = 1;
//            System.out.println(file);
        }
        System.out.println("-----------------------------------------------------------");


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
        TextTable tt = new TextTable(Arrays.copyOf(filename.toArray(),filename.toArray().length, String[].class), outputresult);
        tt.printTable();


    }

    static void findindex( double limitdouble1, String limitString1, String operand1, List<Integer> conditionindex, String line1, int linenumber1) {
        if (limitString1.equals("")) {
            switch (operand1) {
                case ">=":
                    if (Double.parseDouble(line1) >= limitdouble1) {
                        conditionindex.add(linenumber1);
                    }
                    break;
                case ">":
                    if (Double.parseDouble(line1) > limitdouble1) {
                        conditionindex.add(linenumber1);
                    }
                    break;
                case "<=":
                    if (Double.parseDouble(line1) <= limitdouble1) {
                        conditionindex.add(linenumber1);
                    }
                    break;
                case "<":
                    if (Double.parseDouble(line1) < limitdouble1) {
                        conditionindex.add(linenumber1);
                    }
                    break;
                case "=":
                    if (Double.parseDouble(line1) == limitdouble1) {
                        conditionindex.add(linenumber1);
                    }
                    break;
                case "!=":
                    if (Double.parseDouble(line1) != limitdouble1) {
                        conditionindex.add(linenumber1);
                    }
                    break;
            }
        } else {
            if (line1.equals(limitString1)) {
                conditionindex.add(linenumber1);
            }
        }
    }

    static void makefilename(File[] filelist, ArrayList<String> dimfilename, ArrayList<String> factvaribalename, ArrayList<String> filename) {
        for (File file : filelist) {
            String s4 = "";
            if (file.getName().contains("ID")) {
                s4 = file.getName();
                s4 = s4.substring(0, s4.length() - 4);
                dimfilename.add(s4);
            } else {
                s4 = file.getName();
                s4 = s4.substring(0, s4.length() - 4);
                factvaribalename.add(s4);
            }
            filename.add(s4);
        }
    }

}