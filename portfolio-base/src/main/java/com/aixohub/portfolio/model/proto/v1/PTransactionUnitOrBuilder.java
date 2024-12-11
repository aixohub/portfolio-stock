// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: client.proto

package com.aixohub.portfolio.model.proto.v1;

public interface PTransactionUnitOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.aixohub.portfolio.PTransactionUnit)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.com.aixohub.portfolio.PTransactionUnit.Type type = 1;</code>
   * @return The enum numeric value on the wire for type.
   */
  int getTypeValue();
  /**
   * <code>.com.aixohub.portfolio.PTransactionUnit.Type type = 1;</code>
   * @return The type.
   */
  com.aixohub.portfolio.model.proto.v1.PTransactionUnit.Type getType();

  /**
   * <code>int64 amount = 2;</code>
   * @return The amount.
   */
  long getAmount();

  /**
   * <code>string currencyCode = 3;</code>
   * @return The currencyCode.
   */
  java.lang.String getCurrencyCode();
  /**
   * <code>string currencyCode = 3;</code>
   * @return The bytes for currencyCode.
   */
  com.google.protobuf.ByteString
      getCurrencyCodeBytes();

  /**
   * <code>optional int64 fxAmount = 4;</code>
   * @return Whether the fxAmount field is set.
   */
  boolean hasFxAmount();
  /**
   * <code>optional int64 fxAmount = 4;</code>
   * @return The fxAmount.
   */
  long getFxAmount();

  /**
   * <code>optional string fxCurrencyCode = 5;</code>
   * @return Whether the fxCurrencyCode field is set.
   */
  boolean hasFxCurrencyCode();
  /**
   * <code>optional string fxCurrencyCode = 5;</code>
   * @return The fxCurrencyCode.
   */
  java.lang.String getFxCurrencyCode();
  /**
   * <code>optional string fxCurrencyCode = 5;</code>
   * @return The bytes for fxCurrencyCode.
   */
  com.google.protobuf.ByteString
      getFxCurrencyCodeBytes();

  /**
   * <code>optional .com.aixohub.portfolio.PDecimalValue fxRateToBase = 6;</code>
   * @return Whether the fxRateToBase field is set.
   */
  boolean hasFxRateToBase();
  /**
   * <code>optional .com.aixohub.portfolio.PDecimalValue fxRateToBase = 6;</code>
   * @return The fxRateToBase.
   */
  com.aixohub.portfolio.model.proto.v1.PDecimalValue getFxRateToBase();
  /**
   * <code>optional .com.aixohub.portfolio.PDecimalValue fxRateToBase = 6;</code>
   */
  com.aixohub.portfolio.model.proto.v1.PDecimalValueOrBuilder getFxRateToBaseOrBuilder();
}
