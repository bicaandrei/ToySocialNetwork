package ubb_221.toysocialnetworkgui.service;

import ubb_221.toysocialnetworkgui.domain.Message;
import ubb_221.toysocialnetworkgui.domain.User;
import ubb_221.toysocialnetworkgui.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;

public class MessageService {

    private MessageRepository repo;

    public MessageService(MessageRepository repo) { this.repo = repo; }

    public Message findMessage(Long id){

        return this.repo.findOne(id);

    }

    public Iterable<Message> getAll(){

        return this.repo.getAll();

    }

    public List<Message> getMessagesOfUsers(User user1, User user2){

        List<Message> messages = new ArrayList<Message>();
        for(Message m : this.getAll())
            if((m.getFrom().getId() == user1.getId() && m.getTo().getId() == user2.getId()) || (m.getFrom().getId() == user2.getId() && m.getTo().getId() == user1.getId()))
                messages.add(m);
        messages.sort((m1, m2)
                -> m1.getDate().compareTo(
                m2.getDate()));
        return messages;

    }

    public Long createId(){

        Long Max = 0L;
        for(Message m : this.getAll()){

            if(m.getId() > Max)
                Max = m.getId();

        }

        return Max+1;

    }

    public Message addMessage(Message m){

        m.setId(this.createId());
        return this.repo.save(m);

    }

}
