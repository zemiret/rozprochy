// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: types.proto

package grpc.types;

/**
 * Protobuf enum {@code types.EventType}
 */
public enum EventType
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>ARMY_DEFEATED = 0;</code>
   */
  ARMY_DEFEATED(0),
  /**
   * <code>CITY_DESTROYED = 1;</code>
   */
  CITY_DESTROYED(1),
  /**
   * <code>KING_CHANGED = 2;</code>
   */
  KING_CHANGED(2),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>ARMY_DEFEATED = 0;</code>
   */
  public static final int ARMY_DEFEATED_VALUE = 0;
  /**
   * <code>CITY_DESTROYED = 1;</code>
   */
  public static final int CITY_DESTROYED_VALUE = 1;
  /**
   * <code>KING_CHANGED = 2;</code>
   */
  public static final int KING_CHANGED_VALUE = 2;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static EventType valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static EventType forNumber(int value) {
    switch (value) {
      case 0: return ARMY_DEFEATED;
      case 1: return CITY_DESTROYED;
      case 2: return KING_CHANGED;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<EventType>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      EventType> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<EventType>() {
          public EventType findValueByNumber(int number) {
            return EventType.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return grpc.types.TypesProto.getDescriptor().getEnumTypes().get(0);
  }

  private static final EventType[] VALUES = values();

  public static EventType valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private EventType(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:types.EventType)
}
