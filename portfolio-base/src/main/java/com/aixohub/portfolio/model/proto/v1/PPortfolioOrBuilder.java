// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: client.proto

package com.aixohub.portfolio.model.proto.v1;

public interface PPortfolioOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.aixohub.portfolio.PPortfolio)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string uuid = 1;</code>
   * @return The uuid.
   */
  java.lang.String getUuid();
  /**
   * <code>string uuid = 1;</code>
   * @return The bytes for uuid.
   */
  com.google.protobuf.ByteString
      getUuidBytes();

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
   * <code>optional string note = 3;</code>
   * @return Whether the note field is set.
   */
  boolean hasNote();
  /**
   * <code>optional string note = 3;</code>
   * @return The note.
   */
  java.lang.String getNote();
  /**
   * <code>optional string note = 3;</code>
   * @return The bytes for note.
   */
  com.google.protobuf.ByteString
      getNoteBytes();

  /**
   * <code>bool isRetired = 4;</code>
   * @return The isRetired.
   */
  boolean getIsRetired();

  /**
   * <code>optional string referenceAccount = 5;</code>
   * @return Whether the referenceAccount field is set.
   */
  boolean hasReferenceAccount();
  /**
   * <code>optional string referenceAccount = 5;</code>
   * @return The referenceAccount.
   */
  java.lang.String getReferenceAccount();
  /**
   * <code>optional string referenceAccount = 5;</code>
   * @return The bytes for referenceAccount.
   */
  com.google.protobuf.ByteString
      getReferenceAccountBytes();

  /**
   * <code>repeated .com.aixohub.portfolio.PKeyValue attributes = 6;</code>
   */
  java.util.List<com.aixohub.portfolio.model.proto.v1.PKeyValue> 
      getAttributesList();
  /**
   * <code>repeated .com.aixohub.portfolio.PKeyValue attributes = 6;</code>
   */
  com.aixohub.portfolio.model.proto.v1.PKeyValue getAttributes(int index);
  /**
   * <code>repeated .com.aixohub.portfolio.PKeyValue attributes = 6;</code>
   */
  int getAttributesCount();
  /**
   * <code>repeated .com.aixohub.portfolio.PKeyValue attributes = 6;</code>
   */
  java.util.List<? extends com.aixohub.portfolio.model.proto.v1.PKeyValueOrBuilder> 
      getAttributesOrBuilderList();
  /**
   * <code>repeated .com.aixohub.portfolio.PKeyValue attributes = 6;</code>
   */
  com.aixohub.portfolio.model.proto.v1.PKeyValueOrBuilder getAttributesOrBuilder(
      int index);

  /**
   * <code>.google.protobuf.Timestamp updatedAt = 7;</code>
   * @return Whether the updatedAt field is set.
   */
  boolean hasUpdatedAt();
  /**
   * <code>.google.protobuf.Timestamp updatedAt = 7;</code>
   * @return The updatedAt.
   */
  com.google.protobuf.Timestamp getUpdatedAt();
  /**
   * <code>.google.protobuf.Timestamp updatedAt = 7;</code>
   */
  com.google.protobuf.TimestampOrBuilder getUpdatedAtOrBuilder();
}