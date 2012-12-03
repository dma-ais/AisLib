AisLib
======

#### Java library for handling AIS messages ####

## Introduction ##

DaMSA AisLib is a Java library for handling AIS messages. This include

* Reading from AIS sources e.g. serial connection, TCP connection or file
* Handling of proprietary source tagging sentences
* Message filtering like doublet filtering and down sampling
* Decoding sentences and AIS messages
* Encoding sentences and AIS messages
* Sending AIS messages #6, #8, #12 and #14
* Handling application specific messages

The library contains test code and utility applications demonstrating
the use.

The project is separated into three parts:
  
* core     - The main AIS library
* test     - Various JUnit test code
* utils    - Various AIS utilities using AisLib


## Building ##

To build you will need

* JDK 1.6+ (http://java.sun.com/j2se/)
* Apache Ant 1.7+ (http://ant.apache.org)

To build everything
 
	ant
 
To run tests. All tests with filename *Test.java will be executed. 
Change build.xml to allow specific test targets.

	ant test

Make a distributable JAR file in project root

	ant dist
 
To make Javadoc

	ant javadoc
 

## Utils ##

The utilities source is located in utils/src

To build all utilities use 

	ant 
  
or

	ant utils

Each utility are placed under utils/. E.g. the filter application in
utils/filter. It can be started by first building and then using either
filter.sh or filter.bat on Linux and Windows respectively.

	ant
	cd utils/filter
	filter.bat -t localhost:4001 -d
  

## Contributing ##

You're encouraged to contribute to AisLib. Fork the code from 
[https://github.com/DaMSA/AisLib](https://github.com/DaMSA/AisLib) and submit pull requests.

## Versioning/naming ##

The version number/name is controlled in the core/build.xml file. Please use a 
name relating to the branch name. New official versions will only be made from
master branch. 

## License ##

This library is provided under the LGPL, version 3.

## Examples ##

### Simple read and message handling ###

Reading from files or TCP connections is very simple with AisLib. In the example below messages
are read from a file.

```java
AisReader reader = new AisStreamReader(new FileInputStream("sentences.txt"));
reader.registerHandler(new IAisHandler() {			
	@Override
	public void receive(AisMessage aisMessage) {
		System.out.println("message id: " + aisMessage.getMsgId());	
	}
});
reader.start();
reader.join();
```

Reading using a TCP connection is just as easy

```java
AisTcpReader reader = new AisTcpReader("localhost", 4001);
reader.registerHandler(new IAisHandler() {			
	@Override
	public void receive(AisMessage aisMessage) {
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

### Working with messages ###

To determine what messages are received the instanceof operator can be used. The example
below shows how to test and cast, and take advantage of the object oriented design where 
related messages shares parents.

```java
@Override
public void receive(AisMessage aisMessage) {
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
IAisHandler handler = new IAisHandler() {			
	@Override
	public void receive(AisMessage aisMessage) {
		System.out.println("aisMessage: " + aisMessage);				
	}
};

// Make readers and register handler
AisReader reader1 = new AisTcpReader("host1", 4001);
AisReader reader2 = new AisTcpReader("host2", 4001);
AisReader reader3 = new AisTcpReader("host3", 4001);
reader1.registerHandler(handler);
reader2.registerHandler(handler);
reader3.registerHandler(handler);

// Start readers
reader1.start(); reader2.start(); reader3.start();
```

### Round robin reading ###

The example below shows how to round robin between TCP hosts. If
one goes down, the re-connect will be to the next on the list.

```java
RoundRobinAisTcpReader reader = new RoundRobinAisTcpReader();
reader.addHostPort("host1:4001");
reader.addHostPort("host2:4001");
reader.addHostPort("host3:4001");
reader.registerHandler(new IAisHandler() {			
	@Override
	public void receive(AisMessage aisMessage) {
		System.out.println("message id: " + aisMessage.getMsgId());		
	}
});
reader.start();
reader.join();		
```

### Message filtering ###

Filters are message handlers that can be put in between readers
and handlers. In the example below two filters are used. A doublet
filter and a down sampling filter.

```java
IAisHandler handler = new IAisHandler() {
	@Override
	public void receive(AisMessage aisMessage) {
		// Handle doublet filtered down sampled messages
	}
};

// Make down sampling filter with sampling rate 1 min and
// make handler the recipient of down sampled messages
MessageDownSample downsampleFilter = new MessageDownSample(60);
downsampleFilter.registerReceiver(handler);

// Make doublet filter with default window of 10 secs.
// Set down sample filter as recipient of doublet filered messages
MessageDoubletFilter doubletFilter = new MessageDoubletFilter();
doubletFilter.registerReceiver(downsampleFilter);

// Make reader
AisReader reader = new AisTcpReader(host, port);
reader.registerHandler(doubletFilter);
reader.start();
```

### Reading proprietary tags ###

AisLib can handle some proprietary tags inserted before VDM sentences, but
implementations of factories must be given. In the example below Gatehouse
source tags are handled.

```java
AisReader reader = new AisStreamReader(new FileInputStream("sentences.txt"));
reader.addProprietaryFactory(new GatehouseFactory());		
reader.registerHandler(new IAisHandler() {
	@Override
	public void receive(AisMessage aisMessage) {
		if (aisMessage.getSourceTag() != null) {
			IProprietarySourceTag sourceTag = aisMessage.getSourceTag();
			System.out.println("timestamp: " + sourceTag.getTimestamp());
		}
	}
});
reader.start();
reader.join();
```

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
