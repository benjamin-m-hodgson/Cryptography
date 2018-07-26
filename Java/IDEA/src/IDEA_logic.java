import java.math.BigInteger;

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
public class IDEA_logic {

	// initialize the encoding object to convert between text and binary
    private final IDEA_encoding encodings = new IDEA_encoding();
	private boolean encrypt;
	private String keyFormat;
	private String key;
	private String dataFormat;
	private String data;

	public IDEA_logic() {

	}

	public void setEncrypt() {
		encrypt = true;
	}

	public void setDecrypt() {
		encrypt = false;
	}

	public void setKeyBin() {
		keyFormat = "bin";
	}

	public void setKeyHex() {
		keyFormat = "hex";
	}

	public void setKeyTxt() {
		keyFormat = "txt";
	}

	public boolean processKey(String keyInput) {
		// handle the binary input case
		if (keyFormat.equalsIgnoreCase("bin")) {
			// no processing necessary
			key = keyInput.replace(' ', '0');
			// format the key with correct length
			while (key.length() % 128 != 0) {
				key = "0" + key;
			}

			//System.out.printf("key in: %s\n", key);
		}
		// handle the hex input case
		else if (keyFormat.equalsIgnoreCase("hex")) {
			// convert the input string to upper case 
			key = keyInput.toUpperCase();
			//System.out.printf("key in: %s\n", key);

			// Loop through each hex character, generate binary string
			StringBuilder hex_append = new StringBuilder();
			for (int i = 0; i < key.length(); i++) {
				// convert the hex character to it's integer value
				String hex_str = key.substring(i, i + 1);
				int val = Integer.parseInt(hex_str, 16);
				// convert the integer to it's 4-bit binary representation
				hex_str = String.format("%4s", Integer.toBinaryString(val));
				hex_str = hex_str.replace(' ', '0');
				// append to the StringBuilder
				hex_append.append(hex_str);

			}
			key = hex_append.toString();
			// format the key with correct length
			while (key.length() % 128 != 0) {
				key = "0" + key;
			}
		}
		// handle the alphanumeric input case using encoding object
		else if (keyFormat.equalsIgnoreCase("txt")) {
			key = keyInput;
			
			// format the key with correct length 
			while (key.length() % 16 != 0) {
				key = "0" + key;
			}
			//System.out.printf("key in: %s\n", key);

			StringBuilder key_bin = new StringBuilder();
			for (int i = 0; i < key.length(); i++) {	
				String key_str = key.substring(i, i + 1);
				String bin_str = encodings.get_bin(key_str);
				key_bin.append(bin_str);
			}
			key = key_bin.toString();
		}
		else {
			// TODO better error checking here
			System.out.printf("Format error encountered");
			return false;
		}

		// make sure key is proper length
		if (key.length() > 128) {
			key = key.substring(0, 128);
		}
		if (key.length() < 128) {
			System.out.printf("Key should be 128 bits! Currently only %d\n", key.length());
		}
		return true;
	}

	public void setDataBin() {
		dataFormat = "bin";
	}

	public void setDataHex() {
		dataFormat = "hex";
	}

	public void setDataTxt() {
		dataFormat = "txt";
	}
	
	/**
	 * Takes the input and converts it to a base 2 binary string
	 */
	public boolean processData(String inputData) {		
		
		// handle the binary input case
		if (dataFormat.equalsIgnoreCase("bin")) {
			// no processing necessary
			data = inputData.replace(' ', '0');
			
			while (data.length() % 64 != 0) {
				data = "0" + data;
			}
			System.out.printf("data in: %s\n", data);
			return true;
		}
		
		// handle the hex input case
		else if (dataFormat.equalsIgnoreCase("hex")) {
			// convert the input string to lower case 
			data = inputData.toLowerCase();
			// format the data correctly
			while (data.length() % 16 != 0) {
				data = "0" + data;
			}
			System.out.printf("data in: %s\n", data);
			
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
			return true;
		}
		
		// handle the alphanumeric input case using encoding object
		else if (dataFormat.equalsIgnoreCase("txt")) {
			data = inputData;
			
			// format the data correctly 
			while (data.length() % 8 != 0) {
				data = "0" + data;
			}
			System.out.printf("data in: %s\n", data);
			
			StringBuilder data_bin = new StringBuilder();
			for (int i = 0; i < data.length(); i++) {	
				String data_str = data.substring(i, i + 1);
				String bin_str = encodings.get_bin(data_str);
				data_bin.append(bin_str);
			}
			data = data_bin.toString();
			return true;
		}
		
		else {
			// TODO better error checking here
			System.out.printf("Format error encountered");
			return false;
		}
	}
	
