package com.picaproject.pica.Item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReplyItem {
    @SerializedName("reply_id")
    @Expose
    private Integer replyId;
    @SerializedName("member_id")
    @Expose
    private Integer memberId;
    @SerializedName("picture_id")
    @Expose
    private Integer pictureId;
    @SerializedName("reply_text")
    @Expose
    private String replyText;
    @SerializedName("upload_date")
    @Expose
    private String uploadDate;
    @SerializedName("nickName")
    @Expose
    private String nickName;
    @SerializedName("isMyReply")
    @Expose
    private String isMyReply;

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getPictureId() {
        return pictureId;
    }

    public void setPictureId(Integer pictureId) {
        this.pictureId = pictureId;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIsMyReply() {
        return isMyReply;
    }

    public void setIsMyReply(String isMyReply) {
        this.isMyReply = isMyReply;
    }
}
