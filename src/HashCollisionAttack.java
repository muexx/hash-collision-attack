import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class HashCollisionAttack {

	public static void main(String[] args) throws NoSuchAlgorithmException {

		ArrayList<Integer> digests = new ArrayList<Integer>();

		int n = 16;

		TextTemplate textA = new TextTemplate("src/textA.txt", n);
		TextTemplate textB = new TextTemplate("src/textB.txt", n);

		double number = Math.pow(2, n);
		
		final long timeStart = System.currentTimeMillis(); 

		// calculate all hash values for the first text
		for (int i = 0; i < number; i++) {
			byte[] hash = generateHash(textA.getText(i));
			int hashAsInt = java.nio.ByteBuffer.wrap(hash).getInt();
			digests.add(i, hashAsInt);

		}

		boolean collision = false;
		// calculate the hash values for the second text and test collisions
		for (int i = 0; i < number; i++) {
			byte[] hash = generateHash(textB.getText(i));
			int hashAsInt = java.nio.ByteBuffer.wrap(hash).getInt();

			if (digests.contains(hashAsInt)) {
				collision = true;

				System.out.print("Hash with collision: ");
				for (byte b : hash)
					System.out.printf("%02x", b);
				System.out.println();

				int configOfB =  digests.indexOf(hashAsInt);
				System.out.println("Configuraton text A: "+ configOfB +" : "+ Integer.toBinaryString(configOfB));

				System.out.println("Configuraton text B: "+ i +" : "+ Integer.toBinaryString(i));
				System.out.println();

				// stop after the first collision
				break;
			}

		}

		final long timeEnd = System.currentTimeMillis();
        
		
		if (collision) {
			System.out.println("Kollision gefunden");
		} else {
			System.out.println("keine Kollision gefunden");
		}

		System.out.println("Laufszeit: " + (timeEnd - timeStart) + " Millisek."); 
		
		
		byte[] hashA = generateHash(textA.getText(20914));
		int hashAsIntA = java.nio.ByteBuffer.wrap(hashA).getInt();
		
		byte[] hashB = generateHash(textB.getText(61914));
		int hashAsIntB = java.nio.ByteBuffer.wrap(hashB).getInt();
		
		System.out.println(hashAsIntA+" : "+hashAsIntB);
		System.out.println(Integer.toBinaryString(hashAsIntA));
		System.out.println(Integer.toBinaryString(hashAsIntB));
		System.out.println(Integer.toHexString(hashAsIntA));
		System.out.println(Integer.toHexString(hashAsIntB));
	}

	public static byte[] generateHash(String text) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] digest = md.digest(text.getBytes());

		digest = fould(digest);
		return digest;
	}

	public static byte[] fould(byte[] input) {
		byte[] result = new byte[4];

		for (int i = 0; i < input.length; i++) {
			int rest = i % 4;

			switch (rest) {
			case 0:
				result[0] = (byte) (result[0] ^ input[i]);
				break;
			case 1:
				result[1] = (byte) (result[1] ^ input[i]);
				break;
			case 2:
				result[2] = (byte) (result[2] ^ input[i]);
				break;
			case 3:
				result[3] = (byte) (result[3] ^ input[i]);
				break;
			default:
				break;
			}
		}
		return result;
	}

	// print byte array in bits
	// for (byte b : bytearray) System.out.println(Integer.toBinaryString(b &
	// 255 | 256).substring(1));

	// print byte array as hex number
	// for ( byte b : bytearray ) System.out.printf( "%02x", b );

	/*
	 * // print bits from a byte byte x = (byte) 5; byte y = (byte) 16; byte d =
	 * (byte) (x ^ y); System.out.println(Integer.toBinaryString(d & 255 |
	 * 256).substring(1));
	 */

	/*
	 * // get integer from an byte array byte[] result = new byte[4]; int q =
	 * java.nio.ByteBuffer.wrap(result).getInt(); System.out.println(q);
	 */

}
