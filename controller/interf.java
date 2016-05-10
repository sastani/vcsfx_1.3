import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;


public class interf
{
    static Scanner in = new Scanner(System.in);
    
    public static void main(String[] args){
        String OS = System.getProperty("os.name").toLowerCase();
        String vcs_os = "";
        if (OS.contains("win"))
        {
        	vcs_os = "Windows";
        }
        else if (OS.contains("mac"))
        {
        	vcs_os = "Mac";
        }
        else if (OS.contains("linux")){
        	vcs_os = "Linux";
        }

        VCS repo = new VCS(vcs_os);

        int selection;
        
        System.out.println("Team: SFX ");
        System.out.println("VCSFX v1.3 for " + vcs_os);
        
        while(true){
            try{
                System.out.println();
                display_menu();
                
                selection = in.nextInt();
                
                switch(selection){
                    case 1:
                        System.out.println();
                        repo.create_repo(prompt("Enter source location: "),
                                         prompt("Enter target location: "));
                        test_case1(repo);
                        break;
                    case 2:
                        System.out.println();
                        repo.check_in(prompt("Enter source location: "),
                                      prompt("Enter target location: "));
                        break;
                        
                    case 3:
                        System.out.println();
                        repo.check_out(prompt("Enter source location: "),
                                       prompt("Enter target location: "),
                                       prompt("Enter version: "));
                        break;
                    case 4:
                    	System.out.println();
                        repo.merge(prompt("Enter source location: "),
                                       prompt("Enter target location: "),
                                       prompt("Enter version: "));
                        break;
                    case 5:
                        System.exit(0);      
                    default:
                        System.out.println("Input value out of range.");
                } // End switch
            } catch(InputMismatchException e){
                System.out.println("Invalid Input! Please enter an integer value.");
                in.nextLine();
            }
        } // End while
    } // End main
    
    static String prompt(String p){
        // Assumption: user does not type more than 1 word
        // Bug: will only take first word and then clear the next word
        
        String response;
        System.out.print(p);
        response = in.next();
        
        return response;
    }
    
    static void display_menu(){
        
        System.out.println("        Menu        ");
        System.out.println("--------------------");
        System.out.println("1. Create Repository");
        System.out.println("2. Check in         ");
        System.out.println("3. Check out        ");        
        System.out.println("4. Merge        ");
        System.out.println("5. Exit             ");
        System.out.print("Enter an option: ");
    }
    
    static void test_case1(VCS repo){
        
        String file;
        String lpath; // Leaf folder path
        File new_name;
        String path = repo.src_path + repo.slash + repo.tgt_path + repo.slash + "Repo343";
        PrintWriter w_file; // Automatically creates file if it does not exist

        try{
            // Create test case files
            // ================================ MyPt files ====================================
            // ------------------------- h.txt --------------------
            file = repo.slash + "h.txt";
            lpath = repo.create_leaf(file, (path + repo.slash + "MyPt")); // Create leaf for "h.txt
            
            File h_file = new File(lpath + file);
   
            w_file = new PrintWriter(h_file);
            w_file.print("H");
            w_file.close();
           
           new_name = new File(lpath + repo.slash + repo.checksum(lpath + file) + ".txt");
           h_file.renameTo(new_name);
           
           repo.create_manifest(new_name.getAbsolutePath(), false);
           
            // ================================== MyPt2 files =================================
            // ------------------------- h.txt --------------------
            file = repo.slash + "h.txt";
            lpath = repo.create_leaf(file, (path + repo.slash + "MyPt2")); // Create leaf for "h.txt
            
            File h_file2 = new File(lpath + file);
            
            w_file = new PrintWriter(h_file2);
            w_file.print("H");
            w_file.close();
            
            new_name = new File(lpath + repo.slash + repo.checksum(lpath + file) + ".txt");
            h_file2.renameTo(new_name);
           
            repo.create_manifest(new_name.getAbsolutePath(), false);
            
            // ------------------------- hello.txt --------------------
            file = repo.slash + "hello.txt";
            lpath = repo.create_leaf(file, (path + repo.slash + "MyPt2")); // Create leaf for "hello.txt
            
            File hello_file = new File(lpath + file);
            
            w_file = new PrintWriter(hello_file);
            w_file.print("Hello world");
            w_file.close();
            
            new_name = new File(lpath + repo.slash + repo.checksum(lpath + file) + ".txt");
            hello_file.renameTo(new_name);
           
            repo.create_manifest(new_name.getAbsolutePath(), false);
            
            // ---------------------------- goodbye.txt ---------------
            file = repo.slash + "goodbye.txt";
            lpath = repo.create_leaf(file, (path + repo.slash + "MyPt2")); // Create leaf for "goodbye.txt
            
            File goodbye_file = new File(lpath + file);
            
            w_file = new PrintWriter(goodbye_file);
            w_file.println("Good");
            w_file.print("bye");
            w_file.close();
            
            new_name = new File(lpath + repo.slash + repo.checksum(lpath + file) + ".txt");
            goodbye_file.renameTo(new_name);
           
            repo.create_manifest(new_name.getAbsolutePath(), false);
            
            // ================================== MyPt3 files =================================
            // ------------------------- h.txt --------------------
            file = repo.slash + "h.txt";
            lpath = repo.create_leaf(file, (path + repo.slash + "MyPt3")); // Create leaf for "h.txt

            File h_file3 = new File(lpath + file);

            w_file = new PrintWriter(h_file3);
            w_file.print("H");
            w_file.close();  
            
            new_name = new File(lpath + repo.slash + repo.checksum(lpath + file) + ".txt");
            h_file3.renameTo(new_name);
           
            repo.create_manifest(new_name.getAbsolutePath(), false);
            
            // ------------------------- main.fool.txt --------------------
            file = repo.slash + "main.fool";
            lpath = repo.create_leaf(file, (path + repo.slash + "MyPt3" + repo.slash + "src")); // Create leaf for "main.fool.txt

            File main_file = new File(lpath + file);

            w_file = new PrintWriter(main_file);
            w_file.print("Defoo main, darn sock.");
            w_file.close();  
            
            new_name = new File(lpath + repo.slash + repo.checksum(lpath + file) + ".txt");
            main_file.renameTo(new_name);
           
            repo.create_manifest(new_name.getAbsolutePath(), false);
             
            // ------------------------- darn.fool.txt --------------------
            file = repo.slash + "darn.fool";
            lpath = repo.create_leaf(file, (path + repo.slash + "MyPt3" + repo.slash + "src")); // Create leaf for "main.fool.txt

            File darn_file = new File(lpath + file);
            

            w_file = new PrintWriter(darn_file);
            w_file.println("Defoo darn stuff, set thread.color to param.color;");
            w_file.print(" find hole; knit hole closed.");
            w_file.close();  
            
            new_name = new File(lpath + repo.slash + repo.checksum(lpath + file) + ".txt");
            darn_file.renameTo(new_name);
           
            repo.create_manifest(new_name.getAbsolutePath(), false);
            
        }catch(FileNotFoundException e){
            System.out.println("Error file does not exist on the path specified.");
        }
    }
}
    
