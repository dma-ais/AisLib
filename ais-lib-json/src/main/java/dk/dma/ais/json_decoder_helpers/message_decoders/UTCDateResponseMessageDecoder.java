package dk.dma.ais.json_decoder_helpers.message_decoders;

import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisDate;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisFieldObject;
import dk.dma.ais.json_decoder_helpers.decoded_objects.DecodedAisPosition;
import dk.dma.ais.json_decoder_helpers.enums.*;
import dk.dma.ais.json_decoder_helpers.util.Decodes;
import dk.dma.ais.message.AisPosition;
import dk.dma.ais.message.UTCDateResponseMessage;

import java.util.Date;

@SuppressWarnings("unused")
@Decodes(className = UTCDateResponseMessage.class)
public class UTCDateResponseMessageDecoder extends AisMessageDecoder{

    private transient UTCDateResponseMessage utcDateResponseMessage;

    private DecodedAisDate calendarDateDFO;
    private DecodedAisFieldObject posAccDFO;
    private DecodedAisPosition positionDFO;
    private DecodedAisFieldObject posTypeDFO;
    private DecodedAisFieldObject transmissionControlDFO;
//    private DecodedAisFieldObject spare; //Not Used
    private DecodedAisFieldObject raimDFO;
    private DecodedAisFieldObject syncStateDFO;
//    private DecodedAisFieldObject slotTimeout; //Not properly defined
//    private DecodedAisFieldObject subMessage; //Not properly defined

    public UTCDateResponseMessageDecoder(UTCDateResponseMessage utcDateResponseMessage) {
        super(utcDateResponseMessage);
        this.utcDateResponseMessage = utcDateResponseMessage;
    }

    //region Getters
    public DecodedAisDate getCalendarDateDFO() {
        Date date = utcDateResponseMessage.getDate();
        return new DecodedAisDate(date);
    }

    public DecodedAisFieldObject getPosAccDFO() {
        int posAcc = utcDateResponseMessage.getPosAcc();
        return new DecodedAisFieldObject(posAcc, PositionAccuracy.get(posAcc).prettyPrint());
    }

    public DecodedAisPosition getPositionDFO() {
        AisPosition aisPosition = utcDateResponseMessage.getPos();
        return new DecodedAisPosition(aisPosition);
    }

    public DecodedAisFieldObject getPosTypeDFO() {
        int code = utcDateResponseMessage.getPosType();
        return new DecodedAisFieldObject(code, EPFDFixType.get(code).prettyPrint());
    }

    public DecodedAisFieldObject getTransmissionControlDFO() {
        int code = utcDateResponseMessage.getTransmissionControl();
        return new DecodedAisFieldObject(code, TransmissionControl.get(code).prettyPrint());
    }

    public DecodedAisFieldObject getRaimDFO() {
        int raim = utcDateResponseMessage.getRaim();
        return new DecodedAisFieldObject(raim, Raim.get(raim).prettyPrint());
    }

    public DecodedAisFieldObject getSyncStateDFO() {
        int syncState = utcDateResponseMessage.getSyncState();
        return new DecodedAisFieldObject(syncState, SyncState.get(syncState).prettyPrint());
    }

    //endregion

    //region Setters

    public void setCalendarDateDFO(DecodedAisDate calendarDateDFO) {
        this.calendarDateDFO = calendarDateDFO;
    }

    public void setPosAccDFO(DecodedAisFieldObject posAccDFO) {
        this.posAccDFO = posAccDFO;
    }

    public void setPositionDFO(DecodedAisPosition positionDFO) {
        this.positionDFO = positionDFO;
    }

    public void setPosTypeDFO(DecodedAisFieldObject posTypeDFO) {
        this.posTypeDFO = posTypeDFO;
    }

    public void setTransmissionControlDFO(DecodedAisFieldObject transmissionControlDFO) {
        this.transmissionControlDFO = transmissionControlDFO;
    }

    public void setRaimDFO(DecodedAisFieldObject raimDFO) {
        this.raimDFO = raimDFO;
    }

    public void setSyncStateDFO(DecodedAisFieldObject syncStateDFO) {
        this.syncStateDFO = syncStateDFO;
    }

    //endregion
}
