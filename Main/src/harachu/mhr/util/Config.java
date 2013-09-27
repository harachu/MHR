package harachu.mhr.util;

public enum Config {
	RUN_LEVEL("DEBUG"),
	RUN_MODE("Jazz"),
	INTERVAL("30"),
	SERVER_URL("https://chm01.dv.jp.honda.com:9443/jazz"),
	USER_ID("AA999999"),
	PASSWORD("password"),
	PROJECT("PROJECT");
	
	public String $;
	
	private Config(String initial){
		this.$ = ConfigManager.getProperty(this,initial);
	}
	
	public void updateValue(String value){
		this.$ = value;
		ConfigManager.setProperty(this, value);
		ConfigManager.store();
	}
	
	static{
		ConfigManager.store();
	}
}
