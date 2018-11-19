AIS JSON Decoders
=================

## Need ##
The need for this module arose out of a need to display well formatted and human readable AIS messages. This led to a need for multiple natural enums that would show the actual text of what an enum value was according to the AIS message structure.

## Example ##
Instead of looking at the common Position Accuracy DFO (posAccDFO) field of a message as returning 0 it will return a json that is not only human readable but also still contains the enum value for use in other places where the value is still needed.
```json
"posAccDFO": {
    "ais_value": 0,
    "decoded_text": "Low (> 10 m) (default)"
  }
```

## Usage ##
The architecture of this module is build for reusability, in the fact that it does the loading of all of the possible message types into a Hashmap up front so the user does not have to know the message type before asking the decoder for the message: you can work straight from the AIS sentence(s) and the decoders will leverage the AIS messages module automatically to decode the message, then the json decoder will interpret that for the user.
 
 ```java
 String aisSentence = "!AIVDM,1,1,,A,181:Jqh02c1Qra`E46I<@9n@059l,0*30";
 Decoder decoder = new Decoder(aisSentence);
 String json = decoder.decode();
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

You can decode json message in different ways with different inputs, also multi-line messages.
```java
Vdm vdm = new Vdm();

vdm.parse("!AIVDM,2,1,9,B,53nFBv01SJ<thHp6220H4heHTf2222222222221?50:454o<`9QSlUDp,0*09");
vdm.parse("!AIVDM,2,2,9,B,888888888888880,2*2E");

AisMessage message = AisMessage.getInstance(vdm);
Decoder decoder = new Decoder(message);
String json = decoder.decode(true);
System.out.println(json);
```

```json
{
  "msgId" : 5,
  "repeatDFO" : {
    "ais_value" : 0,
    "decoded_text" : "Message has been repeated 0 times"
  },
  "userId" : 0,
  "callsign" : "LFNA   ",
  "name" : "FALKVIK             ",
  "shipTypeDFO" : {
    "ais_value" : 79,
    "decoded_text" : "Cargo-no-additional-information"
  },
  "dimBowDFO" : {
    "ais_value" : 40,
    "decoded_text" : "Distance from GPS antenna to bow 40 m"
  },
  "dimSternDFO" : {
    "ais_value" : 10,
    "decoded_text" : "Distance from GPS antenna to stern 10 m"
  },
  "dimPortDFO" : {
    "ais_value" : 4,
    "decoded_text" : "Distance from GPS antenna to port 4 m"
  },
  "dimStarboardDFO" : {
    "ais_value" : 5,
    "decoded_text" : "Distance from GPS antenna to starboard 5 m"
  },
  "versionDFO" : {
    "ais_value" : 0,
    "decoded_text" : "Station compliant with Recommendation ITU-R M.1371-1"
  },
  "imo" : 6514895,
  "posTypeDFO" : {
    "ais_value" : 1,
    "decoded_text" : "Gps"
  },
  "etaDFO" : {
    "dateInMillis" : 1521031200000,
    "textDate" : "Wed 2018 Mar 14 05:40:00 PDT "
  },
  "draughtDFO" : {
    "ais_value" : 38,
    "decoded_text" : "Draught is 3.8 m"
  },
  "dest" : "FORUS               ",
  "dteDFO" : {
    "ais_value" : 0,
    "decoded_text" : "Available"
  }
}
```
This module was contributed by [Brandon Hessler](brandonhessler.com), do not hesitate to contact me for questions on [My Github](https://github.com/CallSign-Filter) or by email or mention.