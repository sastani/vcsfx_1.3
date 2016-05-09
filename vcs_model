import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Collections;

import org.apache.commons.io.FileUtils; // Requires external jar file


public class VCS 
{
    String src_path;
    String tgt_path;
    String manifest_path, mom_manifest_path;
    String op_system;
    String slash;
    
    VCS(){};         // Empty default constructor
    
    VCS(String os){ // Display proper path depending on operating system
    	if(os == "Windows")
    	{
        	op_system = "WIN";
    		slash = "\\";
    	}
    	else
    	{
        	op_system = "UNIX";
    		slash = "/";
    	}
    }
    
    VCS(String source, String target){
        src_path = source;
        tgt_path = target;
    }
    
    // Creates an empty repository directory structure
    // source\target\Repo343\Manifests
    public void create_repo(String source, String target){
        mom_manifest_path = null;
        
        //remove extra slash char at end of source
        src_path = format_dir(source);
 
        //remove slash at beginning of target
        tgt_path = format_dir(target);

        String path  = src_path;
        //check for case that target is not a dir (just slash was passed)
        if(tgt_path.length()!=0)
        {
        	path  = path + slash + tgt_path + slash + "Repo343";
        }
        else
        {
        	path  = path + slash +  "Repo343";
        }
        
        manifest_path = path + slash + "Manifests";
        
        if(new File(manifest_path).mkdirs())
            System.out.println("Directory created: " + path);
        else
            System.out.println("Failed to create: " + path);
    } // End create_repo
    
    // Create leaf folder for a given file in the specified path
    public String create_leaf(String file_name, String path){
        
        String leaf_path;
        
        leaf_path = path + file_name;
        new File(leaf_path).mkdirs();
        
        return leaf_path;
    }// End create_leaf
    
    // Creates a manifest that keeps track of files checked into the directory
    public void create_manifest(String artif_path, boolean hidden){ 
        
    	String file_name = manifest_path;
    	//if manifest is supposed to be mom file, add ".mom" to beginning of file
        if (hidden)
        {
            file_name = file_name + slash + ".mom-" + get_timestamp() + ".txt";
        }
        //otherwise, name file normally
        else
        {
            file_name = file_name + slash + get_timestamp() + ".txt";
        }
        File manifest_file = new File(file_name);
        //if OS is WIN and manifest supposed to be hidden, set file attribute to hidden
        if (hidden && op_system == "WIN")
        {
            try {
                Runtime.getRuntime().exec("attrib +H " + file_name);
            } catch (IOException e){
                System.err.println("Could not set attribute to hidden: " + e);
            }
        }
        
        String line;
        String temp_mom = null;
        
        try{
            BufferedReader reader;
            PrintWriter writer; 
            
            if(manifest_file.exists()){ // If file exists -> open file and add to it
                reader = new BufferedReader(new FileReader(manifest_file)); 
                File temp = new File(manifest_path + slash + "temp.txt"); 
                writer = new PrintWriter(temp);
                
                while(true) // Read current file and copy contents over to temp file
                {
                    line = reader.readLine();
                    
                    if(line == null)
                        break;
                    
                    if(line.contains("Mom: ")) // Don't copy mom
                        temp_mom = line;
                    else if(line.equals(artif_path)){} // Don't copy path if already on there
                    else
                       writer.println(line);
                    
                }
                writer.println(artif_path);
                writer.println(temp_mom);
              
                reader.close();
                writer.close();
                
                manifest_file.delete();          // Delete previous manifest
                temp.renameTo(manifest_file);    // Rename new manifest
            }
            else{  // If file does not exist (ex. new day) create it
                writer = new PrintWriter(manifest_file); // Creates file if it does not exist
                
                writer.println(get_timestamp());
                writer.println(artif_path);
                writer.print("Mom: " + mom_manifest_path);
                
                mom_manifest_path = manifest_file.getAbsolutePath();
                writer.close();
            }
        }catch(FileNotFoundException e){
            System.out.println("Error: File " + manifest_file + " not found");
        }catch(IOException e){
            System.out.println("Failed to create new manifest file.");
        }catch(NullPointerException e){
            System.out.println("Done reading.");
        }
    }
    
