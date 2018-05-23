public enum HttpResponseCodes
{
    HTTP_200(200, "OK"),
    HTTP_201(201, "SUCCESS"),
    HTTP_204(204, "TRANSACTION IS TOO OLD."),
    HTTP_404(404, "ENDPOINT NOT FOUND."),
    HTTP_409(409, "INVALID PARAMS."),
    HTTP_500(500, "INTERNAL ERROR.");

    private int response_code;
    private String description;

    HttpResponseCodes(int response_code, String description)
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