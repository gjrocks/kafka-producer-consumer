/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.gj.kafka.model;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class Employee extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 3075715584313187749L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Employee\",\"namespace\":\"com.gj.kafka.model\",\"fields\":[{\"name\":\"fName\",\"type\":\"string\"},{\"name\":\"lName\",\"type\":\"string\"},{\"name\":\"age\",\"type\":\"int\",\"default\":0},{\"name\":\"phoneNumber\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"empId\",\"type\":\"string\",\"default\":\"\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<Employee> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<Employee> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<Employee> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<Employee> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<Employee> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this Employee to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a Employee from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a Employee instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static Employee fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private java.lang.CharSequence fName;
  private java.lang.CharSequence lName;
  private int age;
  private java.lang.CharSequence phoneNumber;
  private java.lang.CharSequence empId;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public Employee() {}

  /**
   * All-args constructor.
   * @param fName The new value for fName
   * @param lName The new value for lName
   * @param age The new value for age
   * @param phoneNumber The new value for phoneNumber
   * @param empId The new value for empId
   */
  public Employee(java.lang.CharSequence fName, java.lang.CharSequence lName, java.lang.Integer age, java.lang.CharSequence phoneNumber, java.lang.CharSequence empId) {
    this.fName = fName;
    this.lName = lName;
    this.age = age;
    this.phoneNumber = phoneNumber;
    this.empId = empId;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return fName;
    case 1: return lName;
    case 2: return age;
    case 3: return phoneNumber;
    case 4: return empId;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: fName = (java.lang.CharSequence)value$; break;
    case 1: lName = (java.lang.CharSequence)value$; break;
    case 2: age = (java.lang.Integer)value$; break;
    case 3: phoneNumber = (java.lang.CharSequence)value$; break;
    case 4: empId = (java.lang.CharSequence)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'fName' field.
   * @return The value of the 'fName' field.
   */
  public java.lang.CharSequence getFName() {
    return fName;
  }


  /**
   * Sets the value of the 'fName' field.
   * @param value the value to set.
   */
  public void setFName(java.lang.CharSequence value) {
    this.fName = value;
  }

  /**
   * Gets the value of the 'lName' field.
   * @return The value of the 'lName' field.
   */
  public java.lang.CharSequence getLName() {
    return lName;
  }


  /**
   * Sets the value of the 'lName' field.
   * @param value the value to set.
   */
  public void setLName(java.lang.CharSequence value) {
    this.lName = value;
  }

  /**
   * Gets the value of the 'age' field.
   * @return The value of the 'age' field.
   */
  public int getAge() {
    return age;
  }


  /**
   * Sets the value of the 'age' field.
   * @param value the value to set.
   */
  public void setAge(int value) {
    this.age = value;
  }

  /**
   * Gets the value of the 'phoneNumber' field.
   * @return The value of the 'phoneNumber' field.
   */
  public java.lang.CharSequence getPhoneNumber() {
    return phoneNumber;
  }


  /**
   * Sets the value of the 'phoneNumber' field.
   * @param value the value to set.
   */
  public void setPhoneNumber(java.lang.CharSequence value) {
    this.phoneNumber = value;
  }

  /**
   * Gets the value of the 'empId' field.
   * @return The value of the 'empId' field.
   */
  public java.lang.CharSequence getEmpId() {
    return empId;
  }


  /**
   * Sets the value of the 'empId' field.
   * @param value the value to set.
   */
  public void setEmpId(java.lang.CharSequence value) {
    this.empId = value;
  }

  /**
   * Creates a new Employee RecordBuilder.
   * @return A new Employee RecordBuilder
   */
  public static com.gj.kafka.model.Employee.Builder newBuilder() {
    return new com.gj.kafka.model.Employee.Builder();
  }

  /**
   * Creates a new Employee RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new Employee RecordBuilder
   */
  public static com.gj.kafka.model.Employee.Builder newBuilder(com.gj.kafka.model.Employee.Builder other) {
    if (other == null) {
      return new com.gj.kafka.model.Employee.Builder();
    } else {
      return new com.gj.kafka.model.Employee.Builder(other);
    }
  }

  /**
   * Creates a new Employee RecordBuilder by copying an existing Employee instance.
   * @param other The existing instance to copy.
   * @return A new Employee RecordBuilder
   */
  public static com.gj.kafka.model.Employee.Builder newBuilder(com.gj.kafka.model.Employee other) {
    if (other == null) {
      return new com.gj.kafka.model.Employee.Builder();
    } else {
      return new com.gj.kafka.model.Employee.Builder(other);
    }
  }

  /**
   * RecordBuilder for Employee instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Employee>
    implements org.apache.avro.data.RecordBuilder<Employee> {

    private java.lang.CharSequence fName;
    private java.lang.CharSequence lName;
    private int age;
    private java.lang.CharSequence phoneNumber;
    private java.lang.CharSequence empId;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.gj.kafka.model.Employee.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.fName)) {
        this.fName = data().deepCopy(fields()[0].schema(), other.fName);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.lName)) {
        this.lName = data().deepCopy(fields()[1].schema(), other.lName);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.age)) {
        this.age = data().deepCopy(fields()[2].schema(), other.age);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.phoneNumber)) {
        this.phoneNumber = data().deepCopy(fields()[3].schema(), other.phoneNumber);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
      if (isValidValue(fields()[4], other.empId)) {
        this.empId = data().deepCopy(fields()[4].schema(), other.empId);
        fieldSetFlags()[4] = other.fieldSetFlags()[4];
      }
    }

    /**
     * Creates a Builder by copying an existing Employee instance
     * @param other The existing instance to copy.
     */
    private Builder(com.gj.kafka.model.Employee other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.fName)) {
        this.fName = data().deepCopy(fields()[0].schema(), other.fName);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.lName)) {
        this.lName = data().deepCopy(fields()[1].schema(), other.lName);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.age)) {
        this.age = data().deepCopy(fields()[2].schema(), other.age);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.phoneNumber)) {
        this.phoneNumber = data().deepCopy(fields()[3].schema(), other.phoneNumber);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.empId)) {
        this.empId = data().deepCopy(fields()[4].schema(), other.empId);
        fieldSetFlags()[4] = true;
      }
    }

    /**
      * Gets the value of the 'fName' field.
      * @return The value.
      */
    public java.lang.CharSequence getFName() {
      return fName;
    }


    /**
      * Sets the value of the 'fName' field.
      * @param value The value of 'fName'.
      * @return This builder.
      */
    public com.gj.kafka.model.Employee.Builder setFName(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.fName = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'fName' field has been set.
      * @return True if the 'fName' field has been set, false otherwise.
      */
    public boolean hasFName() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'fName' field.
      * @return This builder.
      */
    public com.gj.kafka.model.Employee.Builder clearFName() {
      fName = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'lName' field.
      * @return The value.
      */
    public java.lang.CharSequence getLName() {
      return lName;
    }


    /**
      * Sets the value of the 'lName' field.
      * @param value The value of 'lName'.
      * @return This builder.
      */
    public com.gj.kafka.model.Employee.Builder setLName(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.lName = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'lName' field has been set.
      * @return True if the 'lName' field has been set, false otherwise.
      */
    public boolean hasLName() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'lName' field.
      * @return This builder.
      */
    public com.gj.kafka.model.Employee.Builder clearLName() {
      lName = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'age' field.
      * @return The value.
      */
    public int getAge() {
      return age;
    }


    /**
      * Sets the value of the 'age' field.
      * @param value The value of 'age'.
      * @return This builder.
      */
    public com.gj.kafka.model.Employee.Builder setAge(int value) {
      validate(fields()[2], value);
      this.age = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'age' field has been set.
      * @return True if the 'age' field has been set, false otherwise.
      */
    public boolean hasAge() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'age' field.
      * @return This builder.
      */
    public com.gj.kafka.model.Employee.Builder clearAge() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'phoneNumber' field.
      * @return The value.
      */
    public java.lang.CharSequence getPhoneNumber() {
      return phoneNumber;
    }


    /**
      * Sets the value of the 'phoneNumber' field.
      * @param value The value of 'phoneNumber'.
      * @return This builder.
      */
    public com.gj.kafka.model.Employee.Builder setPhoneNumber(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.phoneNumber = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'phoneNumber' field has been set.
      * @return True if the 'phoneNumber' field has been set, false otherwise.
      */
    public boolean hasPhoneNumber() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'phoneNumber' field.
      * @return This builder.
      */
    public com.gj.kafka.model.Employee.Builder clearPhoneNumber() {
      phoneNumber = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'empId' field.
      * @return The value.
      */
    public java.lang.CharSequence getEmpId() {
      return empId;
    }


    /**
      * Sets the value of the 'empId' field.
      * @param value The value of 'empId'.
      * @return This builder.
      */
    public com.gj.kafka.model.Employee.Builder setEmpId(java.lang.CharSequence value) {
      validate(fields()[4], value);
      this.empId = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'empId' field has been set.
      * @return True if the 'empId' field has been set, false otherwise.
      */
    public boolean hasEmpId() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'empId' field.
      * @return This builder.
      */
    public com.gj.kafka.model.Employee.Builder clearEmpId() {
      empId = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Employee build() {
      try {
        Employee record = new Employee();
        record.fName = fieldSetFlags()[0] ? this.fName : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.lName = fieldSetFlags()[1] ? this.lName : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.age = fieldSetFlags()[2] ? this.age : (java.lang.Integer) defaultValue(fields()[2]);
        record.phoneNumber = fieldSetFlags()[3] ? this.phoneNumber : (java.lang.CharSequence) defaultValue(fields()[3]);
        record.empId = fieldSetFlags()[4] ? this.empId : (java.lang.CharSequence) defaultValue(fields()[4]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<Employee>
    WRITER$ = (org.apache.avro.io.DatumWriter<Employee>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<Employee>
    READER$ = (org.apache.avro.io.DatumReader<Employee>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.fName);

    out.writeString(this.lName);

    out.writeInt(this.age);

    out.writeString(this.phoneNumber);

    out.writeString(this.empId);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.fName = in.readString(this.fName instanceof Utf8 ? (Utf8)this.fName : null);

      this.lName = in.readString(this.lName instanceof Utf8 ? (Utf8)this.lName : null);

      this.age = in.readInt();

      this.phoneNumber = in.readString(this.phoneNumber instanceof Utf8 ? (Utf8)this.phoneNumber : null);

      this.empId = in.readString(this.empId instanceof Utf8 ? (Utf8)this.empId : null);

    } else {
      for (int i = 0; i < 5; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.fName = in.readString(this.fName instanceof Utf8 ? (Utf8)this.fName : null);
          break;

        case 1:
          this.lName = in.readString(this.lName instanceof Utf8 ? (Utf8)this.lName : null);
          break;

        case 2:
          this.age = in.readInt();
          break;

        case 3:
          this.phoneNumber = in.readString(this.phoneNumber instanceof Utf8 ? (Utf8)this.phoneNumber : null);
          break;

        case 4:
          this.empId = in.readString(this.empId instanceof Utf8 ? (Utf8)this.empId : null);
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










