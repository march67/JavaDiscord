package com.javadiscordproject.business.service.implementation;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.javadiscordproject.business.service.api.ConversationService;
import com.javadiscordproject.business.service.api.FriendlistService;
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
	private FriendlistService friendlistService;
	
	@Disabled()
	@Test
	void sendFriendRequestByUsername_shouldSaveFriendRequest_whenHappyPath() {
		// Arrange method parameters
		Long idRequester = 1L;
		String username = "friendName";
		
		// Arrange mock configuration
		UserModel user1 = new UserModel();
	}
	
}
