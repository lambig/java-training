package date.response;

/**
 * 業務日付返却オブジェクト
 * ※改修しないこと
 */
public class BusinessDateResponse {
    private String date;
    private boolean isInBusiness;

    public BusinessDateResponse(String date, boolean isInBusiness) {
        this.date = date;
        this.isInBusiness = isInBusiness;
    }

    public String date (){
        return this.date;
    }
    public boolean isInBusiness(){
        return this.isInBusiness;
    }
}
