package net.neophyte.messaging.jms.topic;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import net.neophyte.messaging.jms.AbstractJMSClient;
import net.neophyte.messaging.jms.Configuration;
import net.neophyte.messaging.jms.SimpleConnectionProvider;
import net.neophyte.messaging.jms.Utils.Util;
/**
 * A simple JMS topic subscriber that receives message in async mode
 * 
 * @author shuvro
 *
 */
public class SimpleAsyncSubscriber extends AbstractJMSClient {
	
	public static void main(String[] a) {
		SimpleAsyncSubscriber pc = new SimpleAsyncSubscriber();
		System.out.println("-Calling subscribeAndReceive-");
		pc.subscribeAndReceive(Configuration.getMessageCount(), Configuration.getRuntime());
		System.exit(0);
	}
	
	public void subscribeAndReceive(long numOfMessages, long runTime) {
		Connection connection = null;
		Session session = null;
		MessageConsumer msgReceiver = null;
		startTime = System.currentTimeMillis();
		try {
			connection = SimpleConnectionProvider.createConnectionInstance(Configuration.getBrokerurl(),
					Configuration.getUserid(), Configuration.getPassword());
			session = SimpleConnectionProvider.getSession(Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createTopic(Configuration.getTopicName());
			msgReceiver = session.createConsumer(destination);
			msgReceiver.setMessageListener(new SimpleMessageListener());
			connection.start();
			while (runTimeRemains(runTime) || moreMessagesToReceive(numOfMessages)) {
				try {
                    Thread.sleep(200); /* sleep 200 mili seconds*/
                }catch(Exception e){
                    /* ignore */
                }
			}
	        long totalRunTime = System.currentTimeMillis() - startTime;
	        logger.info("Total run time: " + Util.getHh_Mm_Ss_ssss_Time(totalRunTime) 
	                + ", Total messages received: " + msgs.get());
	        logger.info("Total errors: " + errors.get());
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			SimpleConnectionProvider.closeConnection();
		}
	}
}