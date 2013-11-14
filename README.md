AisLib
======

*Previously DaMSA/AisLib*

## Introduction ##

DMA AisLib is a Java library for handling AIS messages. This include

* Reading from AIS sources e.g. serial connection, TCP connection or file
* Handling of proprietary source tagging sentences
* Message filtering like doublet filtering and down sampling
* Decoding sentences and AIS messages
* Encoding sentences and AIS messages
* Sending AIS messages #6, #8, #12 and #14
* Handling application specific messages

The library contains test code and utility applications demonstrating
the use.

## Prerequisites ##

* Java 1.7
* Maven 3

## Building ##

To build
 
	mvn clean install
 
To run tests

	mvn test
	
## Developing in Eclipse ##

Use M2 plugin or 

	mvn eclipse:eclipse

and import as regular project.

## Contributing ##

You're encouraged to contribute to AisLib. Fork the code from 
[https://github.com/dma-ais/AisLib](https://github.com/dma-ais/AisLib) and submit pull requests.

## License ##

This library is provided under the LGPL, version 3.

## Examples ##

### Simple read and message handling ###

Reading from files or TCP connections is very simple with AisLib. In the example below messages
are read from a file.

```java
AisReader reader = AisReader.createReaderFromInputStream(new FileInputStream("sentences.txt"));
reader.registerHandler(new Consumer<AisMessage>() {			
	@Override
	public void accept(AisMessage aisMessage) {
		System.out.println("message id: " + aisMessage.getMsgId());	
	}
});
reader.start();
reader.join();
```

Reading using a TCP connection is just as easy

```java
AisReader reader = AisReaders.createReader("localhost", 4001);
reader.registerHandler(new Consumer<AisMessage>() {			
	@Override
	public void accept(AisMessage aisMessage) {
		System.out.println("message id: " + aisMessage.getMsgId());		
	}
});
reader.start();
reader.join();
```

If the connection is broken the reader will try to reconnect after a certain amount of
time that can be set with:
```java
reader.setReconnectInterval(1000);
```

A read timeout can be defined for the reader. If no data is received within this period
the connection will be closed and a reconnect will be tried. 

### Reading raw message packets ###

Instead of working with AisMessage objects, it is possible to work
with unparsed raw message packets (proprietary tags, comment blocks and VDM's).
A packet consumer is registred in a reader.

```java
AisReader reader = AisReaders.createReader("localhost", 4001);
reader.registerPacketHandler(new Consumer<AisPacket>() {			
	@Override
	public void accept(AisPacket packet) {
		try {
			AisMessage message = packet.getAisMessage();
		} catch (AisMessageException | SixbitException e) {
			// Handle
            		return;
		}
		// Alternative returning null if no valid AIS message
		AisMessage message = packet.tryGetAisMessage();
		
		Date timestamp = packet.getTimestamp();
		CommentBlock cb = packet.getVdm().getCommentBlock();
	}
});
reader.start();
reader.join();
```

Alternatively packets can be read as a packet stream.

```java
Path path = ...
try (AisPacketReader pReader = AisPacketReader.createFromFile(path, true)) {
	AisPacket p;
	while ((p = pReader.readPacket()) != null) {
		count++;
		// Handle packet
		...
	}
}
```

If the filename has '.zip' suffix decompression will automatically be applied.

### Reader factory methods ###

An `AisReader` instance is created using factory methods in `AisReaders`. The following
methods can be used.

   * `createReader(String hostname, int port)` - Creates a reader connection to host:port.
   * `createReader(String commaHostPort)` - Creates a {@link AisTcpReader} from a list of one or more hosts on the 
form: host1:port1,...,hostN:portN. If more than one host/port round robin will be used.
   * `createReaderFromInputStream(InputStream inputStream)` - Creates a reader reading from an input stream.
   * `createReaderFromFile(String filename)` - Creates a reader reading from a file. If the filename suffix is '.zip' or '.gz',
zip or gzip decompression will be applied respectively.


### Reader group ###

A collection of readers can be organized in an `AisReaderGroup` that will deliver packets in a
single join packet stream. Sources are defined by a commaseparated list of host:port. If more than 
one host for each source, round robin reading is used.

```java
List<String> sources = ...
sources.add("s1host1:s1port1,s1host2:s1port2,s1host3:s1port3);
sources.add("s2host1:s1port1,s2host2:s2port2);
...
AisReaderGroup g = AisReaders.createGroup("name", sources);

TBD

```

### Working with messages ###

To determine what messages are received the instanceof operator can be used. The example
below shows how to test and cast, and take advantage of the object oriented design where 
related messages shares parents.

```java
@Override
public void accept(AisMessage aisMessage) {
	// Handle AtoN message
	if (aisMessage instanceof AisMessage21) {
		AisMessage21 msg21 = (AisMessage21) aisMessage;
		System.out.println("AtoN name: " + msg21.getName());
	}
	// Handle position messages 1,2 and 3 (class A) by using their shared parent
	if (aisMessage instanceof AisPositionMessage) {					
		AisPositionMessage posMessage = (AisPositionMessage)aisMessage;
		System.out.println("speed over ground: " + posMessage.getSog());
	}
	// Handle position messages 1,2,3 and 18 (class A and B)  
	if (aisMessage instanceof IGeneralPositionMessage) {
		IGeneralPositionMessage posMessage = (IGeneralPositionMessage)aisMessage;
		System.out.println("course over ground: " + posMessage.getCog());
	}
	// Handle static reports for both class A and B vessels (msg 5 + 24)
	if (aisMessage instanceof AisStaticCommon) {
		AisStaticCommon staticMessage = (AisStaticCommon)aisMessage;
		System.out.println("vessel name: " + staticMessage.getName());
	}					
}
```

### Multiple sources ###

The example below shows how messages from multiple sources can be handled by a single
handler.

```java
// Make a handler
Consumer<AisMessage> handler = new Consumer<AisMessage>() {			
	@Override
	public void accept(AisMessage aisMessage) {
		System.out.println("aisMessage: " + aisMessage);				
	}
};

// Make readers and register handler
AisReader reader1 = AisReaders.createReader("host1", 4001);
AisReader reader2 = AisReaders.createReader("host2", 4001);
AisReader reader3 = AisReaders.createReader("host3", 4001);
reader1.registerHandler(handler);
reader2.registerHandler(handler);
reader3.registerHandler(handler);

// Start readers
reader1.start(); reader2.start(); reader3.start();
```

Alternatively an `AisReaderGroup` can be used.

### Round robin reading ###

The example below shows how to round robin between TCP hosts. If
one goes down, the re-connect will be to the next on the list.

```java
AisReader reader = AisReaders.createReader("host1:port1,host2:port2,host3:port3");
reader.registerHandler(new Consumer<AisMessage>() {			
	@Override
	public void accept(AisMessage aisMessage) {
		System.out.println("message id: " + aisMessage.getMsgId());		
	}
});
reader.start();
reader.join();		
```

### Message filtering ###

Message filters implement a single method

     boolean rejectedByFilter(AisMessage message);

Down sample example:

```java
AisReader reader = AisReaders.createReader("ais163.sealan.dk:4712");
reader.registerHandler(new Consumer<AisMessage>() {			
	DownSampleFilter downSampleFilter = new DownSampleFilter(60);
	@Override
	public void accept(AisMessage aisMessage) {
		if (downSampleFilter.rejectedFilter(aisMessage)) {
			return;
		}
		// Handle message
	}
});
reader.start();
reader.join();		
```

A MessageHandlerFilter can be used to put in between readers
and handlers. In the example below two filters are used. A doublet
filter and a down sampling filter.

```java
Consumer<AisMessage> handler = new Consumer<AisMessage>() {
	@Override
	public void accept(AisMessage aisMessage) {
		// Handle doublet filtered down sampled messages
	}
};

// Make down sampling filter with sampling rate 1 min and
// make handler the recipient of down sampled messages
MessageHandlerFilter downsampleFilter = new MessageHandlerFilter(new DownSampleFilter());
downsampleFilter.registerReceiver(handler);

// Make doublet filter with default window of 10 secs.
// Set down sample filter as recipient of doublet filered messages
MessageHandlerFilter doubletFilter = new MessageHandlerFilter(new DuplicateFilter());
doubletFilter.registerReceiver(doubletFilter);

// Make reader
AisReader reader = AisReaders.createReader(host, port);
reader.registerHandler(doubletFilter);
reader.start();
```

### Packet transformers ###

TBD. See `dk.dma.ais.transform.*`, `dk.dma.ais.transform.TransformTest` and
`dk.dma.ais.packet.AisPacketTagsTest`.

### Reading proprietary tags ###

AisLib can handle some proprietary tags inserted before VDM sentences, but
implementations of factories must be given. In the example below Gatehouse
source tags are handled.

Proprietary factories are defined in the file

    ais-lib-messages/src/main/resources/META-INF/services/dk.dma.ais.proprietary.ProprietaryFactory

```java
AisReader reader = AisReaders.createReaderFromInputStream(new FileInputStream("sentences.txt"));
reader.registerHandler(new Consumer<AisMessage>() {
	@Override
	public void accept(AisMessage aisMessage) {
		if (aisMessage.getSourceTag() != null) {
			IProprietarySourceTag sourceTag = aisMessage.getSourceTag();
			System.out.println("timestamp: " + sourceTag.getTimestamp());
		}
	}
});
reader.start();
reader.join();
```

### AIS metadata ###

Besides from propritary tags, comment blocks carry metadata about
the VDM sentences carrying the AIS message.

The example below shows how to handle comment blocks.

```java
AisReader reader = ...
reader.registerPacketHandler(new Consumer<AisPacket>() {            
    @Override
    public void accept(AisPacket aisPacket) {
    	Vdm vdm = packet.getVdm();
        if (vdm == null) {
            return;
        }
        CommentBlock cb = vdm.getCommentBlock();
        if (cb != null) {
        	String source = cb.getString();
        	Long cbTimestamp = cb.getTimestamp();
        }
    }
});
reader.start();
reader.join();
```

Common metadata has been standardized in the class `AisPacketTags`. The 
following attributes are used:

   * Timestamp
   * Source id
   * Source basestation
   * Source country (ISO three letter)
   * Source type SAT | LIVE

Example

```java
AisPacketTags tags = packet.getTags();
Assert.assertEquals(tags.getTimestamp().getTime(), 1354719387000L);
Assert.assertEquals(tags.getSourceId(), "SISSM");
Assert.assertEquals(tags.getSourceBs().intValue(), 2190047);
Assert.assertEquals(tags.getSourceCountry().getThreeLetter(), "DNK");
```

Full timestamp of VDM sentences are done in different proprietary fashions. AisLib
tries to get the timestamp in three ways

   1. Comment block key 'c'
   2. Gatehouse propritary source tag
   3. MSSIS timestamp appended to VDM sentence (the first occurence of a timestamp is used)

The timestamp is retrived from a packet using the following

```java
Packet packet = ...
Date timestamp = packet.getTimestamp();
```

### AisBus ###

TBD. See `dk.dma.ais.bus.AisBusTest`.

### Sending a message ###

Example of sending an addressed text message in an ABM sentence. See test cases on how
to send application specific message. See AisReader for different sending options. In the 
example below all ABM packaging is handled by AisReader.

```java
AisReader aisReader = new AisTcpReader(host, port);
aisReader.start();
...
// Make AIS message 12
AisMessage12 msg12 = new AisMessage12();
msg12.setDestination(destination);
msg12.setMessage(message);

// Send using a blocking call
Abk abk = aisReader.send(msg12, 1, destination);
if (abk.isSuccess()) {
	...
} else {
	...
}
```
