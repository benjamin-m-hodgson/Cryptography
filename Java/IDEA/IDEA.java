import java.math.BigInteger;
import java.util.Scanner;

/**
 * 
 * @author Benjamin Hodgson
 * @date 11/23/17
 * 
 * This program runs through the complete IDEA (International Data Encryption Algorithm)
 * 
 ***Key Scheduling***
 
 	IDEA uses 52 sub-keys, each 16 bits long.
 
 	The 128-bit key of IDEA is taken as the first eight sub-keys, K(1) through K(8). 
 	The next eight sub-keys are obtained the same way, after a 25-bit circular left shift, 
 	and this is repeated until all encryption sub-keys are derived.
 	
 ***Encryption Sub-keys***
 
 	Round Encryption sub-keys
 	
 	1 		Z1(1) 	Z2(1) 	Z3(1) 	Z4(1) 	Z5(1) 	Z6(1) 	
 	2 		Z1(2) 	Z2(2)	Z3(2) 	Z4(2) 	Z5(2) 	Z6(2) 	
 	3 		Z1(3) 	Z2(3) 	Z3(3) 	Z4(3) 	Z5(3) 	Z6(3) 	
 	4 		Z1(4) 	Z2(4) 	Z3(4) 	Z4(4) 	Z5(4) 	Z6(4) 	
 	5 		Z1(5) 	Z2(5) 	Z3(5) 	Z4(5) 	Z5(5) 	Z6(5) 	
 	6 		Z1(6) 	Z2(6) 	Z3(6) 	Z4(6) 	Z5(6) 	Z6(6) 	
 	7 		Z1(7) 	Z2(7) 	Z3(7) 	Z4(7) 	Z5(7) 	Z6(7) 	
 	8 		Z1(8) 	Z2(8) 	Z3(8) 	Z4(8) 	Z5(8) 	Z6(8) 	
 	9		Z1(9) 	Z2(9) 	Z3(9) 	Z4(9)
 	
***Decryption Sub-keys***
 
 	Round Decryption sub-keys
 	
 	1 		*Z1(9) 	-Z2(9) 	-Z3(9) 	*Z4(9) 	Z5(8) 	Z6(8) 	
 	2 		*Z1(8) 	-Z3(8)	-Z2(8) 	*Z4(8) 	Z5(7) 	Z6(7) 	
 	3 		*Z1(7) 	-Z3(7) 	-Z2(7) 	*Z4(7) 	Z5(6) 	Z6(6) 	
 	4 		*Z1(6) 	-Z3(6) 	-Z2(6) 	*Z4(6) 	Z5(5) 	Z6(5) 	
 	5 		*Z1(5) 	-Z3(5) 	-Z2(5) 	*Z4(5) 	Z5(4) 	Z6(4) 	
 	6 		*Z1(4) 	-Z3(4) 	-Z2(4) 	*Z4(4) 	Z5(3) 	Z6(3) 	
 	7 		*Z1(3) 	-Z3(3) 	-Z2(3) 	*Z4(3) 	Z5(2) 	Z6(2) 	
 	8 		*Z1(2) 	-Z3(2) 	-Z2(2) 	*Z4(2) 	Z5(1) 	Z6(1) 	
 	9		*Z1(1) 	-Z3(1) 	-Z2(1) 	*Z4(1)
 
 	- indicates additive inverse modulo MOD_ADDITION
 	* indicates multiplicative inverse modulo MOD_MULTIPLICATION
 
 ***IDEA Round Steps***
 
	The fourteen steps used in the eight full rounds:

	1.	Multiply X1 and the first sub-key K1
	2.	Add X2 and the second sub-key K2
	3.	Add X3 and the third sub-key K3
	4.	Multiply X4 and the fourth sub-key K4
	5.	Bitwise XOR the results of steps 1 and 3
	6.	Bitwise XOR the results of steps 2 and 4.
	7.	Multiply the result of step 5 and the fifth sub-key K5
	8.	Add the results of steps 6 and 7
	9.	Multiply the result of step 8 and the sixth sub-key K6
	10.	Add the results of steps 7 and 9
	11.	Bitwise XOR the results of steps 1 and 9
	12.	Bitwise XOR the results of steps 3 and 9
	13.	Bitwise XOR the results of steps 2 and 10
	14.	Bitwise XOR the results of steps 4 and 10

	The four steps used in the ninth, transformation round:	
	
	1.	Multiply X1 and the first of the remaining four sub-keys, K1
	2.	Add X2 and the second of the remaining four sub-keys , K2
	3.	Add X3 and the third of the remaining four sub-keys, K3
	4.	Multiply X4 and the fourth sub-key, K4
 *
 */
