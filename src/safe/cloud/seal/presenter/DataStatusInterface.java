package safe.cloud.seal.presenter;


public interface DataStatusInterface {
	void onStatusSuccess(String action, String templateInfo);
	void onStatusStart();
	void onStatusError(String action, String error);
}