    public void create_manifest_merge(ArrayList<String> new_files, String destination, String[] moms, String date_time, boolean log)
    {
    	{ 
        	String file_name = destination;
        	//if manifest is supposed to be log file, add "_log" to end of file name
            if (log)
            {
                file_name = file_name + slash + date_time + "_log" + ".txt";
            }
            //otherwise, name file normally
            else
            {
                file_name = file_name + slash + date_time + ".txt";
            }
            File manifest_file = new File(file_name);
                        
            try{
                PrintWriter writer; 
                
                writer = new PrintWriter(manifest_file); // Creates file 
                
                writer.println(date_time);
                //write each of the new file names to the manifest
                for(String s: new_files)
                {
                	writer.println(s);
                }
                writer.print("Moms: " + moms[0] + ", ");
                writer.print(moms[1]);
                writer.close();
                }
            catch(FileNotFoundException e){
                System.out.println("Error: File " + manifest_file + " not found");
            }
            catch(NullPointerException e){
                System.out.println("Done reading.");
            }
        }	
    	
    }
        
    public void check_in(String source, String target){
        
        String mom_file;
        String mani_file;
        
        //remove extra slash char at end of source
        src_path = format_dir(source);
        
        if(tree_dne(src_path))
        {
        	return;
        }

        //remove extra slash char at end of target
        tgt_path = format_dir(target);
        
        manifest_path = tgt_path + slash + "Manifests";
        
        mani_file = manifest_path + slash;
        mom_file = src_path + slash;
         
        // Find most current mani file using private function which uses file iterator to iterate over files in directory
        //Allows us to do multiple check ins in a row*/
        
        /* Find most current mom file using private function which uses file iterator to iterate over files in directory
        // If path that iterator points to contains ".mom". have found mom file    */
     
        
        mani_file = get_latest(mani_file);
        mom_file = get_latest(mom_file);
        
        BufferedReader reader; 
        String line;
        ArrayList<String> files = new ArrayList<String>();
        ArrayList<String> files_2 = new ArrayList<String>();
        String old_checksum;
        String leaf, tgt_file;
        int old_aid, new_aid;
        boolean new_files = false;
        
        try{ 
            reader = new BufferedReader(new FileReader(mom_file)); // Open manifest version file on repo
            line = reader.readLine();							  //throw out first line in text file (which contains timestamp)
            line = reader.readLine();							  
            while(line!=null && !line.contains("Mom: ")){		//while line is not null or the last line (which contains the mom path)
                old_checksum = line.substring((line.lastIndexOf(slash)+1), line.lastIndexOf("."));	//get old checksum from filename
                old_aid = Integer.valueOf(old_checksum);
                new_aid = checksum(line);						//calculate new checksum for each file
                String new_name;

                if(new_aid != old_aid)							//if new aid is different from old, rename the file
                {
                    File old_file = new File(line);
                    new_name = line.substring(0,line.lastIndexOf(slash) + 1) + Integer.toString(new_aid) + ".txt";

                    File updated_file = new File (new_name);  	//rename file with new file name
                    old_file.renameTo(updated_file);
                    
                    files_2.add(new_name);						//add new file name to array which will be used to update mom

                    leaf = line.substring(src_path.lastIndexOf(slash), line.lastIndexOf(slash));											//get relative path of leaf folder
                    tgt_file = tgt_path + leaf + slash + Integer.toString(new_aid) + ".txt";	

                    //compose path of new file as it appears in target
                    FileUtils.copyFile(updated_file, new File(tgt_file));																//copy over newly named file from source to target
                    files.add(tgt_file);
                    System.out.println("Copy file: " + new_name + " success");
                }
                else
                {
                	files_2.add(line);
                }
                line = reader.readLine();
                }
            }
        catch(IOException e){
            System.out.println("Mom file: " + mom_file + " does not exist");
        }
        
        //set boolean flag to true, if there are any new files sent to repo
        if(files.size()!=0)
        {
        	new_files = true;
        }
        
        String current_manifest = tgt_path + slash + "Manifests";
        String current_mom = mom_manifest_path;
        //if there are new artifacts, update the manifest in repo
        
        mom_manifest_path = mani_file;
        if(files.size()!=0)
        {
	        // Update manifest
	        for(int i = 0; i < files.size(); i++)
	            create_manifest(files.get(i), false);
        }

        
        //if there are new artifacts, create new ".mom" in source pt
        if(new_files)
        {
        	//send new mom file to source directory
            manifest_path = src_path;
            //use previous mom file as mom of new mom file
            mom_manifest_path = mom_file;
	        //Create new ".mom"
	        for(int i=0; i < files_2.size(); i++)
	        	create_manifest(files_2.get(i), true);
        }
        
        manifest_path = current_manifest; 			// Return to previous manifest location
        mom_manifest_path = current_mom; 
        
    }
    