	/**
	 * Formats the output to match the input format
	 */
	public String execute() {
		String output = processOutput();
		// Display the data bit string to be encrypted/decrypted
		if (dataFormat.equalsIgnoreCase("hex") ||
				dataFormat.equalsIgnoreCase("txt")) {
			System.out.printf("binary out: %s\n", output);
		}
		
		if (dataFormat.equalsIgnoreCase("bin")) {
			output = output.trim();
		}
		else if (dataFormat.equalsIgnoreCase("hex")) {
			StringBuilder out_str = new StringBuilder();
			for (int i = 0; i < output.length() / 4; i++) {
				String out_sub = output.substring(i*4, (i + 1)*4);
				Long out_val = Long.parseUnsignedLong(out_sub, 2);
				String out_block = Long.toString(out_val, 16);
				//System.out.printf("out_block: %s\n", out_block);
				out_block = out_block.toLowerCase();
				out_str.append(out_block);
			}
			output = out_str.toString();
		}
		else if (dataFormat.equalsIgnoreCase("txt")) {	
			StringBuilder out_str = new StringBuilder();
			// encoding byte is 8 bits, so iterate over 8-bit blocks
			for (int i = 0; i < output.length() / 8; i++) {
				String bit_sub = output.substring(i*8, (i + 1)*8);
				String str_sub = encodings.get_str(bit_sub);
				out_str.append(str_sub);
			}
			output = out_str.toString();
 		}
		else {
			// TODO better error checking here
			System.out.printf("Format error encountered");
			return "Critical format error encountered";
		}
		System.out.printf("Output data: %s\n", output);
		return output;
	}
	
	/**
	 * Pads the input data so it's a bit-string with length divisible by 64.
	 * Calls the method to process encryption or decryption depending upon 
	 * @param op type.
	 */
	private String processOutput() {
		int pad_num = 64 - (data.length() % 64);
		
		// make sure the bit length of data is divisible by 64
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
		if (dataFormat.equalsIgnoreCase("hex") ||
				dataFormat.equalsIgnoreCase("txt")) {
			System.out.printf("binary in: %s\n", data);
		}
		
		// Check if op is dec, if so decrypt 
		if (encrypt) {
			return executeEncrypt();
		}
		else {
			return executeDecrypt();
		}
	}
	
	/**
	 * Assume at this point the data bit length is a multiple of 64.
	 */
	private String executeDecrypt() {
		// create a StringBuilder to build output binary string 
		StringBuilder out_add = new StringBuilder();
		
		// loop through data, process 64-bit blocks at a time
		for (int i = 0; i < data.length() / 64; i++) {
			// gather the data
			String data_block = data.substring(i*64, (i+1)*64);
			long data_val = Long.parseUnsignedLong(data_block, 2);
			BigInteger key_val = new BigInteger(key, 2);
			
			//System.out.printf("In long val: %d\n", data_val);
			
			// decrypt the data
			IDEA_decrypt decrypt_block = new IDEA_decrypt(data_val, key_val);
			long dec_val = decrypt_block.get_decrypted_text();
			
			//System.out.printf("Out long val: %d\n", dec_val);
			
			// convert data to binary
			String dec_str = String.format("%64s", Long.toBinaryString(dec_val));
			dec_str = dec_str.replace(' ', '0');
			
			// append data
			out_add.append(dec_str);
		}
		return out_add.toString();
	}
	
	/**
	 * Assume at this point the data bit length is a multiple of 64.
	 */
	private String executeEncrypt() {
		// create a StringBuilder to build output binary string 
		StringBuilder out_add = new StringBuilder();
		
		// loop through data, process 64-bit blocks at a time
		for (int i = 0; i < data.length() / 64; i++) {
			// gather the data
			String data_block = data.substring(i*64, (i+1)*64);
			long data_val = Long.parseUnsignedLong(data_block, 2);
			BigInteger key_val = new BigInteger(key, 2);
			
			//System.out.printf("In long val: %d\n", data_val);
			
			// encrypt the data
			IDEA_encrypt encrypt_block = new IDEA_encrypt(data_val, key_val);
			long enc_val = encrypt_block.get_cipher_text();
			
			//System.out.printf("Out long val: %d\n", enc_val);
			
			// convert data to binary
			String enc_str = String.format("%64s", Long.toBinaryString(enc_val));
			enc_str = enc_str.replace(' ', '0');
			// append data
			out_add.append(enc_str);
		}
		return out_add.toString();
	}


}
