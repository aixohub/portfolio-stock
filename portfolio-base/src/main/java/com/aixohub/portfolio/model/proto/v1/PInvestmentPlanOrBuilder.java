// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: client.proto

package com.aixohub.portfolio.model.proto.v1;

public interface PInvestmentPlanOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.aixohub.portfolio.PInvestmentPlan)
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
   * <code>optional string note = 2;</code>
   * @return Whether the note field is set.
   */
  boolean hasNote();
  /**
   * <code>optional string note = 2;</code>
   * @return The note.
   */
  java.lang.String getNote();
  /**
   * <code>optional string note = 2;</code>
   * @return The bytes for note.
   */
  com.google.protobuf.ByteString
      getNoteBytes();

  /**
   * <code>optional string security = 3;</code>
   * @return Whether the security field is set.
   */
  boolean hasSecurity();
  /**
   * <code>optional string security = 3;</code>
   * @return The security.
   */
  java.lang.String getSecurity();
  /**
   * <code>optional string security = 3;</code>
   * @return The bytes for security.
   */
  com.google.protobuf.ByteString
      getSecurityBytes();

  /**
   * <code>optional string portfolio = 4;</code>
   * @return Whether the portfolio field is set.
   */
  boolean hasPortfolio();
  /**
   * <code>optional string portfolio = 4;</code>
   * @return The portfolio.
   */
  java.lang.String getPortfolio();
  /**
   * <code>optional string portfolio = 4;</code>
   * @return The bytes for portfolio.
   */
  com.google.protobuf.ByteString
      getPortfolioBytes();

  /**
   * <code>optional string account = 5;</code>
   * @return Whether the account field is set.
   */
  boolean hasAccount();
  /**
   * <code>optional string account = 5;</code>
   * @return The account.
   */
  java.lang.String getAccount();
  /**
   * <code>optional string account = 5;</code>
   * @return The bytes for account.
   */
  com.google.protobuf.ByteString
      getAccountBytes();

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
   * <code>bool autoGenerate = 7;</code>
   * @return The autoGenerate.
   */
  boolean getAutoGenerate();

  /**
   * <code>int64 date = 8;</code>
   * @return The date.
   */
  long getDate();

  /**
   * <code>int32 interval = 9;</code>
   * @return The interval.
   */
  int getInterval();

  /**
   * <code>int64 amount = 10;</code>
   * @return The amount.
   */
  long getAmount();

  /**
   * <code>int64 fees = 11;</code>
   * @return The fees.
   */
  long getFees();

  /**
   * <pre>
   * uuids
   * </pre>
   *
   * <code>repeated string transactions = 12;</code>
   * @return A list containing the transactions.
   */
  java.util.List<java.lang.String>
      getTransactionsList();
  /**
   * <pre>
   * uuids
   * </pre>
   *
   * <code>repeated string transactions = 12;</code>
   * @return The count of transactions.
   */
  int getTransactionsCount();
  /**
   * <pre>
   * uuids
   * </pre>
   *
   * <code>repeated string transactions = 12;</code>
   * @param index The index of the element to return.
   * @return The transactions at the given index.
   */
  java.lang.String getTransactions(int index);
  /**
   * <pre>
   * uuids
   * </pre>
   *
   * <code>repeated string transactions = 12;</code>
   * @param index The index of the value to return.
   * @return The bytes of the transactions at the given index.
   */
  com.google.protobuf.ByteString
      getTransactionsBytes(int index);

  /**
   * <code>int64 taxes = 13;</code>
   * @return The taxes.
   */
  long getTaxes();

  /**
   * <code>.com.aixohub.portfolio.PInvestmentPlan.Type type = 14;</code>
   * @return The enum numeric value on the wire for type.
   */
  int getTypeValue();
  /**
   * <code>.com.aixohub.portfolio.PInvestmentPlan.Type type = 14;</code>
   * @return The type.
   */
  com.aixohub.portfolio.model.proto.v1.PInvestmentPlan.Type getType();
}