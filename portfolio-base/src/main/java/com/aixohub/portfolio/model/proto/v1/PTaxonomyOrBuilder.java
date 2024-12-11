// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: client.proto

package com.aixohub.portfolio.model.proto.v1;

public interface PTaxonomyOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.aixohub.portfolio.PTaxonomy)
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
   * <code>optional string source = 3;</code>
   * @return Whether the source field is set.
   */
  boolean hasSource();
  /**
   * <code>optional string source = 3;</code>
   * @return The source.
   */
  java.lang.String getSource();
  /**
   * <code>optional string source = 3;</code>
   * @return The bytes for source.
   */
  com.google.protobuf.ByteString
      getSourceBytes();

  /**
   * <code>repeated string dimensions = 4;</code>
   * @return A list containing the dimensions.
   */
  java.util.List<java.lang.String>
      getDimensionsList();
  /**
   * <code>repeated string dimensions = 4;</code>
   * @return The count of dimensions.
   */
  int getDimensionsCount();
  /**
   * <code>repeated string dimensions = 4;</code>
   * @param index The index of the element to return.
   * @return The dimensions at the given index.
   */
  java.lang.String getDimensions(int index);
  /**
   * <code>repeated string dimensions = 4;</code>
   * @param index The index of the value to return.
   * @return The bytes of the dimensions at the given index.
   */
  com.google.protobuf.ByteString
      getDimensionsBytes(int index);

  /**
   * <code>repeated .com.aixohub.portfolio.PTaxonomy.Classification classifications = 5;</code>
   */
  java.util.List<com.aixohub.portfolio.model.proto.v1.PTaxonomy.Classification> 
      getClassificationsList();
  /**
   * <code>repeated .com.aixohub.portfolio.PTaxonomy.Classification classifications = 5;</code>
   */
  com.aixohub.portfolio.model.proto.v1.PTaxonomy.Classification getClassifications(int index);
  /**
   * <code>repeated .com.aixohub.portfolio.PTaxonomy.Classification classifications = 5;</code>
   */
  int getClassificationsCount();
  /**
   * <code>repeated .com.aixohub.portfolio.PTaxonomy.Classification classifications = 5;</code>
   */
  java.util.List<? extends com.aixohub.portfolio.model.proto.v1.PTaxonomy.ClassificationOrBuilder> 
      getClassificationsOrBuilderList();
  /**
   * <code>repeated .com.aixohub.portfolio.PTaxonomy.Classification classifications = 5;</code>
   */
  com.aixohub.portfolio.model.proto.v1.PTaxonomy.ClassificationOrBuilder getClassificationsOrBuilder(
      int index);
}