import java.math.BigInteger;

/**
 * 
 * @author Benjamin Hodgson
 * @date 12/20/17
 *
 ****Key Scheduling***
 
 	IDEA uses 52 sub-keys, each 16 bits long.
 
 	The 128-bit key of IDEA is taken as the first eight sub-keys, K(1) through K(8). 
 	The next eight sub-keys are obtained the same way, after a 25-bit circular left shift, 
 	and this is repeated until all encryption sub-keys are derived.
 	
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

public class IDEA_decrypt {
	private static long cipher_text;
	private static BigInteger key;
	private static long decrypted_text;
	
	private static long MOD_ADDITION = 65536;
	private static long MOD_MULTIPLICATION = 65537;
	
	// constructor
	public IDEA_decrypt(long decrypt_text, BigInteger decrypt_key) {
		cipher_text = decrypt_text;
		key = decrypt_key;
		decrypt();
	}
	
	/**
	 * @return the decrypted text
	 */
	public long get_decrypted_text() {
		return decrypted_text;
	}
	
	/**
	 * Prints the key set used for decryption
	 */
	public void print_key_set() {
		long[] key_set = decrypt_keys();
		print_key_set(key_set);
	}
	
	/**
	 * prints the keyset consisting of 16-bit subkeys 
	 * 
	 * @param keys: and array of 52 long values corresponding to the sub-keys
	 */
	private static void print_key_set(long[] keys) {
		/*
		if (keys.length != 52) {
			throw new ArrayIndexOutOfBoundsException("Failed to generate 52 sub-keys!");
		}*/
		System.out.printf("Printing decryption subkeys:\n");
		
		for (int i = 0; i < keys.length; i++) {
			System.out.printf("Key %d: %d\n", i+1, keys[i]);
		}	
	}
	
	/**
	 * Prints the concatenated text block from parameter text
	 * 
	 * @param long[] text: array with each element 16-bit text 
	 */
	private static void print_text_blocks(long[] text) {
		StringBuilder text_blocks = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			long num = text[i];
			String bit_str = String.format("%16s", Long.toBinaryString(num));
			bit_str = bit_str.replace(' ', '0');
			text_blocks.append(bit_str);
		}
		String ret = String.format("%64s", text_blocks.toString());
		ret = ret.replace(' ', '0');
		System.out.printf("%s\n", ret);
	}
	
	/**
	 * @param a: first input
	 * @param b: second input
	 * @return (a + b) mod 65536;
	 */
	private static long mod_addition(long a, long b) {
		return (a + b) % MOD_ADDITION;
	}
	
	/**
	 * @param a: first input
	 * @param b: second input
	 * @return (a*b) mod 65537;
	 */
	private static long mod_multiplication(long text_block, long key_block) {
		// if key_block is 0, reassign it's value so it becomes it's own inverse
		if (key_block == 0) {
			key_block = MOD_MULTIPLICATION - 1;
		}
		if (text_block == 0) {
			return 0;
		}
		else {
			return (text_block*key_block) % MOD_MULTIPLICATION;
		}
	}
	
	/**
	 * @param a: the long value we wish to find the inverse of
	 * @return the multiplicative inverse of a mod MOD_MULTIPLICATION, 65537
	 * 
	 * Since MOD_MULTIPLICATION is a prime number, by Fermat's Little Theorem we have
	 * a^(p - 1) = 1 (mod p). 
	 * Multipy both by  a^(-1)
	 * a^(-1) = a^(p - 2) (mod p)
	 * 
	 * So the inverse of parameter a mod a prime p can be found by exponentiation 
	 */
	private static long multi_inverse(long a) {
		// In IDEA 0 is treated as 65536 which is it's own inverse mod 65537
		if (a == 0) {
			return MOD_MULTIPLICATION - 1;
		}
		else {
			return exp_long(a, MOD_MULTIPLICATION-2) % MOD_MULTIPLICATION;
		}
	}
	
	/**
	 * Performs fast exponentiation using exponentiation by squaring
	 * 
	 * @param a: the base
	 * @param n: the exponent
	 * @return a^(n) mod MOD_MULTIPLICATION 
	 */
	private static long exp_long(long a, long n) {
		if (n == 0) {
			return 1;
		}
		if (n == 1) {
			return a;
		}
		if (n % 2 == 0) {
			return exp_long(a*a % MOD_MULTIPLICATION, n/2) % MOD_MULTIPLICATION;
		}
		else {
			return a*exp_long(a*a % MOD_MULTIPLICATION, n/2) % MOD_MULTIPLICATION;
		}
	}
	
	/***************************************************************************************
	 * Methods below are presented in execution order 
	/***************************************************************************************
	
	/**
	 * Generates the decrypted text from the cipher text block using the key
	 */
	private static void decrypt() {
		// gather the sub keys and the text sub blocks for encryption
		long[] text_sub_blocks = text_sub_blocks();
		long[] key_set = decrypt_keys();
		
		//print_key_set(key_set);
		//System.out.printf("Decyrpting text:\t");
		//print_text_blocks(text_sub_blocks);
		
		// encrypt the full eight rounds
		for (int i = 0; i < 8; i++) {
			// swap the plain text sub blocks
			long[] new_text = decrypt_round(text_sub_blocks, key_set, i);
			//System.out.printf("Decyrpting text:\t");
			//print_text_blocks(new_text);
			text_sub_blocks = new_text;
		}
		
		// encrypt the ninth, half round
		long[] decrypted_text_long = decrypt_half_round(text_sub_blocks, key_set);
		
		//System.out.printf("Decyrpting text:\t");
		//print_text_blocks(decrypted_text_long);
		
		StringBuilder decrypted_text_build = new StringBuilder();
		for (int i = 0; i < decrypted_text_long.length; i++) {
			
			// MOD_MULTIPLICATION - 1 == 65536
			// 65536 represents 16 bits containing all 0's
			if(decrypted_text_long[i] == MOD_MULTIPLICATION - 1) {
				decrypted_text_long[i] = 0;
			}
			
			String decrypted_bytes = String.format("%16s", Long.toBinaryString(decrypted_text_long[i]));
			decrypted_bytes = decrypted_bytes.replace(' ', '0');
			decrypted_text_build.append(decrypted_bytes);
		}
		
		// make a 64-bit String from the StringBuilder
		String decrypted_text_str = String.format("%64s", decrypted_text_build.toString());
		decrypted_text_str = decrypted_text_str.replace(' ', '0');
		
		// parse the long from the 2's complement binary string cipher_text_str
		decrypted_text = new BigInteger(decrypted_text_str, 2).longValue();
	}
	
	/**
	 * The 64-bit plaintext block is split into four 16-bit sub-blocks
	 * 
	 * @return long[] sub_blocks: an array of the four 16-bit sub-blocks
	 * long[] sub_blocks = {block_1, block_2, block_3, block_4}, where 
	 * (block_1 || block_2 || block_3 || block_4) is the 64-bit plain text block
	 */
	private static long[] text_sub_blocks() {
		long[] sub_blocks = new long[4];
		long block_1 = (cipher_text & 0xFFFF000000000000L) >>> 48;
		sub_blocks[0] = block_1;
		long block_2 = (cipher_text & 0x0000FFFF00000000L) >>> 32;
		sub_blocks[1] = block_2;
		long block_3 = (cipher_text & 0x00000000FFFF0000L) >>> 16;
		sub_blocks[2] = block_3;
		long block_4 = cipher_text & 0x000000000000FFFFL;
		sub_blocks[3] = block_4;
		return sub_blocks;
	}
	
	/**
	 * Generates the 52 16-bit subkeys used for decryption
	 * 
	 * The 128-bit key is split into eight 16-bit subkeys. Then the bits are shifted to
	 * the left 25 bits. The resulting 128-bit string is split into eight 16-bit blocks that
	 * become the next eight subkeys. The shifting and splitting process is repeated until
	 * 52 subkeys for encryption are generated. Then the decryption keys are generated from 
	 * this key set by the IDEA protocol outlined at the top. 
	 * 
	 * @return long[] decrypt_keys: an array of the 52 16-bit subkeys used for decryption
	 */
	private static long[] decrypt_keys() {
		long[] encrypt_keys = sub_keys();
		long[] decrypt_keys = new long[52];
		
		for (int i = 0; i < 4; i++) {
			long encrypt_key = encrypt_keys[48+i];
					
			long decrypt_key;
			if (i == 1 || i == 2) {
				decrypt_key = MOD_ADDITION - encrypt_key;
			}
			else {
				decrypt_key = multi_inverse(encrypt_key);
				//System.out.printf("decrypt key: %d\n", decrypt_key);
			}
			decrypt_keys[i] = decrypt_key;
		}
		
		for (int i = 0, j = 8; i < 8 && j > 0; i++, j--) {
			decrypt_keys[6*i + 4] = encrypt_keys[j*6 - 2];
			decrypt_keys[6*i + 5] = encrypt_keys[j*6 - 1];
			decrypt_keys[6*i + 6] = multi_inverse(encrypt_keys[j*6 - 6]);
			decrypt_keys[6*i + 7] = MOD_ADDITION - encrypt_keys[j*6 - 4];
			decrypt_keys[6*i + 8] = MOD_ADDITION - encrypt_keys[j*6 - 5];
			decrypt_keys[6*i + 9] = multi_inverse(encrypt_keys[j*6 - 3]);
		}
		return decrypt_keys;
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
	private static long[] sub_keys() {
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
		for (int i = 0; i < 4; i++) {
			sub_keys[48+i] = sub_8[i];
		}
		return sub_keys;
	}
	
	/**
	 * The 128-bit key is split into eight 16-bit subkeys
	 * 
	 * @param full_key: 128-bit key 
	 * @return long[] split_key: an array of the key split into eight 16-bit subkeys
	 */
	private static long[] key_array(BigInteger full_key) {
		
		// take the BigInteger object and turn it into a 128-bit String
		String binary_key = String.format("%128s", full_key.toString(2));
		binary_key = binary_key.replace(' ', '0');
		//System.out.printf("Binary representation of the key: %s", binary_key);
		
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
	 * The key bits are shifted to the left 25 bits
	 * 
	 * @param long[] key: an array of the key split into eight 16-bit subkeys
	 * @return an array of the shifted key split into eight 16-bit subkeys
	 */
	private static long[] key_shift(long[] key) {
		// combine the key into a 128-bit String
		StringBuilder combine_key = new StringBuilder();
		for (int i = 0; i < key.length; i++) {
			String key_part = String.format("%16s", Long.toBinaryString(key[i]));
			key_part = key_part.replace(' ', '0');
			//System.out.printf("Key part %d: %d := %s\n", i, key[i], key_part);
			combine_key.append(key_part);
		}
		
		String combined_key = String.format("%128s", combine_key.toString());
		combined_key = combined_key.replace(' ', '0');
		//System.out.printf("Combined key: %s\n", combined_key);
		
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
		for (int i = 0; i < 25; i++) {
			full_key = shift_one(full_key);
		}
		
		char[] shift_key = full_key;
		
		// convert the array into a string 
		StringBuilder shift_key_build = new StringBuilder();
		for (int i = 0; i < 128; i++) {
			shift_key_build.append(shift_key[i]);
		}
		String shift_key_str = String.format("%128s", shift_key_build.toString()); 
		shift_key_str = shift_key_str.replace(' ', '0');
		//System.out.printf("Shift result: %s\n", shift_key_str);
		
		// convert the string into a BigInteger Object
		BigInteger shifted_key = new BigInteger(shift_key_str, 2);
		
		// Split the 128-bit BigInteger into eight 16-bit sub blocks 
		return key_array(shifted_key);
	}
	
	/**
	 * Performs one shift
	 * 
	 * @param char[] bit_string: a string of bits
	 * @return bit_string circularly shifted one to the left
	 */
	private static char[] shift_one(char[] bit_string) {
		char first_bit = bit_string[0];
		for (int i = 1; i < bit_string.length; i++) {
			bit_string[i-1] = bit_string[i];
		}
		bit_string[bit_string.length - 1] = first_bit;
		return bit_string;
	}
	
	/**
	 * Performs a single full round of IDEA as outlined in the code header. 
	 * 
	 * @param long[] text_sub_blocks: array containing text blocks to be decrypted 
	 * @param long[] key_set: array containing the 52 subkeys for decryption
	 * @param int round_num: current round number. Used to index into the key_set
	 * @return long[] new_text: array containing the decrypted text blocks
	 */
	private static long[] decrypt_round(long[] text_sub_blocks, long[] key_set, int round_num) {
		// assign the sub blocks to variables
		long x1 = text_sub_blocks[0];
		long x2 = text_sub_blocks[1];
		long x3 = text_sub_blocks[2];
		long x4 = text_sub_blocks[3];
		
		// obtain the necessary keys;
		int index = round_num*6;
		
		long z1 = key_set[index];
		long z2 = key_set[index + 1];
		long z3 = key_set[index + 2];
		long z4 = key_set[index + 3];
		long z5 = key_set[index + 4];
		long z6 = key_set[index + 5];
		
		// initialize the mangler variables
		long m1;
		long m2;
		
		//System.out.printf("Key z2: %d\nText x2: %d\n", z2, x2);
		
		// perform the steps in the round
		x1 = mod_multiplication(x1, z1);
		x2 = mod_addition(x2, z2);
		x3 = mod_addition(x3, z3);
		x4 = mod_multiplication(x4, z4);
		
		//long[] print_text = {x1, x2, x3, x4};
		//System.out.printf("Round %d:\t\t", round_num + 1);
		//print_text_blocks(print_text);
		
		m1 = x1 ^ x3;
		m2 = x2 ^ x4;
		m1 = mod_multiplication(m1, z5);
		m2 = mod_addition(m2, m1);
		m2 = mod_multiplication(m2, z6);
		m1 = mod_addition(m1, m2);
		x1 = x1 ^ m2;
		x3 = x3 ^ m2;
		x2 = x2 ^ m1;
		x4 = x4 ^ m1;
		
		// swap the return text sub-blocks, unless it is the last full round
		if (round_num == 7) {
			long[] new_text = {x1, x2, x3, x4};
			return new_text;
		}
		else {
			long[] new_text = {x1, x3, x2, x4};
			return new_text;
		}
	}
	
	/**
	 * Performs the last, half-round of IDEA as outlined in the code header.
	 * 
	 * @param long[] text_sub_blocks: array containing text blocks to be decrypted 
	 * @param long[] key_set: array containing the 52 subkeys for decryption
	 * @return long[] new_text: array containing the decrypted text blocks
	 */
	private static long[] decrypt_half_round(long[] text_sub_blocks, long[] key_set) {
		// assign the sub blocks to variables
		long x1 = text_sub_blocks[0];
		long x2 = text_sub_blocks[2];
		long x3 = text_sub_blocks[1];
		long x4 = text_sub_blocks[3];
		
		// obtain the necessary keys;
		long z1 = key_set[48];
		long z2 = key_set[49];
		long z3 = key_set[50];
		long z4 = key_set[51];
		
		x1 = mod_multiplication(x1, z1);
		x2 = mod_addition(x2, z2);
		x3 = mod_addition(x3, z3);
		x4 = mod_multiplication(x4, z4); 
		
		// return the concatenation of these blocks
		long[] new_text = {x1, x3, x2, x4};
		return new_text;
	}
}
