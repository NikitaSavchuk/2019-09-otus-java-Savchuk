package ru.otus.messagesystem;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@SuppressWarnings("LoopStatementThatDoesntLoop")
public final class MessageSystem {
    private final static Logger logger = Logger.getLogger(MessageSystem.class.getName());

    private final Map<Address, LinkedBlockingQueue<Message>> messagesMap;
    private final Map<Address, Addressee> addresseeMap;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public MessageSystem() {
        messagesMap = new HashMap<>();
        addresseeMap = new HashMap<>();
    }

    public void addAddressee(Addressee addressee) {
        addresseeMap.put(addressee.getAddress(), addressee);
        messagesMap.put(addressee.getAddress(), new LinkedBlockingQueue<>());
        executorService.submit(() -> this.startWatch(addressee.getAddress(), addressee));
    }

    public void sendMessage(Message message) {
        messagesMap.get(message.getTo()).add(message);
    }


    private void startWatch(Address address, Addressee addressee) {
        LinkedBlockingQueue<Message> queue = messagesMap.get(address);
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message message = queue.take();
                message.exec(addressee);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