public class IDEA {
	
	public static Scanner in;
	public static String data;
	public static String format;
	public static String op;
	public static String output;
	public static BigInteger key;
	
	/**
	 * clears the console window
	 */
	public static void clear_console() {
		for (int i = 0; i < 1000; i++) {
			System.out.println("\n");
		}
	}
	
	/**
	 * prints a 64-bit representation of a given long value
	 */
	public static void print_text(long plain_text){
		String binary_plain_text = String.format("%64s", Long.toBinaryString(plain_text));
		binary_plain_text = binary_plain_text.replace(' ', '0');
		System.out.printf("Bits: %s\n", binary_plain_text);
	}
	
	/**
	 * prints a 128-bit representation of the key
	 */
	public static void print_key() {
		byte[] key_bytes = key.toByteArray();
		StringBuilder binary_key_bytes = new StringBuilder();
		for (int i = 0; i < key_bytes.length; i++) {
			String binary_bytes = String.format("%8s", Integer.toBinaryString(key_bytes[i]));
			binary_key_bytes.append(binary_bytes);
		}
		String binary_key = String.format("%128s", binary_key_bytes.toString());
		binary_key = binary_key.replace(' ', '0');
		System.out.printf("Key bits: %s\n", binary_key);
	}
	
	/**
	 * @param num: a value of type long 
	 * @return a String of the 16 bit representation of num
	 */
	public static String bits_16(long num) {
		String bit_str = String.format("%16s", Long.toBinaryString(num));
		bit_str = bit_str.replace(' ', '0');
		return bit_str;
	}
	
	/***************************************************************************************
	 * Methods below are presented in execution order 
	/***************************************************************************************
	
	/**
	 * obtain the operation, text, and key from the user
	 */
	public static void get_info() {
		in = new Scanner(System.in);
		get_op();
		get_text();
		get_key();
		in.close();
	}
	
	/**
	 * Determines the operation (encrypt or decrypt) 
	 * the user wishes to perform upon the text. 
	 */
	public static void get_op() {
		String op_str = "";
		
		// while loop to collect the user input
		while (true) {
			// print the information and user prompt
			System.out.printf("Would you like to encrypt or decrypt the text?\n");
			System.out.printf("\t -Type 'dec' for decrypt\n");
			System.out.printf("\t -Type 'enc' for encrypt\n");
			System.out.printf("Encrypt or decrypt: ");
			
			try {
				// scan the input
				op_str = in.next();
				
				//trim the input string and convert to lower case
				op_str = op_str.toLowerCase();
				op_str = op_str.trim();
				
				if (op_str.equalsIgnoreCase("dec") ||
					op_str.equalsIgnoreCase("enc")) {
					op = op_str;
					break;
				}
				else {
					System.out.println("Please enter one of the responses listed!");
				}
			}
			catch (NumberFormatException e) {
				//clear_console();
				System.out.println("Please enter one of the responses listed!");
			}
		}
	}
	
	/**
	 * gets the text to be encrypted/decrypted from the user
	 */
	public static void get_text() {
		get_data_format();
		get_data();
		process_data();
	}
	
	/**
	 * Gets the text input format from the user
	 */
	public static void get_data_format() {
		String format_str = "";
		
		// while loop to collect the user input
		while (true) {
			// print the information and user prompt
			System.out.printf("What format is the text in?\n");
			System.out.printf("\t -Type 'bin' for binary\n");
			System.out.printf("\t -Type 'hex' for hexadecimal\n");
			System.out.printf("\t -Type 'txt' for alphanumeric text\n");
			System.out.printf("Text format: ");
			
			try {
				// scan the input
				format_str = in.next();
				
				//trim the input string and convert to lower case
				format_str = format_str.toLowerCase();
				format_str = format_str.trim();
				
				if (format_str.equalsIgnoreCase("bin") ||
					format_str.equalsIgnoreCase("hex") ||
					format_str.equalsIgnoreCase("txt")) {
					format = format_str;
					break;
				}
				else {
					System.out.println("Please enter one of the responses listed!");
				}
			}
			catch (NumberFormatException e) {
				//clear_console();
				System.out.println("Please enter one of the responses listed!");
			}
		}
	}
	
