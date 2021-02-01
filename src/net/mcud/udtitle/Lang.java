package net.mcud.udtitle;

public enum Lang {
    noper("noper"),
    hava("hava"),
    nohava("nohava"),
    CurrentTitle("CurrentTitle"),
    LoadFail("LoadFail"),
    CanNotOpenGui("CanNotOpenGui"),
    List1("List1"),
    List2("List2"),
    help1("help1"),
    help2("help2"),
    help3("help3"),
    InvalidTitle("InvalidTitle"),
    NotEnoughMoney("NotEnoughMoney"),
    ReloadSuccess("ReloadSuccess"),
    ReloadFail("ReloadFail"),
    NotFoundTitle("NotFoundTitle"),
    ExpendMoney("ExpendMoney"),
    change("change"),
    LastPage("LastPage"),
    NextPage("NextPage"),
    CancelTitle("CancelTitle");
    
    private String path;

    private Lang(String path2) {
        this.path = path2;
    }

    public String GetPath() {
        return this.path;
    }
}
