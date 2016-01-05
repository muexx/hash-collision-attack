import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextTemplate {
	private int n;
	private String[][] templates;
	private String text;

	/**
	 * @param n
	 */
	public TextTemplate(String filepath, int n) {

		this.n = n;
		this.templates = new String[n][2];
		String text = "";

		// read text file
		try {
			Scanner in = new Scanner(new FileReader(filepath));
			while (in.hasNextLine()) {
				text += in.nextLine();
			}
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// find pattern
		Pattern pattern = Pattern.compile("\\[(.*?)\\]");
		Matcher m = pattern.matcher(text);
		int i = 0;
		while (m.find()) {
			if (i < n) {
				String s = m.group(1);
				String[] parts = s.split(Pattern.quote("|"));
				this.templates[i][0] = parts[0];
				this.templates[i][1] = parts[1];
			}
			i++;
		}

		// replace pattern
		this.text = text.replaceAll("\\[(.*?)\\]", "%s");
	}

	public String getText(int configuration) {

		String[] template = new String[this.n];
		for (int i = 0; i < this.n; i++) {
			// read configuration
			int option = getBit(configuration, i);
			template[i] = this.templates[i][option];
		}

		String resultText = String.format(text, (Object[]) template);

		return resultText;
	}
	
	public static int getBit(int n, int k) {
		/*
		n
		100010101011101010 (example)
		n >> 5
		000001000101010111 (all bits are moved over 5 spots, therefore
		&                   the bit you want is at the end)
		000000000000000001 (0 means it will always be 0,
		=                   1 means that it will keep the old value)
		1
		*/
	    return (n >> k) & 1;
	}
		
}