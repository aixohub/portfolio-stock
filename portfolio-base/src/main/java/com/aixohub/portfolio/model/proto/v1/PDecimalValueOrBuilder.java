// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: client.proto

package com.aixohub.portfolio.model.proto.v1;

public interface PDecimalValueOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.aixohub.portfolio.PDecimalValue)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>uint32 scale = 1;</code>
   * @return The scale.
   */
  int getScale();

  /**
   * <code>uint32 precision = 2;</code>
   * @return The precision.
   */
  int getPrecision();

  /**
   * <code>bytes value = 3;</code>
   * @return The value.
   */
  com.google.protobuf.ByteString getValue();
}