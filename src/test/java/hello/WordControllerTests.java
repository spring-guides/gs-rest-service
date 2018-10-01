/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hello;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WordControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFivePositiveWords() throws Exception {
        List<String> words = Arrays.asList("kayak", "sagas", "solos", "eve", "anna");

        for (String word : words) {
            this.mockMvc.perform(get("/words/" + word )).andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.palindrome").value(true));
        }
    }

    @Test
    public void testFiveNegativeWords() throws Exception {
        List<String> words = Arrays.asList("foo", "bar", "buzz", "fizz", "cloud");

        for (String word : words) {
            this.mockMvc.perform(get("/words/" + word )).andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.palindrome").value(false));
        }
    }

    @Test
    public void testFivePositiveAnagrams() throws Exception {
        List<String> words = Arrays.asList("foo", "kkppd", "buzzbu", "pizizp", "gggoo");

        for (String word : words) {
            this.mockMvc.perform(get("/words/" + word )).andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.anagramOfPalindrome").value(true));
        }
    }

    @Test
    public void testFiveNegativeAnagrams() throws Exception {
        List<String> words = Arrays.asList("abcd", "five", "eleven", "pizza", "eved");

        for (String word : words) {
            this.mockMvc.perform(get("/words/" + word )).andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.anagramOfPalindrome").value(false));
        }
    }
}
