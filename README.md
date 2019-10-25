AisLib
[![Build Status](https://travis-ci.com/dma-ais/AisLib.svg?branch=master)](https://travis-ci.com/dma-ais/AisLib)
======

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

* Java 8
* Maven 3

## Building ##

To build

	mvn clean install

To run tests

	mvn test
	
---	

NOTE: Temporary manual build procedure described here: <https://github.com/dma-ais/AisLib/issues/48#issuecomment-448634262>.

---

## Developing in Eclipse ##

Use M2 plugin or

	mvn eclipse:eclipse

and import as regular project.

## Contributing ##

You're encouraged to contribute to AisLib. Fork the code from
[https://github.com/dma-ais/AisLib](https://github.com/dma-ais/AisLib) and submit pull requests.

## License ##

This library is licensed under the Apache License, Version 2.0.

## Examples ##

### Simple read and message handling ###

Reading an **AisMessage** from an sentence in code is easily done with the **Vdm** object whether it it is a single message or multiple.

##### For single messages (AisPositionMessage) #####
```java
String aisSentence = "!AIVDM,1,1,,A,181:Jqh02c1Qra`E46I<@9n@059l,0*30";

Vdm vdm = new Vdm();
vdm.parse(aisSentence);

AisMessage aisMessage = AisMessage.getInstance(vdm);
```
##### For multiple messages (AisMessage5) #####
```java
String aisSentence1 = "!AIVDM,2,1,9,B,53nFBv01SJ<thHp6220H4heHTf2222222222221?50:454o<`9QSlUDp,0*09"; 
String aisSentence2 = "!AIVDM,2,2,9,B,888888888888880,2*2E";

Vdm vdm = new Vdm();
vdm.parse(aisSentence1);
vdm.parse(aisSentence2);

AisMessage aisMessage = AisMessage.getInstance(vdm);
```

Reading from files or TCP/UDP connections is very simple with AisLib. In the example below messages
are read from a file.

```java
AisReader reader = AisReaders.createReaderFromInputStream(new FileInputStream("sentences.txt"));
reader.registerHandler(new Consumer<AisMessage>() {
	@Override
	public void accept(AisMessage aisMessage) {
		System.out.println("message id: " + aisMessage.getMsgId());
	}
});
reader.start();
reader.join();
```

Reading using a TCP connection:

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

Reading using an UDP connection:

```java
AisReader reader = AisReaders.createUdpReader(8888);
reader.registerHandler(new Consumer<AisMessage>() {
	@Override
	public void accept(AisMessage aisMessage) {
		System.out.println("message id: " + aisMessage.getMsgId());
	}
});
reader.start();
reader.join();
```

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
   * `createUdpReader(int port)` - Creates a UDP reader reading from port on any interface.
   * `createUdpReader(String address, int port)` - Creates a UDP reader reading from port on interface with address.
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

[See UML diagram of messages](ais-lib-messages/src/main/doc/ais-messages-diagram.jpg)

#### Outputting Human Readable JSON ####
AisMessages can be decoded to human readable JSON messages automatically with a single handler.
```java
String aisSentence = "!AIVDM,1,1,,A,181:Jqh02c1Qra`E46I<@9n@059l,0*30";
Decoder decoder = new Decoder(aisSentence);
String json = decoder.decode(true);
``` 
outputs
```json
{
  "msgId": 1,
  "repeatDFO": {
    "ais_value": 0,
    "decoded_text": "Message has been repeated 0 times"
  },
  "userId": 0,
  "navStatusDFO": {
    "ais_value": 0,
    "decoded_text": "Under way using engine"
  },
  "rotDFO": {
    "ais_value": 0,
    "decoded_text": "Turning right at 0.0 degrees/ min"
  },
  "sogDFO": {
    "ais_value": 171,
    "decoded_text": "17.1 knots"
  },
  "posAccDFO": {
    "ais_value": 0,
    "decoded_text": "Low (> 10 m) (default)"
  },
  "position": {
    "latitude": 36.8121133,
    "longitude": 21.3901667
  },
  "cogDFO": {
    "ais_value": 3136,
    "decoded_text": "313.6 degrees"
  },
  "trueHeadingDFO": {
    "ais_value": 315,
    "decoded_text": "315 degrees"
  },
  "utcSecDFO": {
    "ais_value": 8,
    "decoded_text": "8"
  },
  "specialManIndicatorDFO": {
    "ais_value": 0,
    "decoded_text": "Not available"
  },
  "raimDFO": {
    "ais_value": 0,
    "decoded_text": "Raim not in use"
  },
  "syncStateDFO": {
    "ais_value": 0,
    "decoded_text": "Utc direct"
  }
}
```
With the decoder you can also pass in any **AisMessage** data type for the same result

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
doubletFilter.registerReceiver(downsampleFilter);

// Make reader
AisReader reader = AisReaders.createReader(host, port);
reader.registerHandler(doubletFilter);
reader.start();
```

### Expression based packet filtering ##
It is also possible to perform packet filtering based on packet sources and contents using free-text expressions.
These expressions must comply with a specified grammar.

#### Using the filter ####
The expression filter can be used programmatically like this:

##### Filtering on packets source #####
```java
import static dk.dma.ais.packet.AisPacketFiltersExpressionFilterParser.parseExpressionFilter;
...
parseExpressionFilter("s.country = DNK & s.type = LIVE").test(aisPacket);
```

In this example the test method wll return `true` for all packets received from a source in Denmark ('DNK') and
coming from a terrestrial source (typically a VHF base station as opposed to e.g. data received via satellite).
For packets coming sources not fulfilling this expression the `test` method will return false.

Filtering on source attributes is indicated by the 's' in front of the field ('country').

##### Filtering on message contents #####
```java
import static dk.dma.ais.packet.AisPacketFiltersExpressionFilterParser.parseExpressionFilter;
...
parseExpressionFilter("m.pos witin circle(37.9121, -122.4229, 2000)").test(aisPacket);
```

In this example the test method will return `false` for packets containing a position outside a cartesian circle
centered in 37.912 degrees north (37°N54'44"), 122.4229 degrees west (22°W25'22"), and with a radius of 2000 meters.
For other packets (including packets without any position information) the method will return `true`.

Filtering on message (~packet) attributes is indicated by the 'm.' in front of the field ('pos').

##### Filtering on targets #####
In some cases it is insufficient to filter on packet sources and content alone. For instance it may be desirable to
block AIS packets with static and voyage related data (see class `AisStaticCommon`) for vessels which are outside a
given area. Since the `AisStaticCommon` messages do not contain any position information, it is not possible to filter
on these packets alone.

Instead, it is possible to use an expression filter which will "remember" all AIS packets it has previously been
served. It uses these messages to track all AIS targets and keep a cache of the latest known positions, speeds, and
other characteristis. This why, it is possible to filter on the target's characteristics rather than the latest packet
received.

As an example:

```java
import static dk.dma.ais.packet.AisPacketFiltersExpressionFilterParser.parseExpressionFilter;
...
parseExpresionFiter("t.sog > 10").test(aisPacket);
```

Of all packets send to the `test`-method only those which are related to a vesel with a speed over ground larger
than 10 knots will result in `true` being returned from `test`. This is true even for e.g. AIS messages of type 5
which contain no speed information.

Filtering on target attributes is indicated by the 't.' in front of the field ('sog').

##### Example application ####
An example of an existing application which uses the expression filter is AisFilter which is based on main class
`dk.dma.ais.utils.filter.AisFilter`:

```
java dk.dma.ais.utils.filter.Aisilter -t localhost:4001 -exp ".sog in 2..8"
```

This will make the application connect via TCP to localhost:4001 to receive AIS packets, filter them using the
supplied expression (so that packets with speed over ground outside the range 2 to 8 knots are rejected), and
output the remaining packets on the standard output.

#### Grammar ####
The full grammar is specified usit ANTLR notation in file `expresionFilter.g4`.

The following are examples of filter expressions:

##### Simple comparisons #####
- `m.sog = 0`
- `m.sog != 0`
- `m.sog > 6.0`
- `m.sog < 7.0`
- `m.sog >= 6.6`
- `m.sog <= 6.6`

###### In lists ######
- `m.month = jan,feb,mar`
- `m.month = (jan,feb,mar)`
- `m.month in jan,feb,mar`
- `m.month in (jan,feb,mar)`
- `m.month @ jan,feb,mar`
- `m.month @ (jan,feb,mar)`
- `m.mmsi in 220431000,220435325,220430999`
- `m.mmsi in (220431000,220435325,220430999)`

- `m.month != jan,feb,mar`
- `m.month != (jan,feb,mar)`
- `m.month not in jan,feb,mar`
- `m.month not in (jan,feb,mar)`
- `m.month !@ jan,feb,mar`
- `m.month !@ (jan,feb,mar)`
- `m.mmsi not in 220431000,220435325,220430999`
- `m.mmsi not in (220431000,220435325,220430999)`

##### In ranges ######
- `m.id = 5..15`
- `m.id in 5..15`
- `m.id in (5..15)`
- `m.id @ (5..15)`

- `m.id != 5..15`
- `m.id not in 5..15`
- `m.id !@ (5..15)`

##### Within geographical areas #####
- `m.pos within circle(37.9058, -122.4310, 1000)`
- `m.pos within bbox(37.9058, -122.4310, 37.9185, -122.4149)`

##### With string resembling glob #####
- `m.name like D*`
- `m.name LIKE DI*`
- `m.name LIKE DI?NA`
- `m.name ~ D*A`
- `m.name ~ 'DIA*'`

##### Named values #####
- `m.type = TANKER`
- `m.type = military`
- `m.type in MILITARY, TANKER, HSC, FISHING`
- `m.type in 'tanker', 'military', HSC`

##### Composite expressions with boolean and/or #####
- `m.id = 1 & m.sog >= 6.1 & m.sog <= 6.9`
- `m.sog > 6.6 | m.sog < 6.4`
- `m.sog > 6.6 & m.sog < 6.7`
- `s.type=SAT|s.id=AISD|s.id=MSSIS`

##### Fields #####
The following fields can be used in filter expressions

 Group     | Field     | Meaning                    | Example values
:----------|:----------|:---------------------------|:-------------------
 sources   | s.id      | Source id                  |
           | s.bs      | Source base station        |
           | s.country | Source country             | DNK
           | s.type    | Source type                | LIVE, SAT 
           | s.region  | Source region              | 0
<hr>       | <hr>      | <hr>                       | <hr>
 messages  | m.id      | Message type               | 1, 2, 3, 5
           | m.mmsi    | MMSI number                | 219010123
           | m.year    | Msg recv'd in year         | 2014
           | m.month   | Msg recv'd in month        | jan, january, 1
           | m.dom     | Msg recv'd on day-of-month | 1, 31
           | m.dow     | Msg recv'd on day-of-week  | mon, monday, 1
           | m.hour    | Msg recv'd on hour         | 14
           | m.minute  | Msg recv'd on minute       | 34
<hr>       | <hr>      | <hr>                       | <hr>
           | m.imo     | IMO number                 | 6159463
           | m.type    | Ship type                  | tanker, 32
           | m.navstat | Navigational status        | AT_ANCHOR, 1
           | m.name    | Ship name                  | Maersk Alabama
           | m.cs      | Radio call sign            | XP1234
           | m.sog     | Speed over ground          | 10.0
           | m.cog     | Course over ground         | 234
           | m.hdg     | True heading               | 180
           | m.draught | Current draught            | 4.6
           | m.lat     | Latitude                   | 56.1234
           | m.lon     | Longitude                  | 12.4321
           | m.pos(*)  | Position                   | (56.1234, 12.4321)
<hr>       | <hr>      | <hr>                       | <hr>
 targets   | t.imo     | IMO number                 | 6159463
           | t.type    | Ship type                  | tanker, 32
           | t.navstat | Navigational status        | AT_ANCHOR, 1
           | t.name    | Ship name                  | Maersk Alabama
           | t.cs      | Radio call sign            | XP1234
           | t.sog     | Speed over ground          | 10.0
           | t.cog     | Course over ground         | 234
           | t.hdg     | True heading               | 180
           | t.draught | Current draught            | 4.6
           | t.lat     | Latitude                   | 56.1234
           | t.lon     | Longitude                  | 12.4321
           | t.pos(*)  | Position                   | (56.1234, 12.4321)

(*) pos is represents same values as (lat, lon) but is used in contexts where complete position (not just latitude or
longitude) is used.

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
