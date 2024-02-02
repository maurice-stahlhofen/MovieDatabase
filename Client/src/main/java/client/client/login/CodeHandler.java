package client.client.login;

public class CodeHandler {

    private static CodeHandler codeHandler;
    private String code;

    public static CodeHandler getCodeHandler(){
        if(codeHandler == null){
            codeHandler = new CodeHandler();
        }

        return codeHandler;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
