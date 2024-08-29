package com.samsung.framework.domain.file;

public class DownloadResponse {

    private String status; // 성공 여부 (success, error)
    private String message; // 메시지 (예: 다운로드 URL, 오류 메시지)
    private String downloadURL; // 다운로드 URL (성공 시만 사용)

    public DownloadResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public DownloadResponse(String status, String message, String downloadURL) {
        this.status = status;
        this.message = message;
        this.downloadURL = downloadURL;
    }

    // Getters and setters omitted for brevity

    @Override
    public String toString() {
        return "DownloadResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", downloadURL='" + downloadURL + '\'' +
                '}';
    }
}