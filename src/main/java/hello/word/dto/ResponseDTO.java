package hello.word.dto;

/**
 * @author akash
 *
 */
public class ResponseDTO {
	private String word;
    private boolean palindrome;
    private boolean anagramOfPalindrome;

    /**
     * @return
     */
    public String getWord() {
		return word;
	}
	/**
	 * @param word
	 */
	public void setWord(String word) {
		this.word = word;
	}
	/**
	 * @return
	 */
	public boolean isPalindrome() {
		return palindrome;
	}
	/**
	 * @param palindrome
	 */
	public void setPalindrome(boolean palindrome) {
		this.palindrome = palindrome;
	}
	/**
	 * @return
	 */
	public boolean isAnagramOfPalindrome() {
		return anagramOfPalindrome;
	}
	/**
	 * @param anagramOfPalindrome
	 */
	public void setAnagramOfPalindrome(boolean anagramOfPalindrome) {
		this.anagramOfPalindrome = anagramOfPalindrome;
	} 
}
