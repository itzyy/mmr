package cn.crxy.hadoop.mr;

public class AccessLog {

	public AccessLog(Integer appid, String ip, String mid, Integer userid,
			Integer loginType, String request, String status,
			String httpReferer, String userAgent, Long time) {
		this.appid = appid;
		this.ip = ip;
		this.mid = mid;
		this.userid = userid;
		this.loginType = loginType;
		this.request = request;
		this.status = status;
		this.httpReferer = httpReferer;
		this.userAgent = userAgent;
		this.time = time;
	}
	private Integer appid;

	private String ip;
	private String province;
	private String city;

	private String mid;
	private Integer userid;
	private Integer loginType;
	 
	private String request;
	private String method;
	private String path;
	private String http_version;

	private String status;
	private String httpReferer;
	private String userAgent;
	private String ieType;

	private Long time;
	private String dateTime;
	
	@Override
	public String toString() {
		return appid + "\t" + ip + "\t" + province + "\t" + city + "\t" + mid
				+ "\t" + userid + "\t" + loginType + "\t" + request + "\t"
				+ method + "\t" + path + "\t" + http_version + "\t" + status
				+ "\t" + httpReferer + "\t" + userAgent + "\t" + ieType + "\t"
				+ time + "\t" + dateTime;
	}

	public Integer getAppid() {
		return appid;
	}

	public void setAppid(Integer appid) {
		this.appid = appid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getHttp_version() {
		return http_version;
	}

	public void setHttp_version(String http_version) {
		this.http_version = http_version;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHttpReferer() {
		return httpReferer;
	}

	public void setHttpReferer(String httpReferer) {
		this.httpReferer = httpReferer;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getIeType() {
		return ieType;
	}

	public void setIeType(String ieType) {
		this.ieType = ieType;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	
	
	
	
	
	
}
