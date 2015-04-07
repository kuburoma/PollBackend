package cz.wa2.poll.backend.websocket;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;


/**
 * The producer endpoint that writes to the queue.
 * @author syntx
 *
 */
public class Producer extends EndPoint{

    public Producer(String endPointName) throws IOException{
        super(endPointName);
    }

    public void sendMessage(Serializable object) throws IOException {
        System.out.println("zprava pošli");
        channel.basicPublish("",endPointName, null, SerializationUtils.serialize(object));
    }
}