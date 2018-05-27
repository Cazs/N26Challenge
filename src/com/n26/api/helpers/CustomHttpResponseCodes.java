package com.n26.api.helpers;

/**
 * @author ghost
 * @date 2018/05/22
 */

public enum CustomHttpResponseCodes
{
    HTTP_201(201, "SUCCESS"),
    HTTP_204(204, "TRANSACTION IS TOO OLD.");

    private int response_code;
    private String description;

    CustomHttpResponseCodes(int response_code, String description)
    {
        this.response_code = response_code;
        this.description = description;
    }

    public int getResponse_code()
    {
        return this.response_code;
    }

    public String getDescription()
    {
        return this.description;
    }
}