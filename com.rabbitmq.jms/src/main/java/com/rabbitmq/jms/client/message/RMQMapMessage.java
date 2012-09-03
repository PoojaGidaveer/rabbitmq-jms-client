package com.rabbitmq.jms.client.message;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageFormatException;

import com.rabbitmq.jms.client.RMQMessage;
import com.rabbitmq.jms.util.DiscardingObjectOutput;
import com.rabbitmq.jms.util.IteratorEnum;
import com.rabbitmq.jms.util.Util;

public class RMQMapMessage extends RMQMessage implements MapMessage {

    private static final String UNABLE_TO_CAST = "Unable to cast the object, %s, into the specified type %s";
    private Map<String, Serializable> data = new HashMap<String, Serializable>();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(String name) throws JMSException {
        Object o = this.data.get(name);
        if (o == null)
            return false;
        else if (o instanceof Boolean)
            return ((Boolean) o).booleanValue();
        else if (o instanceof String)
            return Boolean.parseBoolean((String) o);
        else
            throw new MessageFormatException(String.format(UNABLE_TO_CAST, o, "boolean"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByte(String name) throws JMSException {
        Object o = this.data.get(name);
        if (o == null)
            throw new NumberFormatException(String.format(UNABLE_TO_CAST, o, "byte"));
        else if (o instanceof Byte)
            return ((Byte) o).byteValue();
        else if (o instanceof String)
            return Byte.parseByte((String) o);
        else
            throw new MessageFormatException(String.format(UNABLE_TO_CAST, o, "byte"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getShort(String name) throws JMSException {
        Object o = this.data.get(name);
        if (o == null)
            throw new NumberFormatException(String.format(UNABLE_TO_CAST, o, "short"));
        else if (o instanceof Byte)
            return ((Byte) o).byteValue();
        else if (o instanceof Short)
            return ((Short) o).shortValue();
        else if (o instanceof String)
            return Short.parseShort((String) o);
        else
            throw new MessageFormatException(String.format(UNABLE_TO_CAST, o, "short"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char getChar(String name) throws JMSException {
        Object o = this.data.get(name);
        if (o == null)
            throw new NumberFormatException(String.format(UNABLE_TO_CAST, o, "char"));
        else if (o instanceof Character)
            return ((Character) o).charValue();
        else
            throw new MessageFormatException(String.format(UNABLE_TO_CAST, o, "char"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(String name) throws JMSException {
        Object o = this.data.get(name);
        if (o == null)
            throw new NumberFormatException(String.format(UNABLE_TO_CAST, o, "int"));
        else if (o instanceof Byte)
            return ((Byte) o).byteValue();
        else if (o instanceof Short)
            return ((Short) o).shortValue();
        else if (o instanceof Integer)
            return ((Integer) o).intValue();
        else if (o instanceof String)
            return Integer.parseInt((String) o);
        else
            throw new MessageFormatException(String.format(UNABLE_TO_CAST, o, "int"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(String name) throws JMSException {
        Object o = this.data.get(name);
        if (o == null)
            throw new NumberFormatException(String.format(UNABLE_TO_CAST, o, "long"));
        else if (o instanceof Byte)
            return ((Byte) o).byteValue();
        else if (o instanceof Short)
            return ((Short) o).shortValue();
        else if (o instanceof Integer)
            return ((Integer) o).intValue();
        else if (o instanceof Long)
            return ((Long) o).longValue();
        else if (o instanceof String)
            return Long.parseLong((String) o);
        else
            throw new MessageFormatException(String.format(UNABLE_TO_CAST, o, "long"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloat(String name) throws JMSException {
        Object o = this.data.get(name);
        if (o == null)
            throw new NumberFormatException(String.format(UNABLE_TO_CAST, o, "float"));
        else if (o instanceof Float) {
            return ((Float) o).floatValue();
        } else if (o instanceof String)
            return Float.parseFloat((String) o);
        else
            throw new MessageFormatException(String.format(UNABLE_TO_CAST, o, "float"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(String name) throws JMSException {
        Object o = this.data.get(name);
        if (o == null)
            throw new NumberFormatException(String.format(UNABLE_TO_CAST, o, "double"));
        else if (o instanceof Float)
            return ((Float) o).floatValue();
        else if (o instanceof Double) {
            return ((Double) o).doubleValue();
        } else if (o instanceof String)
            return Double.parseDouble((String) o);
        else
            throw new MessageFormatException(String.format(UNABLE_TO_CAST, o, "double"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String name) throws JMSException {
        Object o = this.data.get(name);
        if (o == null)
            return null;
        else if (o instanceof String)
            return ((String) o);
        else if (o instanceof byte[])
            throw new MessageFormatException(String.format(UNABLE_TO_CAST, o, "String"));
        else
            return o.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getBytes(String name) throws JMSException {
        Object o = this.data.get(name);
        if (o == null) {
            return null;
        } else if (o instanceof byte[]) {
            byte[] b1 = ((byte[]) o);
            byte[] b2 = new byte[b1.length];
            System.arraycopy(b1, 0, b2, 0, b1.length);
            return b2;
        } else
            throw new MessageFormatException(String.format(UNABLE_TO_CAST, o, "byte[]"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(String name) throws JMSException {
        Object o = this.data.get(name);
        if (o == null) {
            return null;
        } else if (o instanceof byte[]) {
            return getBytes(name);
        } else {
            return o;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enumeration<String> getMapNames() throws JMSException {
        return new IteratorEnum<String>(this.data.keySet().iterator());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBoolean(String name, boolean value) throws JMSException {
        this.data.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setByte(String name, byte value) throws JMSException {
        this.data.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShort(String name, short value) throws JMSException {
        this.data.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setChar(String name, char value) throws JMSException {
        this.data.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInt(String name, int value) throws JMSException {
        this.data.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLong(String name, long value) throws JMSException {
        this.data.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFloat(String name, float value) throws JMSException {
        this.data.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDouble(String name, double value) throws JMSException {
        this.data.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setString(String name, String value) throws JMSException {
        this.data.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBytes(String name, byte[] value) throws JMSException {
        setBytes(name, value, 0, value.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBytes(String name, byte[] value, int offset, int length) throws JMSException {
        if (value == null) {
            this.data.remove(name);
            return;
        }
        byte[] buf = new byte[length];
        System.arraycopy(value, offset, buf, 0, length);
        this.data.put(name, buf);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setObject(String name, Object value) throws JMSException {
        if (name==null && value==null) {
        } else if (value==null) {
            this.data.remove(name);
        } else if (!(value instanceof Serializable)) {
            throw new MessageFormatException(String.format(UNABLE_TO_CAST, value, Serializable.class.getName()));
        } else {
            try {
                Util.util().writePrimitiveData(value, new DiscardingObjectOutput(), false);
            } catch (IOException x) {
                Util.util().handleException(x);
            }
        }
        
        if (value instanceof byte[]) {
            setBytes(name, (byte[])value);
        } else {
            this.data.put(name, (Serializable) value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean itemExists(String name) throws JMSException {
        return this.data.containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearBody() throws JMSException {
        this.data.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeBody(ObjectOutput out) throws IOException {
        int size = this.data.size();
        out.writeInt(size);
        for (Map.Entry<String, Serializable> entry : this.data.entrySet()) {
            out.writeUTF(entry.getKey());
            RMQMessage.writePrimitive(entry.getValue(), out);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readBody(ObjectInput in) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            String name = in.readUTF();
            Object value = RMQMessage.readPrimitive(in);
            this.data.put(name, (Serializable) value);
        }
    }

}