	/**
	 * Retrieves the input text from the user
	 */
	public static void get_data() {
		String text_input = "";
		
		// trash the new line
		in.nextLine();
		
		// print the information and user prompt
		System.out.printf("Enter the text to %srypt: ", op);
		
		// while loop to collect the user input
		while (true) {
			try {
				// scan the input
				text_input = in.nextLine();
				
				//trim the input string 
				text_input = text_input.trim();
				data = text_input;
				break;
			
			}
			catch (NumberFormatException e) {
				//clear_console();
				System.out.println("Error retrieving data from the user!");
				// print the information and user prompt
				System.out.printf("Enter the text to %srypt: ", op);
			}
		}
	}
	
	/**
	 * Takes the input and converts it to a base 2 binary string
	 */
	public static void process_data() {		
		
		// handle the binary input case
		if (format.equalsIgnoreCase("bin")) {
			// no processing necessary
			data = data.replace(' ', '0');
		}
		
		// handle the hex input case
		if (format.equalsIgnoreCase("hex")) {
			// convert the input string to upper case 
			data = data.toUpperCase();	
			
			// Loop through each hex character, generate binary string
			StringBuilder hex_append = new StringBuilder();
			for (int i = 0; i < data.length(); i++) {
				// convert the hex character to it's integer value
				String hex_str = data.substring(i, i + 1);
				int val = Integer.parseInt(hex_str, 16);
				// convert the integer to it's 4-bit binary representation
				hex_str = String.format("%4s", Integer.toBinaryString(val));
				hex_str = hex_str.replace(' ', '0');
				// append to the StringBuilder
				hex_append.append(hex_str);
				
			}
			data = hex_append.toString();
		}
		
		// handle the alphanumeric input case
		// WIP: use US-ASCII encoding
		if (format.equalsIgnoreCase("txt")) {
			byte[] data_bytes = data.getBytes();
			StringBuilder data_bin = new StringBuilder();
			for (byte data_byte : data_bytes) {	
				// & 0xFF to make sure byte is always 8-bits
				String data_str = String.format("%8s", Integer.toBinaryString(data_byte & 0xFF));
				data_str = data_str.replace(' ', '0');
				data_bin.append(data_str);
			}
			data = data_bin.toString();
		}
		
		// else we have a problem!
		else {
			throw new IllegalArgumentException("Data type must be either bin, hex, or txt!");
		}
	}
	
	/**
	 * gets the key to be used for encryption from the user
	 */
	public static void get_key() {
		// While loop to collect the 128-bit key
		while (true) {
			System.out.printf("128-bit key used to %srypt: ", op);
			try {
				key = in.nextBigInteger();
				if (key.bitLength() > 128) {
					System.out.println("Key must be less than 128 bits!");
				}
				else {
					break;
				}
			}
			catch (NumberFormatException e) {
				//clear_console();
				System.out.println("Key must be less than 128 bits!");
			}
		}
	}
	
	/**
	 * Pads the input data so it's a bit-string with length divisible by 64.
	 * Calls the method to process encryption or decryption depending upon 
	 * @param op type.
	 */
	public static void process_out() {
		int pad_num = 64 - (data.length() % 64);
		
		// made the bit length of data divisible by 64
		if (pad_num != 64) {
			StringBuilder data_add = new StringBuilder();
			for (int i = 0; i < pad_num; i++) {
				data_add.append("0");
			}
			data_add.append(data);
			data = data_add.toString();
		}
			
		// check the data bit string is of the correct length
		if ((data.length() % 64) != 0) {
			throw new StringIndexOutOfBoundsException("Bit String length must be divisible by 64!");
		}
		
		// Display the data bit string to be encrypted/decrypted
		System.out.printf("%srypting data: %s\n", op, data);
		
		// Check if op is dec, if so decrypt 
		if (op.equalsIgnoreCase("dec")) {
			process_dec();
		}
		
		// Check if op is enc, if so encrypt
		if (op.equalsIgnoreCase("enc")) {
			process_enc();
		}
		
		// else we have a problem!
		else {
			throw new IllegalArgumentException("Operation type must be either dec or enc!");
		}
	}
	
