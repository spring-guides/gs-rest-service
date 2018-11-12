package hello.words.util;

/**
 * @author akash
 *
 */
public class CommonUtil {
	
	
	/**
	 * 
	 * This method check taken parameter a String and check is it sAnagramPalindrome 
	 * or not .
	 * 
	 * **/
	public static boolean isAnagramPalindrome(String word) {
		int oddOccur = 0;
		int[] count = new int[256];

		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			count[ch]++;
		}

		for (int cnt : count) {
			if (oddOccur > 1) {
				return false;
			}
			if (cnt % 2 == 1) {
				oddOccur++;
			}
		}
		return true;
	}


	/**
	 * 
	 * This method check taken parameter a String and check is it Palindrome 
	 * or not .
	 * 
	 * **/
	
	public static boolean isPalindrome(String word) {
		int length = word.length();
		String reverse = "";
		for (int i = length - 1; i >= 0; i--)
			reverse = reverse + word.charAt(i);
	
		if (word.toLowerCase().equals(reverse.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}
}
