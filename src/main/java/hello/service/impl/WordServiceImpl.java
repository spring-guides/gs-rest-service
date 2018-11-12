package hello.service.impl;

import org.springframework.stereotype.Service;

import hello.service.IWordService;
import hello.word.dto.ResponseDTO;
import hello.words.util.CommonUtil;

/**
 * @author akash
 *
 */
@Service("IWordService")
public class WordServiceImpl implements IWordService {

	/* (non-Javadoc)
	 * @see hello.service.IWordService#processWord(java.lang.String)
	 */
	@Override
	public ResponseDTO processWord(String word) {
		ResponseDTO dto =  new ResponseDTO();
		dto.setPalindrome(CommonUtil.isPalindrome(word));
		dto.setAnagramOfPalindrome(CommonUtil.isAnagramPalindrome(word));
		dto.setWord(word);
		return dto;
	}
}
