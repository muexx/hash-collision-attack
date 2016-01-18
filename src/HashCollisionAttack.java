import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
import java.util.HashMap;

public class HashCollisionAttack {

	public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException {

		//ArrayList<Integer> hashList = new ArrayList<Integer>();
		HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();

		int n = 16;

		// TODO Exception handling
		TextTemplate textA = new TextTemplate("src/textA.txt", n);
		TextTemplate textB = new TextTemplate("src/textB.txt", n);

		double number = Math.pow(2, n);
		
		final long timeStart = System.currentTimeMillis(); 

		// calculate all hash values for the first text
		for (int i = 0; i < number; i++) {
			byte[] hash = generateHash(textA.getText(i));
			int hashAsInt = java.nio.ByteBuffer.wrap(hash).getInt();
			//hashList.add(i, hashAsInt);
			hashMap.put(hashAsInt, i);

		}

		boolean collision = false;
		// calculate the hash values for the second text and test collisions
		for (int i = 0; i < number; i++) {
			byte[] hash = generateHash(textB.getText(i));
			int hashAsInt = java.nio.ByteBuffer.wrap(hash).getInt();

			//if (hashList.contains(hashAsInt)) {
			if (hashMap.containsKey(hashAsInt)) {	
				collision = true;

				System.out.print("Hash with collision: ");
				for (byte b : hash)
					System.out.printf("%02x", b);
				System.out.println();

				//int configOfA =  hashList.indexOf(hashAsInt);
				int configOfA =  hashMap.get(hashAsInt);
				
				System.out.println("Configuraton text A: "+   String.format("%16s", Integer.toBinaryString(configOfA)).replace(' ', '0')+" : "+configOfA);
				System.out.println("Configuraton text B: "+  String.format("%16s", Integer.toBinaryString(i)).replace(' ', '0') +" : "+i);

				
				System.out.println("\n\n Version of text A: ********************************************************");
				System.out.println(textA.getText(configOfA));
				
				System.out.println("\n\n Version of text B: ********************************************************");
				System.out.println(textB.getText(i));
				
				// stop after the first collision
				//break;
			}

		}

		final long timeEnd = System.currentTimeMillis();
        
		
		if (collision) {
			System.out.println("Kollision gefunden");
		} else {
			System.out.println("keine Kollision gefunden");
		}

		System.out.println("Laufszeit: " + (timeEnd - timeStart) + " Millisek."); 		
		
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

}
