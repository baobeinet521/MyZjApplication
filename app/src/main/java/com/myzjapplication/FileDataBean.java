package com.myzjapplication;

import java.util.List;

public class FileDataBean {
    private String fileName;
    private String filePath;
    private String fileSizeHasCompany;
    private long fileSizeNoCompany;
    private String fileLastModifyTime;
    private long fileLastModifyTimeNotDeal;
    private String audioTime;
    private List<String> mSliceFilePath;//分割文件路径

    public List<String> getmSliceFilePath() {
        return mSliceFilePath;
    }

    public void setmSliceFilePath(List<String> mSliceFilePath) {
        this.mSliceFilePath = mSliceFilePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSizeHasCompany() {
        return fileSizeHasCompany;
    }

    public void setFileSizeHasCompany(String fileSizeHasCompany) {
        this.fileSizeHasCompany = fileSizeHasCompany;
    }

    public long getFileSizeNoCompany() {
        return fileSizeNoCompany;
    }

    public void setFileSizeNoCompany(long fileSizeNoCompany) {
        this.fileSizeNoCompany = fileSizeNoCompany;
    }

    public String getFileLastModifyTime() {
        return fileLastModifyTime;
    }

    public void setFileLastModifyTime(String fileLastModifyTime) {
        this.fileLastModifyTime = fileLastModifyTime;
    }

    public long getFileLastModifyTimeNotDeal() {
        return fileLastModifyTimeNotDeal;
    }

    public void setFileLastModifyTimeNotDeal(long fileLastModifyTimeNotDeal) {
        this.fileLastModifyTimeNotDeal = fileLastModifyTimeNotDeal;
    }

    public String getAudioTime() {
        return audioTime;
    }

    public void setAudioTime(String audioTime) {
        this.audioTime = audioTime;
    }
}
