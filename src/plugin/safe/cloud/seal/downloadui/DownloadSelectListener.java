package plugin.safe.cloud.seal.downloadui;

	
public interface DownloadSelectListener {
    public void onDownloadSelectionChanged(long downloadId, boolean isSelected);
    public boolean isDownloadSelected(long id);
    public void downloadStatus(int status);
}
