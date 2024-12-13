-- Schéma relationnel
CREATE TABLE Application_User (
 application_user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
 email VARCHAR(50) NOT NULL,
 username VARCHAR(50) NOT NULL,
 password VARCHAR(50) NOT NULL
);

CREATE TABLE Conversation (
 conversation_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
 application_user_id UUID NOT NULL REFERENCES Application_User(application_user_id)
);

CREATE TABLE ConversationParticipant (
 application_user_id UUID,
 conversation_id UUID,
 PRIMARY KEY (application_user_id, conversation_id),
 FOREIGN KEY (application_user_id) REFERENCES Application_User(application_user_id),
 FOREIGN KEY (conversation_id) REFERENCES Conversation(conversation_id)
);

CREATE TABLE Message (
 message_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
 application_user_id UUID REFERENCES Application_User(application_user_id),
  conversation_id UUID NOT NULL REFERENCES Conversation(conversation_id),
 message_content VARCHAR(500) NOT NULL,
 created_at TIMESTAMP NOT NULL
);