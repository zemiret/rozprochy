//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.3
//
// <auto-generated>
//
// Generated from file `types.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package server.src.main.java.Smarthouse;

public class OvenProgram implements java.lang.Cloneable,
                                    java.io.Serializable
{
    public float temperature;

    public int hours;

    public int minutes;

    public int seconds;

    public OvenProgram()
    {
    }

    public OvenProgram(float temperature, int hours, int minutes, int seconds)
    {
        this.temperature = temperature;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        OvenProgram r = null;
        if(rhs instanceof OvenProgram)
        {
            r = (OvenProgram)rhs;
        }

        if(r != null)
        {
            if(this.temperature != r.temperature)
            {
                return false;
            }
            if(this.hours != r.hours)
            {
                return false;
            }
            if(this.minutes != r.minutes)
            {
                return false;
            }
            if(this.seconds != r.seconds)
            {
                return false;
            }

            return true;
        }

        return false;
    }

    public int hashCode()
    {
        int h_ = 5381;
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::Smarthouse::OvenProgram");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, temperature);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, hours);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, minutes);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, seconds);
        return h_;
    }

    public OvenProgram clone()
    {
        OvenProgram c = null;
        try
        {
            c = (OvenProgram)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void ice_writeMembers(com.zeroc.Ice.OutputStream ostr)
    {
        ostr.writeFloat(this.temperature);
        ostr.writeInt(this.hours);
        ostr.writeInt(this.minutes);
        ostr.writeInt(this.seconds);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.temperature = istr.readFloat();
        this.hours = istr.readInt();
        this.minutes = istr.readInt();
        this.seconds = istr.readInt();
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, OvenProgram v)
    {
        if(v == null)
        {
            _nullMarshalValue.ice_writeMembers(ostr);
        }
        else
        {
            v.ice_writeMembers(ostr);
        }
    }

    static public OvenProgram ice_read(com.zeroc.Ice.InputStream istr)
    {
        OvenProgram v = new OvenProgram();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<OvenProgram> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, OvenProgram v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.VSize))
        {
            ostr.writeSize(16);
            ice_write(ostr, v);
        }
    }

    static public java.util.Optional<OvenProgram> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.VSize))
        {
            istr.skipSize();
            return java.util.Optional.of(OvenProgram.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final OvenProgram _nullMarshalValue = new OvenProgram();

    /** @hidden */
    public static final long serialVersionUID = -956564737501771535L;
}
