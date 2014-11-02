package com.example.quickcompapp;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GDSSDKResponse implements Serializable{
	private String[] Headers;

    private Rows[] Rows;

    private String NumCols;

    private String Seniority;

    private String Mnemonic;

    private String Function;

    private String ErrMsg;

    private Properties Properties;

    private String EndDate;

    private String StartDate;

    private String NumRows;

    private String CacheExpiryTime;

    private String Frequency;

    private String Identifier;

    private String Limit;

    public String[] getHeaders ()
    {
        return Headers;
    }

    public void setHeaders (String[] Headers)
    {
        this.Headers = Headers;
    }

    public Rows[] getRows ()
    {
        return Rows;
    }

    public void setRows (Rows[] Rows)
    {
        this.Rows = Rows;
    }

    public String getNumCols ()
    {
        return NumCols;
    }

    public void setNumCols (String NumCols)
    {
        this.NumCols = NumCols;
    }

    public String getSeniority ()
    {
        return Seniority;
    }

    public void setSeniority (String Seniority)
    {
        this.Seniority = Seniority;
    }

    public String getMnemonic ()
    {
        return Mnemonic;
    }

    public void setMnemonic (String Mnemonic)
    {
        this.Mnemonic = Mnemonic;
    }

    public String getFunction ()
    {
        return Function;
    }

    public void setFunction (String Function)
    {
        this.Function = Function;
    }

    public String getErrMsg ()
    {
        return ErrMsg;
    }

    public void setErrMsg (String ErrMsg)
    {
        this.ErrMsg = ErrMsg;
    }

    public Properties getProperties ()
    {
        return Properties;
    }

    public void setProperties (Properties Properties)
    {
        this.Properties = Properties;
    }

    public String getEndDate ()
    {
        return EndDate;
    }

    public void setEndDate (String EndDate)
    {
        this.EndDate = EndDate;
    }

    public String getStartDate ()
    {
        return StartDate;
    }

    public void setStartDate (String StartDate)
    {
        this.StartDate = StartDate;
    }

    public String getNumRows ()
    {
        return NumRows;
    }

    public void setNumRows (String NumRows)
    {
        this.NumRows = NumRows;
    }

    public String getCacheExpiryTime ()
    {
        return CacheExpiryTime;
    }

    public void setCacheExpiryTime (String CacheExpiryTime)
    {
        this.CacheExpiryTime = CacheExpiryTime;
    }

    public String getFrequency ()
    {
        return Frequency;
    }

    public void setFrequency (String Frequency)
    {
        this.Frequency = Frequency;
    }

    public String getIdentifier ()
    {
        return Identifier;
    }

    public void setIdentifier (String Identifier)
    {
        this.Identifier = Identifier;
    }

    public String getLimit ()
    {
        return Limit;
    }

    public void setLimit (String Limit)
    {
        this.Limit = Limit;
    }
}
