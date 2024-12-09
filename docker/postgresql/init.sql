-- Schéma relationnel
CREATE TABLE Application_User (
 application_user_id VARCHAR(50) PRIMARY KEY,
 email VARCHAR(50) NOT NULL,
 username VARCHAR(50) NOT NULL,
 password VARCHAR(50) NOT NULL
);

CREATE TABLE Conversation (
 conversation_id VARCHAR(50) PRIMARY KEY,
 application_user_id VARCHAR(50) NOT NULL REFERENCES Application_User(application_user_id)
);

CREATE TABLE ConversationParticipant (
 application_user_id VARCHAR(50),
 conversation_id VARCHAR(50),
 PRIMARY KEY (application_user_id, conversation_id),
 FOREIGN KEY (application_user_id) REFERENCES Application_User(application_user_id),
 FOREIGN KEY (conversation_id) REFERENCES Conversation(conversation_id)
);

CREATE TABLE Message (
 message_id VARCHAR(50) PRIMARY KEY,
 application_user_id VARCHAR(50) REFERENCES Application_User(application_user_id),
 message_content VARCHAR(500) NOT NULL,
 created_at TIMESTAMP NOT NULL,
 conversation_id VARCHAR(50) NOT NULL REFERENCES Conversation(conversation_id)
);