    public void check_out(String source, String target, String version){
        //remove extra slash characters at end of source
        src_path = format_dir(source);
        
        if(tree_dne(src_path))
        {
        	return;
        }
        File pt = new File(src_path);

        // Get index of last slash character before name of pt to create reference to manifest folder
        int last_slash = src_path.lastIndexOf(slash);
        manifest_path = src_path.substring(0, last_slash) + slash + "Manifests";
        
        //remove extra slash characters at end of target
        tgt_path = format_dir(target);
        
        // Get full path to manifest file 
        String version_path = manifest_path + slash + version + ".txt";
      
        BufferedReader reader; 
        String line;
        ArrayList<String> files = new ArrayList<String>();
        // Read from manifest file if it exists, otherwise throw error
        try{ 
            reader = new BufferedReader(new FileReader(version_path)); // Open manifest version file on repo
            
            while(true){
                line = reader.readLine();
                
                if(line == null){
                    reader.close();
                    break;
                }
                
                if(line.contains(src_path + slash)) // If file is part of the project tree then copy it over to target location
                {
                    String file_name = line.substring(pt.getPath().lastIndexOf(slash) + 1); // Get file name from line
                    FileUtils.copyFile(new File(line), new File(tgt_path + slash + file_name));
                    files.add(tgt_path + slash + file_name);
                    System.out.println("Copy file: " + line + " success");
                }
            }
            
            mom_manifest_path = version_path;
            // Update manifest in repo
            for(int i = 0; i < files.size(); i++)
                create_manifest(files.get(i), false);
           
            String current_manifest = manifest_path;
            String current_mom = mom_manifest_path;
                        
            // Send mom manifest to target pt
            manifest_path = tgt_path + slash + pt.getPath().substring(pt.getPath().lastIndexOf(slash));
            
            for(int i = 0; i < files.size(); i++)
                create_manifest(files.get(i), true);
            
            manifest_path = current_manifest; // Return to previous manifest location
            mom_manifest_path = current_mom; // Don't want repo to have manifest version from remote project tree
            
        }catch(IOException e){
            System.out.println("Manifest file: " + version_path + " does not exist!");
        }   
    }
    
    public void merge(String source, String target, String version){
        // Requirements: manifest file of project tree to be merged
        // project tree to be merged location
        // target project tree to be merged
        
    	//remove extra slash char at end of source or target
        src_path = format_dir(source);
        tgt_path = format_dir(target);
                
        if(tree_dne(src_path))
        {
        	return;
        }

        // Get index of last slash char before name of pt to create reference to manifest folder
        int last_slash = src_path.lastIndexOf(slash);
        manifest_path = src_path.substring(0, last_slash) + slash + "Manifests";
                
        
        // Get full path to manifest file 
        String version_path = manifest_path + slash + version + ".txt";
        String proj_tree = tgt_path.substring(tgt_path.lastIndexOf(slash)) + slash;
        String tgt_file;
        int files_changed = 0;
        
        
        try{
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(version_path));
            reader.readLine(); // Ignore datestamp on manifest file
            
            while((line = reader.readLine()) != null)
            {
                if(line.contains(proj_tree) && !line.contains(tgt_path)) // Only compare files with same project tree
                {
                    tgt_file = tgt_path + slash + line.substring(line.indexOf(slash, line.indexOf(proj_tree) + 1) + 1);
                    files_changed = files_changed + compare(line, tgt_file, version_path);
                }
            }
            
        }catch(IOException e){
            System.out.println("Manifest file: " + version + ".txt not found");
        } 
        
     
        //Create array of all subdirectories/leaf folders (we dont care about .moms)
        ArrayList<String> new_tgt_files = new ArrayList<String>();

        File[] leaf_dirs = new File(tgt_path).listFiles(File::isDirectory);
        for (File s: leaf_dirs)
        {
        	for(File subfile : s.listFiles())
        	{
        		new_tgt_files.add(subfile.getAbsolutePath());
        	}
        }
        
