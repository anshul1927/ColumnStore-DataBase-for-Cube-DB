import java.io.File;
import java.util.regex.Pattern;

public class DeleteFile {

   public void testFile(String dbname)
   {
        String[] pathnames;
        String pat = "src/main/resources/"+dbname+"/lattice/";

        File f = new File("src/main/resources/"+dbname+"/lattice/");
        pathnames = f.list();

        for (String pathname : pathnames) {
            File m = new File("src/main/resources/"+dbname+"/lattice/" + pathname);
            String newPath = pat + pathname;
            String[] newAr;
            newAr = m.list();
            for (String ms : newAr) {
                System.out.println(ms);
                String x ="lattice";
                if (ms.matches("(" + x+ ").*"))
                    deletefile(newPath + "/" + ms);
            }

        }
    }



    private static void deletefile(String s) {
        System.out.println(s);
        File myObj = new File(s);
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
    }
}

