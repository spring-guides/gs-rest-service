package hello.service;

import org.springframework.stereotype.Service;

import hello.word.dto.ResponseDTO;


public interface IWordService {
	public ResponseDTO processWord(String word);
}
