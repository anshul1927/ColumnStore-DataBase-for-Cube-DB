package OLAP;

        import dnl.utils.text.table.TextTable;

        import java.io.BufferedReader;
        import java.io.File;
        import java.io.FileReader;
        import java.io.IOException;
        import java.util.*;

public class DrillDown {

    static Scanner sc = new Scanner(System.in);
    public static void getDrillDown(Map<String, String> dimension, Map<String, String> factvaribale, String dbname) throws IOException {

        System.out.println("Thest are the available dimension for DB "+dbname);

        for(Map.Entry<String, String> s: dimension.entrySet()){
            System.out.println(s.getKey());
        }
        System.out.println("how many dimension want for Drill Down");
        int totalDim = sc.nextInt();

        ArrayList<String> selectedDim = new ArrayList<>();
        for(int i=0;i<totalDim;i++){
            selectedDim.add(dimension.get(sc.next()));
        }
        ArrayList<List<String>> columnvalues1 = new ArrayList<>();
        int t1 = 0;
        for(Map.Entry<String, String> s: dimension.entrySet()){

            if(selectedDim.contains(s.getValue())){
                File selectedFile = new File("src/main/resources/"+dbname+"/Fact/" + s.getValue() + ".csv");
                BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                String line = "";
                int linenumber = 0;
                while((line=br.readLine())!=null){
                   if(t1==0){
                       columnvalues1.add(linenumber, Collections.singletonList(line));
                   }else{
                       List<String> s4 = new ArrayList<>(columnvalues1.get(linenumber));
                       s4.add(line);
                       columnvalues1.set(linenumber, s4);
                   }
                    linenumber++;
                }
                t1++;
            }
        }
        for(Map.Entry<String, String> s: factvaribale.entrySet()){

                selectedDim.add(s.getKey());
                File selectedFile = new File("src/main/resources/"+dbname+"/Fact/" + s.getKey() + ".csv");
                BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                String line = "";
                int linenumber = 0;
                while((line=br.readLine())!=null){
                        List<String> s4 = new ArrayList<>(columnvalues1.get(linenumber));
                        s4.add(line);
                        columnvalues1.set(linenumber, s4);
                    linenumber++; }
        }

        System.out.println(columnvalues1);
        System.out.println(selectedDim);

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
        TextTable tt = new TextTable(Arrays.copyOf(selectedDim.toArray(),selectedDim.toArray().length, String[].class), outputresult);
        tt.printTable();
    }

}
