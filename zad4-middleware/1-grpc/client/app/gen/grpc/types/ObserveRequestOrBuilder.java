// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: types.proto

package grpc.types;

public interface ObserveRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:types.ObserveRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.types.EventType type = 1;</code>
   * @return The enum numeric value on the wire for type.
   */
  int getTypeValue();
  /**
   * <code>.types.EventType type = 1;</code>
   * @return The type.
   */
  grpc.types.EventType getType();

  /**
   * <code>.types.EventSeverity severity = 2;</code>
   * @return The enum numeric value on the wire for severity.
   */
  int getSeverityValue();
  /**
   * <code>.types.EventSeverity severity = 2;</code>
   * @return The severity.
   */
  grpc.types.EventSeverity getSeverity();

  /**
   * <code>string kingdom = 3;</code>
   * @return The kingdom.
   */
  java.lang.String getKingdom();
  /**
   * <code>string kingdom = 3;</code>
   * @return The bytes for kingdom.
   */
  com.google.protobuf.ByteString
      getKingdomBytes();
}
