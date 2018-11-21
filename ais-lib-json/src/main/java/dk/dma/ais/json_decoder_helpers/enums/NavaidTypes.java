package dk.dma.ais.json_decoder_helpers.enums;

public enum NavaidTypes {
    TYPE_OF_AID_TO_NAVIGATION_NOT_SPECIFIED(0), //default
    REFERENCE_POINT(1),
    RACON_RADAR_TRANSPONDER_MARKING_A_NAVIGATION_HAZARD(2),
    FIXED_STRUCTURE_OFF_SHORE_SUCH_AS_OIL_PLATFORMS_WIND_FARMS_RIGS(3),
    SPARE_RESERVED_FOR_FUTURE_USE(4),
    LIGHT_WITHOUT_SECTORS(5),
    LIGHT_WITH_SECTORS(6),
    LEADING_LIGHT_FRONT(7),
    LEADING_LIGHT_REAR(8),
    BEACON_CARDINAL_N(9),
    BEACON_CARDINAL_E(10),
    BEACON_CARDINAL_S(11),
    BEACON_CARDINAL_W(12),
    BEACON_PORT_HAND(13),
    BEACON_STARBOARD_HAND(14),
    BEACON_PREFERRED_CHANNEL_PORT_HAND(15),
    BEACON_PREFERRED_CHANNEL_STARBOARD_HAND(16),
    BEACON_ISOLATED_DANGER(17),
    BEACON_SAFE_WATER(18),
    BEACON_SPECIAL_MARK(19),
    CARDINAL_MARK_N(20),
    CARDINAL_MARK_E(21),
    CARDINAL_MARK_S(22),
    CARDINAL_MARK_W(23),
    PORT_HAND_MARK(24),
    STARBOARD_HAND_MARK(25),
    PREFERRED_CHANNEL_PORT_HAND(26),
    PREFERRED_CHANNEL_STARBOARD_HAND(27),
    ISOLATED_DANGER(28),
    SAFE_WATER(29),
    SPECIAL_MARK(30),
    LIGHT_VESSEL_LANBY_RIGS(31);

    private final int code;

    NavaidTypes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static NavaidTypes get(int navAidCode) {
        for (NavaidTypes navaidTypes : NavaidTypes.values()) {
            if (navaidTypes.getCode() == navAidCode) {
                return navaidTypes;
            }
        }
        return TYPE_OF_AID_TO_NAVIGATION_NOT_SPECIFIED;
    }

    public String prettyPrint() {
        String navaid = name().replace("_", " ");
        return navaid.substring(0, 1) + navaid.substring(1).toLowerCase();
    }


    @Override
    public String toString() {
        return prettyPrint();
    }
}
