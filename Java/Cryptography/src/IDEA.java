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
 
 	IDEA uses 52 subkeys, each 16 bits long.
 
 	The 128-bit key of IDEA is taken as the first eight subkeys, K(1) through K(8). 
 	The next eight subkeys are obtained the same way, after a 25-bit circular left shift, 
 	and this is repeated until all encryption subkeys are derived.
 
 ***IDEA Round Steps***
 
	The fourteen steps used in the eight full rounds:

	1.	Multiply X1 and the first sub-key Z1
	2.	Add X2 and the second sub-key Z2
	3.	Add X3 and the third sub-key Z3
	4.	Multiply X4 and the fourth sub-key Z4
	5.	Bitwise XOR the results of steps 1 and 3
	6.	Bitwise XOR the results of steps 2 and 4.
	7.	Multiply the result of step 5 and the fifth sub-key Z5
	8.	Add the results of steps 6 and 7
	9.	Multiply the result of step 8 and the sixth sub-key Z6
	10.	Add the results of steps 7 and 9
	11.	Bitwise XOR the results of steps 1 and 9
	12.	Bitwise XOR the results of steps 3 and 9
	13.	Bitwise XOR the results of steps 2 and 10
	14.	Bitwise XOR the results of steps 4 and 10

	The four steps used in the ninth, transformation round:	
	1.	Multiply X1 and the first of the remaining four sub-keys
	2.	Add X2 and the second of the remaining four sub-keys 
	3.	Add X3 and the third of the remaining four sub-keys
	4.	Multiply X4 and the fourth sub-key

 * long max value = 2^63 - 1 = 9223372036854775807
 * 2^128 = 340282366920938463463374607431768211456
 *
 */
public class IDEA {
	
	public static Scanner in;
	public static long plain_text;
	public static BigInteger key;
	
	private static long MOD_ADDITION = 65536;
	private static long MOD_MULTIPLICATION = 65537;
	
	/**
	 * clears the console window
	 */
	public static void clear_console() {
		for (int i = 0; i < 1000; i++) {
			System.out.println("\n");
		}
	}
	
	/**
	 * @param n: a number, power of 2
	 * @return log base 2 of n 
	 * */
	public static long log2(long n) {
		int r = 0;
		while (n > 1) {
			n = n >> 1;
			r++;
		}
		return r;
	}
	
	/**
	 * @param a: first input
	 * @param b: second input
	 * @return (a + b) mod 65536;
	 */
	public static long mod_addition(long a, long b) {
		return (a + b) % MOD_ADDITION;
	}
	
	/**
	 * @param a: first input
	 * @param b: second input
	 * @return (a*b) mod 65537;
	 */
	public static long mod_multiplication(long a, long b) {
		return (a*b) % MOD_MULTIPLICATION;
	}
	
	/**
	 * the 64-bit plaintext block is split into four 16-bit sub-blocks
	 * @return long[] sub_blocks: an array of the four 16-bit sub-blocks
	 * long[] sub_blocks = {block_1, block_2, block_3, block_4}, where 
	 * (block_1 || block_2 || block_3 || block_4) is the 64-bit plain text block
	 */
	public static long[] text_sub_blocks() {
		long[] sub_blocks = new long[4];
		long block_1 = (plain_text & 0xFFFF000000000000L) >> 48;
		sub_blocks[0] = block_1;
		long block_2 = (plain_text & 0x0000FFFF00000000L) >> 32;
		sub_blocks[1] = block_2;
		long block_3 = (plain_text & 0x00000000FFFF0000L) >> 16;
		sub_blocks[2] = block_3;
		long block_4 = plain_text & 0x000000000000FFFFL;
		sub_blocks[3] = block_4;
		return sub_blocks;
	}
	
	/**
	 * Generates the 52 16-bit subkeys used for encryption
	 * 
	 * The 128-bit key is split into eight 16-bit subkeys. Then the bits are shifted to
	 * the left 25 bits. The resulting 128-bit string is split into eight 16-bit blocks that
	 * become the next eight subkeys. The shifting and splitting process is repeated until
	 * 52 subkeys are generated.
	 * @return long[] sub_keys: an array of the 52 16-bit subkeys
	 */
	public static long[] sub_keys() {
		long[] sub_keys = new long[52];
		long[] sub_8 = key_array(key);
		for(int i = 0; i < 6; i++) {
			for (int j = 0; j < 8; j++) {
				int index = i*8 + j;
				sub_keys[index] = sub_8[j];
			}
			long[] new_8 = key_shift(sub_8);
			sub_8 = new_8;
		}
		long[] last_8 = key_shift(sub_8);
		for (int i = 0; i < 4; i++) {
			sub_keys[48+i] = last_8[i];
		}
		return sub_keys;
	}
	
