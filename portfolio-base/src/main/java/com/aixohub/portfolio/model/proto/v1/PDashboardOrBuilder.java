// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: client.proto

package com.aixohub.portfolio.model.proto.v1;

public interface PDashboardOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.aixohub.portfolio.PDashboard)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string name = 1;</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <code>string name = 1;</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>map&lt;string, string&gt; configuration = 2;</code>
   */
  int getConfigurationCount();
  /**
   * <code>map&lt;string, string&gt; configuration = 2;</code>
   */
  boolean containsConfiguration(
      java.lang.String key);
  /**
   * Use {@link #getConfigurationMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.String>
  getConfiguration();
  /**
   * <code>map&lt;string, string&gt; configuration = 2;</code>
   */
  java.util.Map<java.lang.String, java.lang.String>
  getConfigurationMap();
  /**
   * <code>map&lt;string, string&gt; configuration = 2;</code>
   */
  /* nullable */
java.lang.String getConfigurationOrDefault(
      java.lang.String key,
      /* nullable */
java.lang.String defaultValue);
  /**
   * <code>map&lt;string, string&gt; configuration = 2;</code>
   */
  java.lang.String getConfigurationOrThrow(
      java.lang.String key);

  /**
   * <code>repeated .com.aixohub.portfolio.PDashboard.Column columns = 3;</code>
   */
  java.util.List<com.aixohub.portfolio.model.proto.v1.PDashboard.Column> 
      getColumnsList();
  /**
   * <code>repeated .com.aixohub.portfolio.PDashboard.Column columns = 3;</code>
   */
  com.aixohub.portfolio.model.proto.v1.PDashboard.Column getColumns(int index);
  /**
   * <code>repeated .com.aixohub.portfolio.PDashboard.Column columns = 3;</code>
   */
  int getColumnsCount();
  /**
   * <code>repeated .com.aixohub.portfolio.PDashboard.Column columns = 3;</code>
   */
  java.util.List<? extends com.aixohub.portfolio.model.proto.v1.PDashboard.ColumnOrBuilder> 
      getColumnsOrBuilderList();
  /**
   * <code>repeated .com.aixohub.portfolio.PDashboard.Column columns = 3;</code>
   */
  com.aixohub.portfolio.model.proto.v1.PDashboard.ColumnOrBuilder getColumnsOrBuilder(
      int index);

  /**
   * <code>string id = 4;</code>
   * @return The id.
   */
  java.lang.String getId();
  /**
   * <code>string id = 4;</code>
   * @return The bytes for id.
   */
  com.google.protobuf.ByteString
      getIdBytes();
}
