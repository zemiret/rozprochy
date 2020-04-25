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

public class GenericError extends com.zeroc.Ice.UserException
{
    public GenericError()
    {
        this.reason = "";
    }

    public GenericError(Throwable cause)
    {
        super(cause);
        this.reason = "";
    }

    public GenericError(String reason)
    {
        this.reason = reason;
    }

    public GenericError(String reason, Throwable cause)
    {
        super(cause);
        this.reason = reason;
    }

    public String ice_id()
    {
        return "::Smarthouse::GenericError";
    }

    public String reason;

    /** @hidden */
    @Override
    protected void _writeImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice("::Smarthouse::GenericError", -1, true);
        ostr_.writeString(reason);
        ostr_.endSlice();
    }

    /** @hidden */
    @Override
    protected void _readImpl(com.zeroc.Ice.InputStream istr_)
    {
        istr_.startSlice();
        reason = istr_.readString();
        istr_.endSlice();
    }

    /** @hidden */
    public static final long serialVersionUID = -7576045308203820580L;
}
