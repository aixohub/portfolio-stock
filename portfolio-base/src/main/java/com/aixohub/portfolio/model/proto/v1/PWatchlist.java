// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: client.proto

package com.aixohub.portfolio.model.proto.v1;

/**
 * Protobuf type {@code com.aixohub.portfolio.PWatchlist}
 */
public final class PWatchlist extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.aixohub.portfolio.PWatchlist)
    PWatchlistOrBuilder {
private static final long serialVersionUID = 0L;
  // Use PWatchlist.newBuilder() to construct.
  private PWatchlist(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private PWatchlist() {
    name_ = "";
    securities_ =
        com.google.protobuf.LazyStringArrayList.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new PWatchlist();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.aixohub.portfolio.model.proto.v1.ClientProtos.internal_static_name_abuchen_portfolio_PWatchlist_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.aixohub.portfolio.model.proto.v1.ClientProtos.internal_static_name_abuchen_portfolio_PWatchlist_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.aixohub.portfolio.model.proto.v1.PWatchlist.class, com.aixohub.portfolio.model.proto.v1.PWatchlist.Builder.class);
  }

  public static final int NAME_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private volatile java.lang.Object name_ = "";
  /**
   * <code>string name = 1;</code>
   * @return The name.
   */
  @java.lang.Override
  public java.lang.String getName() {
    java.lang.Object ref = name_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      name_ = s;
      return s;
    }
  }
  /**
   * <code>string name = 1;</code>
   * @return The bytes for name.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getNameBytes() {
    java.lang.Object ref = name_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      name_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int SECURITIES_FIELD_NUMBER = 2;
  @SuppressWarnings("serial")
  private com.google.protobuf.LazyStringArrayList securities_ =
      com.google.protobuf.LazyStringArrayList.emptyList();
  /**
   * <pre>
   * uuids
   * </pre>
   *
   * <code>repeated string securities = 2;</code>
   * @return A list containing the securities.
   */
  public com.google.protobuf.ProtocolStringList
      getSecuritiesList() {
    return securities_;
  }
  /**
   * <pre>
   * uuids
   * </pre>
   *
   * <code>repeated string securities = 2;</code>
   * @return The count of securities.
   */
  public int getSecuritiesCount() {
    return securities_.size();
  }
  /**
   * <pre>
   * uuids
   * </pre>
   *
   * <code>repeated string securities = 2;</code>
   * @param index The index of the element to return.
   * @return The securities at the given index.
   */
  public java.lang.String getSecurities(int index) {
    return securities_.get(index);
  }
  /**
   * <pre>
   * uuids
   * </pre>
   *
   * <code>repeated string securities = 2;</code>
   * @param index The index of the value to return.
   * @return The bytes of the securities at the given index.
   */
  public com.google.protobuf.ByteString
      getSecuritiesBytes(int index) {
    return securities_.getByteString(index);
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(name_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, name_);
    }
    for (int i = 0; i < securities_.size(); i++) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, securities_.getRaw(i));
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(name_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, name_);
    }
    {
      int dataSize = 0;
      for (int i = 0; i < securities_.size(); i++) {
        dataSize += computeStringSizeNoTag(securities_.getRaw(i));
      }
      size += dataSize;
      size += 1 * getSecuritiesList().size();
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.aixohub.portfolio.model.proto.v1.PWatchlist)) {
      return super.equals(obj);
    }
    com.aixohub.portfolio.model.proto.v1.PWatchlist other = (com.aixohub.portfolio.model.proto.v1.PWatchlist) obj;

    if (!getName()
        .equals(other.getName())) return false;
    if (!getSecuritiesList()
        .equals(other.getSecuritiesList())) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + NAME_FIELD_NUMBER;
    hash = (53 * hash) + getName().hashCode();
    if (getSecuritiesCount() > 0) {
      hash = (37 * hash) + SECURITIES_FIELD_NUMBER;
      hash = (53 * hash) + getSecuritiesList().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.aixohub.portfolio.model.proto.v1.PWatchlist parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.aixohub.portfolio.model.proto.v1.PWatchlist parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.aixohub.portfolio.model.proto.v1.PWatchlist parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.aixohub.portfolio.model.proto.v1.PWatchlist parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.aixohub.portfolio.model.proto.v1.PWatchlist parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.aixohub.portfolio.model.proto.v1.PWatchlist parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.aixohub.portfolio.model.proto.v1.PWatchlist parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.aixohub.portfolio.model.proto.v1.PWatchlist parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.aixohub.portfolio.model.proto.v1.PWatchlist parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.aixohub.portfolio.model.proto.v1.PWatchlist parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.aixohub.portfolio.model.proto.v1.PWatchlist parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.aixohub.portfolio.model.proto.v1.PWatchlist parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.aixohub.portfolio.model.proto.v1.PWatchlist prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code com.aixohub.portfolio.PWatchlist}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.aixohub.portfolio.PWatchlist)
      com.aixohub.portfolio.model.proto.v1.PWatchlistOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.aixohub.portfolio.model.proto.v1.ClientProtos.internal_static_name_abuchen_portfolio_PWatchlist_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.aixohub.portfolio.model.proto.v1.ClientProtos.internal_static_name_abuchen_portfolio_PWatchlist_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.aixohub.portfolio.model.proto.v1.PWatchlist.class, com.aixohub.portfolio.model.proto.v1.PWatchlist.Builder.class);
    }

    // Construct using com.aixohub.portfolio.model.proto.v1.PWatchlist.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      name_ = "";
      securities_ =
          com.google.protobuf.LazyStringArrayList.emptyList();
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.aixohub.portfolio.model.proto.v1.ClientProtos.internal_static_name_abuchen_portfolio_PWatchlist_descriptor;
    }

    @java.lang.Override
    public com.aixohub.portfolio.model.proto.v1.PWatchlist getDefaultInstanceForType() {
      return com.aixohub.portfolio.model.proto.v1.PWatchlist.getDefaultInstance();
    }

    @java.lang.Override
    public com.aixohub.portfolio.model.proto.v1.PWatchlist build() {
      com.aixohub.portfolio.model.proto.v1.PWatchlist result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.aixohub.portfolio.model.proto.v1.PWatchlist buildPartial() {
      com.aixohub.portfolio.model.proto.v1.PWatchlist result = new com.aixohub.portfolio.model.proto.v1.PWatchlist(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.aixohub.portfolio.model.proto.v1.PWatchlist result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.name_ = name_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        securities_.makeImmutable();
        result.securities_ = securities_;
      }
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.aixohub.portfolio.model.proto.v1.PWatchlist) {
        return mergeFrom((com.aixohub.portfolio.model.proto.v1.PWatchlist)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.aixohub.portfolio.model.proto.v1.PWatchlist other) {
      if (other == com.aixohub.portfolio.model.proto.v1.PWatchlist.getDefaultInstance()) return this;
      if (!other.getName().isEmpty()) {
        name_ = other.name_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (!other.securities_.isEmpty()) {
        if (securities_.isEmpty()) {
          securities_ = other.securities_;
          bitField0_ |= 0x00000002;
        } else {
          ensureSecuritiesIsMutable();
          securities_.addAll(other.securities_);
        }
        onChanged();
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              name_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 18: {
              java.lang.String s = input.readStringRequireUtf8();
              ensureSecuritiesIsMutable();
              securities_.add(s);
              break;
            } // case 18
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private java.lang.Object name_ = "";
    /**
     * <code>string name = 1;</code>
     * @return The name.
     */
    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        name_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string name = 1;</code>
     * @return The bytes for name.
     */
    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string name = 1;</code>
     * @param value The name to set.
     * @return This builder for chaining.
     */
    public Builder setName(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      name_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>string name = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearName() {
      name_ = getDefaultInstance().getName();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>string name = 1;</code>
     * @param value The bytes for name to set.
     * @return This builder for chaining.
     */
    public Builder setNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      name_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }

    private com.google.protobuf.LazyStringArrayList securities_ =
        com.google.protobuf.LazyStringArrayList.emptyList();
    private void ensureSecuritiesIsMutable() {
      if (!securities_.isModifiable()) {
        securities_ = new com.google.protobuf.LazyStringArrayList(securities_);
      }
      bitField0_ |= 0x00000002;
    }
    /**
     * <pre>
     * uuids
     * </pre>
     *
     * <code>repeated string securities = 2;</code>
     * @return A list containing the securities.
     */
    public com.google.protobuf.ProtocolStringList
        getSecuritiesList() {
      securities_.makeImmutable();
      return securities_;
    }
    /**
     * <pre>
     * uuids
     * </pre>
     *
     * <code>repeated string securities = 2;</code>
     * @return The count of securities.
     */
    public int getSecuritiesCount() {
      return securities_.size();
    }
    /**
     * <pre>
     * uuids
     * </pre>
     *
     * <code>repeated string securities = 2;</code>
     * @param index The index of the element to return.
     * @return The securities at the given index.
     */
    public java.lang.String getSecurities(int index) {
      return securities_.get(index);
    }
    /**
     * <pre>
     * uuids
     * </pre>
     *
     * <code>repeated string securities = 2;</code>
     * @param index The index of the value to return.
     * @return The bytes of the securities at the given index.
     */
    public com.google.protobuf.ByteString
        getSecuritiesBytes(int index) {
      return securities_.getByteString(index);
    }
    /**
     * <pre>
     * uuids
     * </pre>
     *
     * <code>repeated string securities = 2;</code>
     * @param index The index to set the value at.
     * @param value The securities to set.
     * @return This builder for chaining.
     */
    public Builder setSecurities(
        int index, java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      ensureSecuritiesIsMutable();
      securities_.set(index, value);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * uuids
     * </pre>
     *
     * <code>repeated string securities = 2;</code>
     * @param value The securities to add.
     * @return This builder for chaining.
     */
    public Builder addSecurities(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      ensureSecuritiesIsMutable();
      securities_.add(value);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * uuids
     * </pre>
     *
     * <code>repeated string securities = 2;</code>
     * @param values The securities to add.
     * @return This builder for chaining.
     */
    public Builder addAllSecurities(
        java.lang.Iterable<java.lang.String> values) {
      ensureSecuritiesIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, securities_);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * uuids
     * </pre>
     *
     * <code>repeated string securities = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearSecurities() {
      securities_ =
        com.google.protobuf.LazyStringArrayList.emptyList();
      bitField0_ = (bitField0_ & ~0x00000002);;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * uuids
     * </pre>
     *
     * <code>repeated string securities = 2;</code>
     * @param value The bytes of the securities to add.
     * @return This builder for chaining.
     */
    public Builder addSecuritiesBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      ensureSecuritiesIsMutable();
      securities_.add(value);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:com.aixohub.portfolio.PWatchlist)
  }

  // @@protoc_insertion_point(class_scope:com.aixohub.portfolio.PWatchlist)
  private static final com.aixohub.portfolio.model.proto.v1.PWatchlist DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.aixohub.portfolio.model.proto.v1.PWatchlist();
  }

  public static com.aixohub.portfolio.model.proto.v1.PWatchlist getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<PWatchlist>
      PARSER = new com.google.protobuf.AbstractParser<PWatchlist>() {
    @java.lang.Override
    public PWatchlist parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<PWatchlist> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<PWatchlist> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.aixohub.portfolio.model.proto.v1.PWatchlist getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

