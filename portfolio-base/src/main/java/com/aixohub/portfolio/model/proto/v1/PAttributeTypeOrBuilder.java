// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: client.proto

package com.aixohub.portfolio.model.proto.v1;

public interface PAttributeTypeOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.aixohub.portfolio.PAttributeType)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string id = 1;</code>
   * @return The id.
   */
  java.lang.String getId();
  /**
   * <code>string id = 1;</code>
   * @return The bytes for id.
   */
  com.google.protobuf.ByteString
      getIdBytes();

  /**
   * <code>string name = 2;</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <code>string name = 2;</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>string columnLabel = 3;</code>
   * @return The columnLabel.
   */
  java.lang.String getColumnLabel();
  /**
   * <code>string columnLabel = 3;</code>
   * @return The bytes for columnLabel.
   */
  com.google.protobuf.ByteString
      getColumnLabelBytes();

  /**
   * <code>optional string source = 4;</code>
   * @return Whether the source field is set.
   */
  boolean hasSource();
  /**
   * <code>optional string source = 4;</code>
   * @return The source.
   */
  java.lang.String getSource();
  /**
   * <code>optional string source = 4;</code>
   * @return The bytes for source.
   */
  com.google.protobuf.ByteString
      getSourceBytes();

  /**
   * <code>string target = 5;</code>
   * @return The target.
   */
  java.lang.String getTarget();
  /**
   * <code>string target = 5;</code>
   * @return The bytes for target.
   */
  com.google.protobuf.ByteString
      getTargetBytes();

  /**
   * <code>string type = 6;</code>
   * @return The type.
   */
  java.lang.String getType();
  /**
   * <code>string type = 6;</code>
   * @return The bytes for type.
   */
  com.google.protobuf.ByteString
      getTypeBytes();

  /**
   * <code>string converterClass = 7;</code>
   * @return The converterClass.
   */
  java.lang.String getConverterClass();
  /**
   * <code>string converterClass = 7;</code>
   * @return The bytes for converterClass.
   */
  com.google.protobuf.ByteString
      getConverterClassBytes();

  /**
   * <code>.com.aixohub.portfolio.PMap properties = 8;</code>
   * @return Whether the properties field is set.
   */
  boolean hasProperties();
  /**
   * <code>.com.aixohub.portfolio.PMap properties = 8;</code>
   * @return The properties.
   */
  com.aixohub.portfolio.model.proto.v1.PMap getProperties();
  /**
   * <code>.com.aixohub.portfolio.PMap properties = 8;</code>
   */
  com.aixohub.portfolio.model.proto.v1.PMapOrBuilder getPropertiesOrBuilder();
}
