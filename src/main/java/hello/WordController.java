package hello;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.service.IWordService;
import hello.word.dto.ResponseDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/** 
 * Rest controller used to deals with words APIS
 * 
 * 
 * ***/
@RestController
@RequestMapping("/words")
public class WordController {

	@Autowired
	private IWordService iWordService;

	@ApiOperation(value = "Check word is palidrom or Anagram of palidrom", response = ResponseDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Response received"),
	@ApiResponse(code = 500, message = "Internal server error occur "),
	@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
	@ApiResponse(code = 400, message = "Bad request") })
	
	@GetMapping(value = "/{word}", produces = "application/json")
	private ResponseEntity<ResponseDTO> checkWord(@PathVariable("word") @NotBlank @Validated String word) {
		return new ResponseEntity<>(iWordService.processWord(word),HttpStatus.OK);
	}
}