	/**
	 * The 128-bit key is split into eight 16-bit subkeys.
	 * @param full_key: 128-bit key 
	 * @return long[] split_key: an array of the key split into eight 16-bit subkeys
	 */
	public static long[] key_array(BigInteger full_key) {
		
		// take the BigInteger object and turn it into a 128-bit String
		byte[] key_bytes = full_key.toByteArray();
		StringBuilder binary_key_bytes = new StringBuilder();
		for (int i = 0; i < key_bytes.length; i++) {
			String binary_bytes = String.format("%8s", Integer.toBinaryString(key_bytes[i]));
			binary_key_bytes.append(binary_bytes);
		}
		String binary_key = String.format("%128s", binary_key_bytes.toString());
		binary_key = binary_key.replace(' ', '0');
		
		// divide this 128-bit String into eight 16-bit Strings and store as long values
		long[] split_key = new long[8];
		for (int i = 0; i < 8; i++) {
			StringBuilder key_sub_16 = new StringBuilder();
			for (int j = 0; j < 16; j++) {
				int index = 16*i + j;
				key_sub_16.append(Character.toString(binary_key.charAt(index)));
			}
			String key_sub_16_str = String.format("%16s", key_sub_16.toString());
			key_sub_16_str = key_sub_16_str.replace(' ', '0');
			long key_long_16 = new BigInteger(key_sub_16_str, 2).longValue();
			split_key[i] = key_long_16;
		}
		return split_key;
	}
	
	/**
	 * the key bits are shifted to the left 25 bits
	 * @param long[] key: an array of the key split into eight 16-bit subkeys
	 * @return an array of the shifted key split into eight 16-bit subkeys
	 */
	public static long[] key_shift(long[] key) {
		// combine the key into a 128-bit String
		StringBuilder combine_key = new StringBuilder();
		for (int i = 0; i < key.length; i++) {
			String key_part = String.format("%16s", Long.toBinaryString(key[i]));
			key_part = key_part.replace(' ', '0');
			combine_key.append(key_part);
		}
		
		String combined_key = String.format("%128s", combine_key.toString());
		combined_key = combined_key.replace(' ', '0');
		
		// if the 128-bit String is less than 128 bits, throw exception
		if (combined_key.length() < 128) {
			throw new StringIndexOutOfBoundsException("String is not 128 bits!");
		}
		
		// put the string into an array where each element is one bit
		char[] full_key = new char[128];
		for (int i = 0; i < 128; i++) {
			char key_bit = combined_key.charAt(i);
			full_key[i] = key_bit;
		}
		
		// shift the key to the left by 25
		char[] shift_key = new char[128];
		for (int i = 25; i < 128; i++) {
			shift_key[i-25] = full_key[i];
		}
		for (int i = 0, j = 103; i < 25 && j < 128; i++, j++) {
			shift_key[j] = full_key[i];
		}
		
		// convert the array into a string 
		StringBuilder shift_key_build = new StringBuilder();
		for (int i = 0; i < 128; i++) {
			shift_key_build.append(shift_key[i]);
		}
		String shift_key_str = String.format("%128s", shift_key_build.toString()); 
		
		// convert the string into a BigInteger Object
		BigInteger shifted_key = new BigInteger(shift_key_str);
		
		// Split the 128-bit BigInteger into eight 16-bit sub blocks 
		return key_array(shifted_key);
	
	}
	
	/**
	 * gets the plain text to be encrypted from the user
	 */
	public static void get_plain_text() {
		// While loop to collect the 64-bit plain text
		while (true) {
			System.out.print("64-bit number to encrypt: ");
			try {
				String input = in.next();
				plain_text = Long.parseLong(input);
				break;
			}
			catch (NumberFormatException e) {
				clear_console();
				System.out.println("Plain text must be less than 64 bits!");
			}
		}
	}
	
	/**
	 * gets the key to be used for encryption from the user
	 */
	public static void get_key() {
		// While loop to collect the 128-bit key
		while (true) {
			System.out.print("128-bit key used to encrypt: ");
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
	 * prints a 64-bit representation of the plain text
	 */
	public static void print_plain_text(){
		String binary_plain_text = String.format("%64s", Long.toBinaryString(plain_text));
		binary_plain_text = binary_plain_text.replace(' ', '0');
		System.out.printf("\nEncrypting bits: %s\n", binary_plain_text);
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
	
	/**
	 * @return the 64-bit cipher text
	 */
	public static int encrypt() {
		long[] text_sub_blocks = text_sub_blocks();
		long x1 = text_sub_blocks[0];
		long x2 = text_sub_blocks[1];
		long x3 = text_sub_blocks[2];
		long x4 = text_sub_blocks[3];
		
		return 0;
	}
	
	public static void main(String[] args) {
		in = new Scanner(System.in);
		
		get_plain_text();
		get_key();
				
		in.close();
		
		// Print the bits being used
		print_plain_text();
		print_key();	
		
		long[] new_key = key_array(key);
		long[] shift_key = key_shift(new_key);
	}

}