	/**
	 * Assume at this point the data bit length is a multiple of 64.
	 */
	public static void process_dec() {
		// create a StringBuilder to build output binary string 
		StringBuilder out_add = new StringBuilder();
		
		// loop through data, process 64-bit blocks at a time
		for (int i = 0; i < data.length() / 64; i++) {
			// gather the data
			String data_block = data.substring(i*64, (i+1)*64);
			long data_val = Long.parseUnsignedLong(data_block, 2);
			
			System.out.printf("In long val: %d\n", data_val);
			
			// decrypt the data
			IDEA_decrypt decrypt_block = new IDEA_decrypt(data_val, key);
			long dec_val = decrypt_block.get_decrypted_text();
			
			System.out.printf("Out long val: %d\n", dec_val);
			
			// convert data to binary
			String dec_str = String.format("%64s", Long.toBinaryString(dec_val));
			dec_str = dec_str.replace(' ', '0');
			
			// append data
			out_add.append(dec_str);
		}
		output = out_add.toString();
	}
	
	/**
	 * Assume at this point the data bit length is a multiple of 64.
	 */
	public static void process_enc() {
		// create a StringBuilder to build output binary string 
		StringBuilder out_add = new StringBuilder();
		
		// loop through data, process 64-bit blocks at a time
		for (int i = 0; i < data.length() / 64; i++) {
			// gather the data
			String data_block = data.substring(i*64, (i+1)*64);
			long data_val = Long.parseUnsignedLong(data_block, 2);
			
			System.out.printf("In long val: %d\n", data_val);
			
			// encrypt the data
			IDEA_encrypt encrypt_block = new IDEA_encrypt(data_val, key);
			long enc_val = encrypt_block.get_cipher_text();
			
			System.out.printf("Out long val: %d\n", enc_val);
			
			// convert data to binary
			String enc_str = String.format("%64s", Long.toBinaryString(enc_val));
			enc_str = enc_str.replace(' ', '0');
			// append data
			out_add.append(enc_str);
		}
		output = out_add.toString();
	}
	
	/**
	 * 
	 */
	public static void format_out() {
		System.out.printf("Initial out: %s\n", output);
		
		if (format.equalsIgnoreCase("bin")) {
			output = output.trim();
		}
		if (format.equalsIgnoreCase("hex")) {
			StringBuilder out_str = new StringBuilder();
			for (int i = 0; i < output.length() / 64; i++) {
				String out_sub = output.substring(i*64, (i + 1)*64);
				long out_val = Long.parseUnsignedLong(out_sub, 2);
				String out_block = Long.toString(out_val, 16);
				System.out.printf("out_block: %s\n", out_block);
				out_str.append(out_block);
			}
			output = out_str.toString();
		}
		if (format.equalsIgnoreCase("txt")) {
			byte[] out_bytes = new byte[output.length() / 8];
			for (int i = 0; i < output.length() / 8; i++) {
				String out_sub = output.substring(i*8, (i + 1)*8);
				byte out_val = (byte) Integer.parseUnsignedInt(out_sub, 2);
				out_bytes[i] = out_val;
			}
			output = new String(out_bytes);
		}
		System.out.printf("%srypted text: %s\n", op, output);
	}
	
	/**
	 * Check that that decrypting the encrypted data returns original data
	 * @param plain_text: original data
	 * @param decrypt_text: data obtained from decrypting the encrypted data
	 * @return true if they are equal, false otherwise
	 */
	private static boolean decrypt_check(long plain_text, long decrypt_text) {
		if (plain_text == decrypt_text) {
			//System.out.printf("Success! Decrypted the original text");
			return true;
		}
		else {
			//System.out.print("Failure! Decrypted text is NOT original text");
			return false;
		}
	}
	
	public static void main(String[] args) {
		// obtain the operation, text, and key from the user
		get_info();
		// process the text 
		process_out();
		// print out
		format_out();
	}
}
