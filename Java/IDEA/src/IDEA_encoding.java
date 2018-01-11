import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * 
 * @author Benjamin Hodgson
 * @date 1/6/2018
 * 
 * Creates two maps to convert between characters and their custom, 8-bit encoding values.
 * Data for the maps is taken from the text file IDEA_text_encoding.txt.
 *
 */

public class IDEA_encoding {
	
	private static HashMap<String, String> char_to_binary = new HashMap<String, String>();
	private static HashMap<String, String> binary_to_char = new HashMap<String, String>();
	
	/**
	 * 
	 * @param bin_val
	 * @return
	 */
	public String get_str(String bin_val) {
		if (!binary_to_char.containsKey(bin_val)) {
			construct_maps();
		}
		return binary_to_char.get(bin_val);
	}
	
	/**
	 * 
	 * @param str_val
	 * @return
	 */
	public String get_bin(String str_val) {
		if (!char_to_binary.containsKey(str_val)) {
			construct_maps();
		}
		return char_to_binary.get(str_val);
	}
	
	/**
	 * 
	 */
	private static void construct_maps() {
		// Read and parse from the file
		File encoding_file = new File("IDEA_text_encoding.txt");
		
		try {
			Scanner encoding_scan = new Scanner(encoding_file, "utf-8");
			//System.out.printf("Successfully made scanner\n");
			
			// skip the first five informational lines in the text file
			for (int i = 0; i < 5; i++) {
				encoding_scan.nextLine();
			}
			
			// read the data from the file
			while (encoding_scan.hasNextLine()) {
				String encoding_line = encoding_scan.nextLine();
				//System.out.println(encoding_line);
				String[] encoding_split = encoding_line.split("\\s+");
				
				// reference variables
				int enc_val = 0;
				String bin_val = "";
				String str_val = "";
				
				// loop through the pieces of the line read in
				for (int i = 0; i < encoding_split.length; i++) {
					// get the current String
					String enc_data = encoding_split[i];
					//System.out.printf("i value: %d Data: %s\n", i, enc_data);
					
					
					//Determine what data it represents
					if (i % 3 == 0) {
						enc_val = Integer.parseInt(enc_data);
					}
					if (i % 3 == 1) {
						bin_val = enc_data;
					}
					if (i % 3 == 2) {
						// take a substring to trim the identifying quotation marks
						if (enc_val != 10) {
							str_val = enc_data.substring(1, 2);
						}
						else {
							str_val = " ";
						}
							
						// check the binary string is correct
						int int_val = Integer.parseInt(bin_val, 2);
						if (enc_val != int_val) {
							System.out.printf("Incorrect binary value!\n");
							System.out.printf("Binary value: %d\n", int_val);
							System.out.printf("Expected value: %d\n", enc_val);
							break;
						}
						
						if (char_to_binary.containsKey(str_val)) {
							System.out.printf("%d: Wrong value! %s is already mapped!\n",enc_val, str_val);
							break;
						}
						if (binary_to_char.containsKey(bin_val)) {
							System.out.printf("%d: Wrong value! %s is already mapped!\n",enc_val, bin_val);
							break;
						}
						
						char_to_binary.put(str_val, bin_val);
						binary_to_char.put(bin_val, str_val);
						
					}
					
				}
				
				
			}
			
			encoding_scan.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.printf("Could not find the file!\n");
		}
	}
}
