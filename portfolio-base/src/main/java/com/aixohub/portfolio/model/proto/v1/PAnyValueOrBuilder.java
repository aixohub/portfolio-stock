// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: client.proto

package com.aixohub.portfolio.model.proto.v1;

public interface PAnyValueOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.aixohub.portfolio.PAnyValue)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.google.protobuf.NullValue null = 1;</code>
   * @return Whether the null field is set.
   */
  boolean hasNull();
  /**
   * <code>.google.protobuf.NullValue null = 1;</code>
   * @return The enum numeric value on the wire for null.
   */
  int getNullValue();
  /**
   * <code>.google.protobuf.NullValue null = 1;</code>
   * @return The null.
   */
  com.google.protobuf.NullValue getNull();

  /**
   * <code>string string = 2;</code>
   * @return Whether the string field is set.
   */
  boolean hasString();
  /**
   * <code>string string = 2;</code>
   * @return The string.
   */
  java.lang.String getString();
  /**
   * <code>string string = 2;</code>
   * @return The bytes for string.
   */
  com.google.protobuf.ByteString
      getStringBytes();

  /**
   * <code>int32 int32 = 3;</code>
   * @return Whether the int32 field is set.
   */
  boolean hasInt32();
  /**
   * <code>int32 int32 = 3;</code>
   * @return The int32.
   */
  int getInt32();

  /**
   * <code>int64 int64 = 4;</code>
   * @return Whether the int64 field is set.
   */
  boolean hasInt64();
  /**
   * <code>int64 int64 = 4;</code>
   * @return The int64.
   */
  long getInt64();

  /**
   * <code>double double = 5;</code>
   * @return Whether the double field is set.
   */
  boolean hasDouble();
  /**
   * <code>double double = 5;</code>
   * @return The double.
   */
  double getDouble();

  /**
   * <code>bool bool = 6;</code>
   * @return Whether the bool field is set.
   */
  boolean hasBool();
  /**
   * <code>bool bool = 6;</code>
   * @return The bool.
   */
  boolean getBool();

  /**
   * <code>.com.aixohub.portfolio.PMap map = 7;</code>
   * @return Whether the map field is set.
   */
  boolean hasMap();
  /**
   * <code>.com.aixohub.portfolio.PMap map = 7;</code>
   * @return The map.
   */
  com.aixohub.portfolio.model.proto.v1.PMap getMap();
  /**
   * <code>.com.aixohub.portfolio.PMap map = 7;</code>
   */
  com.aixohub.portfolio.model.proto.v1.PMapOrBuilder getMapOrBuilder();

  com.aixohub.portfolio.model.proto.v1.PAnyValue.KindCase getKindCase();
}
