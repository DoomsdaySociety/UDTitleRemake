package net.mcud.udtitle;

public enum Lang {
    NOPER("noper"),
    HAVA("hava"),
    NOHAVA("nohava"),
    CURRENTTITLE("CurrentTitle"),
    LOADFAIL("LoadFail"),
    CANNOTOPENGUI("CanNotOpenGui"),
    LIST1("List1"),
    LIST2("List2"),
    HELP1("help1"),
    HELP2("help2"),
    HELP3("help3"),
    INVALIDTITLE("InvalidTitle"),
    NOTENOUGHMONEY("NotEnoughMoney"),
    RELOADSUCCESS("ReloadSuccess"),
    RELOADFAIL("ReloadFail"),
    NOTFOUNDTITLE("NotFoundTitle"),
    EXPENDMONEY("ExpendMoney"),
    CHANGE("change"),
    LASTPAGE("LastPage"),
    NEXTPAGE("NextPage"),
    CANCELTITLE("CancelTitle");
    
    private String path;

    private Lang(String path2) {
        this.path = path2;
    }

    public String getPath() {
        return this.path;
    }
}
