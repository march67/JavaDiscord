package com.javadiscordproject.business.service.implementation;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.javadiscordproject.business.service.api.ConversationService;
import com.javadiscordproject.business.service.api.UserConversationService;
import com.javadiscordproject.business.service.api.UserService;
import com.javadiscordproject.domain.model.UserModel;
import com.javadiscordproject.infrastructure.persistence.repository.FriendlistRepository;

@ExtendWith(MockitoExtension.class)
public class FriendlistServiceImplTest {
	@Mock
	private FriendlistRepository friendlistRepository;
	
	@Mock
	private UserService userService;
	
	@Mock
	private UserConversationService userConversationService;
	
	@Mock
	private ConversationService conversationService;
	
	@InjectMocks
	private FriendlistServiceImpl friendlistService;
	
	@Test
	void sendFriendRequestByUsername_shouldSaveFriendRequest_whenHappyPath() {
		// Arrange method parameters
		Long idRequester = 1L;
		String username = "friendName";
		
		// Arrange mock configuration
		UserModel user1 = new UserModel();
		user1.setApplicationUserId(idRequester);
		
		UserModel user2 = new UserModel();
		user2.setApplicationUserId(2L);
		user2.setUsername(username);
		
		
		// Arrange spy
		FriendlistServiceImpl spyService = Mockito.spy(friendlistService);
		
		
		when(userService.findUserById(idRequester)).thenReturn(user1);
		when(userService.findUserByUsername(username)).thenReturn(user2);
		Mockito.doReturn(false).when(spyService).areFriends(user1, user2);
		Mockito.doReturn(false).when(spyService).autoAcceptFriendRequest(user1, user2, idRequester);
		
		// Act
		spyService.sendFriendRequestByUsername(idRequester, username);
		
		// Assert
		verify(friendlistRepository).save(Mockito.argThat(model ->
			model.getRequesterId().equals(idRequester)
			&& model.getUser1().equals(user1)
			&& model.getUser2().equals(user2)
			&& model.getFriendRequest_status().equals("pending")
				));
	}
	
}
