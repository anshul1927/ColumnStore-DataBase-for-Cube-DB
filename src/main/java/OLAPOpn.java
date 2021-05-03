import OLAP.Dice;
import OLAP.DrillDown;
import OLAP.Slice;
import OLAP.RollUp;

import java.io.*;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

class OLAPOpn{
    public static void queries () throws IOException, ClassNotFoundException {
        System.out.println("These are the available DataBase Name choose one on which you want to run OLAP Operation");
        File file = new File("src/main/resources/dbname.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = "";
        while((line=br.readLine())!=null){
            System.out.println(line);
        }



        Scanner sc = new Scanner(System.in);
        String dbname = sc.next();


        FileInputStream fs;
        ObjectInputStream os;


        fs = new FileInputStream("src/main/resources/"+dbname+"/savedData/dimension");
        os = new ObjectInputStream(fs);

        Map<String, String> dimension = (Map<String, String>) os.readObject();


        fs = new FileInputStream("src/main/resources/"+dbname+"/savedData/factvaribale");
        os = new ObjectInputStream(fs);

        Map<String, String> factvaribale = (Map<String, String>)os.readObject();

        fs = new FileInputStream("src/main/resources/"+dbname+"/savedData/lattice");
        os = new ObjectInputStream(fs);

        Set<Set<String>> lattice = (Set<Set<String>>)os.readObject();

        fs = new FileInputStream("src/main/resources/"+dbname+"/savedData/dimensionAttributeMap");
        os = new ObjectInputStream(fs);
        Map<String, Map<String, String>> dimensionAttributeMap = (Map<String, Map<String, String>>)os.readObject();

        while (true){
            System.out.println();
            System.out.println("1. RollUP.\n2. Slice.\n3. Dice.\n4. Drill Down.\n5. Exit");
            System.out.println();
            System.out.println("Choose Operation ");
            int choice = sc.nextInt();

            if(choice==5){
                break;
            }
            switch (choice){
                case 1:
                    RollUp rollUp = new RollUp();
                    rollUp.rollUpFunction(dimensionAttributeMap, dimension, factvaribale, lattice, dbname);
                    break;
                case 2:

                    Slice slice = new Slice();
                    Slice.getSlice(dimensionAttributeMap, dimension, factvaribale, lattice, dbname);
                    break;
                case 3:
                    Dice dice = new Dice();
                    Dice.getDice(dimensionAttributeMap, dimension, factvaribale, lattice, dbname);
                    break;
                case 4:
                    DrillDown drillDown = new DrillDown();
                    DrillDown.getDrillDown(dimension, factvaribale, dbname);
                    break;
            }
        }
    }
}