package de.ovgu.dke.glue.xmpp.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.configuration.ConfigurationException;
import org.jivesoftware.smack.XMPPException;

import de.ovgu.dke.glue.api.transport.Packet;
import de.ovgu.dke.glue.api.transport.PacketThread;
import de.ovgu.dke.glue.api.transport.TransportException;
import de.ovgu.dke.glue.api.transport.TransportRegistry;
import de.ovgu.dke.glue.xmpp.config.XMPPConfigurationLoader;
import de.ovgu.dke.glue.xmpp.config.XMPPPropertiesConfigurationLoader;
import de.ovgu.dke.glue.xmpp.transport.XMPPClient;
import de.ovgu.dke.glue.xmpp.transport.XMPPPacketThread;
import de.ovgu.dke.glue.xmpp.transport.XMPPTransport;
import de.ovgu.dke.glue.xmpp.transport.XMPPTransportFactory;

public class TestClient {
	public static void main(String args[]) throws TransportException,
			URISyntaxException, IOException, ConfigurationException,
			XMPPException {

		final XMPPConfigurationLoader confLoader = new XMPPPropertiesConfigurationLoader();
		final XMPPClient client = new XMPPClient(confLoader.loadConfiguration());

		client.startup();

		// init the transport registry
		TransportRegistry.getInstance().registerTransportFactory("xmpp",
				new XMPPTransportFactory(client));
		TransportRegistry.getInstance().setDefaultTransportFactory("xmpp");

		// get a transport
		final XMPPTransport xmpp = (XMPPTransport)TransportRegistry
				.getInstance()
				.getDefaultTransportFactory()
				.createTransport(
						URI.create("xmpp:shaun@bison.cs.uni-magdeburg.de"));

		final PacketThread thread = xmpp.createThread(null);

		// TODO woher das packet nehmen?
		final Packet packet = ((XMPPPacketThread) thread).createPacket(
				"Hallo Welt!", Packet.Priority.DEFAULT);

		
		thread.send(packet);

		System.out.println("Press any key...");
		System.in.read();

		thread.dispose();
		client.teardown();

	}
}