        if(files_changed!=0)
        {
	        //get the date and time
	        String curr_time = get_timestamp();
	        
	        //make array containing names of both moms
	        String[] merge_moms = {get_oldest(tgt_path), version_path};
	
	        //create manifest in source repo
	        String dest = src_path.substring(0, last_slash) + slash + "Manifests";
	        create_manifest_merge(new_tgt_files, dest, merge_moms, curr_time, false);
	        
	        //create hidden log file in target repo
	        dest = tgt_path;
	        create_manifest_merge(new_tgt_files, dest, merge_moms, curr_time, true);
        }
    
    }
    
    public int compare(String src_file, String tgt_file, String mani_vers){
        // If file does not exist in target directory, then copy it from the repo
        // File should not exist if the parent directory does not exist
    	String temp_leaf;
    	String mr_path;
        String mt_path;
        String mg_path;
        String oldest_mom = "";
        String common_anc = "";
        String target_leaf = tgt_file.substring(0,tgt_file.lastIndexOf(slash));
        
        String proj_tree = tgt_path.substring(tgt_path.lastIndexOf(slash)) + slash;

        try{

        	// If target leaf folder does not exist, copy file over to target
            if(!(new File(target_leaf).exists()))
            {
                FileUtils.copyFile(new File(src_file), new File(tgt_file));
                System.out.println("Successfully copied " + src_file);
                return 1;
            } 
            else if(!(new File(tgt_file).exists())) // If target exists (is exactly the same) and article id is different from repo target id
            {
            	temp_leaf = get_leaf(tgt_file);
            	mr_path = temp_leaf + "_MR";
                mt_path = temp_leaf + "_MT";
                mg_path = temp_leaf + "_MG";
                
                FileUtils.copyFile(new File(src_file), new File(mr_path + slash + get_file_name(src_file)));
                
                get_leaf(get_leaf(tgt_file));
                new File(get_leaf(tgt_file)).renameTo(new File(mt_path));
       
                //get oldest ".mom" in source folder and pass to find grandpa function
                //oldest ".mom" reflects when folder was first checked out from repo
                oldest_mom = get_oldest(tgt_path);

                //get mom of oldest mom, i.e. the equivalent manifest 
                //strip out extra chars, "Mom: ", from the last line
                oldest_mom = read_last_line(oldest_mom).substring(5);
                common_anc = find_grandpa(oldest_mom, mani_vers);
                
                //make directory for grandpa
                new File(mg_path).mkdir();
                //copy file with same leaf in manifest as one in target tree
                
                try{
                    String line;
                    BufferedReader reader = new BufferedReader(new FileReader(common_anc));
                    reader.readLine(); // Ignore datestamp on manifest file
                    
                    while((line = reader.readLine()) != null)
                    {
                        // Read each line and compare to target location
                        if(line.contains(proj_tree + get_rel_dir(temp_leaf))) // Only compare files with same project tree
                        {
                            FileUtils.copyFile(new File(line), new File(mg_path + slash + get_rel_dir(line)));
                        }
                        
                    }
                    reader.close();
                    
                }catch(IOException e){
                    System.out.println("Common anc file: " + common_anc + " not found");
                }
                return 1;
            }
       
        }
        catch(IOException e){
            System.out.println("Failed to copy file: " + src_file);
}
        return 0;
           
    }
    
    public int checksum(String file_path){
        int c = 0;
        char sum = 0;
        
        try{
            BufferedReader reader = new BufferedReader(
                                    new FileReader(file_path));
            
            while((c = reader.read()) != -1){
                char character = (char) c;
                sum += character;
            }
            
            reader.close();
        }catch(FileNotFoundException e){
            System.out.println("File does not exist.");
        }catch(IOException f){
            System.out.println("Failed to open reader.");
        }
        return sum % 256;
    }
    
    //check if pt directory exists
    private boolean tree_dne(String src_tgt){
    	// Enter tree selected
        File pt = new File(src_tgt);

        // Check that tree does not exist
        if(!pt.exists())
        {
            System.out.println("Project tree: " + src_tgt + " does not exist!");
            return true;
        }
        return false;
    }
    
    public String format_dir(String path){
        // Check if (src/target) path has an extra slash char at the beginning or end
    	int first_slash = path.indexOf(slash);
    	int last_slash; 
    	
    	//if string is only a slash just remove and return
    	if(first_slash==0 && path.length()==1)
    	{
    		return path = "";
    	}
        // If it has one at beginning, remove it from path
    	if(first_slash == 0)
    	{
    		path = path.substring(1, path.length());
    	}
    	last_slash = path.lastIndexOf(slash);
        
        // If it has one at end, remove it from path
        if(last_slash == (path.length()-1))
        {
            path = path.substring(0,(path.length()-1));
        }  
        return path;
    }
    
    //recursive function to find common ancestor of both the .mom and user supplied manifest
    //works in all cases!
    public String find_grandpa(String last_line_mom, String user_manifest) 
    {
    	//compare "Mom:" fields of both the oldest .mom and the user supplied manifest
    	//until they are the same and use this "Mom:" field as the common ancestor manifest

    	//get timestamp from file names
    	String f1 = get_file_name(user_manifest);
    	String f2 = get_file_name(last_line_mom);
  
    	Date mom_date = convert_totimestamp(f1);
		Date mani_date = convert_totimestamp(f2);
		
		//base case, when the dates of both are equal
		if (mom_date.equals(mani_date))
    	{
    		return user_manifest;
    	}
			 
		//if .mom is newer, call get last line on mom (until we have gone back enough in hierarchy
    	//else
		if(mani_date.after(mom_date))
		{
		    return find_grandpa((read_last_line(last_line_mom)).substring(5), user_manifest);
		}
				 
    	//if .mom is newer, call get last line on mom (until we have gone back enough in hierarchy
		 return find_grandpa(last_line_mom, (read_last_line(user_manifest)).substring(5));
    }
    
    public Date convert_totimestamp(String date)
    {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");
    	Date file_date = null;
    	try 
    	{
			file_date = format.parse(date);
			return file_date;

		} catch (ParseException e)
    	{			
		}
    		return file_date;
    }
    	
    public String read_last_line(String file)
    {
    	String curr_line;
    	String last_line = "";
    	try 
		{
			BufferedReader mom_reader = new BufferedReader(new FileReader(file));
	    	while((curr_line = mom_reader.readLine())!= null)
	    	{
	    		last_line = curr_line;
	    	}
	    	mom_reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found! " + file);
		} 
		catch (IOException e)
		{
		}
		return last_line;
    }
    

	private String get_leaf(String path)
    {
       File file = new File(path);
       String leaf_path = null;
       
       if(file.isDirectory())
       {
           // Don't do anything
       }
       else
       {
           leaf_path = path.substring(0, path.lastIndexOf("\\"));
       }
       
       return leaf_path;
    }
    
    private String get_file_name(String path)
    {
        String file_name = null;
        File file = new File(path);
        
        if(file.isDirectory())
        {
           // Don't do anything
        }
        else
        {
           file_name = path.substring(path.lastIndexOf("\\") + 1);
        }
        
        return file_name;
    }
    
    private String get_rel_dir(String path)
    {
        String file_name = null;
        file_name = path.substring(path.lastIndexOf("\\") + 1);
        
        return file_name;
    }
    
    private String get_latest(String path)
    {
    	Iterator<File> it = FileUtils.iterateFiles(new File(path), null, false);
        ArrayList<String> all_files = new ArrayList<String>();
        //Array of all files currently in dir
    	
    	//Strings to hold full path, relative file names in dir, and to return newest
    	String full_file;
    	String curr_file;
    	String latest;
    	
    	path = format_dir(path);

       
        while(it.hasNext())
        {
            curr_file = ((File)it.next()).getName();
            full_file = path + slash + curr_file; 
            all_files.add(full_file);
        }
        
        Collections.sort(all_files);
        //get the last element in arraylist (the newest file)
        latest = all_files.get(all_files.size()-1);
        
        return latest;
    }
    
    private String get_oldest(String path)
    {
    	Iterator<File> it = FileUtils.iterateFiles(new File(path), null, false);
        ArrayList<String> all_files = new ArrayList<String>();
        //Array of all files currently in dir
    	
    	//Strings to hold full path, relative file names in dir, and for the oldest
    	String full_file;
    	String curr_file;
    	String oldest;
    	
    	path = format_dir(path);
               
        while(it.hasNext())
        {
            curr_file = ((File)it.next()).getName();
            full_file = path + slash + curr_file; 
            all_files.add(full_file);
        }
        
        Collections.sort(all_files);
        //get first element in arraylist (the oldest file)
        oldest = all_files.get(0);
        
        return oldest;
    }
    
    private String get_timestamp(){
        return new SimpleDateFormat("MM-dd-YY-HH.mm.ss").format(new Date());
    }
