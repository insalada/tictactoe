package tictactoe;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipbsoft.tictactoe.config.AppConfig;
import com.ipbsoft.tictactoe.core.Move;
import com.ipbsoft.tictactoe.core.Player;
import com.ipbsoft.tictactoe.core.Position;
import com.ipbsoft.tictactoe.core.Room;
import com.ipbsoft.tictactoe.impl.PlayerAck;
import com.ipbsoft.tictactoe.managers.RoomPoolManager;
import com.ipbsoft.tictactoe.wrappers.PlayerWrapper;


/**
 * Some fancy tests for checking everything is going well...
 * 
 * @author Iván
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
@WebAppConfiguration
public class TictactoeTest {

	@Resource
	private WebApplicationContext webApplicationContext;
	private ObjectMapper mapper;
	private MockMvc mockMvc;

	private final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),                        
			Charset.forName("utf8"));

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mapper = new ObjectMapper();
	}

	@Test
	public void shouldNewRequestReturnUnprocessableEntityWhenNoBody() throws Exception {
		mockMvc.perform(post("/new"))
		.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void shouldNewRequestCreateNewPlayer() throws Exception {
		Player playerMock = new Player("ivan");
		mockMvc.perform(post("/new").contentType(APPLICATION_JSON_UTF8).content(getJSON(playerMock)))
		.andExpect(status().isCreated())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		//.andDo(print())
		.andExpect(jsonPath("success", is(true)));
	}

	@Test
	public void shouldJoinRequestJoinsPlayerIntoRoom() throws Exception {
		//1. Call NEW request to get a proper uuid and add the player to the pool
		Player playerMock = new Player("ivan");
		MvcResult result = mockMvc.perform(post("/new").contentType(APPLICATION_JSON_UTF8).content(getJSON(playerMock))).andReturn();		
		UUID uuid = toJSON(result, PlayerAck.class).getUuid();
		PlayerWrapper wrapper = new PlayerWrapper(uuid.toString());
		
		//2. Test JOIN request
		mockMvc.perform(post("/join").contentType(APPLICATION_JSON_UTF8).content(getJSON(wrapper)))
		.andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		//.andDo(print())
		.andExpect(jsonPath("success", is(true)));
	}
	
	@Test
	public void shouldFindTheFirstAvailableRoom() {
		//Instance a pool manager
		RoomPoolManager pool = new RoomPoolManager();
		//Create the mocks
		Room mockUnavailable = mock(Room.class);
		Room mockAvailable = mock(Room.class);
		//Define mocks behaviour
		when(mockUnavailable.isAvailable()).thenReturn(false);
		when(mockUnavailable.getId()).thenReturn(1);
		when(mockAvailable.isAvailable()).thenReturn(true);
		when(mockAvailable.getId()).thenReturn(2);
		//Add to the pool
		pool.addRoom(mockUnavailable);
		pool.addRoom(mockAvailable);
		//Find first available room
		Room roomFound = pool.findAvailable();
		//We expect to find the available one
		assertEquals(roomFound, mockAvailable);
	}
	
	@Test
	public void shouldRoomBeAvailableWithNoPlayers() {
		Room room = new Room();
		assertTrue(room.isAvailable());
	}
	
	@Test
	public void shouldRoomBeAvailableWithOnePlayer() {
		Room room = new Room();
		room.addPlayer(new Player());
		assertTrue(room.isAvailable());
	}
	
	@Test
	public void shouldRoomBeNotAvailableWhenFull() {
		Room room = new Room();
		room.addPlayer(new Player());
		room.addPlayer(new Player());
		assertFalse(room.isAvailable());
	}

	@Test
	public void shouldPositionBeTaken() {
		Room room = new Room();
		String[][] board = new String[3][3];
		board[0][0] = "X";
		room.setBoard(board);
		assertTrue(room.isPositionTaken(new Position(0,0)));
	}
	
	@Test
	public void shouldPositionBeEmpty() {
		Room room = new Room();
		assertFalse(room.isPositionTaken(new Position(0,0)));
	}
	
	@Test
	public void shouldSwitchTurn() {
		Room room = new Room();
		Player player1 = new Player();
		Player player2 = new Player();
		room.addPlayer(player1); //false by default
		room.addPlayer(player2);
		player1.setTurn(false);
		player2.setTurn(true);
		room.switchTurn();
		assertTrue(player1.isTurn() && !player2.isTurn());
	}
	
	
	@Test
	public void shouldWinHorizontalRow() {
		String[][] board = {{"X", "X", "X"}, {"", "", ""}, {"", "", ""}};
		Room room = new Room();
		room.setBoard(board);
		assertTrue(room.hasWinner(new Move("X", new Position(0,2))));
	}
	
	@Test
	public void shouldWinVerticalRow() {
		String[][] board = {{"X", "", ""}, {"X", "", ""}, {"X", "", ""}};
		Room room = new Room();
		room.setBoard(board);
		assertTrue(room.hasWinner(new Move("X", new Position(2,0))));
	}
	
	@Test
	public void shouldWinDiagonalRow() {
		String[][] board = {{"X", "", ""}, {"", "X", ""}, {"", "", "X"}};
		Room room = new Room();
		room.setBoard(board);
		assertTrue(room.hasWinner(new Move("X", new Position(2,2))));
	}
	
	@Test
	public void shouldWinReverseDiagonal() {
		String[][] board = {{"", "", "X"}, {"", "X", ""}, {"X", "", ""}};
		Room room = new Room();
		room.setBoard(board);
		assertTrue(room.hasWinner(new Move("X", new Position(2,0))));
	}
	
	/**
	 * Returns an Stringified JSON from a given object
	 * @param t
	 * @return
	 * @throws JsonProcessingException
	 */
	private <T extends Object> String getJSON(T t) throws JsonProcessingException {
		return mapper.writeValueAsString(t);
	}

	/**
	 * Return an object from the specified class from a given mvcMock result
	 * @param result
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private <T> T toJSON(MvcResult result, Class<T> type) throws Exception {
		return mapper.readValue(result.getResponse().getContentAsString(), type);
	}

}
