import java.io.IOException;
import java.util.Scanner;

class Main{
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        while (true){
            System.out.println("********************Cube DB using Column Store********************");
            System.out.println();
            System.out.println("1. Create DataBase. \n2. Run OLAP Queries\n3. Exit.");
            System.out.println("Enter Your Choice :- ");
            System.out.println();
            Scanner sc = new Scanner(System.in);

            int choice = sc.nextInt();

            if (choice==3){
                break;
            }
            switch (choice){
                case 1:
                    System.out.println("Create Database");
                    FolderCreation folderCreation = new FolderCreation();
                    FolderCreation.folderCreation();
                    break;
                case 2:
                    System.out.println("Run OLAP Queries ");
                    OLAPOpn olap = new OLAPOpn();
                    OLAPOpn.queries();
                    break;
            }
        }
    }
}