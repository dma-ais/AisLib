package dk.dma.ais.json_decoder_helpers.enums;

public enum SyncState {
    UTC_DIRECT(0),
    UTC_INDIRECT(1),
    STATION_IS_SYNCHRONIZED_TO_BASE_STATION(2),
    STATION_IS_SYNCHRONIZED_TO_ANOTHER_STATION(3);

    private final int code;

    SyncState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static SyncState get(int intSyncState) {
        for (SyncState syncState : SyncState.values()) {
            if (intSyncState == syncState.getCode()) {
                return syncState;
            }
        }
        return UTC_DIRECT;
    }

    public String prettyPrint() {
        String specialMan = name().replace("_", " ");
        return specialMan.substring(0, 1) + specialMan.substring(1).toLowerCase();
    }


    @Override
    public String toString() {
        return prettyPrint();
    }
}
