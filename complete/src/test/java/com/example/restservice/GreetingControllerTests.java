/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.restservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class GreetingControllerTests {

	/* O trecho destacado na classe GreetingControllerTests utiliza a anotação @Autowired para injetar uma 
	instância de MockMvc. Essa classe é usada para simular requisições HTTP e testar endpoints do controlador 
	GreetingController sem a necessidade de iniciar um servidor real.
	No contexto da classe, o MockMvc é usado em dois testes:

	noParamGreetingShouldReturnDefaultMessage: Verifica se o endpoint /greeting retorna a mensagem padrão "Hello, World!" quando nenhum parâmetro é fornecido.
	paramGreetingShouldReturnTailoredMessage: Verifica se o endpoint retorna uma mensagem personalizada quando o parâmetro name é fornecido.
	Esses testes garantem que o controlador responde corretamente às requisições simuladas.
	*/
	@Autowired
	private MockMvc mockMvc;

	/* O método noParamGreetingShouldReturnDefaultMessage é um teste que verifica o comportamento do endpoint /greeting quando nenhum parâmetro é fornecido. Ele utiliza o MockMvc para simular uma requisição HTTP GET e valida que:	O status da resposta é 200 OK (andExpect(status().isOk())).
	O corpo da resposta JSON contém o campo content com o valor "Hello, World!" (andExpect(jsonPath("$.content").value("Hello, World!"))).
	Esse teste garante que o controlador retorna a mensagem padrão corretamente em cenários sem parâmetros. */
	@Test
	public void noParamGreetingShouldReturnDefaultMessage() throws Exception {

		this.mockMvc.perform(get("/greeting")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value("Hello, World!"));
	}

	/*
	O método paramGreetingShouldReturnTailoredMessage é um teste que verifica o comportamento do endpoint /greeting quando o parâmetro name é fornecido. Ele utiliza o MockMvc para simular uma requisição HTTP GET com o parâmetro name definido como "Spring Community" e valida que:
	O status da resposta é 200 OK (andExpect(status().isOk())).
	O corpo da resposta JSON contém o campo content com o valor "Hello, Spring Community!" (andExpect(jsonPath("$.content").value("Hello, Spring Community!"))).
	Esse teste garante que o controlador retorna uma mensagem personalizada corretamente quando o parâmetro é fornecido.
	 */
	@Test
	public void paramGreetingShouldReturnTailoredMessage() throws Exception {

		this.mockMvc.perform(get("/greeting").param("name", "Spring Community"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value("Hello, Spring Community!"));
	}

}
