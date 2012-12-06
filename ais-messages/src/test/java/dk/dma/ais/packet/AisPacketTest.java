/* Copyright (c) 2012 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.ais.packet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.message.IPositionMessage;
import dk.dma.ais.proprietary.GatehouseFactory;
import dk.dma.ais.proprietary.GatehouseSourceTag;
import dk.dma.ais.proprietary.IProprietaryTag;
import dk.dma.ais.reader.AisStreamReader;
import dk.dma.ais.reader.IAisPacketHandler;
import dk.dma.ais.sentence.CommentBlock;

public class AisPacketTest {

	@Test
	public void readPacketTest() throws IOException, InterruptedException {
		// Open input stream
		URL url = ClassLoader.getSystemResource("small_cb_example.txt");
		Assert.assertNotNull(url);
		InputStream inputStream = url.openStream();
		Assert.assertNotNull(inputStream);

		// Make AIS reader instance
		AisStreamReader aisReader = new AisStreamReader(inputStream);
		aisReader.addProprietaryFactory(new GatehouseFactory());
		// Set the source name 
		aisReader.setSourceName("some_file_dump");
		
		aisReader.registerPacketHandler(new IAisPacketHandler() {
			@Override
			public void receivePacket(AisPacket aisPacket) {
				System.out.println("--\npacket received: " + aisPacket.getStringMessage());
				
				// Try to get timestamp from comment block
				Long timestamp = null;
				CommentBlock cb = aisPacket.getVdm().getCommentBlock();
				if (cb != null) {
					timestamp = cb.getTimestamp();
				}
				System.out.println("cb timestamp: " + timestamp);
				// Try proprietary tag if no timestamp
				for (IProprietaryTag tag : aisPacket.getProprietaryTags()) {
					if (tag instanceof GatehouseSourceTag) {
						Date t = ((GatehouseSourceTag)tag).getTimestamp();
						if (t != null) {
							timestamp = t.getTime();
							System.out.println("pt timestamp: " + timestamp);
							break;
						}
					}
				}
				
				// If we cannot get timestamp we need to warn and discard the message
				if (timestamp == null) {
					System.out.println("Failed to get timestamp");
				}
								
				
				// Try to get AIS message
				try {
					AisMessage message = AisMessage.getInstance(aisPacket.getVdm());
					if (message instanceof IPositionMessage) {
						// Position message
						((IPositionMessage)message).getPos();
					}
				} catch (AisMessageException | SixbitException e) {
					// Failed to parse AIS message in VDM
					e.printStackTrace();
				}
				
				
			}
		});

		aisReader.start();
		aisReader.join();

	}

}
