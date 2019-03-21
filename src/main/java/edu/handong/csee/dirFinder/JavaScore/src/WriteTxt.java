import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
public class WriteTxt {
   private String  path;
   
   WriteTxt()
   {
      path =null;
   }
   
   WriteTxt(String   path)
   {
      this.path  = path;
   }

   public void writeToFile( String textLine1, String textLine2 ) throws IOException {
      
      FileWriter write = new FileWriter( path , true);
      PrintWriter print_line = new PrintWriter( write );
      print_line.printf( "%s %s" + "%n" , textLine1, textLine2);
      print_line.close();
   }
   
   public void writeToFile( String textLine1) throws IOException {
	      
	      FileWriter write = new FileWriter( path , true);
	      PrintWriter print_line = new PrintWriter( write );
	      print_line.printf( "%s" + "%n" , textLine1);
	      print_line.close();
	   }
}