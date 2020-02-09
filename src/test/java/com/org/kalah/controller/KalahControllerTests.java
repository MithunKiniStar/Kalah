package com.org.kalah.controller;

import com.org.kalah.dto.GameDTO;
import com.org.kalah.service.KalahService;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@RunWith(SpringRunner.class)
class KalahControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private KalahService kalahService;

	@Value("${kalah.game.url}")
	private String kalahURL;

	@Test
	public void startGameTest() throws Exception {
		GameDTO newGame = new GameDTO(1,kalahURL,null,null);
		given(kalahService.createKalahGame()).willReturn(newGame);

		mockMvc.perform(post("/games"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.gameId").value(1))
			    .andExpect(jsonPath("$.url").value(newGame.getUrl()));
	}

	@Test
	public void makeMoveTest() throws Exception {
		Map<Integer, Integer> boardStatus = Stream.of(new Integer[][] {
				{ 1, 0}, { 2, 7 },{ 3, 7 },{ 4, 7 },{ 5, 7 },{ 6, 7 },{ 7, 1 },{ 8, 6 },{ 9, 6 },{ 10, 6 },{ 11, 6 },{ 12, 6 },{ 13, 6 },{ 14, 0 }
		}).collect(Collectors.toMap(data -> data[0], data -> data[1]));
		GameDTO newGame = new GameDTO(1,kalahURL, boardStatus,"FIRST_PLAYER");
		given(kalahService.makeMove(1,1)).willReturn(newGame);

		mockMvc.perform(put("/games/1/pits/1"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.gameId").value(1))
				.andExpect(jsonPath("$.url").value(newGame.getUrl()))
				.andExpect(jsonPath("$.boardStatus").exists())
				.andExpect(jsonPath("$.playerTurn").value(newGame.getPlayerTurn()));
	}


}
